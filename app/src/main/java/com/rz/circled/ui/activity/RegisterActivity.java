package com.rz.circled.ui.activity;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.litesuits.common.utils.HexUtil;
import com.litesuits.common.utils.MD5Util;
import com.rz.circled.R;
import com.rz.circled.presenter.IPresenter;
import com.rz.circled.presenter.impl.UserInfoPresenter;
import com.rz.circled.service.BackGroundService;
import com.rz.circled.widget.CommomUtils;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.Constants;
import com.rz.common.constant.IntentCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.constant.Type;
import com.rz.common.event.BaseEvent;
import com.rz.common.event.NotifyEvent;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.NetUtils;
import com.rz.common.utils.StringUtils;
import com.rz.common.utils.TextViewUtils;
import com.rz.common.widget.SwipeBackLayout;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.httpapi.bean.UserInfoBean;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/9/4 0004.
 */

public class RegisterActivity extends BaseActivity {

    public String TAG;
    protected IPresenter presenter;
    protected IPresenter homeBannerPresenter;
    protected SwipeBackLayout layout;

    /**
     * 手机号
     */
    @BindView(R.id.id_regist_phone_edit)
    EditText mEditPhone;
    private String mPhone = "";
    @BindView(R.id.id_clear_phone)
    ImageView mImgClearPhone;

    /**
     * 发送验证码
     */
    @BindView(R.id.id_regist_send_sms_btn)
    Button mBtnSendCode;

    /**
     * 验证码
     */
    @BindView(R.id.id_regist_code_edit)
    EditText mEditCode;
    private String mCode = "";
    @BindView(R.id.id_clear_code)
    ImageView mImgClearCode;

    /**
     * 密码
     */
    @BindView(R.id.id_regist_pw_edit)
    EditText mEditPassw;
    private String mPassw = "";
    @BindView(R.id.id_clear_pw)
    ImageView mImgClearPw;
    @BindView(R.id.id_watch_pass)
    ImageView mImgWatchPw;

//    //是否显示同意条款
//    @BindView(R.id.id_regist_agree_ll)
//    RelativeLayout mAgreeLL;
//    /**
//     * 同意条款
//     */
//    @BindView(R.id.id_regist_checkbox)
//    CheckBox mCheckBox;
//
    /**
     * 条款
     */
    @BindView(R.id.find_pass2_protocol)
    TextView mTvProtocol;

    /**
     * 倒计时类
     */
    private MyCount mc;

    /**
     * 从那个页面跳过来的
     */
    private int fromPage;


