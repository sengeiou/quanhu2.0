package com.rz.common.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.rz.common.application.BaseApplication;
import com.rz.common.permission.EasyPermissions;
import com.zhuge.analysis.stat.ZhugeSDK;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by JS01 on 2016/6/8.
 */
public class SystemUtils {
    //诸葛io通用工具
    public static void trackUser(String text, String name, String value) {
        //定义与事件相关的属性信息
        JSONObject eventObject = new JSONObject();
        try {
            eventObject.put(name, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }


//记录事件
        ZhugeSDK.getInstance().track(BaseApplication.getContext(), text, eventObject);

    }

    public static String imei;

    public static boolean isX86() {
        Log.d("app", "isX86 arch " + Build.CPU_ABI);
        return Build.CPU_ABI.contains("x86");
    }

    /**
     * 系统检查
     */
    public static String checkSystem(Activity activity) {
        Context appContext = activity.getApplicationContext();
//        String channel = SystemUtils.getMetaData(appContext, CodeStatus.Constants.CHANNEL_KEY);
        String versionName = getVersionName(appContext);
        int versionCode = getVersionCode(appContext);
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        float density = metrics.density;
        int dpi = metrics.densityDpi;
        JSONObject json = new JSONObject();
        try {
            json.putOpt("model", Build.MODEL);
            json.putOpt("width", width);
            json.putOpt("height", height);
            json.putOpt("density", density);
            json.putOpt("dpi", dpi);
//            json.putOpt("mode", (App.isDebug ? "debug" : "release"));
//            json.putOpt("debug_level", App.DEBUG_LEVEL);
//            json.putOpt("channel", channel);
            json.putOpt("api", Build.VERSION.SDK_INT);
            json.putOpt("android", Build.VERSION.RELEASE);
            json.putOpt("version_name", versionName);
            json.putOpt("version_code", versionCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String info = json.toString();
        System.out.println("System Info:" + info);
        return info;
    }

    /**
     * 是否有NavigationBar
     *
     * @param context
     * @return
     */
    public static boolean hasNavBar(Context context) {
        boolean hasNavBar = false;
        Resources res = context.getResources();
        int id = res.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavBar = res.getBoolean(id);
        }

        try {
            Class<?> sysprop = Class.forName("android.os.SystemProperties");
            Method m = sysprop.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(sysprop, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavBar = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasNavBar;
    }


    /**
     * 获取NavigationBar的高度
     *
     * @param context
     * @return
     */
    public static int getNavBarHeight(Context context) {
        int navBarHeight = 0;
        Resources res = context.getResources();
        int id = res.getIdentifier("navigation_bar_height", "dimen", "android");
        if (id > 0 && hasNavBar(context)) {
            navBarHeight = res.getDimensionPixelOffset(id);
        }
        return navBarHeight;
    }

    /**
     * 获取屏幕宽度
     *
     * @param activity
     * @return
     */
    public static final int getScreenWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param activity
     * @return
     */
    public static final int getScreenHeight(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 读取清单文件中的配置
     *
     * @return
     */
    public static String getMetaData(Context context, String key) {
        String value = null;
        try {
            ApplicationInfo appinfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            value = appinfo.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 获取版本名称
     *
     * @return
     */
    public static String getVersionName(Context context) {
        String versionCode = "1.0.0";
        try {
            PackageInfo pkginfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionCode = pkginfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取版本号
     *
     * @return
     */
    public static int getVersionCode(Context context) {
        int versionCode = 1;
        try {
            PackageInfo pkginfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionCode = pkginfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取进程名
     *
     * @param appContext
     * @return
     */
    public static String getProcessName(Context appContext) {
        String pname = null;
        int pid = android.os.Process.myPid();
        ActivityManager am = (ActivityManager) appContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processes = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo p : processes) {
            if (p.pid == pid) {
                pname = p.processName;
            }
        }
        return pname;
    }

    /**
     * 获得设备id
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        if (TextUtils.isEmpty(imei)) {
            if (context != null) {
                if (EasyPermissions.hasPermissions(context, Manifest.permission.READ_PHONE_STATE)) {
                    TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                    imei = tm.getDeviceId();
                    if (TextUtils.isEmpty(imei)) {
                        return "imei";
                    }
                    return imei;
                } else {
                    return "imei";
                }
            }
        } else {
            return imei;
        }
        return "imei";

    }

    /**
     * 获取ip
     *
     * @param mContent
     * @return
     */
    public static String getIp(Context mContent) {
        String ip = "unknow";
        if (null == mContent) {
            return ip;
        }
        try {
            if (EasyPermissions.hasPermissions(mContent, Manifest.permission.ACCESS_WIFI_STATE)) {
                //获取wifi服务
                WifiManager wifiManager = (WifiManager) mContent.getSystemService(Context.WIFI_SERVICE);
                //判断wifi是否开启
                if (!wifiManager.isWifiEnabled()) {
//                wifiManager.setWifiEnabled(true);
                    //当前使用2G/3G/4G网络
                    try {
                        //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                            NetworkInterface intf = en.nextElement();
                            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                                InetAddress inetAddress = enumIpAddr.nextElement();
                                if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                    return inetAddress.getHostAddress().toString();
                                }
                            }
                        }
                    } catch (SocketException e) {
                        e.printStackTrace();
                        return ip;
                    }
                }
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                int ipAddress = wifiInfo.getIpAddress();
                return intToIp(ipAddress);
            } else {
                return "unknow";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ip;


    }

    private static String intToIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }


    public static boolean inMainProcess(Context context) {
        String packageName = context.getPackageName();
        String processName = getProcessName(context);
        return packageName.equals(processName);
    }
}
