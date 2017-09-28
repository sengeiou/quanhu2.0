package com.rz.circled.js;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.rz.circled.modle.TransferModule;
import com.rz.circled.ui.activity.RewardGiftActivity;
import com.rz.httpapi.bean.RewardInfoBean;
import com.rz.sgt.jsbridge.BaseParamsObject;
import com.rz.sgt.jsbridge.ServerHandler;
import com.rz.sgt.jsbridge.core.Callback;
import com.rz.sgt.jsbridge.core.ParamsObject;
import com.rz.sgt.jsbridge.core.WebViewProxy;

import java.util.HashMap;

public class TransferHandler extends ServerHandler {

    public TransferHandler(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public String getInvokeName() {
        return "forward";
    }

    @Override
    public void handle(String params, ParamsObject paramObj, Callback callback) {
        String result = new Gson().toJson(paramObj.data);
        RewardInfoBean transferModule = new Gson().fromJson(result, RewardInfoBean.class);
        Log.d("webView", "handle " + result);
        RewardGiftActivity.startRewardGiftAty(mActivity, transferModule, true);
    }

    @Override
    public Callback generateCallBack(WebViewProxy webViewProxy, ParamsObject paramsObject) {
        Callback callback = new Callback(webViewProxy, paramsObject.getInvokeId(), paramsObject.getInvokeName()) {
            @Override
            public void invoke(Object businessParms, BaseParamsObject baseParamsObject) {
                HashMap<String, String> data = new HashMap<>();
                data.put("code", (String) businessParms);
                baseParamsObject.data = data;
            }
        };
        return callback;
    }

    @Override
    public boolean isUi() {
        return true;
    }

}
