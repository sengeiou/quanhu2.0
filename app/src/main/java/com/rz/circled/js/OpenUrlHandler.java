package com.rz.circled.js;

import android.app.Activity;

import com.google.gson.Gson;
import com.rz.circled.ui.activity.CommonH5Activity;
import com.rz.sgt.jsbridge.BaseParamsObject;
import com.rz.sgt.jsbridge.ServerHandler;
import com.rz.sgt.jsbridge.core.Callback;
import com.rz.sgt.jsbridge.core.ParamsObject;
import com.rz.sgt.jsbridge.core.WebViewProxy;

import org.json.JSONException;

/**
 * Created by Gsm on 2017/8/11.
 */
public class OpenUrlHandler extends ServerHandler {

    public OpenUrlHandler(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public String getInvokeName() {
        return "openUrl";
    }

    @Override
    public void handle(String params, ParamsObject paramObj, Callback callback) {
        Gson gson = new Gson();
        String dataJson = gson.toJson(paramObj.getData());
        String url = "";
        try {
            org.json.JSONObject jsonObject = new org.json.JSONObject(dataJson);
            url = jsonObject.getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CommonH5Activity.startCommonH5(mActivity, "", url);
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
        return false;
    }
}
