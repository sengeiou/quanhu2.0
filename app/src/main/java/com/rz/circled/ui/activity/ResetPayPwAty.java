package com.rz.circled.ui.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.circled.presenter.impl.UserInfoPresenter;
import com.rz.circled.service.BackGroundService;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.Constants;
import com.rz.common.constant.IntentKey;
import com.rz.common.constant.Type;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.NetUtils;
import com.rz.common.utils.StringUtils;
import com.rz.common.widget.svp.SVProgressHUD;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 作者：Administrator on 2017/1/6 0006 16:33
 * 功能：重置支付密码
 * 说明：
 */
public class ResetPayPwAty extends BaseActivity {

    //验证码
    @BindView(R.id.id_reset_edit)
    EditText mEditCode;
    private String mCode;

    //发送验证码按钮
    @BindView(R.id.id_reset_send_btn)
    Button mBtnSendCode;

    //姓名
    @BindView(R.id.id_your_name_edit)
    EditText mEditName;
    private String mName;

    //身份证号码
    @BindView(R.id.id_idcard_edit)
    EditText mEditIdCard;
    private String mIdCard;

    //错误信息提醒
    @BindView(R.id.id_remind_error_txt)
    TextView mTxtError;

    /**
     * 倒计时类
     */
    private MyCount mc;
    private UserInfoPresenter mPresenter;

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.aty_reset_pay, null);
    }

    @Override
    public void initPresenter() {
        mPresenter = new UserInfoPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void initView() {
        setTitleText(getString(R.string.rest_pay_pw));
        if (BackGroundService.time_code != 0) {
            startCount(BackGroundService.time_code);
        }
    }

    @Override
    public void initData() {
        mEditCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCode = charSequence + "";
                if (StringUtils.isEmpty(mCode)) {
                    mTxtError.setText(R.string.input_code);
                } else {
                    mTxtError.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mEditName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mName = charSequence + "";
                if (StringUtils.isEmpty(mName) || mName.length() < 2 || mName.length() > 20) {
                    mTxtError.setText(R.string.name_length);
                } else {
                    mTxtError.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mEditIdCard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mIdCard = charSequence + "";
                if (StringUtils.isEmpty(mIdCard) || mIdCard.length() != 18) {
                    mTxtError.setText(R.string.id_length);
                } else {
                    mTxtError.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    /**
     * 启动倒计时
     */
    private void startCount(long time) {
        mBtnSendCode.setEnabled(false);
        if (null != mc) {
            mc.cancel();
            mc = null;
        }
        mc = new MyCount(time, 1000);
        mc.start();
    }


    /**
     * 倒计时类
     */
    public class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mBtnSendCode.setText(millisUntilFinished / 1000
                    + getString(R.string.regist_second));
        }

        @Override
        public void onFinish() {
            mBtnSendCode.setText(getString(R.string.send_check_code));
            mBtnSendCode.setEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mc) {
            mc.cancel();
        }
    }

    @OnClick({R.id.id_reset_send_btn, R.id.id_save_userinfo_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            //发送手机验证码
            case R.id.id_reset_send_btn:
                if (StringUtils.isEmpty(Session.getUserPhone())) {
                    SVProgressHUD.showInfoWithStatus(aty, getString(R.string.bind_phone));
                    return;
                }
                if (!NetUtils.isNetworkConnected(aty)) {
                    SVProgressHUD.showErrorWithStatus(aty, getString(R.string.no_net_work));
                } else {
                    BackGroundService.countDownCode(Constants.COUNTDOWN * 1000);
                    startCount(Constants.COUNTDOWN * 1000);
                    mPresenter.getVeriCode(Session.getUserPhone(), Type.FUNCTION_CODE_6);
                }
                break;
            //下一步
            case R.id.id_save_userinfo_btn:
                if (!NetUtils.isNetworkConnected(aty)) {
                    SVProgressHUD.showErrorWithStatus(aty, getString(R.string.no_net_work));
                } else {
                    if (StringUtils.isEmpty(mCode)) {
                        mTxtError.setText(R.string.input_code);
                    } else {
                        if (StringUtils.isEmpty(mName) || mName.length() < 2 || mName.length() > 20) {
                            mTxtError.setText(R.string.name_length);
                        } else {
                            if (StringUtils.isEmpty(mIdCard) || mIdCard.length() != 18) {
                                mTxtError.setText(R.string.id_length);
                            } else {
                                mTxtError.setText("");
                                mPresenter.checkCode(Session.getUserPhone(), mCode, mName, mIdCard);
                            }
                        }
                    }
                }
                break;
        }
    }

    @Override
    public boolean hasDataInPage() {
        return true;
    }

    @Override
    public <T> void updateView(T t) {
        super.updateView(t);
        Intent intent = new Intent(aty, SetPayPassAty.class);
        intent.putExtra(IntentKey.KEY_TYPE, Type.RESET_PAY_PW);
        intent.putExtra(IntentKey.EXTRA_NAME, mName);
        intent.putExtra(IntentKey.EXTRA_NUM, mIdCard);
        showActivity(aty, intent);
        finish();
    }
}
