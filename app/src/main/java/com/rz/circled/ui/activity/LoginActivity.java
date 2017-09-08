package com.rz.circled.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.litesuits.common.utils.HexUtil;
import com.litesuits.common.utils.MD5Util;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.rz.circled.R;
import com.rz.circled.modle.ShowListModel;
import com.rz.circled.presenter.IPresenter;
import com.rz.circled.presenter.impl.SnsAuthPresenter;
import com.rz.circled.widget.CommomUtils;
import com.rz.common.cache.preference.EntityCache;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.Constants;
import com.rz.common.constant.IntentCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.constant.Type;
import com.rz.common.event.BaseEvent;
import com.rz.common.event.NotifyEvent;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.StringUtils;
import com.rz.common.widget.SwipeBackLayout;
import com.rz.common.widget.svp.SVProgressHUD;
import com.rz.httpapi.bean.UserInfoBean;
import com.yryz.yunxinim.DemoCache;
import com.yryz.yunxinim.config.preference.Preferences;
import com.yryz.yunxinim.config.preference.UserPreferences;
import com.yryz.yunxinim.uikit.cache.DataCacheManager;
import com.yryz.yunxinim.uikit.common.util.log.LogUtil;
import com.zhuge.analysis.stat.ZhugeSDK;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Administrator on 2017/9/4 0004.
 */

public class LoginActivity extends BaseActivity {

    public String TAG;
    protected IPresenter presenter;
    protected IPresenter homeBannerPresenter;
    protected SwipeBackLayout layout;

    @BindView(R.id.id_login_register_btn)
    TextView idLoginRegisterBtn;
    @BindView(R.id.id_login_pw_btn)
    TextView idLoginPwBtn;
    @BindView(R.id.reg_layout)
    LinearLayout regLayout;
    @BindView(R.id.layout_login_qq)
    TextView layoutLoginQq;
    @BindView(R.id.layout_login_weixin)
    TextView layoutLoginWeixin;
    @BindView(R.id.layout_login_webo)
    TextView layoutLoginWebo;
    private long lastClickTime;

    @BindView(R.id.id_watch_pass)
    ImageView mImgWatchPw;

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
    TextView mLoginBtn;

//    /**
//     * 3.0版本title
//     */
//    @BindView(R.id.titlebar_main_tv)
//    TextView mTvTitle;
    @BindView(R.id.titlebar_main_left_btn)
    ImageView mIvBack;
//    @BindView(R.id.titlebar_root)
//    RelativeLayout mRlTitleRoot;
    private int loginType;
    private int mGuideType;


