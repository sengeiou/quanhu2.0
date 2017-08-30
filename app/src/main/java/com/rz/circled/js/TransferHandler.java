package com.rz.circled.js;

import android.app.Activity;

import com.google.gson.Gson;
import com.rz.sgt.jsbridge.BaseParamsObject;
import com.rz.sgt.jsbridge.ServerHandler;
import com.rz.sgt.jsbridge.core.Callback;
import com.rz.sgt.jsbridge.core.ParamsObject;
import com.rz.sgt.jsbridge.core.WebViewProxy;

public class TransferHandler extends ServerHandler {

    public TransferHandler(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public String getInvokeName() {
        return "forward";
    }

    @Override
    public void handle(String params, ParamsObject paramObj, Callback callback) {
        String result = new Gson().toJson(paramObj.data);
//        V3TransferMoneyAty.TransferModule transferModule = new Gson().fromJson(result, V3TransferMoneyAty.TransferModule.class);
//        String infoId = transferModule.infoId;
//        if (!TextUtils.isEmpty(infoId)) {
//            String[] s = infoId.split("\\.");
//            if (s.length > 0) {
//                transferModule.infoId = infoId;
//            }
//        }
//        Log.d("yeying", "handle " + transferModule.toString());
//        V3TransferGiftAty.startTransferGiftAty(mActivity, transferModule, true);
    }

    @Override
    public Callback generateCallBack(WebViewProxy webViewProxy, ParamsObject paramsObject) {
        Callback callback = new Callback(webViewProxy, paramsObject.getInvokeId(), paramsObject.getInvokeName()) {
            @Override
            public void invoke(Object businessParms, BaseParamsObject baseParamsObject) {

            }
        };
        return callback;
    }

    @Override
    public boolean isUi() {
        return true;
    }

}
