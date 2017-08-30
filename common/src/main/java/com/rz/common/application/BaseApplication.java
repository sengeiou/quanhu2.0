package com.rz.common.application;

import android.app.Application;
import android.content.Context;

import com.rz.common.oss.OssManager;

/**
 * Created by Gsm on 2017/8/28.
 */
public class BaseApplication extends Application {
    private static BaseApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initOSS();
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

    private void initOSS() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OssManager.initUpload(BaseApplication.this);
            }
        }).start();
    }
}
