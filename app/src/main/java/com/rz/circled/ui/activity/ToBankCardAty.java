package com.rz.circled.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.litesuits.common.utils.HexUtil;
import com.litesuits.common.utils.MD5Util;
import com.rz.circled.R;
import com.rz.circled.dialog.MyReferenceDialog;
import com.rz.circled.presenter.impl.BankPresenter;
import com.rz.circled.presenter.impl.PayPresenter;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.IntentCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.constant.Type;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.CountDownTimer;
import com.rz.common.utils.Currency;
import com.rz.common.utils.DialogUtils;
import com.rz.common.utils.StringUtils;
import com.rz.common.utils.UnitUtil;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.httpapi.bean.BankCardModel;
import com.rz.httpapi.bean.CashModel;
import com.rz.httpapi.bean.UserInfoModel;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/7/26 0026.
 * 提现
 */
public class ToBankCardAty extends BaseActivity {

    //是否显示银行卡
    @BindView(R.id.id_layout_card)
    LinearLayout mRelaBank;
    //显示添加按钮
    @BindView(R.id.id_add_bank_ll)
    LinearLayout mRelaAdd;
    //银行图标
    @BindView(R.id.id_iv_icon)
    ImageView idIvIcon;
    //银行卡名称
    @BindView(R.id.id_tv_bank_name)
    TextView idTvBankName;
    //银行卡类别
    @BindView(R.id.id_tv_bank_type)
    TextView idTvBankType;
    //银行卡号
    @BindView(R.id.id_tv_bank_num)
    TextView idTvBankNum;
    //我的收益
    @BindView(R.id.id_my_shouyi_txt)
    TextView mTxtMyIncome;
    //可提现收益
    @BindView(R.id.id_tv_score)
    TextView idTvScore;
    //输入提现金额
    @BindView(R.id.id_et_recharge)
    EditText idEtRecharge;
    private String mRecharge;
    @BindView(R.id.id_back_clear_img)
    ImageView mImgClearMoney;
    //提现手续费
    @BindView(R.id.id_bank_charge_txt)
    TextView mTxtCharge;

    private String mCharge;

    //可提现收益
    private String mIntegralSum;

    //记录默认绑定银行卡的信息
    private BankCardModel mDefaultModel;

    private PayPresenter mPayPresenter;
    private BankPresenter presenter;

    /**
     * 启动提现界面
     *
     * @param activity    上下文
     * @param integralSum 可提现收益
     */
    public static void startBankCard(Activity activity, String integralSum) {
        Intent intent = new Intent(activity, ToBankCardAty.class);
        intent.putExtra(IntentKey.EXTRA_MONEY, integralSum);
        activity.startActivityForResult(intent, IntentCode.RechargeMoney.RECHARGE_REQUEST_CODE);
    }

    @Override
    public boolean hasDataInPage() {
        return true;
    }

