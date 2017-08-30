package com.rz.circled.js;


import android.app.Activity;

import com.rz.sgt.jsbridge.ServerHandler;
import com.rz.sgt.jsbridge.core.Callback;
import com.rz.sgt.jsbridge.core.ParamsObject;
import com.rz.sgt.jsbridge.core.WebViewProxy;

/**
 * Created by Administrator on 2017/3/9 0009.
 */

public class BackHandler extends ServerHandler {

    public BackHandler(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public String getInvokeName() {
        return "back";
    }

    @Override
    public void handle(String params, ParamsObject paramObj, Callback callback) {
        String simpleName = mActivity.getClass().getSimpleName();
        if (simpleName.equals("MainActivity")) return;
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
