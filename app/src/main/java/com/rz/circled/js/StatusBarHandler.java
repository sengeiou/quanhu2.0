package com.rz.circled.js;


import android.app.Activity;

import com.rz.common.utils.StatusBarUtils;
import com.rz.sgt.jsbridge.ServerHandler;
import com.rz.sgt.jsbridge.core.Callback;
import com.rz.sgt.jsbridge.core.ParamsObject;
import com.rz.sgt.jsbridge.core.WebViewProxy;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/4/1 0001.
 */

public class StatusBarHandler extends ServerHandler {


    public StatusBarHandler(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public String getInvokeName() {
        return "statusBar";
    }

    @Override
    public void handle(String params, ParamsObject paramObj, Callback callback) {
        boolean dark = true;
        try {
            JSONObject jsonObject = new JSONObject(String.valueOf(paramObj.data));
            if (jsonObject.has("dark")) {
                dark = jsonObject.getBoolean("dark");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (android.os.Build.BRAND.equalsIgnoreCase("xiaomi") || android.os.Build.BRAND.equalsIgnoreCase("meizu"))
            return;
        StatusBarUtils.setDarkStatusIcon(mActivity, !dark);
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