    @Override
    public void initPresenter() {
        presenter = new UserInfoPresenter();
        presenter.attachView(this);
    }

    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_register, null);
    }

    @Override
    public void initView() {
        setTitleText("注册");
        fromPage = getIntent().getIntExtra(IntentKey.EXTRA_PAGE, Constants.DEFAULTVALUE);
        if (fromPage == IntentCode.Register.REGISTER_RESULT_CODE) {
//            mAgreeLL.setVisibility(View.GONE);
            setTitleText("绑定手机号");
        }
        if (BackGroundService.time_code != 0) {
            startCount(BackGroundService.time_code);
        }
        TextViewUtils.getInstance(getString(R.string.protocol));
        TextViewUtils.setTxtIndex(11, 19, new TextViewUtils.TextViewOnClickListener() {
            @Override
            public void onTextClick(View view) {

            }
        });
        TextViewUtils.setSpannableStyle(mTvProtocol);

        if(mEditPassw.getText().length()>0){
            mImgWatchPw.setVisibility(View.VISIBLE);
        }else{
            mImgWatchPw.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean needShowTitle() {
        return false;
    }

    @OnClick(R.id.titlebar_main_left_btn)
    public void onClick() {
        finish();
    }

    @Override
    public void initData() {
        mEditPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2,
                                      int arg3) {
                updateEtInfo();
                if (!StringUtils.isEmpty(mPhone)) {
                    mImgClearPhone.setVisibility(View.VISIBLE);
                } else {
                    mImgClearPhone.setVisibility(View.GONE);
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
        mEditCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2,
                                      int arg3) {
                updateEtInfo();
                if (!StringUtils.isEmpty(mCode)) {
                    mImgClearCode.setVisibility(View.VISIBLE);
                } else {
                    mImgClearCode.setVisibility(View.GONE);
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
        mEditPassw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateEtInfo();
                if (!StringUtils.isEmpty(mPassw)) {
                    mImgClearPw.setVisibility(View.VISIBLE);
                } else {
                    mImgClearPw.setVisibility(View.GONE);
                }

                if(mEditPassw.getText().length()>0){
                    mImgWatchPw.setVisibility(View.VISIBLE);
                }else{
                    mImgWatchPw.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mEditPassw.setKeyListener(new DigitsKeyListener() {
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

    private void updateEtInfo() {
        mPhone = mEditPhone.getText().toString().trim();
        mCode = mEditCode.getText().toString().trim();
        mPassw = mEditPassw.getText().toString().trim();
    }

    /**
     * 检查输入信息格式是否正确
     *
     * @return
     */
    public boolean checkData() {
        updateEtInfo();
//        if (fromPage == IntentCode.Register.REGISTER_RESULT_CODE) {
//            mCheckBox.setChecked(true);
//        }
//        if (mCheckBox.isChecked()) {
            if (StringUtils.isMobile(mPhone)) {
                if (StringUtils.isEmpty(mCode)) {
                    SVProgressHUD.showErrorWithStatus(aty, getString(R.string.input_check_code));
                } else {
                    if (StringUtils.isEmpty(mPassw) || mPassw.length() > 18 || mPassw.length() < 6) {
                        SVProgressHUD.showErrorWithStatus(aty, getString(R.string.regist_input_pw));
                    } else {
                        if (StringUtils.isNumRic(mPassw) || StringUtils.isLetterRic(mPassw)) {
                            SVProgressHUD.showErrorWithStatus(aty, getString(R.string.pw_num_letter));
                        } else {
                            return true;
                        }
                    }
                }
            } else {
                SVProgressHUD.showErrorWithStatus(aty, getString(R.string.input_right_phone));
            }
//        } else {
//            SVProgressHUD.showErrorWithStatus(aty, getString(R.string.regist_wrong_check));
//        }
        return false;
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

    @OnClick({R.id.id_regist_send_sms_btn, R.id.id_watch_pass, R.id.id_clear_phone, R.id.id_clear_code, R.id.id_clear_pw, R.id.id_regist_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            //发送手机验证码
            case R.id.id_regist_send_sms_btn:
                if (!StringUtils.isMobile(mPhone)) {
                    SVProgressHUD.showErrorWithStatus(aty, getString(R.string.input_right_phone));
                    return;
                }
                if (!NetUtils.isNetworkConnected(aty)) {
                    SVProgressHUD.showErrorWithStatus(aty, getString(R.string.no_net_work));
                } else {
                    BackGroundService.countDownCode(Constants.COUNTDOWN * 1000);
                    startCount(Constants.COUNTDOWN * 1000);
                    if (fromPage == IntentCode.Register.REGISTER_RESULT_CODE) {
                        ((UserInfoPresenter) presenter).getVeriCode(mPhone, Type.FUNCTION_CODE_5);
                    } else {
                        ((UserInfoPresenter) presenter).getVeriCode(mPhone, Type.FUNCTION_CODE_1);
                    }
                }
                break;
            //密码显示隐藏按钮
            case R.id.id_watch_pass:
                int length = TextUtils.isEmpty(mEditPassw.getText()) ? 0 : mEditPassw.getText().length();
                if (mEditPassw.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
                    mEditPassw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mImgWatchPw.setImageDrawable(getResources().getDrawable(R.mipmap.pwd_unsee));
                } else {
                    mEditPassw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mImgWatchPw.setImageDrawable(getResources().getDrawable(R.mipmap.pwd_see));
                }
                mEditPassw.setSelection(length);
                break;
            case R.id.id_clear_phone:
                mEditPhone.setText("");
                break;
            case R.id.id_clear_code:
                mEditCode.setText("");
                break;
            case R.id.id_clear_pw:
                mEditPassw.setText("");
                break;
            //注册
            case R.id.id_regist_btn:
                if (com.rz.common.utils.CountDownTimer.isFastClick()) {
                    return;
                }
                if (checkData()) {
                    CommomUtils.trackUser("注册登录", "注册", "注册完成");
                    if (fromPage == IntentCode.Register.REGISTER_RESULT_CODE) {
                        //绑定手机号
                        ((UserInfoPresenter) presenter).bindPhone(mPhone, HexUtil.encodeHexStr(MD5Util.md5(mPassw)), mCode);
                    } else {
                        ((UserInfoPresenter) presenter).registerUser(mPhone, HexUtil.encodeHexStr(MD5Util.md5(mPassw)), mCode);
                    }
                }
                break;
            //服务条款
//            case R.id.find_pass2_protocol:
//                CommH5Aty.startCommonH5(aty, H5Address.USER_SERVER, "悠然一指");
//                break;
        }
    }

    @Override
    public boolean hasDataInPage() {
        return true;
    }

    @Override
    public void onLoadingStatus(int loadingStatus, String string) {
        super.onLoadingStatus(loadingStatus, string);
        if (loadingStatus == CommonCode.General.ERROR_DATA) {
            mBtnSendCode
                    .setText(getString(R.string.send_check_code));
            mBtnSendCode.setEnabled(true);
            if (mc != null) {
                mc.cancel();
            }
            if (null != BackGroundService.countCode) {
                BackGroundService.countCode.cancel();
            }
            BackGroundService.time_code = 0;
        }
    }

    /**
     * 注册成功
     *
     * @param t
     * @param <T>
     */
    @Override
    public <T> void updateView(T t) {
        super.updateView(t);
        if (null != t) {
            if (t instanceof UserInfoBean) {
                UserInfoBean model = (UserInfoBean) t;
                if (null != model) {
//                    Intent intent = new Intent();
//                    intent.putExtra(IntentKey.General.KEY_MODEL, model);
//                    setResult(IntentCode.Register.REGISTER_RESULT_ONLY_CODE, intent);
                    NotifyEvent notifyEvent = new NotifyEvent("register", model, true);
                    EventBus.getDefault().post(notifyEvent);
                    finish();
                }
            } else {
                BaseEvent event = new BaseEvent();
                event.info = "1";
                EventBus.getDefault().post(event);
                finish();
            }
        }
    }


}
