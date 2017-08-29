package com.rz.circled.js;

import android.app.Activity;

import com.rz.sgt.jsbridge.RequestParamsObject;
import com.rz.sgt.jsbridge.core.RequestJsServerHandler;

/**
 * Created by Gsm on 2017/8/9.
 */
public class RequestBackHandler extends RequestJsServerHandler {

    public RequestBackHandler(Activity activity) {
        super(activity);
    }

    @Override
    public String getNativeEvent() {
        return "nativeBack";
    }

    @Override
    public void onCallBack(String params, RequestParamsObject paramsObject) {

    }

    @Override
    public boolean isUi() {
        return false;
    }
}
