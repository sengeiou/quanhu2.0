package com.rz.circled.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.litesuits.common.utils.HexUtil;
import com.litesuits.common.utils.MD5Util;
import com.rz.circled.R;
import com.rz.circled.presenter.impl.AccountPresenter;
import com.rz.circled.widget.password.GridPasswordView;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.Constants;
import com.rz.common.constant.IntentKey;
import com.rz.common.constant.Type;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.CountDownTimer;
import com.rz.common.utils.DialogUtils;
import com.rz.common.utils.StringUtils;
import com.rz.common.widget.svp.SVProgressHUD;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 设置或者修改支付密码或者忘记
 */
public class SetPayPassAty extends BaseActivity {

    //旧密码
    @BindView(R.id.id_old_pay_pw_txt)
    TextView mTxtOldPw;
    @BindView(R.id.id_old_pay_pw_pass)
    GridPasswordView mPassOldPw;
    private String mOldPw;
    //新密码
    @BindView(R.id.id_new_pay_pw_txt)
    TextView mTxtNewPw;
    @BindView(R.id.id_new_pay_pw_pass)
    GridPasswordView mPassNewPw;
    private String mNewPw;
    //再次输入新密码
    @BindView(R.id.id_sure_pay_pw_txt)
    TextView mTxtSureNewPw;
    @BindView(R.id.id_sure_new_pw_pass)
    GridPasswordView mPassSurePw;
    private String mReNewPw;
    //显示再次输入新密码
    @BindView(R.id.id_sure_pay_pw_ll)
    LinearLayout mShowLL;
    //是否设置了安全保护信息
    @BindView(R.id.id_is_set_user_txt)
    TextView mTxtIsSetUser;
    //确定
    @BindView(R.id.id_set_pay_pw_btn)
    Button mBtnPass;
    //类型-是设置支付密码还是修改支付密码
    private int type;
    //记录姓名
    private String mName;
    //记录银行卡号
    private String mIdCard;
    private AccountPresenter mPresenter;

