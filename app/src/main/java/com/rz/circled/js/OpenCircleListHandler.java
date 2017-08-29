package com.rz.circled.js;

import android.app.Activity;

import com.rz.sgt.jsbridge.ServerHandler;
import com.rz.sgt.jsbridge.core.Callback;
import com.rz.sgt.jsbridge.core.ParamsObject;
import com.rz.sgt.jsbridge.core.WebViewProxy;

/**
 * Created by KF on 2017/6/6.
 */
public class OpenCircleListHandler extends ServerHandler {
    public OpenCircleListHandler(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public String getInvokeName() {
        return "openYrSquare";
    }

    @Override
    public void handle(String params, ParamsObject paramObj, Callback callback) {
//        Intent intent = new Intent(mActivity, V3CircleListAty.class);
//        mActivity.startActivity(intent);
//        mActivity.finish();
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
