package com.rz.circled.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bumptech.glide.Glide;
import com.litesuits.common.utils.HexUtil;
import com.litesuits.common.utils.MD5Util;
import com.rz.rz_rrz.R;
import com.rz.rz_rrz.cache.preference.Session;
import com.rz.rz_rrz.constant.CodeStatus;
import com.rz.rz_rrz.constant.Constants;
import com.rz.rz_rrz.constant.IntentCode;
import com.rz.rz_rrz.constant.IntentKey;
import com.rz.rz_rrz.constant.Type;
import com.rz.rz_rrz.model.BankCardModel;
import com.rz.rz_rrz.model.UserInfoModel;
import com.rz.rz_rrz.presenter.impl.BankPresenter;
import com.rz.rz_rrz.presenter.impl.PayPresenter;
import com.rz.rz_rrz.utils.CountDownTimer;
import com.rz.rz_rrz.utils.StringUtils;
import com.rz.rz_rrz.utils.UnitUtil;
import com.rz.rz_rrz.view.base.BaseCommonAty;
import com.rz.rz_rrz.widget.CommonAdapter;
import com.rz.rz_rrz.widget.GlideCircleImage;
import com.rz.rz_rrz.widget.PopupView;
import com.rz.rz_rrz.widget.ViewHolder;
import com.rz.rz_rrz.widget.XListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 银行卡列表
 */
public class BankCardListAty extends BaseCommonAty implements XListView.IXListViewListener {

    @BindView(R.id.id_bank_listv)
    XListView mListview;
    @BindView(R.id.root)
    LinearLayout mRootLL;

    private CommonAdapter<BankCardModel> mAdapter;
    //存储银行卡列表
    private List<BankCardModel> mBanks = new ArrayList<BankCardModel>();

    //弹出框
    private PopupView mPopupView;

    private String[] mTitle = {getString(R.string.unbind_bank_card), getString(com.rz.rz_rrz.R.string.set_default)};
    private String[] mTitleColor = {"#333333", "#1bc2b8"};

    private PayPresenter mPayPresenter;

    //绑定银行关联ID
    private String mCust2BankId;

    //
    private int type;

    /**
     * 启动我绑定的银行卡列表
     *
     * @param type 1 表示我的银行卡列表 2表示从更换银行卡列表进入
     */
    public static void startBankCardList(Activity activity, int type) {
        Intent intent = new Intent(activity, BankCardListAty.class);
        intent.putExtra(IntentKey.General.KEY_TYPE, type);
        activity.startActivityForResult(intent, IntentCode.BankCard.BankCard_REQUEST_CODE);
    }

    @Override
    public boolean hasDataInPage() {
        return true;
    }

    @Override
    public View loadView(LayoutInflater inflater, View childView) {
        return super.loadView(inflater, inflater.inflate(R.layout.aty_bankcard_list, null));
    }

    @Override
    public void initPresenter() {
        presenter = new BankPresenter();
        mPayPresenter = new PayPresenter(false);
        mPayPresenter.attachView(this);
    }

    @Override
    public void initView() {
        type = getIntent().getIntExtra(IntentKey.General.KEY_TYPE, Constants.DEFAULTVALUE);
        if (type == 1) {
            setTitleText(getString(R.string.bank_card));
            mPopupView = new PopupView(aty);
            mPopupView.setOnItemPopupClick(new PopupView.OnItemPopupClick() {
                @Override
                public void OnItemClick(int position, String str) {
                    switch (position) {
                        //解绑银行卡
                        case 0:
                            if (CountDownTimer.isFastClick()) {
                                return;
                            }
                            mPayPresenter.isSettingPw(true);
                            break;
                        //设为默认
                        case 1:
                            if (CountDownTimer.isFastClick()) {
                                return;
                            }
                            ((BankPresenter) presenter).setDefaultBanckCard(mCust2BankId);
                            break;
                    }
                }
            });
            //添加银行卡按钮
            mBanks.add(addCard());
        } else {
            setTitleText(getString(R.string.change_bank_card));
        }
        mListview.setPullRefreshEnable(true);
        mListview.setPullLoadEnable(false);
        mListview.setXListViewListener(this);
    }

