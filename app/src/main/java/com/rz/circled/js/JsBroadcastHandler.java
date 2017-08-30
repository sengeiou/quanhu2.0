package com.rz.circled.js;

import android.app.Activity;

import com.google.gson.Gson;
import com.rz.common.event.BaseEvent;
import com.rz.sgt.jsbridge.BaseParamsObject;
import com.rz.sgt.jsbridge.JsEvent;
import com.rz.sgt.jsbridge.ServerHandler;
import com.rz.sgt.jsbridge.core.Callback;
import com.rz.sgt.jsbridge.core.ParamsObject;
import com.rz.sgt.jsbridge.core.WebViewProxy;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Gsm on 2017/8/16.
 */
public class JsBroadcastHandler extends ServerHandler {
    public JsBroadcastHandler(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public String getInvokeName() {
        return "broadcast";
    }

    @Override
    public void handle(String params, ParamsObject paramObj, Callback callback) {
        //收到消息发送给所有webView 去请求js方法 , js方法名为type 内容
        Gson gson = new Gson();
        String dataJson = gson.toJson(paramObj.getData());
//        BroadcastModel broadcastModel = gson.fromJson(dataJson, BroadcastModel.class);
//        if (!TextUtils.isEmpty(broadcastModel.getData().getUserId()))
//            Session.setJsUserId(broadcastModel.getData().getUserId());
//        sendEvent(CommonCode.EventType.LOGIN_WEB_CONTAINER, paramObj.data, broadcastModel.getType());
//        sendEvent(CommonCode.EventType.LOGIN_HOME, paramObj.data, broadcastModel.getType());
//        sendEvent(CommonCode.EventType.LOGIN_MINE, paramObj.data, broadcastModel.getType());
        JsEvent.callJsEvent(null, true);
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

    private void sendEvent(int type, Object data, String requestName) {
        BaseEvent webEvent = new BaseEvent(type);
        webEvent.data = data;
        webEvent.info = requestName;
        EventBus.getDefault().post(webEvent);
    }
}
