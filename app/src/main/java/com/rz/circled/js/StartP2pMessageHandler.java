package com.rz.circled.js;


import android.app.Activity;
import android.util.Log;

import com.rz.sgt.jsbridge.BaseParamsObject;
import com.rz.sgt.jsbridge.ServerHandler;
import com.rz.sgt.jsbridge.core.Callback;
import com.rz.sgt.jsbridge.core.ParamsObject;
import com.rz.sgt.jsbridge.core.WebViewProxy;

/**
 * Created by Administrator on 2017/3/7 0007.
 */

public class StartP2pMessageHandler extends ServerHandler {

    public StartP2pMessageHandler(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public String getInvokeName() {
        return "sessionP2P";
    }

    @Override
    public void handle(String params, ParamsObject paramObj, Callback callback) {
        Log.d("zxw", "handle: params" + params);
//        JSONObject object = JSON.parseObject(params);
//        if (object.containsKey("data") && object.getJSONObject("data").containsKey("custId"))
//            SessionHelper.startP2PSession(webContainerAty, object.getJSONObject("data").getString("custId"));
//        else
//            JsEvent.callJsEvent(paramObj.getInvokeId(), null, BaseParamsObject.RESULT_CODE_FAILED);
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