    @Override
    protected View loadView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.activity_to_bankcard, null);
        return view;
    }

    @Override
    public void initPresenter() {
        mPayPresenter = new PayPresenter(false);
        mPayPresenter.attachView(this);
        presenter = new BankPresenter();
        presenter.attachView(this);
    }

    @Override
    public void initView() {
        setTitleText(getString(R.string.tixian));
        mIntegralSum = getIntent().getStringExtra(IntentKey.EXTRA_MONEY);
        mTxtMyIncome.setText(Currency.returnDollar(Currency.RMB, mIntegralSum, 0) + "元");
        if (TextUtils.isEmpty(mIntegralSum)) {
            idTvScore.setText("0.00" + "元");
        } else {
            if (Double.parseDouble(mIntegralSum) <= 200) {
                idTvScore.setText("0.00" + "元");
            } else {
                idTvScore.setText(Currency.returnDollar(Currency.RMB, "" + (Long.parseLong(mIntegralSum) - 200), 0) + "元");
            }
        }
        idEtRecharge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mRecharge = charSequence + "";
                if (TextUtils.isEmpty(mRecharge)) {
                    mImgClearMoney.setVisibility(View.GONE);
                } else {
                    mImgClearMoney.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void initData() {
        presenter.getCharge();
        presenter.getBanckCardList(Session.getUserId());

    }

    /**
     * 设置默认显示的银行卡
     */
    public void setDefaultCard(BankCardModel model) {
        mRelaBank.setVisibility(View.VISIBLE);
        mRelaAdd.setVisibility(View.GONE);
        mDefaultModel = model;
        //银行卡名称
        if (model != null) {
            String bankName = model.bankCode;
            idTvBankName.setText(bankName);
            idIvIcon.setBackgroundResource(UnitUtil.checkBankLogo(bankName));
            //卡号
            String bankNo = model.bankCardNo;
            idTvBankNum.setText(StringUtils.replaceBankString(bankNo.replace(" ", ""), bankNo.length() - 4));
        }
    }

    @Override
    public void onLoadingStatus(int loadingStatus, String string) {
        if (TextUtils.equals(string, "error")) {
            mPayPresenter.showResetDialog();
        } else {
            if (loadingStatus == CommonCode.General.DATA_EMPTY) {
                mRelaBank.setVisibility(View.GONE);
                mRelaAdd.setVisibility(View.VISIBLE);
                showRechargeDialog();
            }
            super.onLoadingStatus(loadingStatus, string);
        }
    }

    //弹出选择货币弹出框
    public void showRechargeDialog() {
        View dialogView = LayoutInflater.from(aty).inflate(R.layout.dialog_recharge_view, null);
        final Dialog mDialog = DialogUtils.selfDialog(aty, dialogView, false);
        mDialog.show();
        TextView mRemindTxt = (TextView) dialogView.findViewById(R.id.id_recharge_txt);
        mRemindTxt.setText(R.string.mei_bangding);
        dialogView.findViewById(R.id.id_recharge_money_txt).setVisibility(View.GONE);

        Button mBtnCancel = (Button) dialogView.findViewById(R.id.id_dialog_rehcarge_cancel_btn);
        mBtnCancel.setText(R.string.cancel);
        mBtnCancel.setTextColor(getResources().getColor(R.color.font_gray_l));
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
                finish();
            }
        });
        Button mBtn = (Button) dialogView.findViewById(R.id.id_dialog_recharge_sure_btn);
        mBtn.setText(R.string.bangka);
        mBtn.setTextColor(getResources().getColor(R.color.font_color_blue));
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
                Intent intent = new Intent(aty, AddBankCardAty.class);
                startActivityForResult(intent, IntentCode.BankCard.BIND_REQUEST_CODE);
            }
        });
    }

    @Override
    public <T> void updateViewWithFlag(T t, int flag) {
        setResult(IntentCode.BankCard.BankCard_RESULT_CODE);
        finish();
    }

    @Override
    public <T> void updateView(T t) {
        if (null != t) {
            if (t instanceof List) {
                List<BankCardModel> cards = (List<BankCardModel>) t;
                if (null != cards && !cards.isEmpty()) {
                    for (BankCardModel model : cards) {
                        if (model.getDefaultCard() == 1) {
                            setDefaultCard(model);
                        }
                    }
                }
            } else if (t instanceof CashModel) {
                CashModel model = (CashModel) t;
                int charge = model.getRegulate();
                if (charge <= 0) {
                    mTxtCharge.setText(R.string.shouqu);
                } else {
                    mCharge = charge + "";
                    mTxtCharge.setText(getString(R.string.unit_shou) + Currency.returnDollar(Currency.RMB, mCharge, 0) + getString(R.string.fuwufei));
                }
            } else if (t instanceof String) {
                //获取支付密码
                String pw = (String) t;
                if (!StringUtils.isEmpty(pw)) {
                    if (TextUtils.equals("1", pw)) {
                        SVProgressHUD.showSuccessWithStatus(aty, "提现申请成功，请等待2个工作日");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setResult(IntentCode.BankCard.BankCard_RESULT_CODE);
                                finish();
                            }
                        }, 2000);
                    } else {
                        //提现
                        presenter.getCash(HexUtil.encodeHexStr(MD5Util.md5(pw)), mRecharge + "00", mDefaultModel.getCust2BankId());

                    }
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
                    mPayPresenter.checkIsOpenEasyPay(Double.parseDouble(mRecharge) * 100, Double.parseDouble(mIntegralSum), "提现金额", 2);
                }
            }
        }
    }

    @OnClick({R.id.id_tv_change_bank, R.id.id_recharge_submit_btn, R.id.id_back_clear_img, R.id.tv_to_bankcard_reference})
    public void onClick(View view) {
        switch (view.getId()) {
            //更换银行卡
            case R.id.id_tv_change_bank:
                if (CountDownTimer.isFastClick()) {
                    return;
                }
                BankCardListAty.startBankCardList(aty, 2, mDefaultModel);
                break;
            case R.id.id_back_clear_img:
                idEtRecharge.setText("");
                break;
            //确认提现
            case R.id.id_recharge_submit_btn:
                if (CountDownTimer.isFastClick()) {
                    return;
                }
                if (StringUtils.isEmpty(mRecharge) || Double.parseDouble(mRecharge) <= 0) {
                    SVProgressHUD.showInfoWithStatus(aty, getString(R.string.tixian_money));
                } else {
                    if (StringUtils.isEmpty(mIntegralSum)) {
                        SVProgressHUD.showInfoWithStatus(aty, getString(R.string.tixian_no));
                    } else {
                        if (Double.parseDouble(mIntegralSum) <= 200) {
                            SVProgressHUD.showInfoWithStatus(aty, getString(R.string.tixian_no));
                        } else {
                            if (Double.parseDouble(mIntegralSum) > Double.parseDouble(mRecharge)) {
                                if (Double.parseDouble(mRecharge) < 5) {
                                    SVProgressHUD.showInfoWithStatus(aty, getString(R.string.atmost));
                                } else {
                                    if (Double.parseDouble(mRecharge) > 1000) {
                                        SVProgressHUD.showInfoWithStatus(aty, getString(R.string.xiane));
                                    } else {
                                        //支付密码验证
                                        mPayPresenter.isSettingPw(true);
                                    }
                                }
                            } else {
                                SVProgressHUD.showInfoWithStatus(aty, getString(R.string.fanwei));
                            }
                        }
                    }
//                    if (StringUtils.isEmpty(mCharge)) {
//                        //无提现手续费
//
//                    } else {
//                        //有提现手续费
//                        if (Double.parseDouble(mIntegralSum) >= (Double.parseDouble(mRecharge) * 100 + Double.parseDouble(mCharge))) {
//                            if (Double.parseDouble(mRecharge) < 5) {
//                                SVProgressHUD.showInfoWithStatus(aty, "单笔兑换至少5元");
//                            } else {
//                                if (Double.parseDouble(mRecharge) > 1000) {
//                                    SVProgressHUD.showInfoWithStatus(aty, "单笔兑换限额1000元");
//                                } else {
//                                    //支付密码验证
//                                    mPayPresenter.isSettingPw(true);
//                                }
//                            }
//                        } else {
//                            SVProgressHUD.showInfoWithStatus(aty, "提现金额不得大于可提现金额+手续费的总和");
//                        }
//                    }
                }
                break;
            case R.id.tv_to_bankcard_reference:
                MyReferenceDialog.newInstance().show(getSupportFragmentManager(), "");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IntentCode.BankCard.BankCard_REQUEST_CODE) {
            if (resultCode == IntentCode.BankCard.BankCard_RESULT_CODE) {
                if (null != data) {
                    BankCardModel model = (BankCardModel) data.getSerializableExtra(IntentKey.EXTRA_MODEL);
                    setDefaultCard(model);
                }
            }
        } else if (requestCode == IntentCode.BankCard.BIND_REQUEST_CODE) {
            if (resultCode == IntentCode.BankCard.BankCard_RESULT_CODE) {
                presenter.getCharge();
                presenter.getBanckCardList(Session.getUserId());

            } else {
                finish();
            }
        }
    }

    @Override
    public void refreshPage() {

    }
}
