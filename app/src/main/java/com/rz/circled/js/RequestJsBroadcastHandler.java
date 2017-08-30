package com.rz.circled.js;

import android.app.Activity;

import com.rz.sgt.jsbridge.RequestParamsObject;
import com.rz.sgt.jsbridge.core.RequestJsServerHandler;

/**
 * Created by Gsm on 2017/8/16.
 */
public class RequestJsBroadcastHandler extends RequestJsServerHandler {
    private String nativeName;

    public RequestJsBroadcastHandler(Activity activity) {
        super(activity);
    }

    public void setNativeEvent(String nativeName) {
        this.nativeName = nativeName;
    }

    @Override
    public String getNativeEvent() {
        return nativeName;
    }

    @Override
    public void onCallBack(String params, RequestParamsObject paramsObject) {

    }

    @Override
    public boolean isUi() {
        return false;
    }
}
