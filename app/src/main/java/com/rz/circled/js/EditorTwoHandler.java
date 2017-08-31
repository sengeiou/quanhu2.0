package com.rz.circled.js;

import android.app.Activity;
import android.content.Intent;

import com.google.gson.Gson;
import com.rz.circled.js.model.EditorRootTwoBean;
import com.rz.circled.ui.activity.EditorTwoActivity;
import com.rz.common.constant.IntentKey;
import com.rz.sgt.jsbridge.BaseParamsObject;
import com.rz.sgt.jsbridge.ServerHandler;
import com.rz.sgt.jsbridge.core.Callback;
import com.rz.sgt.jsbridge.core.ParamsObject;
import com.rz.sgt.jsbridge.core.WebViewProxy;

/**
 * 作品编辑处理->2
 */

public class EditorTwoHandler extends ServerHandler {

    public EditorTwoHandler(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public String getInvokeName() {
        return "publishImageText";
    }

    @Override
    public void handle(String params, ParamsObject paramObj, Callback callback) {
        Gson gson = new Gson();
        String dataStr = gson.toJson(paramObj.data);
        EditorRootTwoBean rootBean = gson.fromJson(dataStr, EditorRootTwoBean.class);
        final Intent intent = new Intent(mActivity, EditorTwoActivity.class);
        intent.putExtra(IntentKey.EXTRA_SERIALIZABLE, rootBean);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mActivity.startActivity(intent);
            }
        });
    }

    @Override
    public Callback generateCallBack(WebViewProxy webViewProxy, ParamsObject paramsObject) {
        Callback callback = new Callback(webViewProxy, paramsObject.getInvokeId(), paramsObject.getInvokeName()) {
            @Override
            public void invoke(Object businessParams, BaseParamsObject baseParamsObject) {
                baseParamsObject.data = businessParams;
            }
        };
        return callback;
    }


    @Override
    public boolean isUi() {
        return true;
    }
}
