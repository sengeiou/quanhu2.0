package com.rz.circled.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rz.circled.R;
import com.rz.circled.event.EventConstant;
import com.rz.circled.presenter.IPresenter;
import com.rz.circled.presenter.impl.ProveInfoPresenter;
import com.rz.circled.presenter.impl.UserInfoPresenter;
import com.rz.circled.service.BackGroundService;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.Constants;
import com.rz.common.constant.IntentCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.constant.Type;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.DialogUtils;
import com.rz.common.utils.NetUtils;
import com.rz.common.utils.StringUtils;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.httpapi.bean.RegisterBean;
import com.rz.httpapi.bean.UserInfoBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by Administrator on 2017/9/5 0005.
 */

public class BoundPhoneActivity extends BaseActivity {

    protected IPresenter presenter;
    /**
     * 手机号
     */
    @BindView(R.id.id_find_1_phone_edit)
    EditText mEditPhone;
    private String mPhone;
    @BindView(R.id.id_clear_phone)
    ImageView mImgClearPhone;

    /**
     * 发送验证码
     */
    @BindView(R.id.id_find_1_code_btn)
    Button mBtnSendCode;

    /**
     * 验证码
     */
    @BindView(R.id.id_find_1_code_edit)
    EditText mEditCode;
    private String mCode;
    @BindView(R.id.id_clear_code)
    ImageView mImgClearCode;
    @BindView(R.id.textView2)
    TextView boundTitleTxt;

    @BindView(R.id.id_find_1_next_btn)
    Button finishBtn;

    @BindView(R.id.second_title_txt)
    TextView secondTxt;

    /**
     * 倒计时类
     */
    private MyCount mc;

