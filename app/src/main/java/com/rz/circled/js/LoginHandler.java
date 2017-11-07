package com.rz.circled.js;

import android.app.Activity;
import android.content.Intent;

import com.rz.circled.ui.activity.LoginActivity;
import com.rz.common.constant.IntentKey;
import com.rz.common.constant.Type;
import com.rz.sgt.jsbridge.BaseParamsObject;
import com.rz.sgt.jsbridge.ServerHandler;
import com.rz.sgt.jsbridge.core.Callback;
import com.rz.sgt.jsbridge.core.ParamsObject;
import com.rz.sgt.jsbridge.core.WebViewProxy;
/**
 * Created by KF on 2017/6/15.
 */
public class LoginHandler extends ServerHandler {
    public LoginHandler(Activity mActivity) {
        super(mActivity);
    }


    @Override
    public String getInvokeName() {
        return "login";
    }

    @Override
    public void handle(String params, ParamsObject paramObj, Callback callback) {
        Intent mIntent = new Intent(mActivity, LoginActivity.class);
        mIntent.putExtra(IntentKey.EXTRA_TYPE, Type.TYPE_LOGIN_WEB);
        mActivity.startActivity(mIntent);
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
