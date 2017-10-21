package com.rz.circled.js;


import android.app.Activity;

import com.rz.circled.application.QHApplication;
import com.rz.circled.ui.activity.MainActivity;
import com.rz.common.constant.CommonCode;
import com.rz.common.event.BaseEvent;
import com.rz.sgt.jsbridge.BaseParamsObject;
import com.rz.sgt.jsbridge.ServerHandler;
import com.rz.sgt.jsbridge.core.Callback;
import com.rz.sgt.jsbridge.core.ParamsObject;
import com.rz.sgt.jsbridge.core.WebViewProxy;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by KF on 2017/8/1.
 */
public class FinishLoadingHandler extends ServerHandler {
    public FinishLoadingHandler(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public String getInvokeName() {
        return "finishLoading";
    }

    @Override
    public void handle(String params, ParamsObject paramObj, Callback callback) {
        //finish loading
        EventBus.getDefault().post(new BaseEvent(CommonCode.EventType.FINISH_LOADING));
        if (mActivity instanceof MainActivity) {
            WebViewProxy mWebViewProxy = (WebViewProxy) QHApplication.getInstance().getWebView(false).getTag();
            mWebViewProxy.setNeedLoading(false);
        }
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
}
