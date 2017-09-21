package com.rz.circled.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.litesuits.common.utils.HexUtil;
import com.litesuits.common.utils.MD5Util;
import com.rz.circled.R;
import com.rz.circled.presenter.impl.PayPresenter;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.IntentCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.constant.Type;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.CountDownTimer;
import com.rz.common.utils.Currency;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.httpapi.bean.UserInfoModel;
import com.rz.httpapi.constans.ReturnCode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/7/26 0026.
 * 兑换到消费账户
 */
public class RechargeAty extends BaseActivity {

    //可兑换收益
    @BindView(R.id.id_tv_score)
    TextView mTvScore;
    //输入兑换的收益
    @BindView(R.id.id_et_recharge)
    EditText mEditRecharge;
    @BindView(R.id.id_clear_recharge_img)
    ImageView mImgClear;

    //可兑换收益
    private String mIntegralSum;
    private PayPresenter mPresenter;
    private String mRecharge;

    /**
     * 启动兑换收益页面
     *
     * @param activity    上下文
     * @param integralSum 可兑换收益
     */
    public static void startRecharge(Activity activity, String integralSum) {
        Intent intent = new Intent(activity, RechargeAty.class);
        intent.putExtra(IntentKey.EXTRA_MONEY, integralSum);
        activity.startActivityForResult(intent, IntentCode.RechargeMoney.RECHARGE_REQUEST_CODE);
    }

    @Override
    public boolean hasDataInPage() {
        return true;
    }

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_recharge, null);
    }

    @Override
    public void initPresenter() {
        mPresenter = new PayPresenter(false);
        mPresenter.attachView(this);
    }

    @Override
    public void initView() {
        setTitleText(getString(R.string.duihuan_xiaofei));
        mIntegralSum = getIntent().getStringExtra(IntentKey.EXTRA_MONEY);
        mTvScore.setText(Currency.returnDollar(Currency.RMB, mIntegralSum, 0));
    }

    @Override
    public void initData() {
        mEditRecharge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mRecharge = charSequence + "";
                if (TextUtils.isEmpty(mRecharge)) {
                    mImgClear.setVisibility(View.GONE);
                } else {
                    mImgClear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @OnClick({R.id.id_submit_btn, R.id.id_clear_recharge_img})
    public void onClick(View v) {
        switch (v.getId()) {
            //确认兑换
            case R.id.id_submit_btn:
                if (CountDownTimer.isFastClick()) {
                    return;
                }
                if (TextUtils.isEmpty(mIntegralSum)) {
                    SVProgressHUD.showInfoWithStatus(aty, getString(R.string.exchange_error));
                    return;
                }
                if (TextUtils.isEmpty(mRecharge) || Double.parseDouble(mRecharge) <= 0) {
                    SVProgressHUD.showInfoWithStatus(aty, getString(R.string.pls_exchange_yrb));
                } else {
                    if (Double.parseDouble(mRecharge) * 100 > Double.parseDouble(mIntegralSum)) {
                        SVProgressHUD.showInfoWithStatus(aty, getString(R.string.no_ryb));
                    } else {
                        //支付密码验证
                        mPresenter.isSettingPw(true);
                    }
                }
                break;
            //清除
            case R.id.id_clear_recharge_img:
                mEditRecharge.setText("");
                break;
        }
    }

    @Override
    public <T> void updateView(T t) {
        if (null != t) {
            if (t instanceof UserInfoModel) {
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
                    mPresenter.checkIsOpenEasyPay(Double.parseDouble(mRecharge) * 100, Double.parseDouble(mIntegralSum), "兑换悠然币", 3);
                }
            } else if (t instanceof String) {
                //去支付
                String pw = (String) t;
                if (TextUtils.isEmpty(pw)) {
                    pw = "";
                } else if (TextUtils.equals("1", pw)) {
                    SVProgressHUD.showInfoWithStatus(aty, getString(R.string.enought_yrb));
                    return;
                }
                mPresenter.pointsToAccount(HexUtil.encodeHexStr(MD5Util.md5(pw)), mRecharge + "00");
            } else if (t instanceof Integer) {
                int ret = ((Integer) t).intValue();
                if (ret == ReturnCode.SUCCESS) {
                    SVProgressHUD.showSuccessWithStatus(aty, getString(R.string.exchange_sucess));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setResult(IntentCode.BankCard.BankCard_RESULT_CODE);
                            finish();
                        }
                    }, 2000);
                }
            }
        }
    }

    @Override
    public void onLoadingStatus(int loadingStatus, String string) {
        if (TextUtils.equals(string, "error")) {
            if (isFinishing()) {
                return;
            }
            mPresenter.showResetDialog();
        } else {
            super.onLoadingStatus(loadingStatus, string);
        }
    }
}
