package com.rz.circled.js;

import android.app.Activity;

import com.google.gson.Gson;
import com.rz.circled.ui.activity.NavigateOpenActivity;
import com.rz.sgt.jsbridge.BaseParamsObject;
import com.rz.sgt.jsbridge.ServerHandler;
import com.rz.sgt.jsbridge.core.Callback;
import com.rz.sgt.jsbridge.core.ParamsObject;
import com.rz.sgt.jsbridge.core.WebViewProxy;

import org.json.JSONException;

/**
 * Created by KF on 2017/7/5.
 */
public class NavigateHandler extends ServerHandler {
    public NavigateHandler(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public String getInvokeName() {
        return "mapNavigation";
    }

    @Override
    public void handle(String params, ParamsObject paramObj, Callback callback) {
        Gson gson = new Gson();
        String dataJson = gson.toJson(paramObj.getData());
        double latitude = 0, longitude = 0;
        String name = null;
        try {
            org.json.JSONObject jsonObject = new org.json.JSONObject(dataJson);
            latitude = jsonObject.getDouble("latitude");
            longitude = jsonObject.getDouble("longitude");
            name = jsonObject.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NavigateOpenActivity.startNavigateOpen(mActivity, latitude, longitude, name);
    }

    @Override
    public Callback generateCallBack(WebViewProxy webViewProxy, ParamsObject paramsObject) {
        return new Callback(webViewProxy, paramsObject.getInvokeId(), paramsObject.getInvokeName()) {
            @Override
            public void invoke(Object businessParams, BaseParamsObject baseParamsObject) {

            }
        };
    }

    @Override
    public boolean isUi() {
        return true;
    }
}
