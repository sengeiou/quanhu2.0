package com.rz.circled.js;

import android.app.Activity;
import android.content.Intent;

import com.rz.circled.ui.activity.MainActivity;
import com.rz.sgt.jsbridge.ServerHandler;
import com.rz.sgt.jsbridge.core.Callback;
import com.rz.sgt.jsbridge.core.ParamsObject;
import com.rz.sgt.jsbridge.core.WebViewProxy;

/**
 * Created by Administrator on 2017/3/9 0009.
 */

public class BackHomeHandler extends ServerHandler {

    public BackHomeHandler(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public String getInvokeName() {
        return "backHome";
    }

    @Override
    public void handle(String params, ParamsObject paramObj, Callback callback) {
        mActivity.startActivity(new Intent(mActivity, MainActivity.class));
        mActivity.finish();
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
