package com.rz.circled.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.j256.ormlite.table.TableUtils;
import com.litesuits.common.utils.HexUtil;
import com.litesuits.common.utils.MD5Util;
import com.rz.circled.BuildConfig;
import com.rz.circled.R;
import com.rz.circled.application.QHApplication;
import com.rz.circled.db.DBHelper;
import com.rz.circled.js.model.HeaderModel;
import com.rz.circled.modle.ShowListModel;
import com.rz.circled.presenter.IPresenter;
import com.rz.circled.presenter.impl.SnsAuthPresenter;
import com.rz.circled.presenter.impl.UserInfoPresenter;
import com.rz.circled.service.BackGroundService;
import com.rz.common.cache.preference.EntityCache;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.Constants;
import com.rz.common.constant.IntentCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.constant.Type;
import com.rz.common.event.BaseEvent;
import com.rz.common.event.NotifyEvent;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.IntentUtil;
import com.rz.common.utils.NetUtils;
import com.rz.common.utils.StringUtils;
import com.rz.common.utils.SystemUtils;
import com.rz.common.widget.SwipeBackLayout;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.httpapi.bean.FriendInformationBean;
import com.rz.httpapi.bean.LoginTypeBean;
import com.rz.httpapi.bean.UserInfoBean;
import com.umeng.socialize.UMShareAPI;
import com.zhuge.analysis.stat.ZhugeSDK;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;


/**
 * Created by Administrator on 2017/9/4 0004.
 */

public class LoginActivity extends BaseActivity {

    public String TAG;
    protected IPresenter presenter;
    protected IPresenter userPresenter;
    protected SwipeBackLayout layout;

    @BindView(R.id.id_login_register_btn)
    TextView idLoginRegisterBtn;
    @BindView(R.id.id_login_pw_btn)
    TextView idLoginPwBtn;
    @BindView(R.id.reg_layout)
    LinearLayout regLayout;
    @BindView(R.id.layout_phone_code)
    TextView layoutLoginPhone;
    @BindView(R.id.layout_login_weixin)
    TextView layoutLoginWeixin;
    @BindView(R.id.layout_login_webo)
    TextView layoutLoginWebo;
    private long lastClickTime;

    @BindView(R.id.id_watch_pass)
    ImageView mImgWatchPw;

    @BindView(R.id.titlebar_main_left_btn)
    ImageView mIvBack;

    /**
     * 手机号
     */
    @BindView(R.id.id_login_edit_phone)
    EditText mEditPhone;
    private String mPhone;

    @BindView(R.id.id_login_clear_phone)
    ImageView mImgClearPhone;

    /**
     * 密码
     */
    @BindView(R.id.id_login_edit_pw)
    EditText mEditPass;
    private String mPassword;

    @BindView(R.id.id_login_clear_pw)
    ImageView mImgClearPass;

    /**
     * 登录
     */
    @BindView(R.id.id_login_btn)
    Button mLoginBtn;

    @BindView(R.id.id_regist_send_sms_btn)
    Button mBtnSendCode;

    @BindView(R.id.pwd_type_img)
    ImageView typePwd;


    private int loginType;
    private int mGuideType;
    UserInfoBean loginModel;

    private int tabHome;

    private int codeType = 1;
    /**
     * 倒计时类
     */
    private MyCount mc;

    @Override
    protected boolean needSwipeBack() {
        return false;
    }

    @Override
    public boolean needShowTitle() {
        return false;
    }

