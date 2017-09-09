package com.rz.circled.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;
import com.rz.circled.R;
import com.rz.circled.modle.ShowListModel;
import com.rz.circled.presenter.IPresenter;
import com.rz.circled.presenter.impl.SnsAuthPresenter;
import com.rz.circled.presenter.impl.UpdateOrExitPresenter;
import com.rz.common.cache.CachePath;
import com.rz.common.cache.preference.EntityCache;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.H5Address;
import com.rz.common.constant.IntentCode;
import com.rz.common.constant.Type;
import com.rz.common.event.BaseEvent;
import com.rz.common.ui.activity.BaseActivity;
import com.rz.common.utils.CountDownTimer;
import com.rz.common.utils.DialogUtils;
import com.rz.common.utils.SystemUtils;
import com.yryz.yunxinim.config.preference.Preferences;
import com.yryz.yunxinim.login.LogoutHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.annotations.Beta;

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

    /**
     * 缓存大小
     */
    private String mCacheSize;
    private SnsAuthPresenter snsPresenter;
    protected IPresenter presenter;

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
        setTitleText("设置");
        mTvVersionName.setText(SystemUtils.getVersionName(this));
        if (!Session.getUserIsLogin()) {
            mExitBtn.setVisibility(View.GONE);
//            mAccountView.setVisibility(View.GONE);
            mLinearRemind.setVisibility(View.GONE);
        }
    }

    @Override
    public void initData() {
//        EventBus.getDefault().register(this);
//
//        /***** 获取升级信息 *****/
//        UpgradeInfo upgradeInfo = Beta.getUpgradeInfo();
//
//        if (upgradeInfo != null) {
//            BaseEvent event = new BaseEvent();
//            event.info = "versionUpdate";
//            EventBus.getDefault().post(event);
//        } else {
//            BaseEvent event = new BaseEvent();
//            event.info = "noVersionUpdate";
//            EventBus.getDefault().post(event);
//        }
//
//        calculateCache();
    }

    /**
     * 计算缓存大小
     */
    private void calculateCache() {
//        try {
//            //外部存储数据大小
//            long wai = DataCleanManager.getFolderSize(new File(
//                    CachePath.wai_path));
////            long nei = DataCleanManager.getFolderSize(new File(
////                    CachePath.nei_path));
////            long databases = DataCleanManager.getFolderSize(new File(
////                    CachePath.databases_path));
//            //shareprefresh存储数据大小
//            long share = DataCleanManager.getFolderSize(new File(
//                    CachePath.share_path));
//            long cache = wai + share;
//            if (cache == 0) {
//                mCacheSize = "0.00B";
//            } else {
//                mCacheSize = DataCleanManager.getFormatSize(cache);
//            }
//            mTxtCacheNum.setText(mCacheSize);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @OnClick({R.id.id_layout_account_and_safe, R.id.id_layout_send_friend_ll, R.id.id_layout_update, R.id.id_layout_clean_cache, R.id.id_btn_exit})
    public void onClick(View view) {
        switch (view.getId()) {
            //账户与安全
            case R.id.id_layout_account_and_safe:
//                if (isLogin()) {
//                    if (CountDownTimer.isFastClick()) {
//                        return;
//                    }
//                    jump(AccountSafeAty.class);
//                }
                break;
            //推荐给朋友
            case R.id.id_layout_send_friend_ll:
//                ShareNewsAty.startShareNews(aty, new ShareModel(
//                                "悠然一指，一指进入你的圈子",
//                                "悠然一指(www.yryz.com)，国内首创的一站式大型社群资源平台。平台自主创新，自主研发，精心打造并陆续推出300个各具特色的社群资源圈，汇聚了丰富的资源与人脉，展示了用户发布和分享的各类知识、经验、技能、专业服务以及商业资源。",
//                                H5Address.ONLINE_TUIGUANG),
//                        IntentCode.Setting.SETTING_RESULT_CODE);
                break;
            case R.id.id_layout_update:
                /***** 检查更新 *****/
//                Beta.checkUpgrade();
//
//                /***** 获取升级信息 *****/
//                UpgradeInfo upgradeInfo = Beta.getUpgradeInfo();
//
//                if (upgradeInfo != null) {
//                    BaseEvent event = new BaseEvent();
//                    event.info = "versionUpdate";
//                    EventBus.getDefault().post(event);
//                } else {
//                    BaseEvent event = new BaseEvent();
//                    event.info = "noVersionUpdate";
//                    EventBus.getDefault().post(event);
//                }
                break;
            //清除缓存
            case R.id.id_layout_clean_cache:
//                ClearCacheUtil.clearCache(aty, 0, "");
//                mTxtCacheNum.setText("0M");
//                Session.setUserIsFirstDownload(false);
//                SVProgressHUD.showSuccessWithStatus(aty, getString(R.string.clean_cache_complete));
                break;
            //退出
            case R.id.id_btn_exit:
                View dialogView = LayoutInflater.from(aty).inflate(R.layout.comm_dialog, null);
                final Dialog dialog = DialogUtils.selfDialog(aty, dialogView, false);
                dialog.show();
                ((TextView) dialogView.findViewById(R.id.id_tv_message)).setText(getString(R.string.quit_confirm));
                dialogView.findViewById(R.id.id_tv_confirm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (CountDownTimer.isFastClick()) {
                            return;
                        }
                        dialog.dismiss();
//                        ((UpdateOrExitPresenter) presenter).ExitApp();
//                        exitApp();

                        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                        startActivity(intent);

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

//        EntityCache<CircleStatsModel> entityCache = new EntityCache<CircleStatsModel>(this, CircleStatsModel.class);
        int loginWay = Session.getLoginWay();
        if (loginWay != Type.LOGIN_PHONE) {
            String openId = Session.getOpenId();
            snsPresenter = new SnsAuthPresenter();
            snsPresenter.attachView(this);
//            switch (loginWay) {
//                case Type.LOGIN_QQ:
//                    snsPresenter.delQQAuth(openId);
//                    break;
//                case Type.LOGIN_SINA:
//                    snsPresenter.delWBAuth(openId);
//                    break;
//                case Type.LOGIN_WX:
//                    snsPresenter.delWXAuth(openId);
//                    break;
//            }
        }

        removeLoginState();

        // 清理缓存&注销监听
        LogoutHelper.logout();

        NIMClient.getService(AuthService.class).logout();

//        MobclickAgent.onProfileSignOff();

        EntityCache entityCache = new EntityCache<ShowListModel>(this, ShowListModel.class);
        List<ShowListModel> showCaches = entityCache.getListEntity(ShowListModel.class);

//        ClearCacheUtil.clearCache(aty, 1, Session.getUserId());

        entityCache.putListEntity(showCaches);

        setResult(IntentCode.Setting.SETTING_RESULT_CODE);

//        Intent intent = new Intent(MyTrendsFragment.MESSAGE_CLEAR);
//        sendBroadcast(intent);
        finish();
    }

    /**
     * 清除登陆状态
     */
    private void removeLoginState() {
//        Preferences.saveUserToken("");
    }

    @Override
    public <T> void updateView(T t) {
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
        EventBus.getDefault().unregister(this);
        if (snsPresenter != null) {
            snsPresenter.detachView();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventUpdate(BaseEvent event) {
        if (TextUtils.equals("versionUpdate", event.info)) {
            mTvVersionName.setText("有更新");
            mTvVersionName.setTextColor(Color.RED);

        } else if (TextUtils.equals("noVersionUpdate", event.info)) {
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

}