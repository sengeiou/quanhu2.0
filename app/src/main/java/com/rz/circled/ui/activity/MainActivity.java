package com.rz.circled.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.Toast;

import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.MsgService;
import com.rz.circled.R;
import com.rz.circled.constants.NewsTypeConstants;
import com.rz.circled.dialog.DefaultTipsDialog;
import com.rz.circled.event.EventConstant;
import com.rz.circled.helper.BannerJumpHelper;
import com.rz.circled.presenter.impl.SnsAuthPresenter;
import com.rz.circled.presenter.impl.UpdateOrExitPresenter;
import com.rz.circled.ui.fragment.FindFragment;
import com.rz.circled.ui.fragment.HomeFragment;
import com.rz.circled.ui.fragment.MineFragment;
import com.rz.circled.ui.fragment.PrivateCircledFragment;
import com.rz.circled.ui.fragment.RewardFragment;
import com.rz.circled.widget.CustomFragmentTabHost;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.Constants;
import com.rz.common.constant.Type;
import com.rz.common.event.BaseEvent;
import com.rz.common.event.KickEvent;
import com.rz.common.permission.EasyPermissions;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.BadgeUtil;
import com.rz.common.utils.ClickCounter;
import com.rz.common.utils.StatusBarUtils;
import com.rz.httpapi.api.ApiNewsService;
import com.rz.httpapi.api.BaseCallback;
import com.rz.httpapi.api.Http;
import com.rz.httpapi.api.ResponseData.ResponseData;
import com.rz.httpapi.bean.NewsUnreadBean;
import com.tencent.bugly.beta.Beta;
import com.yryz.yunxinim.DemoCache;
import com.yryz.yunxinim.config.preference.Preferences;
import com.yryz.yunxinim.config.preference.UserPreferences;
import com.yryz.yunxinim.login.LogoutHelper;
import com.yryz.yunxinim.uikit.LoginSyncDataStatusObserver;
import com.yryz.yunxinim.uikit.cache.DataCacheManager;
import com.yryz.yunxinim.uikit.common.activity.UI;
import com.yryz.yunxinim.uikit.common.ui.dialog.DialogMaker;
import com.yryz.yunxinim.uikit.common.util.log.LogUtil;
import com.yryz.yunxinim.uikit.common.util.string.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;
import retrofit2.Call;
import retrofit2.Response;

import static com.rz.common.constant.Constants.FIRST_BLOOD;
import static com.rz.common.constant.IntentKey.JUMP_FIND_FIRST;
import static com.rz.common.utils.SystemUtils.trackUser;


public class MainActivity extends BaseActivity implements TabHost.OnTabChangeListener, CustomFragmentTabHost.InterceptTagChanged {

    @BindView(android.R.id.tabhost)
    CustomFragmentTabHost tabHost;

    private ImageView mUnread;
    private ClickCounter mCounter;
    private Toast mToast;

    private String[] tabTags = new String[]{"首页", "发现", "悬赏", "私圈", "我的"};

