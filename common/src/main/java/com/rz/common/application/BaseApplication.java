package com.rz.common.application;

import android.app.Application;
import android.content.Context;

/**
 * Created by Gsm on 2017/8/28.
 */
public class BaseApplication extends Application{
    private static BaseApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static BaseApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        if (instance != null) {
            return instance.getApplicationContext();
        }
        return null;
    }
}
