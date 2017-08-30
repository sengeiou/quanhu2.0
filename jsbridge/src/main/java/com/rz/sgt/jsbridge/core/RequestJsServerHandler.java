package com.rz.sgt.jsbridge.core;

import android.app.Activity;

import com.google.gson.Gson;
import com.rz.sgt.jsbridge.RequestParamsObject;

/**
 * Created by KF on 2017/5/23.
 */
public abstract class RequestJsServerHandler implements RequestJsBridgeCallBack {

    public Activity activity;
    public long invokeId;
    private RequestParamsObject requestParamsObject;

    public RequestJsServerHandler(Activity activity) {
        this.activity = activity;
    }

    public void setRequestData(Object data) {
        requestParamsObject = new RequestParamsObject();
        requestParamsObject.invokeId = System.currentTimeMillis();
        requestParamsObject.nativeEvent = getNativeEvent();
        requestParamsObject.data = data;
        invokeId = requestParamsObject.invokeId;
    }

    public String getRequestJsData() {
        return new Gson().toJson(requestParamsObject);
    }
}
