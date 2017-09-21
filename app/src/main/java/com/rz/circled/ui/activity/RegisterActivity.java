package com.rz.circled.ui.activity;

import android.Manifest;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.litesuits.common.utils.HexUtil;
import com.litesuits.common.utils.MD5Util;
import com.rz.circled.R;
import com.rz.circled.presenter.IPresenter;
import com.rz.circled.presenter.impl.UserInfoPresenter;
import com.rz.circled.service.BackGroundService;
import com.rz.circled.widget.CommomUtils;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.Constants;
import com.rz.common.constant.H5Address;
import com.rz.common.constant.IntentCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.constant.Type;
import com.rz.common.event.BaseEvent;
import com.rz.common.event.NotifyEvent;
import com.rz.common.permission.EasyPermissions;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.NetUtils;
import com.rz.common.utils.StringUtils;
import com.rz.common.utils.TextViewUtils;
import com.rz.common.widget.SwipeBackLayout;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.httpapi.bean.UserInfoBean;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

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

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

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

    private String cityCode = "";
    private String location = "";


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

        if (mEditPassw.getText().length() > 0) {
            mImgWatchPw.setVisibility(View.VISIBLE);
        } else {
            mImgWatchPw.setVisibility(View.GONE);
        }
    }

    private void initLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }


    //声明定位回调监听器
    private AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
//                    HashMap<String, Object> hashMap = new HashMap<>();
//                    hashMap.put("longitude", amapLocation.getLongitude());
//                    hashMap.put("latitude", amapLocation.getLatitude());
//                    hashMap.put("province", amapLocation.getProvince());
//                    hashMap.put("city", amapLocation.getCity());
//                    hashMap.put("region", amapLocation.getDistrict());
//                    hashMap.put("cityCode", amapLocation.getAdCode());

                    location = amapLocation.getLocationDetail();
                    cityCode = amapLocation.getAdCode();
                    if (cityCode.length() == 6) {
                        cityCode = cityCode.substring(0, 4) + "00";
                    }

//                    Session.setCityCode(amapLocation.getAdCode());
//                    JsEvent.callJsEvent(hashMap, true);
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };

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

        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            initLocation();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.location_permission), RC_LOCATION_CONTACTS_PERM, perms);
        }

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

                if (mEditPassw.getText().length() > 0) {
                    mImgWatchPw.setVisibility(View.VISIBLE);
                } else {
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

    @Override
    public void refreshPage() {

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
        destroyLocation();
    }

    @OnClick({R.id.id_regist_send_sms_btn, R.id.id_watch_pass, R.id.id_clear_phone, R.id.id_clear_code, R.id.id_clear_pw, R.id.id_regist_btn, R.id.find_pass2_protocol})
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
                    mImgWatchPw.setImageDrawable(getResources().getDrawable(R.mipmap.pwd_see));
                } else {
                    mEditPassw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mImgWatchPw.setImageDrawable(getResources().getDrawable(R.mipmap.pwd_unsee));
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
                        ((UserInfoPresenter) presenter).registerUser(mPhone, HexUtil.encodeHexStr(MD5Util.md5(mPassw)), mCode, location, cityCode);
                    }
                }
                break;
            //服务条款
            case R.id.find_pass2_protocol:
                CommonH5Activity.startCommonH5(aty, H5Address.USER_SERVER, "圈呼");
                break;
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
                    Session.setCityCode(cityCode);
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

    /**
     * 销毁定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void destroyLocation() {
        if (null != mLocationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            mLocationClient.onDestroy();
            mLocationClient = null;
            mLocationOption = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        super.onPermissionsGranted(requestCode, perms);
        initLocation();
    }


}
