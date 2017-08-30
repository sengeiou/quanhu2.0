package com.rz.circled.js;

import android.app.Activity;

import com.rz.sgt.jsbridge.BaseParamsObject;
import com.rz.sgt.jsbridge.JsEvent;
import com.rz.sgt.jsbridge.ServerHandler;
import com.rz.sgt.jsbridge.core.Callback;
import com.rz.sgt.jsbridge.core.ParamsObject;
import com.rz.sgt.jsbridge.core.WebViewProxy;

import org.json.JSONException;

/**
 * Created by Administrator on 2017/3/7 0007.
 */

public class FriendInfoHandler extends ServerHandler {

    public FriendInfoHandler(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public String getInvokeName() {
        return "toPersonalInfo";
    }

    @Override
    public void handle(String params, ParamsObject paramObj, Callback callback) {
        String userId = null;
        try {
            org.json.JSONObject jsonObject = new org.json.JSONObject(String.valueOf(paramObj.data));
            userId = jsonObject.getString("userId");
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        FriendInfoAty.newFrindInfo(webContainerAty, userId);
        JsEvent.callJsEvent(null, true);
    }

    @Override
    public Callback generateCallBack(WebViewProxy webViewProxy, ParamsObject paramsObject) {
        Callback callback = new Callback(webViewProxy, paramsObject.getInvokeId(), paramsObject.getInvokeName()) {
            @Override
            public void invoke(Object businessParms, BaseParamsObject baseParamsObject) {
            }
        };
        return callback;
    }

    @Override
    public boolean isUi() {
        return true;
    }
}
