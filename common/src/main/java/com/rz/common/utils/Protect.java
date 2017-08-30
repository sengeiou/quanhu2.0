package com.rz.common.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

/**
 * Created by Administrator on 2016/9/29 0029.
 */
public class Protect {

//    public static boolean checkLoadImageStatus(Fragment fragment) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed()) {
//            throw new IllegalArgumentException("You cannot start a load for a destroyed activity");
//        }
//    }

    public static boolean checkLoadImageStatus(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed()) {
            return false;
        }
        return true;
    }

    public static boolean checkLoadImageStatus(Fragment fragment) {
        if (fragment == null || fragment.getActivity() == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && fragment.getActivity().isDestroyed()) {
            return false;
        }
        return true;
    }

    public static boolean checkLoadImageStatus(Context context) {
        Activity activity = null;
        if (context instanceof Activity) {
            activity = (Activity) context;
        } else {
            return true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed()) {
            return false;
        }
        return true;
    }

    public static String deleteStr4End(String s) {
        if (!TextUtils.isEmpty(s)) {
            return s.replaceAll("\\<.*?>|\\n", "");
        }
        return s;
    }

    public static String setName(String custName, String noteName) {
        if (StringUtils.isEmpty(custName) && StringUtils.isEmpty(noteName)) {
            return "";
        }
        return TextUtils.isEmpty(noteName) ? custName : noteName;
    }

    public static String removeNull(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        return str;
    }

    public static String toNull(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        return str;
    }
}
