package com.rz.circled.js;

import android.app.Activity;
import android.content.Intent;

import com.rz.sgt.jsbridge.ServerHandler;
import com.rz.sgt.jsbridge.core.Callback;
import com.rz.sgt.jsbridge.core.ParamsObject;
import com.rz.sgt.jsbridge.core.WebViewProxy;

/**
 * Created by Gsm on 2017/8/16.
 */
public class OpenSettingHandler extends ServerHandler {

    public OpenSettingHandler(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public String getInvokeName() {
        return "openSetting";
    }

    @Override
    public void handle(String params, ParamsObject paramObj, Callback callback) {
        try {
            String className = "com.rz.sgt.ui.activity.SettingActivity";
            Class<?> clazz = Class.forName(className);
            mActivity.startActivity(new Intent(mActivity, clazz));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Callback generateCallBack(WebViewProxy webViewProxy, ParamsObject paramsObject) {
        return null;
    }

    @Override
    public boolean isUi() {
        return false;
    }
}