    @Override
    protected View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_main, null);
    }

    @Override
    protected boolean needShowTitle() {
        return false;
    }

    @Override
    protected boolean needSwipeBack() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        processIntent();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        String type = getIntent().getStringExtra(JUMP_FIND_FIRST);
        tabHost.setup(this, getSupportFragmentManager(), R.id.fl_main_content);
        tabHost.setOnTabChangedListener(this);
        tabHost.setInterceptTagChanged(this);
        tabHost.getTabWidget().setDividerDrawable(null);
        tabHost.addTab(tabHost.newTabSpec(tabTags[0]).setIndicator(getLayoutInflater().inflate(R.layout.layout_main_tab_home, null)),
                HomeFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec(tabTags[1]).setIndicator(getLayoutInflater().inflate(R.layout.layout_main_tab_find, null)),
                FindFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec(tabTags[2]).setIndicator(getLayoutInflater().inflate(R.layout.layout_main_tab_reward, null)),
                RewardFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec(tabTags[3]).setIndicator(getLayoutInflater().inflate(R.layout.layout_main_tab_private_circle,
                null)), PrivateCircledFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec(tabTags[4]).setIndicator(getLayoutInflater().inflate(R.layout.layout_main_tab_mine, null)),
                MineFragment.class, null);
        if (FIRST_BLOOD.equals(type)) {
            tabHost.setCurrentTab(1);
        }
        mUnread = (ImageView) tabHost.findViewById(R.id.unread_msg_number);
        initCounter();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        processIntent();
    }

    private void processIntent() {
        Intent intent = getIntent();
        if (intent == null) return;
        Uri uri = intent.getData();
        if (uri != null) {
            List<String> pathSegments = uri.getPathSegments();
            if (pathSegments != null && pathSegments.size() > 0) {
                String scheme = this.getIntent().getScheme();//获得S称
                String host = uri.getHost();
//                    String test  ="quanhu://open/data?type=1&url=https://opus-mo.quanhu365.com/activity/qql&category=1002";
//                    Uri urlff = Uri.parse(test);
                if (!StringUtil.isEmpty(scheme) && !StringUtil.isEmpty(host)) {
                    String tab = uri.getQueryParameter("type");
                    String url = uri.getQueryParameter("url");
                    String categary = uri.getQueryParameter("category");
                    String custId = uri.getQueryParameter("custId");
                    if (BannerJumpHelper.tab_html.equals(tab)) {
                        WebContainerActivity.startActivity(this, url);
                    } else if (BannerJumpHelper.tab_native.equals(tab)) {
                        if (BannerJumpHelper.user_info_acy.equals(categary)) {    //个人中心
                            if (!StringUtil.isEmpty(custId)) {
                                UserInfoActivity.newFrindInfo(this, custId);
                            }
                        } else if (BannerJumpHelper.reward_aty.equals(categary)) {  //悬赏
                            intent.setClass(this, MainActivity.class);
                            startActivity(intent);
                            //发送event到
                            EventBus.getDefault().post(new BaseEvent(EventConstant.SET_REWARD_TAB));
                        }
                    } else {
                        CommonH5Activity.startCommonH5(this, "", url);
                    }
                }
            } else {
                finish();
            }
        }

    }

    @Override
    public void initData() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission
                .READ_EXTERNAL_STORAGE, Manifest.permission.KILL_BACKGROUND_PROCESSES};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, getString(R.string.carme_sd_permission), RC_VIDEO_AND_EXTENER, perms);
        }

        if (Session.getUserIsLogin()) {
            initYX(Session.getUserId(), Session.getUserId());
            loadUnreadMessage();
        }
    }

    private void initCounter() {
        mCounter = new ClickCounter(2, 2000);
        mCounter.setListener(new ClickCounter.OnCountListener() {
            @Override
            public void onCount(int currentTime) {
                mToast = Toast.makeText(mContext, R.string.exit_app_toast, Toast.LENGTH_SHORT);
                mToast.show();
            }

            @Override
            public void onFinish() {
                mToast.cancel();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mCounter != null)
            mCounter.countClick();
    }

    @Override
    public void onTabChanged(String tabId) {
        trackUser("入口", "导航栏", tabId);


        if (tabTags[0].equals(tabId)) {
            StatusBarUtils.setDarkStatusIcon(this, true);
        } else if (tabTags[1].equals(tabId)) {
            StatusBarUtils.setDarkStatusIcon(this, true);
        } else if (tabTags[2].equals(tabId)) {
            StatusBarUtils.setDarkStatusIcon(this, true);
        } else if (tabTags[3].equals(tabId)) {
            StatusBarUtils.setDarkStatusIcon(this, true);
        } else if (tabTags[4].equals(tabId)) {
            StatusBarUtils.setDarkStatusIcon(this, false);

        }
    }

    @Override
    public boolean intercept(String tabId) {

        if (!Session.getUserIsLogin()) {
            if (TextUtils.equals(tabTags[4], tabId)) {
                Bundle bundle = new Bundle();
//                bundle.putString("tabIndex", tabId);
//                bundle.putString(Constants.JUMPTYPE, Constants.BACKLOGIN);
                showActivity(this, LoginActivity.class, bundle);
                return true;
            }
        }

        return false;
    }

    private void initYX(String userId, String appKey) {
        if (NIMClient.getStatus() == StatusCode.LOGINED) {
            if (TextUtils.isEmpty(userId)) {
                return;
            }

            // 等待同步数据完成
            boolean syncCompleted = LoginSyncDataStatusObserver.getInstance().observeSyncDataCompletedEvent(new Observer<Void>() {
                @Override
                public void onEvent(Void v) {
                    DialogMaker.dismissProgressDialog();
                }
            });

            LogUtil.i(TAG, "sync completed = " + syncCompleted);
            registerObservers(true);

        } else {
            loginYunXin(Session.getUserId(), Session.getUserId());
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

                // 初始化免打扰
                if (UserPreferences.getStatusConfig() == null) {
                    UserPreferences.setStatusConfig(DemoCache.getNotificationConfig());
                }
                NIMClient.updateStatusBarNotificationConfig(UserPreferences.getStatusConfig());

                // 构建缓存
                DataCacheManager.buildDataCacheAsync();

                initYX(account, account);
            }

            @Override
            public void onFailed(int code) {
                loginYunXin(account, account);
            }

            @Override
            public void onException(Throwable exception) {
                Toast.makeText(getApplicationContext(), com.yryz.yunxinim.R.string.login_exception, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void registerObservers(boolean register) {
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, register);
    }

    /**
     * 用户状态变化
     */
    Observer<StatusCode> userStatusObserver = new Observer<StatusCode>() {
        @Override
        public void onEvent(StatusCode code) {
            Log.e(TAG, "onEvent: " + code.getValue());
            if (code.wontAutoLogin() && code != StatusCode.PWD_ERROR) {
                kickOut(code);
            } else {
                if (code == StatusCode.NET_BROKEN) {
                } else if (code == StatusCode.UNLOGIN) {
                } else if (code == StatusCode.CONNECTING) {
                } else if (code == StatusCode.LOGINING) {
                } else {
                }
            }
        }
    };

    private void kickOut(StatusCode code) {
        if (code == StatusCode.PWD_ERROR) {
            LogUtil.e("Auth", "user password error");
        } else {
            LogUtil.i("Auth", "Kicked!");
        }

        onLogout(code);

        if (code.wontAutoLogin() && code != StatusCode.PWD_ERROR && code != StatusCode.KICKOUT)
            EventBus.getDefault().post(new BaseEvent(EventConstant.USER_BE_FROZEN));
        if (code == StatusCode.KICKOUT) {
            Intent intent = new Intent();
            intent.setAction(UI.KICKACTION);
            sendBroadcast(intent);

            new Thread(new Runnable() {

                public void run() {
                    try {
                        Thread.sleep(500);
                        EventBus.getDefault().post(new KickEvent(5));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();


        }
    }

    // 注销
    private void onLogout(StatusCode code) {
        // 清理缓存&注销监听&清除状态
        LogoutHelper.logout();
        Preferences.saveUserToken("");

        NIMClient.getService(AuthService.class).logout();
        if (code == StatusCode.KICKOUT) {
            UpdateOrExitPresenter presenter = new UpdateOrExitPresenter();
            presenter.attachView(this);
            presenter.ExitApp();
        }
        Session.clearShareP();
        JPushInterface.setAlias(mContext, "", null);

        int loginWay = Session.getLoginWay();
        if (loginWay != Type.LOGIN_PHONE) {
            String openId = Session.getOpenId();
            SnsAuthPresenter snsPresenter = new SnsAuthPresenter();
            snsPresenter.attachView(this);
            switch (loginWay) {
                case Type.LOGIN_QQ:
                    snsPresenter.delQQAuth(openId);
                    break;
                case Type.LOGIN_SINA:
                    snsPresenter.delWBAuth(openId);
                    break;
                case Type.LOGIN_WX:
                    snsPresenter.delWXAuth(openId);
                    break;

            }
        }

        Session.clearShareP();
    }

    private void loadUnreadMessage() {
        Http.getApiService(ApiNewsService.class).newsUnread(Session.getUserId()).enqueue(new BaseCallback<ResponseData<HashMap<String,
                String>>>() {
            @Override
            public void onResponse(Call<ResponseData<HashMap<String, String>>> call, Response<ResponseData<HashMap<String, String>>>
                    response) {
                super.onResponse(call, response);
                if (response.isSuccessful() && response.body().isSuccessful()) {
                    HashMap<String, String> data = response.body().getData();
                    parsesData(data);
                    EventBus.getDefault().post(new BaseEvent(EventConstant.NEWS_UNREAD_CHANGE));
                }
            }
        });
    }


    private void parsesData(HashMap<String, String> data) {
        List<NewsUnreadBean> unreadBeanList = new ArrayList<>();
        for (String key : data.keySet()) {
            int num = Integer.parseInt(data.get(key));
            String[] types = key.split("\\|");
            int type = 0;
            int label = 0;
            if (types.length > 1) {
                type = Integer.parseInt(types[0]);
                label = Integer.parseInt(types[1]);
            } else {
                type = Integer.parseInt(types[0]);
            }
            unreadBeanList.add(new NewsUnreadBean(type, label, num));
        }

        Map<Integer, Integer> map = new HashMap<>();
        for (NewsUnreadBean item : unreadBeanList) {
            if (item.getType() == NewsTypeConstants.NEWS_INTERACTIVE) {
                Integer val = map.get(item.getLabel());
                if (val == null) {
                    map.put(item.getLabel(), item.getNum());
                } else {
                    map.put(item.getLabel(), val + item.getNum());
                }
            } else {
                Integer val = map.get(item.getType());
                if (val == null) {
                    map.put(item.getType(), item.getNum());
                } else {
                    map.put(item.getType(), val + item.getNum());
                }
            }
        }

        Session.setNewsAnnouncementNum((map.get(NewsTypeConstants.NEWS_ANNOUNCEMENT) != null && map.get(NewsTypeConstants
                .NEWS_ANNOUNCEMENT) != 0) ? Session.getNewsAnnouncementNum() + map.get(NewsTypeConstants.NEWS_ANNOUNCEMENT) : Session
                .getNewsAnnouncementNum());
        Session.setNewsSystemInformationNum((map.get(NewsTypeConstants.NEWS_SYSTEM) != null && map.get(NewsTypeConstants.NEWS_SYSTEM) !=
                0) ? Session.getNewsSystemInformationNum() + map.get(NewsTypeConstants.NEWS_SYSTEM) : Session.getNewsSystemInformationNum
                ());
        Session.setNewsAccountInformationNum((map.get(NewsTypeConstants.NEWS_ACCOUNT) != null && map.get(NewsTypeConstants.NEWS_ACCOUNT)
                != 0) ? Session.getNewsAccountInformationNum() + map.get(NewsTypeConstants.NEWS_ACCOUNT) : Session
                .getNewsAccountInformationNum());
        Session.setNewsRecommendNum((map.get(NewsTypeConstants.NEWS_RECOMMEND) != null && map.get(NewsTypeConstants.NEWS_RECOMMEND) != 0)
                ? Session.getNewsRecommendNum() + map.get(NewsTypeConstants.NEWS_RECOMMEND) : Session.getNewsRecommendNum());
        Session.setNewsCommentNum((map.get(NewsTypeConstants.NEWS_COMMENT) != null && map.get(NewsTypeConstants.NEWS_COMMENT) != 0) ?
                Session.getNewsCommentNum() + map.get(NewsTypeConstants.NEWS_COMMENT) : Session.getNewsCommentNum());
        Session.setNewsQaNum((map.get(NewsTypeConstants.NEWS_ANSWER) != null && map.get(NewsTypeConstants.NEWS_ANSWER) != 0) ? Session
                .getNewsQaNum() + map.get(NewsTypeConstants.NEWS_ANSWER) : Session.getNewsQaNum());
        Session.setNewsGroupNum((map.get(NewsTypeConstants.NEWS_GROUP) != null && map.get(NewsTypeConstants.NEWS_GROUP) != 0) ? Session
                .getNewsGroupNum() + map.get(NewsTypeConstants.NEWS_GROUP) : Session.getNewsGroupNum());
        Session.setNewsActivityNum((map.get(NewsTypeConstants.NEWS_ACTIVITY) != null && map.get(NewsTypeConstants.NEWS_ACTIVITY) != 0) ?
                Session.getNewsActivityNum() + map.get(NewsTypeConstants.NEWS_ACTIVITY) : Session.getNewsActivityNum());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBus(BaseEvent event) {
        switch (event.getType()) {
            case EventConstant.NEWS_COME_UNREAD:
                loadUnreadMessage();
                EventBus.getDefault().post(new BaseEvent(EventConstant.NEWS_OVERVIEW_CHANGE));
                break;
            case EventConstant.NEWS_UNREAD_CHANGE:
                requestMsgUnRead();
                break;
            case EventConstant.USER_BE_FROZEN:
                DefaultTipsDialog.newInstance(getString(R.string.account_lock)).show(getSupportFragmentManager(), "");
                break;
            case CommonCode.EventType.TYPE_LOGOUT:
//                finish();
                tabHost.setCurrentTab(0);
                break;
            case EventConstant.USER_BE_KICKOUT_BY_HTTP:
                kickOut(StatusCode.INVALID);
                break;
            case EventConstant.SET_REWARD_TAB:
                tabHost.setCurrentTab(2);
                break;
            case CommonCode.EventType.TYPE_BACKLOGIN_REFRESH:
                initYX(Session.getUserId(), Session.getUserId());
                loadUnreadMessage();
                break;
        }
    }

    private void requestMsgUnRead() {
        if (null == mUnread)
            return;

        if (Session.getUserIsLogin()) {
            int unreadNum1 = NIMClient.getService(MsgService.class).getTotalUnreadCount();

            int unreadNum2 = Session.getNewsAnnouncementNum() +
                    Session.getNewsSystemInformationNum() +
                    Session.getNewsAccountInformationNum() +
                    Session.getNewsRecommendNum() +
                    Session.getNewsCommentNum() +
                    Session.getNewsQaNum() +
                    Session.getNewsGroupNum() +
                    Session.getNewsActivityNum();

            int unreadNum3 = 0;
            if (!TextUtils.isEmpty(Session.getUserFocusNum()))
                unreadNum3 = Integer.parseInt(Session.getUserFocusNum());

            if (unreadNum2 > 0 || unreadNum3 > 0 || Beta.getUpgradeInfo() != null) {
                mUnread.setVisibility(View.VISIBLE);
            } else {
                mUnread.setVisibility(View.GONE);
            }

            int unreadNum = unreadNum1 + unreadNum2 + unreadNum3;
            if (unreadNum != 0) {
                BadgeUtil.setBadgeCount(getApplicationContext(), unreadNum);
            } else {
                BadgeUtil.resetBadgeCount(getApplicationContext());
            }

        } else {
            mUnread.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
        registerObservers(false);
    }

    @Override
    public void refreshPage() {

    }


}
