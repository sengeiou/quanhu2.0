package com.yryz.yunxinim;

import android.content.Context;

import com.yryz.yunxinim.uikit.NimUIKit;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;

/**
 * Created by jezhee on 2/20/15.
 */
public class DemoCache {

    private static Context context;

    private static String account;

    private static StatusBarNotificationConfig notificationConfig;

    public static float topBarHeight = 0;

    public static float topBarTitleSize = 0;

    private static boolean isShowTeamNum = true;

    public static void clear() {
        account = null;
    }

    public static String getAccount() {
        return account;
    }

    public static void setAccount(String account) {
        DemoCache.account = account;
        NimUIKit.setAccount(account);
    }

    public static boolean isShowTeamNum() {
        return isShowTeamNum;
    }

    public static void setIsShowTeamNum(boolean isShowTeamNum) {
        DemoCache.isShowTeamNum = isShowTeamNum;
    }

    public static void setNotificationConfig(StatusBarNotificationConfig notificationConfig) {
        DemoCache.notificationConfig = notificationConfig;
    }

    public static StatusBarNotificationConfig getNotificationConfig() {
        return notificationConfig;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        DemoCache.context = context.getApplicationContext();
    }
}
