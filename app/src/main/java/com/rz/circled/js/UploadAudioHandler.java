package com.rz.circled.js;

import android.app.Activity;

import com.rz.sgt.jsbridge.BaseParamsObject;
import com.rz.sgt.jsbridge.ServerHandler;
import com.rz.sgt.jsbridge.core.Callback;
import com.rz.sgt.jsbridge.core.ParamsObject;
import com.rz.sgt.jsbridge.core.WebViewProxy;

/**
 * Created by Administrator on 2017/3/7 0007.
 */

public class UploadAudioHandler extends ServerHandler {

    public UploadAudioHandler(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public String getInvokeName() {
        return "uploadAudio";
    }

    @Override
    public void handle(String params, ParamsObject paramObj, Callback callback) {
//        mActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Intent i = new Intent(mActivity, UploadAudioAty.class);
//                i.putExtra(IntentKey.KEY_URL, webContainerAty.getOssDir());
//                mActivity.startActivity(i);
//            }
//        });
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