    @Override
    public boolean needShowTitle() {
        return false;
    }


    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_login, null);
    }

    @Override
    public void initPresenter() {
        presenter = new SnsAuthPresenter();
        presenter.attachView(this);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(IntentKey.EXTRA_BOOLEAN, true);
        setResult(IntentCode.Login.LOGIN_RESULT_CODE, intent);
        finish();
    }

    @Override
    public void initView() {

//        mTvTitle.setText(R.string.login);
        mIvBack.setVisibility(View.VISIBLE);
//        mIvBack.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.selector_titlebar_back));
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        mRlTitleRoot.setBackgroundColor(ContextCompat.getColor(this, R.color.color_5ACBD4));
//        setTitleText(getString(R.string.login_title));
        if (!StringUtils.isEmpty(Session.getUserPhone())) {
            mEditPhone.setText(Session.getUserPhone());
            mImgClearPhone.setVisibility(View.VISIBLE);
        }
//        setTitlebackIntercept(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
        mLoginBtn.setEnabled(true);

        if(mEditPass.getText().length()>0){
            mImgWatchPw.setVisibility(View.VISIBLE);
        }else{
            mImgWatchPw.setVisibility(View.GONE);
        }
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

                if(mEditPass.getText().length()>0){
                    mImgWatchPw.setVisibility(View.VISIBLE);
                }else{
                    mImgWatchPw.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        loginType = getIntent().getIntExtra(IntentKey.KEY_TYPE, -1);
        mGuideType = getIntent().getIntExtra(IntentKey.GUIDE_KEY,-1);
        loginType = getIntent().getIntExtra(IntentKey.EXTRA_TYPE, -1);
    }

//    /**
//     * qq登录
//     */
//    @OnClick(R.id.layout_login_qq)
//    public void qqLogin() {
//        if (isFastClick(7000)) {
//            return;
//        }
//        if (presenter != null) {
////            mEditPhone.setText("");
////            mEditPass.setText("");
//            ((SnsAuthPresenter) presenter).setActionBind(-1);
//            ((SnsAuthPresenter) presenter).qqAuth(true);
//        }
//    }
//
//    /**
//     * 微信登录
//     */
//    @OnClick(R.id.layout_login_weixin)
//    public void wxLogin() {
//        if (isFastClick(7000)) {
//            return;
//        }
//        if (presenter != null) {
////            mEditPhone.setText("");
////            mEditPass.setText("");
//            ((SnsAuthPresenter) presenter).setActionBind(-1);
//            ((SnsAuthPresenter) presenter).wxAuth(true);
//        }
//    }
//
//    /**
//     * 新浪登录
//     */
//    @OnClick(R.id.layout_login_webo)
//    public void sinaLogin() {
//        if (isFastClick(7000)) {
//            return;
//        }
//        if (presenter != null) {
////            mEditPhone.setText("");
////            mEditPass.setText("");
//            ((SnsAuthPresenter) presenter).setActionBind(-1);
//            ((SnsAuthPresenter) presenter).wbAuth(true);
//        }
//    }

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
            mImgWatchPw.setImageDrawable(getResources().getDrawable(R.mipmap.pwd_see));
        } else {
            mEditPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
            mImgWatchPw.setImageDrawable(getResources().getDrawable(R.mipmap.pwd_unsee));
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
            if (mPassword.length() >= 6 && mPassword.length() <= 18) {
                ((SnsAuthPresenter) presenter).loginRequest(mPhone, HexUtil.encodeHexStr(MD5Util.md5(mPassword)));
            } else {
                SVProgressHUD.showErrorWithStatus(aty, getString(R.string.password_error));
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
        CommomUtils.trackUser("注册登录", "注册", "");
        Intent intent = new Intent(aty, RegisterActivity.class);
        intent.putExtra(IntentKey.EXTRA_TYPE, loginType);
//        startActivityForResult(intent, IntentCode.Login.LOGIN_REQUEST_CODE);
        startActivity(intent);
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
        super.updateView(t);
        if (null != t) {
            UserInfoBean model = (UserInfoBean) t;
            if (null != model) {
//                zhugeIdentify(model);
//                switch (Session.getLoginWay()) {
//                    case Type.LOGIN_QQ:
////                        MobclickAgent.onProfileSignIn("qq", model.getCustId());
//                        zhugeTrack("qq");
//                        break;
//                    case Type.LOGIN_WX:
////                        MobclickAgent.onProfileSignIn("wx", model.getCustId());
//                        zhugeTrack("wx");
//                        break;
//                    case Type.LOGIN_SINA:
////                        MobclickAgent.onProfileSignIn("sina", model.getCustId());
//                        zhugeTrack("sina");
//                        break;
//                    default:
////                        MobclickAgent.onProfileSignIn(model.getCustId());
//                        zhugeTrack("phonenum");
//                        break;
//                }

//                MobclickAgent.onEvent(aty, "login");
                //单独记录随手晒缓存记录，防止刷新
                if (TextUtils.equals(Session.getBeforeUserId(),model.getCustId())){
                    EntityCache entityCache = new EntityCache<ShowListModel>(this, ShowListModel.class);
                    List<ShowListModel> showCaches = entityCache.getListEntity(ShowListModel.class);
//                    ClearCacheUtil.clearCache(aty, 1, Session.getUserId());
                    entityCache.putListEntity(showCaches);
                }else{
//                    ClearCacheUtil.clearCache(aty, 1, model.getCustId());
                }

                Session.setUserIsLogin(true);
                Session.setUserId(model.getCustId());
                Session.setUserPicUrl(model.getCustImg());
                Session.setSessionKey(model.getToken());
                Session.setUserPhone(model.getCustPhone());
                Session.setUserLocalUrl(model.getCustQr());
                Session.setUserName(model.getCustNname());
                Session.setUser_signatrue(model.getCustSignature());
                Session.setUser_desc(model.getCustDesc());
                if (TextUtils.equals("0", model.getCustSex())) {
                    Session.setUser_sex("女");
                } else {
                    Session.setUser_sex("男");
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

                Set<String> sset = new HashSet<String>();
                sset.add(Constants.Lottery_Tag);

                // 调用 Handler 来异步设置别名
                mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, Session.getUserId()));

//                loadRewardGiftList();//加载转发打赏礼物列表

                if (getIntent().getBooleanExtra("isFromSplash", false)) {
                    skipActivity(aty, MainActivity.class);
                } else if (loginType == Type.TYPE_LOGIN_WEB) {
                    //从圈子过来跳转登录的
//                    JsEvent.callJsEvent(getLoginWebResultData(), true);
                    finish();
                } else if(mGuideType == Type.TYPE_LOGIN_GUIDE){
                    //从向导页面过来

                    skipActivity(aty, FollowCircle.class);
                    finish();
                }else {
                    BaseEvent event = new BaseEvent();
//                    event.key = LOGIN_IN_SUCCESS;
                    EventBus.getDefault().post(event);

                    EventBus.getDefault().post(new NotifyEvent("login", null, false));

                    setResult(IntentCode.Login.LOGIN_RESULT_CODE);
                    skipActivity(aty, MainActivity.class);
                    finish();
                }


//                loginYunXin(model.getCustId(), model.getCustId());

            }
        }
    }