    @Override
    protected boolean needLoadingView() {
        return true;
    }

    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_login, null);
    }

    @Override
    public void initPresenter() {
        presenter = new SnsAuthPresenter();
        userPresenter = new UserInfoPresenter();
        presenter.attachView(this);
        userPresenter.attachView(this);
    }

    @Override
    public void onBackPressed() {
        if (codeType == 1) {
            Intent intent = new Intent();
            intent.putExtra(IntentKey.EXTRA_BOOLEAN, true);
            setResult(IntentCode.Login.LOGIN_RESULT_CODE, intent);
            finish();
        } else {
            setData();
        }

    }

    @Override
    public void initView() {


//        mBtnSendCode.setVisibility(View.VISIBLE);
//        mEditPass.setHint("请输入验证码");
//        typePwd.setImageResource(R.mipmap.icon_code);
//        mImgWatchPw.setVisibility(View.GONE);
//        mEditPass.setText("");
//        mEditPass.setInputType(InputType.TYPE_CLASS_NUMBER);


//        //动态设置top图片
//        Drawable drawable = getResources().getDrawable(R.mipmap.pwd_lock_ic);
//        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//        layoutLoginPhone.setCompoundDrawables(null, drawable, null, null);
//        layoutLoginPhone.setText("密码登录");

        if (!StringUtils.isEmpty(Session.getUserPhone())) {
            mEditPhone.setText(Session.getUserPhone());
            mImgClearPhone.setVisibility(View.VISIBLE);
        }

        mLoginBtn.setEnabled(true);

    }

    @Override
    public void initData() {
        mEditPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPhone = s + "";
                if (!StringUtils.isEmpty(mPhone)) {
                    mImgClearPhone.setVisibility(View.VISIBLE);
                } else {
                    mImgClearPhone.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mEditPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPassword = s + "";
                if (!StringUtils.isEmpty(mPassword)) {
                    mImgClearPass.setVisibility(View.VISIBLE);
                } else {
                    mImgClearPass.setVisibility(View.GONE);
                }

                if (codeType == 1) {        //密码登录
                    if (mEditPass.getText().length() > 0) {
                        mImgWatchPw.setVisibility(View.VISIBLE);
                    } else {
                        mImgWatchPw.setVisibility(View.GONE);
                    }
                } else {                    //验证码登录
                    mImgWatchPw.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
//        loginType = getIntent().getIntExtra(IntentKey.KEY_TYPE, -1);
        mGuideType = getIntent().getIntExtra(IntentKey.GUIDE_KEY, -1);
        loginType = getIntent().getIntExtra(IntentKey.EXTRA_TYPE, -1);
        tabHome = getIntent().getIntExtra(IntentKey.EXTRA_TAB, -1);
    }


    @OnClick(R.id.titlebar_main_left_btn)
    public void onClick() {

        setData();
    }


    /**
     * 微信登录
     */
    @OnClick(R.id.layout_login_weixin)
    public void wxLogin() {
        if (isFastClick(7000)) {
            return;
        }
        if (presenter != null) {

            ((SnsAuthPresenter) presenter).setActionBind(-1);
            ((SnsAuthPresenter) presenter).wxAuth(true);
        }
    }

    /**
     * 新浪登录
     */
    @OnClick(R.id.layout_login_webo)
    public void sinaLogin() {
        if (isFastClick(7000)) {
            return;
        }
        if (presenter != null) {

            ((SnsAuthPresenter) presenter).setActionBind(-1);
            ((SnsAuthPresenter) presenter).wbAuth(true);
        }
    }

    /**
     * 手机验证码登录
     */
    @OnClick(R.id.layout_phone_code)
    public void codeLogin() {
        setData();
    }


    private void setData() {

        if (codeType == 1) {
            //动态设置top图片
            Drawable drawable = getResources().getDrawable(R.mipmap.pwd_lock_ic);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            layoutLoginPhone.setCompoundDrawables(null, drawable, null, null);
            layoutLoginPhone.setText("密码登录");
            mEditPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

            mBtnSendCode.setVisibility(View.VISIBLE);
            mEditPass.setHint("请输入验证码");
            typePwd.setImageResource(R.mipmap.icon_code);
            mImgWatchPw.setVisibility(View.GONE);
            mEditPass.setText("");
            mEditPass.setInputType(InputType.TYPE_CLASS_NUMBER);
            mIvBack.setVisibility(View.VISIBLE);

            codeType = 2;

        } else {

            //动态设置top图片
            Drawable drawable = getResources().getDrawable(R.mipmap.other_login_icon);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            layoutLoginPhone.setCompoundDrawables(null, drawable, null, null);
            layoutLoginPhone.setText("验证码登录");

            mBtnSendCode.setVisibility(View.GONE);
            mEditPass.setHint("请输入密码");
            mEditPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
            mImgWatchPw.setVisibility(View.VISIBLE);
            typePwd.setImageResource(R.mipmap.ic_login_pw);
            mEditPass.setText("");
            mEditPass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mIvBack.setVisibility(View.GONE);

            codeType = 1;

        }


    }

    @OnClick(R.id.id_regist_send_sms_btn)
    public void getCode() {

        mPhone = mEditPhone.getText().toString();
        if (!StringUtils.isMobile(mPhone)) {
            SVProgressHUD.showErrorWithStatus(aty, getString(R.string.input_right_phone));
            return;
        }
        if (!NetUtils.isNetworkConnected(aty)) {
            SVProgressHUD.showErrorWithStatus(aty, getString(R.string.no_net_work));
        } else {
            BackGroundService.countDownCode(Constants.COUNTDOWN * 1000);
            startCount(Constants.COUNTDOWN * 1000);
            ((UserInfoPresenter) userPresenter).getVeriCode(mPhone, Type.FUNCTION_CODE_9);
        }
    }

    /**
     * 清除手机号
     */
    @OnClick(R.id.id_login_clear_phone)
    public void clearPhone() {
        mEditPhone.setText("");
    }

    /**
     * 清除密码
     */
    @OnClick(R.id.id_login_clear_pw)
    public void clearPw() {
        mEditPass.setText("");
    }

    @OnClick(R.id.id_watch_pass)
    public void exchangePwd() {
        int length = TextUtils.isEmpty(mEditPass.getText()) ? 0 : mEditPass.getText().length();
        if (mEditPass.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
            mEditPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            mImgWatchPw.setImageDrawable(getResources().getDrawable(R.mipmap.pwd_unsee));
        } else {
            mEditPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
            mImgWatchPw.setImageDrawable(getResources().getDrawable(R.mipmap.pwd_see));
        }
        mEditPass.setSelection(length);

    }


    /**
     * 手机号登录操作
     */
    @OnClick(R.id.id_login_btn)
    public void loginBtn() {
        if (isFastClick(2000)) {
            return;
        }
        mPhone = mEditPhone.getText().toString().trim();
        mPassword = mEditPass.getText().toString().trim();
        if (StringUtils.isMobile(mPhone)) {

            if (codeType == 1) { //密码登录

                if (mPassword.length() >= 6 && mPassword.length() <= 18) {
                    ((SnsAuthPresenter) presenter).loginRequest(mPhone, HexUtil.encodeHexStr(MD5Util.md5(mPassword)));
                } else {
                    SVProgressHUD.showErrorWithStatus(mContext, getString(R.string.password_error));
                }

            } else {        //验证码登陆
                if (mPassword.length() == 4) {
                    ((SnsAuthPresenter) presenter).codeLogin(mPhone, mPassword);
                } else {
                    SVProgressHUD.showErrorWithStatus(mContext, getString(R.string.passcode_error));
                }
            }
        } else {
            SVProgressHUD.showErrorWithStatus(aty, getString(R.string.phone_error));
        }
    }

    /**
     * 注册操作
     */
    @OnClick(R.id.id_login_register_btn)
    public void registerBtn() {
        Intent intent = new Intent(aty, RegisterActivity.class);
        intent.putExtra(IntentKey.EXTRA_TYPE, loginType);
        startActivityForResult(intent, IntentCode.Login.LOGIN_REQUEST_CODE);
//        startActivity(intent);
    }

    /**
     * 忘记密码操作
     */
    @OnClick(R.id.id_login_pw_btn)
    public void forgetPw() {
        Intent forget = new Intent(aty, FindPwdActivity.class);
        forget.putExtra(IntentKey.EXTRA_TYPE, loginType);
        startActivityForResult(forget, IntentCode.Login.LOGIN_REQUEST_CODE);
    }

    @Override
    public boolean hasDataInPage() {
        return true;
    }

    /**
     * 登录成功
     *
     * @param t
     * @param <T>
     */
    @Override
    public <T> void updateView(T t) {
        if (null != t) {
            loginModel = (UserInfoBean) t;
            if (null != loginModel) {
                zhugeIdentify(loginModel);
                Session.setSessionKey(loginModel.getToken());
                switch (Session.getLoginWay()) {
                    case Type.LOGIN_WX:
                        zhugeTrack("wx");
                        ((UserInfoPresenter) userPresenter).verfityBoundPhone(loginModel.getCustId());
                        return;
                    case Type.LOGIN_SINA:
                        zhugeTrack("sina");
                        ((UserInfoPresenter) userPresenter).verfityBoundPhone(loginModel.getCustId());
                        return;
                    default:
                        zhugeTrack("phonenum");
                        break;
                }

                saveLoginData(loginModel);

                if (getIntent().getBooleanExtra("isFromSplash", false)) {
                    skipActivity(aty, MainActivity.class);
                } else if (loginType == Type.TYPE_LOGIN_WEB) {
                    //从圈子过来跳转登录的
//                    JsEvent.callJsEvent(getLoginWebResultData(), true);
                } else if (mGuideType == Type.TYPE_LOGIN_GUIDE) {
                    //从向导页面过来

                    skipActivity(aty, FollowCircle.class);
                } else {

                    EventBus.getDefault().post(new BaseEvent(CommonCode.EventType.TYPE_LOGIN));

                    setResult(IntentCode.Login.LOGIN_RESULT_CODE);
                    skipActivity(aty, MainActivity.class);

                }
                //webCon
                BaseEvent baseEvent = new BaseEvent(getLoginWebResultData());
                baseEvent.type = CommonCode.EventType.TYPE_LOGIN_WEB;
                baseEvent.key = "nativeLogin";
                EventBus.getDefault().post(baseEvent);
            }
        }
    }

    //登录成功后保存数据
    private void saveLoginData(UserInfoBean model) {
        //单独记录随手晒缓存记录，防止刷新
        if (TextUtils.equals(Session.getBeforeUserId(), model.getCustId())) {
            EntityCache entityCache = new EntityCache<ShowListModel>(this, ShowListModel.class);
            List<ShowListModel> showCaches = entityCache.getListEntity(ShowListModel.class);
//                    ClearCacheUtil.clearCache(aty, 1, Session.getUserId());
            entityCache.putListEntity(showCaches);
        } else {
//                    ClearCacheUtil.clearCache(aty, 1, model.getCustId());
        }
        //诸葛标识用户
        JSONObject json = new JSONObject();
        ZhugeSDK.getInstance().identify(getApplicationContext(), model.getCustId(), json);
        Session.setUserIsLogin(true);
        Session.setUserId(model.getCustId());
        Session.setUserPicUrl(model.getCustImg());
        Session.setSessionKey(model.getToken());
        Session.setUserPhone(model.getCustPhone());
        Session.setUserLocalUrl(model.getCustQr());
        Session.setUserName(model.getCustNname());
        Session.setUser_signatrue(model.getCustSignature());
        Session.setUser_desc(model.getCustDesc());
        Session.setCityCode(model.getCityCode());
        Session.setCustPoints(model.getCustPoints());
        Session.setUserLevel(model.getCustLevel());
        Session.setCustRole(model.getCustRole());
        if (TextUtils.equals("0", model.getCustSex())) {
            Session.setUser_sex("女");
        } else if (TextUtils.equals("1", model.getCustSex())) {
            Session.setUser_sex("男");
        } else {
            Session.setUser_sex("");
        }
        Session.setUser_area(model.getCustLocation());
        if (Type.HAD_SET_PW == model.getIsPayPassword()) {
            //设置支付密码
            Session.setUserSetpaypw(true);
        } else {
            Session.setUserSetpaypw(false);
        }
        if (Type.OPEN_EASY_PAY == model.getSmallNopass()) {
            //开启免密支付
            Session.setIsOpenGesture(true);
        } else {
            Session.setIsOpenGesture(false);
        }
        if (model.getIsPwdExist() == 1) {
            //设置了登录密码
            Session.setUserLoginPw(true);
        } else {
            Session.setUserLoginPw(false);
        }

        // 调用 Handler 来异步设置别名
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, Session.getUserId()));

        if (!TextUtils.equals(Session.getUserId(), Session.getBeforeUserId())) {
            EntityCache.clean(mContext);
        }
        if (!TextUtils.equals(Session.getUserId(), Session.getBeforeUserId())) {
            try {
                TableUtils.dropTable(DBHelper.getHelper(mContext).getConnectionSource(), FriendInformationBean.class, true);
                TableUtils.createTable(DBHelper.getHelper(mContext).getConnectionSource(), FriendInformationBean.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * web重新登录返回数据
     */
    private HeaderModel getLoginWebResultData() {
        HeaderModel headerModel = new HeaderModel();
        headerModel.sign = "sign";
        headerModel.token = Session.getSessionKey();
        headerModel.devType = "2";
        try {
            headerModel.devName = URLEncoder.encode(Build.MODEL, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        headerModel.appVersion = BuildConfig.VERSION_NAME;
        headerModel.devId = SystemUtils.getIMEI(QHApplication.getContext());
        headerModel.ip = SystemUtils.getIp(QHApplication.getContext());
        headerModel.net = IntentUtil.getNetType(QHApplication.getContext());
        headerModel.custId = Session.getUserId();
        headerModel.userId = Session.getJsUserId();
        headerModel.phone = Session.getUserPhone();
        headerModel.cityCode = Session.getCityCode();
        return headerModel;
    }

    @Override
    public <T> void updateViewWithFlag(T t, int flag) {
        super.updateViewWithFlag(t, flag);

        if (t != null && t instanceof List) {
            List<LoginTypeBean> model = (List<LoginTypeBean>) t;
            if (model.size() > 0 && model.size() == 4) {
                //已经绑定过手机直接登录
                if (!TextUtils.isEmpty(model.get(3).getCreateDate())) {
                    if (Session.getUserIsFirstDownload()) {
                        saveLoginData(loginModel);
                        skipActivity(aty, FollowCircle.class);
//
                    }else {
                        saveLoginData(loginModel);
                        skipActivity(aty, MainActivity.class);
                    }
                } else {
                    //传递登录时返回的对象
                    if (loginModel != null) {
                        Intent intent = new Intent(this, BoundPhoneActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(IntentKey.LOGIN_MODEL, loginModel);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            } else {
                //未绑定手机，前往绑定手机好
                skipActivity(this, BoundPhoneActivity.class);
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent baseEvent) {
        if (baseEvent.type == CommonCode.EventType.TYPE_SAVE) {
            UserInfoBean loginModel = (UserInfoBean) baseEvent.getData();
            if (loginModel != null) {
                saveLoginData(loginModel);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentCode.Login.LOGIN_REQUEST_CODE) {
            if (resultCode == IntentCode.Register.REGISTER_RESULT_CODE) {
                //注册成功(包括从忘记密码中进入)
                if (null != data) {
                    UserInfoBean model = (UserInfoBean) data.getSerializableExtra("111");
                    if (null != model) {
                        updateView(model);
                        finish();
                        Log.e("", "");
                    }
                }
            }
//            else if (resultCode == IntentCode.Register.REGISTER_RESULT_ONLY_CODE) {
//                //或者直接手机号注册
//
//            }
        }
    }

    public boolean isFastClick(long timeMillis) {
        long time = System.currentTimeMillis();
        Log.d("yeying", "time " + time);
        Log.d("yeying", "lastClickTime " + lastClickTime);
        if (time - lastClickTime < timeMillis) {
            return true;
        }
        Log.d("yeying", "time " + time);
        lastClickTime = time;
        return false;
    }


    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Set<String> sset = new HashSet<String>();
            sset.add(Constants.Lottery_Tag);
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d(TAG, "Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            (String) msg.obj,
                            sset,
                            mAliasCallback);
                    break;
                default:
                    Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }
        }
    };

    private void zhugeIdentify(UserInfoBean user) {
        //定义用户识别码
        String userid = user.getCustId();

        //定义用户属性
        JSONObject personObject = new JSONObject();
        if (TextUtils.equals("0", user.getCustSex())) {
            Session.setUser_sex("女");
        } else {
            Session.setUser_sex("男");
        }
        try {
            personObject.put("用户id", user.getCustId());
            personObject.put("性别", Session.getUser_sex());
            personObject.put("地域", Session.getUser_area());
            personObject.put("用户昵称", user.getCustNname());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //标识用户
        ZhugeSDK.getInstance().identify(getApplicationContext(), userid,
                personObject);
    }

    private void zhugeTrack(String loginType) {
        //定义与事件相关的属性信息
        JSONObject eventObject = new JSONObject();
        try {
            eventObject.put("登录", loginType);
        } catch (Exception e) {

        }

        //记录事件
        ZhugeSDK.getInstance().track(getApplicationContext(), "注册登录", eventObject);
    }

    @Subscribe
    public void onEvent(NotifyEvent notifyEvent) {
        if (notifyEvent != null && notifyEvent.tag.equals("register")) {
            Session.setLoginWay(Type.LOGIN_PHONE);
            UserInfoBean model = (UserInfoBean) notifyEvent.object;
            if (null != model) {
                updateView(model);
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
    public void refreshPage() {

    }


}
