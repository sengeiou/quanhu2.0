package com.rz.common.utils;

import android.os.Build;

import java.lang.reflect.Method;

/**
 * Created by Gsm on 2017/12/9.
 */
public class OSUtils {

    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

    public static boolean isMIUI() {
        String manufacturer = Build.MANUFACTURER;
        return "Xiaomi".equalsIgnoreCase(manufacturer);
    }

    public static boolean isFlyme() {
        try {
            final Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        } catch (final Exception e) {
            return false;
        }
    }
}
