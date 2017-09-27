package com.rz.common.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.rz.common.oss.OssManager;

/**
 * Created by Gsm on 2017/8/28.
 */
public class BaseApplication extends Application {
    private static BaseApplication instance;

    public String resumedLocalClassName = "";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                resumedLocalClassName = activity.getLocalClassName();
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
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

    protected void initOSS() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OssManager.initUpload(BaseApplication.this);
            }
        }).start();
    }
}
