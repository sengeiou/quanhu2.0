package com.rz.circled.ui.activity;

import android.Manifest;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.litesuits.common.utils.HexUtil;
import com.litesuits.common.utils.MD5Util;
import com.rz.circled.R;
import com.rz.circled.presenter.IPresenter;
import com.rz.circled.presenter.impl.UserInfoPresenter;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.IntentCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.permission.EasyPermissions;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.CountDownTimer;
import com.rz.common.utils.StringUtils;
import com.rz.common.utils.TextViewUtils;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.httpapi.bean.UserInfoModel;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/9/5 0005.
 */

public class FindPwdActivity2 extends BaseActivity {

    protected IPresenter presenter;

    /**
     * 头部描述
     */
    @BindView(R.id.main_title)
    TextView mTvTitle;

    @BindView(R.id.reset_title)
    TextView resetTitle;

    /**
     * 登录密码
     */
    @BindView(R.id.id_no_regist_pass_edit)
    EditText mEditPassWEdit;
    private String mPassW;
    @BindView(R.id.id_clear_pass)
    ImageView mImgClearPassW;
    @BindView(R.id.id_watch_pass)
    ImageView mImgWatchPass;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    private String cityCode = "";
    private String location = "";
//
//    /**
//     * 协议
//     */
//    @BindView(R.id.id_find_2_xieyi_ll)
//    RelativeLayout mRegistXieYi;

//    /**
//     * 同意条款
//     */
//    @BindView(R.id.id_find_2_check_box)
//    CheckBox mCheckBox;

    /**
     * 协议
     */
    @BindView(R.id.id_find_2_xieyi_txt)
    TextView mTvProtocol;

    /**
     * 电话
     */
    private String mPhone;

    /**
     * 验证码
     */
    private String mCode;

