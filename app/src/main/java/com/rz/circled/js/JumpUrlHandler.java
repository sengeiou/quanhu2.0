package com.rz.circled.js;

import android.app.Activity;

import com.google.gson.Gson;
import com.rz.circled.helper.BannerJumpHelper;
import com.rz.sgt.jsbridge.BaseParamsObject;
import com.rz.sgt.jsbridge.JsEvent;
import com.rz.sgt.jsbridge.ServerHandler;
import com.rz.sgt.jsbridge.core.Callback;
import com.rz.sgt.jsbridge.core.ParamsObject;
import com.rz.sgt.jsbridge.core.WebViewProxy;

import org.json.JSONException;

/**
 * Created by Gsm on 2017/8/11.
 */
public class JumpUrlHandler extends ServerHandler {

    public JumpUrlHandler(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public String getInvokeName() {
        return "jumpUrl";
    }

    @Override
    public void handle(String params, ParamsObject paramObj, Callback callback) {
        Gson gson = new Gson();
        String dataJson = gson.toJson(paramObj.getData());
        try {
            org.json.JSONObject jsonObject = new org.json.JSONObject(dataJson);
            String url = jsonObject.getString("url");

            BannerJumpHelper.bannerJumpHanderHelper(mActivity,url,true);

        } catch (JSONException e) {
            e.printStackTrace();
            JsEvent.callJsEvent(null, false);
        }
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