    @Override
    public void initData() {
        mAdapter = new CommonAdapter<BankCardModel>(aty, mBanks, R.layout.layout_item_bank_card) {
            @Override
            public void convert(ViewHolder helper, final BankCardModel item) {
                //银行卡icon
                ImageView mBankImg = (ImageView) helper.getViewById(R.id.id_iv_icon);
                //信用卡
                TextView mCardType = (TextView) helper.getViewById(R.id.id_tv_bank_type);
                //显示银行卡
                LinearLayout mShowCardLL = (LinearLayout) helper.getViewById(R.id.id_layout_card);
                //显示添加银行卡按钮
                RelativeLayout mShowAdd = (RelativeLayout) helper.getViewById(R.id.id_add_bank_ll);
                //显示默认绑卡的图标
                ImageView mDefaultImg = (ImageView) helper.getViewById(R.id.id_iv_checkon);
                mDefaultImg.setVisibility(View.GONE);

                if (item.isShowAdd) {
                    mShowCardLL.setVisibility(View.GONE);
                    mShowAdd.setVisibility(View.VISIBLE);
                    //添加银行卡
                    mShowAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (CountDownTimer.isFastClick()) {
                                return;
                            }
                            Intent intent = new Intent(aty, AddBankCardAty.class);
                            startActivityForResult(intent, IntentCode.BankCard.BankCard_REQUEST_CODE);
                        }
                    });
                } else {
                    mShowCardLL.setVisibility(View.VISIBLE);
                    mShowAdd.setVisibility(View.GONE);
                    //银行卡名称
                    String bankName = item.bankCode;
                    helper.setText(R.id.id_tv_bank_name, bankName);
                    Glide.with(mContext).load(UnitUtil.checkBankLogo(bankName)).transform(new GlideCircleImage(BankCardListAty.this)).crossFade().into(mBankImg);
//                    mBankImg.setBackgroundResource(UnitUtil.checkBankLogo(bankName));
                    if (item.getDefaultCard() == 1) {
                        mDefaultImg.setVisibility(View.VISIBLE);
                    } else {
                        mDefaultImg.setVisibility(View.GONE);
                    }
                    String cardNum = item.bankCardNo;
                    //卡号
                    helper.setText(R.id.id_tv_bank_num, StringUtils.replaceBankString(cardNum.replace(" ", ""), cardNum.length() - 4));
                    //点击银行卡
                    mShowCardLL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (type == 1) {
                                mCust2BankId = item.cust2BankId;
                                mPopupView.showAtLocPop(mRootLL, mTitle);
                                mPopupView.setItemColor(mTitleColor);
                            } else {
                                Intent card = new Intent();
                                card.putExtra(IntentKey.General.KEY_MODEL, item);
                                setResult(IntentCode.BankCard.BankCard_RESULT_CODE, card);
                                finish();
                            }
                        }
                    });
                }
            }
        };
        mListview.setAdapter(mAdapter);
        ((BankPresenter) presenter).getBanckCardList(Session.getUserId());
    }

    public BankCardModel addCard() {
        BankCardModel model = new BankCardModel();
        model.isShowAdd = true;
        return model;
    }

    @Override
    public <T> void updateView(T t) {
        if (null != t) {
            if (t instanceof List) {
                mListview.stopRefresh();
                List<BankCardModel> dataList = (List<BankCardModel>) t;
                if (null != dataList && !dataList.isEmpty()) {
                    mBanks.clear();
                    mBanks.addAll(dataList);
                    if (type == 1) {
                        mBanks.add(addCard());
                    }
                    mAdapter.notifyDataSetChanged();
                }
            } else if (t instanceof UserInfoModel) {
                UserInfoModel user = (UserInfoModel) t;
                if (null != user) {
                    if (Type.HAD_SET_PW == user.getIsPayPassword()) {
                        Session.setUserSetpaypw(true);
                    } else {
                        Session.setUserSetpaypw(false);
                    }
                    if (Type.OPEN_EASY_PAY == user.getSmallNopass()) {
                        Session.setIsOpenGesture(true);
                    } else {
                        Session.setIsOpenGesture(false);
                    }
                    mPayPresenter.checkIsSetPw();
                }
            } else if (t instanceof String) {
                String payPw = (String) t;
                if (TextUtils.equals("1", payPw)) {
                    //解绑成功
                    SVProgressHUD.showSuccessWithStatus(aty, R.string.unbind_card_success);
                    ((BankPresenter) presenter).getBanckCardList(Session.getUserId());
                } else if (TextUtils.equals("2", payPw)) {
                    //设置默认银行卡成功
                    SVProgressHUD.showSuccessWithStatus(aty, R.string.setting_success);
                    ((BankPresenter) presenter).getBanckCardList(Session.getUserId());
                } else {
                    //解绑银行卡
                    ((BankPresenter) presenter).unBandBanck(mCust2BankId, HexUtil.encodeHexStr(MD5Util.md5(payPw)));
                }
            }
        }
    }

    @Override
    public void onRefresh() {
        ((BankPresenter) presenter).getBanckCardList(Session.getUserId());
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onLoadingStatus(int loadingStatus, String string) {
        if (TextUtils.equals(string, "error")) {
            mPayPresenter.showResetDialog();
        } else {
            mListview.stopRefresh();
            if (loadingStatus == CodeStatus.Gegeneral.DATA_SUCCESS_NULL) {
                mBanks.clear();
                mBanks.add(addCard());
                mAdapter.notifyDataSetChanged();
            }
            super.onLoadingStatus(loadingStatus, string);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IntentCode.BankCard.BankCard_REQUEST_CODE) {
            if (resultCode == IntentCode.BankCard.BankCard_RESULT_CODE) {
                ((BankPresenter) presenter).getBanckCardList(Session.getUserId());
            }
        }
    }
}
