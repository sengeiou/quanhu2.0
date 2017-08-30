package com.rz.circled.js;


import android.app.Activity;

import com.rz.circled.ui.activity.ReportActivity;
import com.rz.common.constant.H5Address;
import com.rz.sgt.jsbridge.BaseParamsObject;
import com.rz.sgt.jsbridge.ServerHandler;
import com.rz.sgt.jsbridge.core.Callback;
import com.rz.sgt.jsbridge.core.ParamsObject;
import com.rz.sgt.jsbridge.core.WebViewProxy;

/**
 * Created by Administrator on 2017/4/18 0018.
 */

public class ReportHandler extends ServerHandler {

    public ReportHandler(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public String getInvokeName() {
        return "report";
    }

    @Override
    public void handle(String params, ParamsObject paramObj, Callback callback) {

        ReportActivity.startAty(mActivity, H5Address.ONLINE_REPORT);
    }

    @Override
    public Callback generateCallBack(WebViewProxy webViewProxy, ParamsObject paramsObject) {
        Callback callback = new Callback(webViewProxy, paramsObject.getInvokeId(), paramsObject.getInvokeName()) {
            @Override
            public void invoke(Object businessParams, BaseParamsObject baseParamsObject) {
            }
        };
        return callback;
    }

    @Override
    public boolean isUi() {
        return true;
    }
}