    @Nullable
    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.aty_set_pay_pass, null);
    }

    @Override
    public void initPresenter() {
        mPresenter = new AccountPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void initView() {
        //类型
        type = getIntent().getIntExtra(IntentKey.KEY_TYPE, Constants.DEFAULTVALUE);
        //姓名
        mName = getIntent().getStringExtra(IntentKey.KEY_NAME);
        //身份证号码
        mIdCard = getIntent().getStringExtra(IntentKey.KEY_NUM);

        if (Session.getUserSafetyproblem()) {
            mTxtIsSetUser.setVisibility(View.GONE);
        } else {
            mTxtIsSetUser.setVisibility(View.VISIBLE);
        }
        if (type == Type.HAD_SET_PW) {
            setTitleText(getString(R.string.modify_pay_pw));
            mShowLL.setVisibility(View.VISIBLE);
            mTxtOldPw.setText(getString(R.string.input_old_pwd));
            mTxtNewPw.setText(getString(R.string.input_new_pwd));
        } else if (type == Type.HAD_NO_SET_PW) {
            setTitleText(getString(R.string.setting_pay_pw));
            mShowLL.setVisibility(View.GONE);
            mTxtOldPw.setText(getString(R.string.regist_pw));
            mTxtNewPw.setText(getString(R.string.regist_sure_pw));
        } else if (type == Type.RESET_PAY_PW) {
            setTitleText(getString(R.string.reast_password));
            mShowLL.setVisibility(View.GONE);
            mTxtOldPw.setText(getString(R.string.regist_pw));
            mTxtNewPw.setText(getString(R.string.regist_sure_pw));
        }
        //旧密码
        mPassOldPw.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {
            }

            @Override
            public void onInputFinish(String psw) {
                mPassNewPw.requestFocus();
            }
        });
        //新密码
        mPassNewPw.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {
            }

            @Override
            public void onInputFinish(String psw) {
                if (type == Type.HAD_SET_PW) {
                    mPassSurePw.requestFocus();
                } else {
                    mBtnPass.requestFocus();
                    hideInputMethod();
                    checkInput();
                }
            }
        });
        //确认新密码
        mPassSurePw.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {
            }

            @Override
            public void onInputFinish(String psw) {
                mBtnPass.requestFocus();
                hideInputMethod();
                checkInput();
            }
        });
    }

    @Override
    public void initData() {

    }

    @OnClick(R.id.id_set_pay_pw_btn)
    public void submitPw() {
        if (checkInput()) {
            if (CountDownTimer.isFastClick()) {
                return;
            }
            //TODO 设置支付密码
            if (type == Type.HAD_SET_PW) {
                //去修改支付密码
                mPresenter.setPayPw(type, HexUtil.encodeHexStr(MD5Util.md5(mNewPw)), HexUtil.encodeHexStr(MD5Util.md5(mOldPw)));
            } else if (type == Type.HAD_NO_SET_PW) {
                //去设置支付密码
                mPresenter.setPayPw(type, HexUtil.encodeHexStr(MD5Util.md5(mNewPw)), "");
            } else if (type == Type.RESET_PAY_PW) {
                //重置支付密码
                mPresenter.forgetPayPw(Type.RESET_PAY_PW, HexUtil.encodeHexStr(MD5Util.md5(mNewPw)), mName, mIdCard);
            }
        }
    }

    @Override
    public boolean hasDataInPage() {
        return true;
    }

    @Override
    public <T> void updateView(T t) {
        if (null != t) {
            int status = (Integer) t;
            Session.setUserSetpaypw(true);
            if (status == Type.HAD_NO_SET_PW) {
                //未设置支付密码,弹出设置成功提醒，提醒用户是否开启免密支付
                View mEasyPayView = LayoutInflater.from(aty).inflate(R.layout.layout_dialog_simple2, null);
                final Dialog mEasyDialog = DialogUtils.selfDialog(aty, mEasyPayView, false);
                mEasyDialog.show();
                //弹出框消失，界面消失
                mEasyPayView.findViewById(R.id.id_chear_dialog_img).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mEasyDialog.dismiss();
                        BaseEvent event = new BaseEvent();
                        event.key = "0";
                        EventBus.getDefault().post(event);
                        finish();
                    }
                });
                //开启免密支付
                mEasyPayView.findViewById(R.id.dialog_simple2_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mEasyDialog.dismiss();
                        mPresenter.openOrClosePay(Type.OPEN_EASY_PAY, HexUtil.encodeHexStr(MD5Util.md5(mNewPw)));
                    }
                });
                //开启免密支付
                mEasyPayView.findViewById(R.id.dialog_simple2_cb).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mEasyDialog.dismiss();
                        mPresenter.openOrClosePay(Type.OPEN_EASY_PAY, HexUtil.encodeHexStr(MD5Util.md5(mNewPw)));
                    }
                });
            } else if (status == Type.HAD_SET_PW) {
//                BaseEvent event = new BaseEvent();
//                event.key = "2";
//                EventBus.getDefault().post(event);
//                finish();
                SVProgressHUD.showSuccessWithStatus(aty, getString(R.string.modify_success));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //修改支付密码
                        finish();
                    }
                }, 2000);
            } else if (status == Type.RESET_PAY_PW) {
                //找回忘记支付密码
                SVProgressHUD.showSuccessWithStatus(aty, getString(R.string.password_sucess));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //修改支付密码
                        finish();
                    }
                }, 2000);
            }
        }

    }

    @Override
    public <T> void updateViewWithLoadMore(T t, boolean loadMore) {
        if (null != t) {
            int type = (Integer) t;
            if (!loadMore) {
                if (type == Type.OPEN_EASY_PAY) {
                    Session.setIsOpenGesture(true);
                    BaseEvent event = new BaseEvent();
                    event.key = "0";
                    EventBus.getDefault().post(event);
                } else {
                    Session.setIsOpenGesture(false);
                }
                finish();
            }
        }
    }

    //检测支付密码
    private boolean checkInput() {
        mOldPw = mPassOldPw.getPassWord();
        mNewPw = mPassNewPw.getPassWord();
        mReNewPw = mPassSurePw.getPassWord();
        if (type == Type.HAD_SET_PW) {
            //已设置
            if (StringUtils.isEmpty(mOldPw) || mOldPw.length() < 6) {
                SVProgressHUD.showErrorWithStatus(aty, getString(R.string.input_old_pay_pw));
                return false;
            } else {
                if (StringUtils.isEmpty(mNewPw) || mNewPw.length() < 6) {
                    SVProgressHUD.showErrorWithStatus(aty, getString(R.string.pay_pw_explain));
                    return false;
                } else {
                    if (StringUtils.isEmpty(mReNewPw) || mReNewPw.length() < 6) {
                        SVProgressHUD.showErrorWithStatus(aty, getString(R.string.input_new_pwd_again));
                        return false;
                    } else {
                        if (!mNewPw.equals(mReNewPw)) {
                            SVProgressHUD.showErrorWithStatus(aty, getString(R.string.no_equal_pay_pw));
                            return false;
                        } else {
                            return true;
                        }
                    }
                }
            }
        } else {
            //未设置
            if (StringUtils.isEmpty(mOldPw) || mOldPw.length() < 6) {
                SVProgressHUD.showErrorWithStatus(aty, getString(R.string.pay_pw_explain));
                return false;
            } else {
                if (StringUtils.isEmpty(mNewPw) || mNewPw.length() < 6) {
                    SVProgressHUD.showErrorWithStatus(aty, getString(R.string.input_new_pwd_again));
                    return false;
                } else {
                    if (!mOldPw.equals(mNewPw)) {
                        SVProgressHUD.showErrorWithStatus(aty, getString(R.string.no_equal_pay_pw));
                        return false;
                    } else {
                        return true;
                    }
                }
            }
        }
    }

    private void hideInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getApplicationWindowToken(), 0);
        }
    }
}
