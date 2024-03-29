package com.rz.circled.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.AuthService;
import com.rz.circled.R;
import com.rz.circled.modle.ShareModel;
import com.rz.circled.presenter.impl.SnsAuthPresenter;
import com.rz.circled.presenter.impl.UpdateOrExitPresenter;
import com.rz.circled.widget.SwitchButton;
import com.rz.common.cache.CachePath;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.H5Address;
import com.rz.common.constant.IntentCode;
import com.rz.common.constant.IntentKey;
import com.rz.common.constant.Type;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.CountDownTimer;
import com.rz.common.utils.DataCleanManager;
import com.rz.common.utils.DialogUtils;
import com.rz.common.utils.SystemUtils;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.yryz.yunxinim.config.preference.Preferences;
import com.yryz.yunxinim.config.preference.UserPreferences;
import com.yryz.yunxinim.login.LogoutHelper;
import com.yryz.yunxinim.uikit.common.util.sys.NetworkUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.HashSet;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * 我的设置
 */
public class SettingActivity extends BaseActivity {

    //账户安全
    @BindView(R.id.id_layout_account_and_safe)
    View mAccountView;

    //推荐给朋友
    @BindView(R.id.id_layout_send_friend_ll)
    LinearLayout mLinearRemind;

    //缓存大小
    @BindView(R.id.id_close_cache_txt)
    TextView mTxtCacheNum;
    //版本号
    @BindView(R.id.tv_version_name)
    TextView mTvVersionName;

    //退出
    @BindView(R.id.id_btn_exit)
    Button mExitBtn;
    @BindView(R.id.id_sb_mess)
    SwitchButton mIdSbMess;

    /**
     * 缓存大小
     */
    private String mCacheSize;
    private SnsAuthPresenter snsPresenter;
    protected UpdateOrExitPresenter presenter;

