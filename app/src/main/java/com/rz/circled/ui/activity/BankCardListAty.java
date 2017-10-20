package com.rz.circled.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.litesuits.common.utils.HexUtil;
import com.litesuits.common.utils.MD5Util;
import com.rz.circled.R;
import com.rz.circled.presenter.impl.BankPresenter;
import com.rz.circled.presenter.impl.PayPresenter;
import com.rz.circled.widget.GlideCircleImage;
import com.rz.circled.widget.PopupView;
import com.rz.circled.widget.XListView;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.Constants;
import com.rz.common.constant.IntentCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.constant.Type;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.CountDownTimer;
import com.rz.common.utils.StringUtils;
import com.rz.common.utils.UnitUtil;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.httpapi.bean.BankCardModel;
import com.rz.httpapi.bean.UserInfoModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * 银行卡列表
 */
public class BankCardListAty extends BaseActivity implements XListView.IXListViewListener {

    @BindView(R.id.id_bank_listv)
    XListView mListview;
    @BindView(R.id.root)
    LinearLayout mRootLL;

    private CommonAdapter<BankCardModel> mAdapter;
    //存储银行卡列表
    private List<BankCardModel> mBanks = new ArrayList<>();

    //弹出框
    private PopupView mPopupView;

    private String[] mTitle;
    private String[] mTitleColor;

    private PayPresenter mPayPresenter;
    private BankPresenter presenter;

    //绑定银行关联ID
    private String mCust2BankId;

    //
    private int type;
    private BankCardModel cardModel;

    /**
     * 启动我绑定的银行卡列表
     *
     * @param type 1 表示我的银行卡列表 2表示从更换银行卡列表进入
     */
    public static void startBankCardList(Activity activity, int type) {
        Intent intent = new Intent(activity, BankCardListAty.class);
        intent.putExtra(IntentKey.EXTRA_TYPE, type);
        activity.startActivityForResult(intent, IntentCode.BankCard.BankCard_REQUEST_CODE);
    }

    public static void startBankCardList(Activity activity, int type, BankCardModel bankCardModel) {
        Intent intent = new Intent(activity, BankCardListAty.class);
        intent.putExtra(IntentKey.EXTRA_TYPE, type);
        intent.putExtra(IntentKey.EXTRA_MODEL, bankCardModel);
        activity.startActivityForResult(intent, IntentCode.BankCard.BankCard_REQUEST_CODE);
    }

    @Override
    protected boolean needLoadingView() {
        return true;
    }

    @Override
    public boolean hasDataInPage() {
        return true;
    }

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_bankcard_list, null);
    }

    @Override
    public void initPresenter() {
        presenter = new BankPresenter();
        presenter.attachView(this);
        mPayPresenter = new PayPresenter(false);
        mPayPresenter.attachView(this);
    }

    @Override
    public void initView() {
        mTitle = new String[]{getString(R.string.unbind_bank_card), getString(R.string.set_default)};
        mTitleColor = new String[]{"#333333", "#0185ff"};
        type = getIntent().getIntExtra(IntentKey.EXTRA_TYPE, Constants.DEFAULTVALUE);
        if (getIntent().hasExtra(IntentKey.EXTRA_MODEL)) {
            cardModel = (BankCardModel) getIntent().getSerializableExtra(IntentKey.EXTRA_MODEL);
        }
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
                            (presenter).setDefaultBanckCard(mCust2BankId);
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
            public void convert(ViewHolder helper, final BankCardModel item, int postion) {
                //银行卡icon
                ImageView mBankImg = (ImageView) helper.getViewById(R.id.id_iv_icon);
                //信用卡
                TextView mCardType = (TextView) helper.getViewById(R.id.id_tv_bank_type);
                //显示银行卡
                LinearLayout mShowCardLL = (LinearLayout) helper.getViewById(R.id.id_layout_card);
                //显示添加银行卡按钮
                LinearLayout mShowAdd = (LinearLayout) helper.getViewById(R.id.id_add_bank_ll);
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
                    if (item.isSelect) {
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
                                card.putExtra(IntentKey.EXTRA_MODEL, item);
                                setResult(IntentCode.BankCard.BankCard_RESULT_CODE, card);
                                finish();
                            }
                        }
                    });
                }
            }
        };
        mListview.setAdapter(mAdapter);
        presenter.getBanckCardList(Session.getUserId());
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
                if (!dataList.isEmpty()) {
                    mBanks.clear();
                    mBanks.addAll(dataList);
                    if (type == 1) {
                        mBanks.add(addCard());
                    }
                    if (cardModel != null) {
                        for (BankCardModel mBank : mBanks) {
                            if (cardModel.bankCardNo.equals(mBank.bankCardNo)) {
                                mBank.isSelect = true;
                            } else mBank.isSelect = false;
                        }
                    } else {
                        for (BankCardModel mBank : mBanks) {
                            if (mBank.getDefaultCard() == 1)
                                mBank.isSelect = true;
                        }
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
                    SVProgressHUD.showSuccessWithStatus(aty, getString(R.string.unbind_card_success));
                    presenter.getBanckCardList(Session.getUserId());
                } else if (TextUtils.equals("2", payPw)) {
                    //设置默认银行卡成功
                    SVProgressHUD.showSuccessWithStatus(aty, getString(R.string.setting_success));
                    presenter.getBanckCardList(Session.getUserId());
                } else {
                    //解绑银行卡
                    presenter.unBandBanck(mCust2BankId, HexUtil.encodeHexStr(MD5Util.md5(payPw)));

                }
            }
        }
    }

    @Override
    public void onRefresh() {
        presenter.getBanckCardList(Session.getUserId());
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
            if (loadingStatus == CommonCode.General.DATA_EMPTY) {
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
                (presenter).getBanckCardList(Session.getUserId());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }

    @Override
    public void refreshPage() {

    }
}