    /**
     * 手机号是否中注册
     */
    private int page;


    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_findpwd2, null);
    }

    @Override
    public void initPresenter() {
        presenter = new UserInfoPresenter();
        presenter.attachView(this);
    }

    @Override
    public boolean needShowTitle() {
        return false;
    }


    @Override
    public void initView() {
        Intent intent = getIntent();
        mPhone = intent.getStringExtra("phone");
        mCode = intent.getStringExtra("code");
        page = intent.getIntExtra("page", -1);
        if (page == 2) {
            //已注册
//            setTitleText("重设密码");
            mTvTitle.setText("重设密码");
            resetTitle.setText("重设密码后可用手机号登录");
        } else if (page == 1) {
            //未注册
//            setTitleText("手机号注册");
            mTvTitle.setText(getString(R.string.forget_register_phone));
            resetTitle.setText(getString(R.string.forget_no_regist_introduce));
            mTvProtocol.setVisibility(View.VISIBLE);
            TextViewUtils.setSpannableStyle(getString(R.string.protocol), 11, 19, mTvProtocol);
        }

        if(mEditPassWEdit.getText().length()>0){
            mImgWatchPass.setVisibility(View.VISIBLE);
        }else{
            mImgWatchPass.setVisibility(View.GONE);
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

    @Override
    public void initData() {

        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            initLocation();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.location_permission), RC_LOCATION_CONTACTS_PERM, perms);
        }

        mEditPassWEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                mPassW = s + "";
                if (!StringUtils.isEmpty(mPassW)) {
                    mImgClearPassW.setVisibility(View.VISIBLE);
                } else {
                    mImgClearPassW.setVisibility(View.GONE);
                }

                if(mEditPassWEdit.getText().length()>0){
                    mImgWatchPass.setVisibility(View.VISIBLE);
                }else{
                    mImgWatchPass.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mEditPassWEdit.setKeyListener(new DigitsKeyListener() {
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

    @OnClick(R.id.titlebar_main_left_btn)
    public void onClick() {
        finish();
    }

    /**
     * 清除密码
     */
    @OnClick(R.id.id_clear_pass)
    public void clearPw() {
        mEditPassWEdit.setText("");
    }

    /**
     * 跳转服务条款
     */
    @OnClick(R.id.id_find_2_xieyi_txt)
    public void jumpH5() {
//        CommH5Aty.startCommonH5(aty, H5Address.USER_SERVER);
    }

    /**
     * 密码显示隐藏按钮
     */
    @OnClick(R.id.id_watch_pass)
    public void controlPassword() {
        int length = TextUtils.isEmpty(mEditPassWEdit.getText()) ? 0 : mEditPassWEdit.getText().length();
        if (mEditPassWEdit.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
            mEditPassWEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            mImgWatchPass.setImageDrawable(getResources().getDrawable(R.mipmap.pwd_unsee));
        } else {
            mEditPassWEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
            mImgWatchPass.setImageDrawable(getResources().getDrawable(R.mipmap.pwd_see));
        }
        mEditPassWEdit.setSelection(length);
    }

    /**
     * 完成
     */
    @OnClick(R.id.id_find_2_finish_btn)
    public void finishNews() {
        mPassW = mEditPassWEdit.getText().toString().trim();
        if (page == 2) {
            //已注册，直接修改密码
            if (StringUtils.isEmpty(mPassW) || mPassW.length() < 6 || mPassW.length() > 18) {
                SVProgressHUD.showErrorWithStatus(aty, getString(R.string.regist_input_pw));
            } else {
                if (StringUtils.isNumber(mPassW) || StringUtils.isLetterRic(mPassW)) {
                    SVProgressHUD.showErrorWithStatus(aty, getString(R.string.pw_num_letter));
                } else {
                    if (CountDownTimer.isFastClick()) {
                        return;
                    }
                    ((UserInfoPresenter) presenter).changePw(mPhone, HexUtil.encodeHexStr(MD5Util.md5(mPassW)), mCode);
                }
            }
        } else if (page == 1) {
            //未注册,请注册
//            if (mCheckBox.isChecked()) {
                if (StringUtils.isEmpty(mPassW) || mPassW.length() < 6 || mPassW.length() > 18) {
                    SVProgressHUD.showErrorWithStatus(aty, getString(R.string.regist_input_pw));
                } else {
                    if (StringUtils.isNumber(mPassW) || StringUtils.isLetterRic(mPassW)) {
                        SVProgressHUD.showErrorWithStatus(aty, getString(R.string.pw_num_letter));
                    } else {
                        ((UserInfoPresenter) presenter).registerUser(mPhone, HexUtil.encodeHexStr(MD5Util.md5(mPassW)), mCode,location,cityCode);
                    }
                }
//            } else {
//                SVProgressHUD.showErrorWithStatus(aty, getString(R.string.forget_no_agree));
//            }
        }
    }

    @Override
    public boolean hasDataInPage() {
        return true;
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
            if (t instanceof String) {
                Toast.makeText(this, R.string.change_pw_success ,Toast.LENGTH_SHORT).show();
                //重置密码成功
                setResult(IntentCode.ForGetPw.FIND_1_RESULT_CODE);

            } else if (t instanceof UserInfoModel) {
                //注册成功
                UserInfoModel model = (UserInfoModel) t;
                if (null != model) {
                    Session.setCityCode(cityCode);

                    Intent intent = new Intent();
                    intent.putExtra(IntentKey.EXTRA_MODEL, model);
                    setResult(IntentCode.Register.REGISTER_RESULT_CODE, intent);
                }
            }
            finish();
        }
    }

//    @Override
//    public void onLoadingStatus(int loadingStatus, String string) {
//        if (loadingStatus == CodeStatus.Gegeneral.ERROR_DATA) {
//            SVProgressHUDDismiss();
//            if (hasDataInPage()) {
//                if (!StringUtils.isEmpty(string) && !isFinishing()) {
//                    SVProgressHUD.showErrorWithStatus(aty, string);
//                }
//            }
//        } else {
//            super.onLoadingStatus(loadingStatus, string);
//        }
//    }

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

    @Override
    public void refreshPage() {

    }
}
