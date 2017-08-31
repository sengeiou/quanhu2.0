package com.rz.circled.js;

import android.app.Activity;
import android.util.Log;

import com.rz.sgt.jsbridge.BaseParamsObject;
import com.rz.sgt.jsbridge.ServerHandler;
import com.rz.sgt.jsbridge.core.Callback;
import com.rz.sgt.jsbridge.core.ParamsObject;
import com.rz.sgt.jsbridge.core.WebViewProxy;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/3/9/009.
 */

public class RechargeHandler extends ServerHandler {
    public RechargeHandler(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public String getInvokeName() {
        return "showCharge";
    }

    @Override
    public void handle(String params, ParamsObject paramObj, Callback callback) {
        //充值
//        Intent intent = new Intent(mActivity, RechargeMoneyAty.class);
//        intent.putExtra(IntentKey.EXTRA_INVOKE_ID, paramObj.getInvokeId());
//        mActivity.startActivity(intent);
    }

    @Override
    public Callback generateCallBack(WebViewProxy webViewProxy, ParamsObject paramsObject) {
        Callback callback = new Callback(webViewProxy, paramsObject.getInvokeId(), paramsObject.getInvokeName()) {
            @Override
            public void invoke(Object businessParams, BaseParamsObject baseParamsObject) {
                Log.e("fengan", "invoke: " + businessParams);
                long rechargeMoney = (long) businessParams;
                HashMap<String, Long> data = new HashMap<>();
                data.put("rechargeMoney", rechargeMoney);
                baseParamsObject.data = data;
            }
        };
        return callback;
    }

    @Override
    public boolean isUi() {
        return true;
    }
}
