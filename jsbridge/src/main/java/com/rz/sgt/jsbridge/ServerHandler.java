package com.rz.sgt.jsbridge;

import android.app.Activity;

import com.rz.sgt.jsbridge.core.JsProtocol;


/**
 * 构造带有web容易的服务处理协议
 */
public abstract class ServerHandler implements JsProtocol {


    public Activity mActivity;

    public ServerHandler(Activity mActivity) {
        this.mActivity = mActivity;
    }
}