    @Override
    public View loadView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_setting, null);
    }

    @Override
    public void initPresenter() {
        super.initPresenter();
        presenter = new UpdateOrExitPresenter();
        presenter.attachView(this);
    }

    @Override
    public void initView() {
        setTitleText(R.string.setting);
        mTvVersionName.setText(SystemUtils.getVersionName(this));
        if (!Session.getUserIsLogin()) {
            mExitBtn.setVisibility(View.GONE);
//            mAccountView.setVisibility(View.GONE);
            mLinearRemind.setVisibility(View.GONE);
        }
        mIdSbMess.setChecked(!UserPreferences.getNotificationToggle());
        mIdSbMess.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!NetworkUtil.isNetAvailable(aty)) {
                    Toast.makeText(getApplicationContext(), com.yryz.yunxinim.R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                    mIdSbMess.setChecked(!isChecked);
                    return;
                }

                setMessageNotify(isChecked);
            }
        });
    }

    @Override
    public void initData() {
        calculateCache();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUpdate(false);
    }

    private void checkUpdate(boolean showUpdateDialog) {
        /***** 检查更新 *****/
        if (showUpdateDialog) Beta.checkUpgrade();

        /***** 获取升级信息 *****/
        UpgradeInfo upgradeInfo = Beta.getUpgradeInfo();

        if (upgradeInfo != null) {
            BaseEvent event = new BaseEvent();
            event.key = "versionUpdate";
            EventBus.getDefault().post(event);
        } else {
            BaseEvent event = new BaseEvent();
            event.key = "noVersionUpdate";
            EventBus.getDefault().post(event);
        }
    }

    /**
     * 计算缓存大小
     */
    private void calculateCache() {
        try {
            //外部存储数据大小
            long wai = DataCleanManager.getFolderSize(new File(
                    CachePath.wai_path));
            //shareprefresh存储数据大小
            long share = DataCleanManager.getFolderSize(new File(
                    CachePath.share_path));
            long cache = wai + share;
            if (cache == 0) {
                mCacheSize = "0.00B";
            } else {
                mCacheSize = DataCleanManager.getFormatSize(cache);
            }
            mTxtCacheNum.setText(mCacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.id_layout_account_and_safe, R.id.id_layout_send_friend_ll, R.id.id_layout_clean_cache, R.id.id_btn_exit, R.id.id_layout_update})
    public void onClick(View view) {
        View dialogView = LayoutInflater.from(aty).inflate(R.layout.comm_dialog, null);
        final Dialog dialog = DialogUtils.selfDialog(aty, dialogView, false);
        switch (view.getId()) {
            //账户与安全
            case R.id.id_layout_account_and_safe:
                if (isLogin()) {
                    if (CountDownTimer.isFastClick()) {
                        return;
                    }
                    showActivity(aty, AccountSafeAty.class);
                }
                break;
            //推荐给朋友
            case R.id.id_layout_send_friend_ll:
                ShareNewsAty.startShareNews(aty, new ShareModel(
                                getString(R.string.share_title),
                                getString(R.string.share_desc),
                                H5Address.APP_DOWNLOAD),
                        IntentCode.Setting.SETTING_RESULT_CODE);
                break;
            case R.id.id_layout_update:
                checkUpdate(true);
                break;
            //清除缓存
            case R.id.id_layout_clean_cache:
                dialog.show();
                ((TextView) dialogView.findViewById(R.id.id_tv_message)).setText(getString(R.string.clear_cache));
                dialogView.findViewById(R.id.id_tv_confirm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        mTxtCacheNum.setText("0M");
                        DataCleanManager.cleanApplicationData(mContext, "");
                        Session.setUserIsFirstDownload(false);
                        Toast.makeText(mContext, getString(R.string.clean_cache_complete), Toast.LENGTH_LONG).show();

                    }
                });
                dialogView.findViewById(R.id.id_tv_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                break;
            //退出
            case R.id.id_btn_exit:
                dialog.show();
                ((TextView) dialogView.findViewById(R.id.id_tv_message)).setText(getString(R.string.quit_confirm));
                dialogView.findViewById(R.id.id_tv_confirm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (CountDownTimer.isFastClick()) {
                            return;
                        }
                        dialog.dismiss();
                        presenter.ExitApp();
                        exitApp();
                        Session.clearShareP();
                        Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                        intent.putExtra(IntentKey.EXTRA_TYPE, CommonCode.Constant.TAB_MAIN_HOME);
                        startActivity(intent);

                        EventBus.getDefault().post(new BaseEvent(CommonCode.EventType.TYPE_LOGOUT));
                        finish();
                    }
                });
                dialogView.findViewById(R.id.id_tv_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                break;
        }
    }

    public void exitApp() {
        int loginWay = Session.getLoginWay();
        if (loginWay != Type.LOGIN_PHONE) {
            String openId = Session.getOpenId();
            snsPresenter = new SnsAuthPresenter();
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

        JPushInterface.setAlias(mContext, "", null);

        removeLoginState();

        // 清理缓存&注销监听
        LogoutHelper.logout();

        NIMClient.getService(AuthService.class).logout();

//        MobclickAgent.onProfileSignOff();

//        setResult(IntentCode.Setting.SETTING_RESULT_CODE);

        finish();
    }

    /**
     * 清除登陆状态
     */
    private void removeLoginState() {
        Preferences.saveUserToken("");
    }

    @Override
    public <T> void updateView(T t) {
//        mMessFreeBean= (MessFreeBean) t;
//        mIdSbMess.setChecked(mMessFreeBean.pushStatus==pushState?true:false);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IntentCode.Login.LOGIN_REQUEST_CODE) {
            if (resultCode == IntentCode.Login.LOGIN_RESULT_CODE) {
                if (Session.getUserIsLogin()) {
                    mLinearRemind.setVisibility(View.VISIBLE);
                    mExitBtn.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (snsPresenter != null) {
            snsPresenter.detachView();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventUpdate(BaseEvent event) {
        if (TextUtils.equals("versionUpdate", event.key)) {
            mTvVersionName.setText(R.string.hava_updata);
            mTvVersionName.setTextColor(Color.RED);

        } else if (TextUtils.equals("noVersionUpdate", event.key)) {
            mTvVersionName.setText(SystemUtils.getVersionName(this));
            mTvVersionName.setTextColor(getResources().getColor(R.color.color_666666));
        }
    }

    /**
     * 用户是否登录
     */
    public boolean isLogin() {
        if (Session.getUserIsLogin()) {
            return true;
        } else {
            Intent login = new Intent(aty, LoginActivity.class);
            startActivityForResult(login, IntentCode.Login.LOGIN_REQUEST_CODE);
//            showLoginDialog();
            return false;
        }
    }

    @Override
    public void refreshPage() {

    }

    private void setMessageNotify(final boolean checkState) {
        mIdSbMess.setChecked(checkState);
        setToggleNotification(!checkState);
        Toast.makeText(aty, com.yryz.yunxinim.R.string.user_info_update_success, Toast.LENGTH_SHORT).show();
    }

    private void setToggleNotification(boolean checkState) {
        try {
            StatusBarNotificationConfig config = UserPreferences.getStatusConfig();
            UserPreferences.setDownTimeToggle(false);
            config.downTimeToggle = false;
            UserPreferences.setStatusConfig(config);
            NIMClient.updateStatusBarNotificationConfig(config);

            setNotificationToggle(checkState);
            NIMClient.toggleNotification(checkState);

            //极光推送
            JPushInterface.setPushTime(mContext, checkState ? null : new HashSet<Integer>(), 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setNotificationToggle(boolean on) {
        UserPreferences.setNotificationToggle(on);
    }
}