//    /**
//     * web跳转登录返回数据
//     */
//    private HeaderModel getLoginWebResultData() {
//        HeaderModel headerModel = new HeaderModel();
//        headerModel.sign = "sign";
//        headerModel.token = Session.getSessionKey();
//        headerModel.devType = "2";
////        String sign = DesUtils.encrypt(act + "." + Session.getUserId() + "." + Session.getSessionKey()).replace("\\s", "").replace("\n", "");
////        headerModel.apiVersion ="V3.0.0";
//        try {
//            headerModel.devName = URLEncoder.encode(Build.MODEL, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        headerModel.appVersion = BuildConfig.VERSION_NAME;
//        headerModel.devId = SystemUtils.getIMEI(App.getContext());
//        headerModel.ip = SystemUtils.getIp(App.getContext());
//        headerModel.net = IntentUtil.getNetType(App.getContext());
//        headerModel.custId = Session.getUserId();
//        return headerModel;
//    }
//
//    public void loadRewardGiftList() {
//        Call<ResponseData<List<RewardGiftModel>>> call = Http.getNewService(this).v3CircleGetTransferPrice(
//                Session.getUserId());
//        CallManager.add(call);
//        call.enqueue(new BaseCallback<ResponseData<List<RewardGiftModel>>>() {
//            @Override
//            public void onResponse(Call<ResponseData<List<RewardGiftModel>>> call, Response<ResponseData<List<RewardGiftModel>>> response) {
//                super.onResponse(call, response);
//                if (response.isSuccessful()) {
//                    ResponseData<List<RewardGiftModel>> res = response.body();
//                    if (res.getRet() == ReturnCode.SUCCESS) {
//                        List<RewardGiftModel> mGifts = res.getData();
//                        if (mGifts != null && !mGifts.isEmpty()) {
//                            try {
//                                EntityCache<RewardGiftModel> entityCache = new EntityCache<RewardGiftModel>(LoginAty.this, RewardGiftModel.class);
//                                entityCache.putListEntityAddTag(mGifts, "transfer");
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            return;
//                        } else {
//                            return;
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseData<List<RewardGiftModel>>> call, Throwable t) {
//                super.onFailure(call, t);
//            }
//        });
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
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

    private void loginYunXin(final String account, final String token) {
        AbortableFuture<LoginInfo> loginRequest;
        // 云信只提供消息通道，并不包含用户资料逻辑。开发者需要在管理后台或通过服务器接口将用户帐号和token同步到云信服务器。
        // 在这里直接使用同步到云信服务器的帐号和token登录。
        // 这里为了简便起见，demo就直接使用了密码的md5作为token。
        // 如果开发者直接使用这个demo，只更改appkey，然后就登入自己的账户体系的话，需要传入同步到云信服务器的token，而不是用户密码。
        // final String account = "wh5120051".toLowerCase();
        // final String token = MD5.getStringMD5("111111");
        // 登录
        loginRequest = NIMClient.getService(AuthService.class).login(new LoginInfo(account, token));
        loginRequest.setCallback(new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
                LogUtil.i(TAG, "login success");

                DemoCache.setAccount(account);
                Preferences.saveUserAccount(account);
                Preferences.saveUserToken(token);

                // 初始化消息提醒
                NIMClient.toggleNotification(UserPreferences.getNotificationToggle());

//                // 初始化免打扰
//                if (UserPreferences.getStatusConfig() == null) {
//                    initStatusBarNotificationConfig();
//                }
                NIMClient.updateStatusBarNotificationConfig(UserPreferences.getStatusConfig());

                // 构建缓存
                DataCacheManager.buildDataCacheAsync();


//                BaseEvent event = new BaseEvent();
//                event.key = "110";
//                EventBus.getDefault().post(event);
//
//                EventBus.getDefault().post(new NotifyEvent("login", null, false));
//
//                setResult(IntentCode.Login.LOGIN_RESULT_CODE);
//
//                finish();
            }

            @Override
            public void onFailed(int code) {
                if (code == 302 || code == 404) {
                    Toast.makeText(getApplicationContext(), com.yryz.yunxinim.R.string.login_failed, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "登录失败: " + code, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onException(Throwable exception) {
                Toast.makeText(getApplicationContext(), com.yryz.yunxinim.R.string.login_exception, Toast.LENGTH_LONG).show();
            }
        });
    }

//    private void initStatusBarNotificationConfig() {
//        // 如果将新消息通知提醒托管给SDK完成，需要添加以下配置。
//        StatusBarNotificationConfig config = UserPreferences.getStatusConfig();
//        if (config == null) {
//            config = new StatusBarNotificationConfig();
//        }
//        // 点击通知需要跳转到的界面
//        config.notificationEntrance = MainActivity.class;
//        config.notificationSmallIconId = R.mipmap.icon;
//
//        // 通知铃声的uri字符串
//        config.notificationSound = "android.resource://com.netease.nim.demo/raw/msg";
//
//        // 呼吸灯配置
//        config.ledARGB = Color.GREEN;
//        config.ledOnMs = 1000;
//        config.ledOffMs = 1500;
//
//        DemoCache.setNotificationConfig(config);
//        UserPreferences.setStatusConfig(config);
//    }

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
//                    JPushInterface.setAliasAndTags(getApplicationContext(),
//                            (String) msg.obj,
//                            sset,
//                            mAliasCallback);
                    break;
                default:
                    Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };


//    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
//        @Override
//        public void gotResult(int code, String alias, Set<String> tags) {
//            String logs;
//            switch (code) {
//                case 0:
//                    logs = "Set tag and alias success";
//                    Log.i(TAG, logs);
//                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
//                    break;
//                case 6002:
//                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
//                    Log.i(TAG, logs);
//                    // 延迟 60 秒来调用 Handler 设置别名
//                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
//                    break;
//                default:
//                    logs = "Failed with errorCode = " + code;
//                    Log.e(TAG, logs);
//            }
//        }
//    };

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

    @OnClick(R.id.titlebar_main_left_btn)
    public void onClick() {
        finish();
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
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
