package com.rz.circled.js;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;
import com.rz.sgt.jsbridge.BaseParamsObject;
import com.rz.sgt.jsbridge.ServerHandler;
import com.rz.sgt.jsbridge.core.Callback;
import com.rz.sgt.jsbridge.core.ParamsObject;
import com.rz.sgt.jsbridge.core.WebViewProxy;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/3/9/009.
 */

public class NetStatusHandler extends ServerHandler {
    public NetStatusHandler(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public String getInvokeName() {
        return "getNetWorkStatus";
//        return "test";
    }

    @Override
    public void handle(String params, ParamsObject paramObj, Callback callback) {
        String netWorkStatus = "" ;
//        if (NetworkUtil.isNetAvailable(mActivity)) {
//            if (NetworkUtil.isWifi(mActivity)) {
//                netWorkStatus  = "wifi";
//            }else{
//                netWorkStatus = "wwan";
//            }
//        }else{
//            netWorkStatus = "notReachable";
//        }
//        JsEvent.callJsEvent(paramObj.getInvokeId(),netWorkStatus, netWorkStatus!=""? BaseParamsObject.RESULT_CODE_SUCRESS:BaseParamsObject.RESULT_CODE_FAILED);
    }


    @Override
    public Callback generateCallBack(WebViewProxy webViewProxy, ParamsObject paramsObject) {

        Callback callback = new Callback(webViewProxy, paramsObject.getInvokeId(), paramsObject.getInvokeName()) {
            @Override
            public void invoke(Object businessParms, BaseParamsObject baseParamsObject) {
                String netWorkStatus = (String) businessParms;
                HashMap<String, String> data = new HashMap<>();
                data.put("netWorkStatus",netWorkStatus);
                String json = new Gson().toJson(data);
                baseParamsObject.data = json;
                Log.e("fengan", "invoke: baseParamsObject=="+baseParamsObject );
            }
        };
        return callback;
    }


    @Override
    public boolean isUi() {
        return false;
    }
}
