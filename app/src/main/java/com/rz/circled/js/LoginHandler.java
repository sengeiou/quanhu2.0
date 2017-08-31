package com.rz.circled.js;

import android.content.Intent;

import com.rz.jsbridge.BaseParamsObject;
import com.rz.jsbridge.ServerHandler;
import com.rz.jsbridge.core.Callback;
import com.rz.jsbridge.core.ParamsObject;
import com.rz.jsbridge.core.WebContainerAty;
import com.rz.jsbridge.core.WebViewProxy;
import com.rz.rz_rrz.constant.IntentKey;
import com.rz.rz_rrz.constant.Type;
import com.rz.rz_rrz.view.activity.LoginAty;

/**
 * Created by KF on 2017/6/15.
 */
public class LoginHandler extends ServerHandler {
    public LoginHandler(WebContainerAty webContainerAty) {
        super(webContainerAty);
    }

    @Override
    public String getInvokeName() {
        return "login";
    }

    @Override
    public void handle(String params, ParamsObject paramObj, Callback callback) {
        Intent mIntent = new Intent(webContainerAty, LoginAty.class);
        mIntent.putExtra(IntentKey.KEY_TYPE, Type.TYPE_LOGIN_WEB);
        webContainerAty.startActivity(mIntent);
    }

    @Override
    public Callback generateCallBack(WebViewProxy webViewProxy, ParamsObject paramsObject) {
        Callback callback = new Callback(webViewProxy, paramsObject.getInvokeId(), paramsObject.getInvokeName()) {
            @Override
            public void invoke(Object businessParms, BaseParamsObject baseParamsObject) {
                baseParamsObject.data = businessParms;
            }
        };
        return callback;
    }

    @Override
    public boolean isUi() {
        return true;
    }
}
