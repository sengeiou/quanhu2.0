package com.rz.common.utils;

import android.content.Context;
import android.util.DisplayMetrics;

import java.lang.reflect.Field;

/**
 * Created by JS01 on 2016/6/7.
 */
public class DensityUtils {

    public static int dip2px(Context var0, float var1) {
        float var2 = var0.getResources().getDisplayMetrics().density;
        return (int) (var1 * var2 + 0.5F);
    }

    public static int px2dip(Context var0, float var1) {
        float var2 = var0.getResources().getDisplayMetrics().density;
        return (int) (var1 / var2 + 0.5F);
    }

    public static int sp2px(Context var0, float var1) {
        float var2 = var0.getResources().getDisplayMetrics().scaledDensity;
        return (int) (var1 * var2 + 0.5F);
    }

    public static int px2sp(Context var0, float var1) {
        float var2 = var0.getResources().getDisplayMetrics().scaledDensity;
        return (int) (var1 / var2 + 0.5F);
    }

    public static int getScreenW(Context aty) {
        new DisplayMetrics();
        DisplayMetrics dm = aty.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenH(Context aty) {
        new DisplayMetrics();
        DisplayMetrics dm = aty.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    public static int getStatusBarHeight() {
        Class<?> c = null;

        Object obj = null;

        Field field = null;

        int x = 0, sbar = 0;

        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
//            sbar = getContext().getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }
}
