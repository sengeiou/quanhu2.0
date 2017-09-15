package com.rz.circled.ui.activity;

import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.litesuits.common.utils.HexUtil;
import com.litesuits.common.utils.MD5Util;
import com.rz.circled.R;
import com.rz.circled.presenter.impl.UserInfoPresenter;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.CountDownTimer;
import com.rz.common.utils.StringUtils;
import com.rz.common.widget.svp.SVProgressHUD;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 修改登录密码
 */
public class ModifyPwdAty extends BaseActivity {

    //旧密码
    @BindView(R.id.id_et_old_pwd)
    EditText mEtOldPwd;
    @BindView(R.id.id_clear_old_pwd)
    ImageView mClearOldPwd;

    private String mOldPw;
    //新密码
    @BindView(R.id.id_et_new_pwd)
    EditText mEtNewPwd;
    @BindView(R.id.id_clear_new_pwd)
    ImageView mClearNewPwd;

    private String mNewPw;
    //输入新密码
    @BindView(R.id.id_et_new_pwd_confirm)
    EditText mEtNewPwdConfirm;
    @BindView(R.id.id_clear_new_pwd_confirm)
    ImageView mClearNewPwdConfirm;

    private String mReNewPw;
    //提交
    @BindView(R.id.id_btn_submit)
    Button mBtnSubmit;
    private UserInfoPresenter mPresenter;

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.aty_modify_pwd, null);
    }

    @Override
    public boolean hasDataInPage() {
        return true;
    }

    @Override
    public void initPresenter() {
        mPresenter = new UserInfoPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void initView() {
        mEtOldPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2,
                                      int arg3) {
                checkIsNull();
                if (!StringUtils.isEmpty(mOldPw)) {
                    mClearOldPwd.setVisibility(View.VISIBLE);
                } else {
                    mClearOldPwd.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
        mEtNewPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2,
                                      int arg3) {
                checkIsNull();
                if (!StringUtils.isEmpty(mNewPw)) {
                    mClearNewPwd.setVisibility(View.VISIBLE);
                } else {
                    mClearNewPwd.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
        mEtNewPwdConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2,
                                      int arg3) {
                checkIsNull();
                if (!StringUtils.isEmpty(mReNewPw)) {
                    mClearNewPwdConfirm.setVisibility(View.VISIBLE);
                } else {
                    mClearNewPwdConfirm.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        mEtOldPwd.setKeyListener(new DigitsKeyListener() {
            @Override
            public int getInputType() {
                return InputType.TYPE_TEXT_VARIATION_PASSWORD;
            }

            @Override
            protected char[] getAcceptedChars() {
                char[] data = getResources().getString(
                        R.string.edit_only_can_input).toCharArray();
                return data;
            }
        });
        mEtNewPwd.setKeyListener(new DigitsKeyListener() {
            @Override
            public int getInputType() {
                return InputType.TYPE_TEXT_VARIATION_PASSWORD;
            }

            @Override
            protected char[] getAcceptedChars() {
                char[] data = getResources().getString(
                        R.string.edit_only_can_input).toCharArray();
                return data;
            }
        });
        mEtNewPwdConfirm.setKeyListener(new DigitsKeyListener() {
            @Override
            public int getInputType() {
                return InputType.TYPE_TEXT_VARIATION_PASSWORD;
            }

            @Override
            protected char[] getAcceptedChars() {
                char[] data = getResources().getString(
                        R.string.edit_only_can_input).toCharArray();
                return data;
            }
        });
    }

    //获得输入框信息
    public void checkIsNull() {
        mOldPw = mEtOldPwd.getText().toString().trim();
        mNewPw = mEtNewPwd.getText().toString().trim();
        mReNewPw = mEtNewPwdConfirm.getText().toString().trim();
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.id_clear_old_pwd, R.id.id_clear_new_pwd, R.id.id_clear_new_pwd_confirm, R.id.id_btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_clear_old_pwd:
                mEtOldPwd.setText("");
                break;
            case R.id.id_clear_new_pwd:
                mEtNewPwd.setText("");
                break;
            case R.id.id_clear_new_pwd_confirm:
                mEtNewPwdConfirm.setText("");
                break;
            //提交
            case R.id.id_btn_submit:
                checkIsNull();
                if (StringUtils.isEmpty(mOldPw)) {
                    SVProgressHUD.showInfoWithStatus(aty, getString(R.string.input_old_pwd));
                } else {
                    if (mOldPw.length() < 6 || mOldPw.length() > 18 || StringUtils.isNumRic(mOldPw) || StringUtils.isLetterRic(mOldPw)) {
                        SVProgressHUD.showInfoWithStatus(aty, getString(R.string.no_right_format));
                    } else {
                        if (StringUtils.isEmpty(mNewPw)) {
                            SVProgressHUD.showInfoWithStatus(aty, getString(R.string.input_new_pwd));
                        } else {
                            if (mNewPw.length() < 6 || mNewPw.length() > 18 || StringUtils.isNumRic(mNewPw) || StringUtils.isLetterRic(mNewPw)) {
                                SVProgressHUD.showInfoWithStatus(aty, getString(R.string.no_right_format_new));
                            } else {
                                if (StringUtils.isEmpty(mReNewPw)) {
                                    SVProgressHUD.showInfoWithStatus(aty, getString(R.string.input_new_pwd_again));
                                } else {
                                    if (TextUtils.equals(mNewPw, mReNewPw)) {
                                        if (CountDownTimer.isFastClick()) {
                                            return;
                                        }
                                        mPresenter.modifyPw(HexUtil.encodeHexStr(MD5Util.md5(mOldPw)), HexUtil.encodeHexStr(MD5Util.md5(mNewPw)));
                                    } else {
                                        SVProgressHUD.showInfoWithStatus(aty, getString(R.string.no_equal_pay_pw));
                                    }
                                }
                            }
                        }
                    }
                }
                break;
        }
    }

    @Override
    public <T> void updateView(T t) {
        super.updateView(t);
        BaseEvent event = new BaseEvent();
        event.key = "3";
        EventBus.getDefault().post(event);
        finish();
    }
}
