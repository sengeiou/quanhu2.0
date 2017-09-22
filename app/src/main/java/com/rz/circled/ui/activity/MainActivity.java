package com.rz.circled.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import com.netease.nimlib.sdk.msg.SystemMessageObserver;
import com.netease.nimlib.sdk.msg.SystemMessageService;
import com.netease.nimlib.sdk.msg.constant.SystemMessageType;
import com.rz.circled.R;
import com.rz.circled.ui.fragment.FindFragment;
import com.rz.circled.ui.fragment.HomeFragment;
import com.rz.circled.ui.fragment.MineFragment;
import com.rz.circled.ui.fragment.PrivateCircledFragment;
import com.rz.circled.ui.fragment.RewardFragment;
import com.rz.circled.widget.CustomFragmentTabHost;
import com.rz.common.cache.preference.Session;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.ClickCounter;
import com.yryz.yunxinim.DemoCache;
import com.yryz.yunxinim.config.preference.Preferences;
import com.yryz.yunxinim.config.preference.UserPreferences;
import com.yryz.yunxinim.login.LogoutHelper;
import com.yryz.yunxinim.main.helper.SystemMessageUnreadManager;
import com.yryz.yunxinim.main.reminder.ReminderItem;
import com.yryz.yunxinim.main.reminder.ReminderManager;
import com.yryz.yunxinim.uikit.LoginSyncDataStatusObserver;
import com.yryz.yunxinim.uikit.cache.DataCacheManager;
import com.yryz.yunxinim.uikit.common.ui.dialog.DialogMaker;
import com.yryz.yunxinim.uikit.common.util.log.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class MainActivity extends BaseActivity implements TabHost.OnTabChangeListener, CustomFragmentTabHost.InterceptTagChanged, ReminderManager.UnreadNumChangedCallback {

    @BindView(android.R.id.tabhost)
    CustomFragmentTabHost tabHost;

    private ClickCounter mCounter;
    private Toast mToast;

    private String[] tabTags = new String[]{"home", "find", "reward", "privateCircle", "mine"};


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
    public void initView() {
        tabHost.setup(this, getSupportFragmentManager(), R.id.fl_main_content);
        tabHost.setOnTabChangedListener(this);
        tabHost.setInterceptTagChanged(this);
        tabHost.getTabWidget().setDividerDrawable(null);

        tabHost.addTab(tabHost.newTabSpec(tabTags[0]).setIndicator(getLayoutInflater().inflate(R.layout.layout_main_tab_home, null)), HomeFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec(tabTags[1]).setIndicator(getLayoutInflater().inflate(R.layout.layout_main_tab_find, null)), FindFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec(tabTags[2]).setIndicator(getLayoutInflater().inflate(R.layout.layout_main_tab_reward, null)), RewardFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec(tabTags[3]).setIndicator(getLayoutInflater().inflate(R.layout.layout_main_tab_private_circle, null)), PrivateCircledFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec(tabTags[4]).setIndicator(getLayoutInflater().inflate(R.layout.layout_main_tab_mine, null)), MineFragment.class, null);

        initCounter();
    }

    @Override
    public void initData() {
        if (Session.getUserIsLogin()) {
            initYX(Session.getUserId(), Session.getUserId());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e(TAG, "onSaveInstanceState: ");
    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e(TAG, "onRestoreInstanceState: ");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e(TAG, "onNewIntent: ");
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
    public void onTabChanged(String s) {

    }

    @Override
    public boolean intercept(String tabId) {
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
            registerMsgUnreadInfoObserver(true);
            registerSystemMessageObservers(true);
            requestSystemMessageUnreadCount();

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

    /**
     * 注册未读消息数量观察者
     */
    private void registerMsgUnreadInfoObserver(boolean register) {
        if (register) {
            ReminderManager.getInstance().registerUnreadNumChangedCallback(this);
        } else {
            ReminderManager.getInstance().unregisterUnreadNumChangedCallback(this);
        }
    }

    /**
     * 未读消息数量观察者实现
     */
    @Override
    public void onUnreadNumChanged(ReminderItem item) {
        requestMsgUnRead();
    }

    private void requestMsgUnRead() {
//        if (null == mUnread || null == versionUpdate)
//            return;

        if (Session.getUserIsLogin()) {
            int unreadNum1 = NIMClient.getService(MsgService.class).getTotalUnreadCount();
            List<SystemMessageType> types = new ArrayList<>();
            types.add(SystemMessageType.ApplyJoinTeam);
            types.add(SystemMessageType.DeclineTeamInvite);
            types.add(SystemMessageType.RejectTeamApply);
            types.add(SystemMessageType.TeamInvite);
            types.add(SystemMessageType.undefined);
            int unreadNum2 = NIMClient.getService(SystemMessageService.class).querySystemMessageUnreadCountByType(types);

            int unreadNum3;
            if (!TextUtils.isEmpty(Session.getUserFocusNum()))
                unreadNum3 = unreadNum2 + Integer.parseInt(Session.getUserFocusNum());
            else
                unreadNum3 = unreadNum2;

            int unreadNum;
            if (!TextUtils.isEmpty(Session.getUserFocusNum()))
                unreadNum = unreadNum1 + unreadNum2 + Integer.parseInt(Session.getUserFocusNum());
            else
                unreadNum = unreadNum1 + unreadNum2;

//            if (unreadNum1 == 0) {
//                mUnread.setVisibility(View.GONE);
//            } else {
//                mUnread.setVisibility(View.VISIBLE);
//            }
//
//            if (unreadNum3 == 0 && upgradeInfo == null) {
//                versionUpdate.setVisibility(View.GONE);
//            } else {
//                versionUpdate.setVisibility(View.VISIBLE);
//            }
//
//            if (unreadNum != 0) {
//                BadgeUtil.setBadgeCount(getApplicationContext(), unreadNum);
//            } else {
//                BadgeUtil.resetBadgeCount(getApplicationContext());
//            }
        } else {
//            mUnread.setVisibility(View.GONE);
//            versionUpdate.setVisibility(View.GONE);
        }
    }

    /**
     * 注册/注销系统消息未读数变化
     *
     * @param register
     */
    private void registerSystemMessageObservers(boolean register) {
        NIMClient.getService(SystemMessageObserver.class).observeUnreadCountChange(sysMsgUnreadCountChangedObserver,
                register);
    }

    private Observer<Integer> sysMsgUnreadCountChangedObserver = new Observer<Integer>() {
        @Override
        public void onEvent(Integer unreadCount) {
            SystemMessageUnreadManager.getInstance().setSysMsgUnreadCount(unreadCount);
            ReminderManager.getInstance().updateContactUnreadNum(unreadCount);
        }
    };


    /**
     * 查询系统消息未读数
     */
    private void requestSystemMessageUnreadCount() {
        int unread = NIMClient.getService(SystemMessageService.class).querySystemMessageUnreadCountBlock();
        SystemMessageUnreadManager.getInstance().setSysMsgUnreadCount(unread);
        ReminderManager.getInstance().updateContactUnreadNum(unread);
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
        Preferences.saveUserToken("");

        if (code == StatusCode.PWD_ERROR) {
            LogUtil.e("Auth", "user password error");
        } else {
            LogUtil.i("Auth", "Kicked!");
        }

        onLogout(code);
    }

    // 注销
    private void onLogout(StatusCode code) {
        // 清理缓存&注销监听&清除状态
        LogoutHelper.logout();

        NIMClient.getService(AuthService.class).logout();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart: ");
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        Log.e(TAG, "onRestoreInstanceState: ");
    }

    @Override
    public void refreshPage() {

    }

}