    //记录手机号类型
    private String pWType;
    //记录返回的手机号
    private String mRecordPhone;
    //接收用户信息
    UserInfoBean loginModel;

    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_findpwd, null);
    }

    @Override
    public boolean needShowTitle() {
        return false;
    }

    @Override
    public void initView() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        boundTitleTxt.setText("绑定手机号");
        secondTxt.setVisibility(View.VISIBLE);
        secondTxt.setText(R.string.bound_waring_txt);
        secondTxt.setVisibility(View.VISIBLE);
        finishBtn.setText("完成");

        loginModel = (UserInfoBean) getIntent().getExtras().getSerializable(IntentKey.LOGIN_MODEL);

        mEditPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2,
                                      int arg3) {
                mPhone = s + "";
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
            public void afterTextChanged(Editable s) {
            }
        });

        mEditCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2,
                                      int arg3) {
                mCode = s + "";
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
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void initData() {
        if (!StringUtils.isEmpty(Session.getUserPhone())) {
//            mEditPhone.setText(Session.getUserPhone());
//            mImgClearPhone.setVisibility(View.VISIBLE);
        }
        if (BackGroundService.time_code != 0) {
            startCount(BackGroundService.time_code);
        }
    }


    @Override
    public void initPresenter() {
        presenter = new UserInfoPresenter();
        presenter.attachView(this);
    }

    @OnClick(R.id.titlebar_main_left_btn)
    public void onClick() {
        showDialog();
    }

    /**
     * 清除手机号
     */
    @OnClick(R.id.id_clear_phone)
    public void clearPhone() {
        mEditPhone.setText("");
    }

    /**
     * 清除验证码
     */
    @OnClick(R.id.id_clear_code)
    public void clearCode() {
        mEditCode.setText("");
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

    @Override
    public void refreshPage() {

    }

    /**
     * 倒计时类
     */
    private class MyCount extends CountDownTimer {
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
            mBtnSendCode.setText(getResources().getString(
                    R.string.send_check_code));
            mBtnSendCode.setEnabled(true);
        }
    }

    /**
     * 发送手机验证码
     */
    @OnClick(R.id.id_find_1_code_btn)
    public void sendCode() {
        if (!StringUtils.isMobile(mPhone)) {
            SVProgressHUD.showInfoWithStatus(aty, getString(R.string.input_right_phone));
            return;
        }
        if (!NetUtils.isNetworkConnected(aty)) {
            SVProgressHUD.showInfoWithStatus(aty, getString(R.string.no_net_work));
        } else {
            pWType = "";
            mRecordPhone = "";
            BackGroundService.countDownCode(Constants.COUNTDOWN * 1000);
            startCount(Constants.COUNTDOWN * 1000);

            mPhone = mEditPhone.getText().toString();
            if (StringUtils.isMobile(mPhone)) {
                ((UserInfoPresenter) presenter).getVeriCode2(mPhone, Type.FUNCTION_CODE_5);
            }else{
                SVProgressHUD.showInfoWithStatus(aty, getString(R.string.input_right_phone));
            }

        }
    }

    @Override
    public boolean hasDataInPage() {
        return true;
    }

    @Override
    public <T> void updateView(T t) {
        super.updateView(t);
        if( t instanceof  RegisterBean){
            RegisterBean model = (RegisterBean) t;
            if (null != model) {
                pWType = model.code;
                mRecordPhone = model.phone;
//                SVProgressHUD.showSuccessWithStatus(aty, model.veriCode);
            }

        }else{
            if (null != t) {
                if(t.equals("1")){              //第三方登录绑定手机号成功
//                    skipActivity(aty, MainActivity.class);
                    if (Session.getUserIsFirstDownload()) {
                        skipActivity(aty, FollowCircle.class);
//
                    }else {
                        skipActivity(aty, MainActivity.class);
                    }

                    //发送存储对象存储用户信息
                    if(loginModel!= null) {
                        EventBus.getDefault().post(new BaseEvent(CommonCode.EventType.TYPE_SAVE,loginModel));
                    }
                    this.finish();
                }
            }
        }

    }

    /**
     * 检测电话是否注册,下一步
     */
    @OnClick(R.id.id_find_1_next_btn)
    public void checkPhone() {
        mPhone = mEditPhone.getText().toString().trim();
        mCode = mEditCode.getText().toString().trim();
        if (StringUtils.isMobile(mPhone)) {
            if (!StringUtils.isEmpty(mCode)) {
                if (TextUtils.equals(mPhone, mRecordPhone)) {
                    if (TextUtils.equals(pWType, Type.FUNCTION_CODE_5)) {
//                        handleSuccess(1);
                        ((UserInfoPresenter) presenter).boundPhone(mPhone, null,mCode,loginModel.getCustId());

                    }
                } else {
                    SVProgressHUD.showErrorWithStatus(aty, "请获取验证码");
                }
            } else {
                SVProgressHUD.showInfoWithStatus(aty, getString(R.string.input_check_code));
            }
        } else {
            SVProgressHUD.showInfoWithStatus(aty, getString(R.string.input_right_phone));
        }
    }

    @Override
    public void onLoadingStatus(int loadingStatus, String string) {
        super.onLoadingStatus(loadingStatus, string);
        if (loadingStatus == CommonCode.General.ERROR_DATA) {
            //短信获取失败
            mBtnSendCode
                    .setText(getString(R.string.send_check_code));
            mBtnSendCode.setEnabled(true);
            mc.cancel();
            if (null != BackGroundService.countCode) {
                BackGroundService.countCode.cancel();
            }
            BackGroundService.time_code = 0;
        }
    }

    /**
     * 获取验证码成功--下一步验证成功
     *
     * @param code 1表示已注册 2表示未注册
     */
    public void handleSuccess(int code) {
        if (code == 1 || code == 2) {
            Intent intent = new Intent(aty, FindPwdActivity2.class);
            intent.putExtra("phone", mPhone);
            intent.putExtra("code", mCode);
            intent.putExtra("page", code);
            startActivityForResult(intent, IntentCode.ForGetPw.FIND_1_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentCode.ForGetPw.FIND_1_REQUEST_CODE) {
            if (resultCode == IntentCode.Register.REGISTER_RESULT_CODE) {
                //注册成功(包括从忘记密码中进入或者直接手机号注册)
                if (null != data) {
                    setResult(IntentCode.Register.REGISTER_RESULT_CODE, data);
                    finish();
                }
            } else if (resultCode == IntentCode.ForGetPw.FIND_1_RESULT_CODE) {
                finish();
            }
        }
    }

    private void showDialog(){

        View dialogView = LayoutInflater.from(aty).inflate(R.layout.comm_dialog, null);
        final Dialog dialog = DialogUtils.selfDialog(aty, dialogView, false);
        dialog.show();
        ((TextView) dialogView.findViewById(R.id.id_tv_message)).setText(getString(R.string.waring_login));
        dialogView.findViewById(R.id.id_tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (com.rz.common.utils.CountDownTimer.isFastClick()) {
                    return;
                }
                dialog.dismiss();
                Session.clearShareP();
                finish();

            }
        });
        dialogView.findViewById(R.id.id_tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }


    @Override
    public void onBackPressed() {
        showDialog();

    }

    @Override
    protected boolean needLoadingView() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent baseEvent) {
        if (baseEvent.type == EventConstant.BOUND_PHONE_FAIL ) {
            if (baseEvent.getData() != null){
                mc.cancel();
                mc.onFinish();
            }
            return;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

}
