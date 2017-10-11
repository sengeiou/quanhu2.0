package com.rz.circled.js;

import android.app.Activity;

import com.rz.common.event.KickEvent;
import com.rz.sgt.jsbridge.ServerHandler;
import com.rz.sgt.jsbridge.core.Callback;
import com.rz.sgt.jsbridge.core.ParamsObject;
import com.rz.sgt.jsbridge.core.WebViewProxy;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Gsm on 2017/10/10.
 */
public class KickOutHandler extends ServerHandler {

    public KickOutHandler(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public String getInvokeName() {
        return "kickOut";
    }

    @Override
    public void handle(String params, ParamsObject paramObj, Callback callback) {
        EventBus.getDefault().post(new KickEvent(4));
    }

    @Override
    public Callback generateCallBack(WebViewProxy webViewProxy, ParamsObject paramsObject) {
        return null;
    }

    @Override
    public boolean isUi() {
        return false;
    }
}
