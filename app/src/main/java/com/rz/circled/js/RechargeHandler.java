package com.rz.circled.js;

import android.content.Intent;
import android.util.Log;

import com.rz.jsbridge.BaseParamsObject;
import com.rz.jsbridge.ServerHandler;
import com.rz.jsbridge.core.Callback;
import com.rz.jsbridge.core.ParamsObject;
import com.rz.jsbridge.core.WebContainerAty;
import com.rz.jsbridge.core.WebViewProxy;
import com.rz.rz_rrz.constant.IntentKey;
import com.rz.rz_rrz.view.activity.RechargeMoneyAty;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/3/9/009.
 *
 */

public class RechargeHandler  extends ServerHandler {
    public RechargeHandler(WebContainerAty webContainerAty) {
        super(webContainerAty);
    }

    @Override
    public String getInvokeName() {
        return "showCharge";
    }

    @Override
    public void handle(String params, ParamsObject paramObj, Callback callback) {
        //充值
        Intent intent = new Intent(webContainerAty, RechargeMoneyAty.class);
        intent.putExtra(IntentKey.KEY_INVOKEID,paramObj.getInvokeId());
        webContainerAty.startActivity(intent);
    }

    @Override
    public Callback generateCallBack(WebViewProxy webViewProxy, ParamsObject paramsObject) {
        Callback callback = new Callback(webViewProxy, paramsObject.getInvokeId(), paramsObject.getInvokeName()) {
            @Override
            public void invoke(Object businessParams, BaseParamsObject baseParamsObject) {
                Log.e("fengan", "invoke: "+businessParams );
                long rechargeMoney = (long) businessParams;
                HashMap<String, Long> data = new HashMap<>();
                data.put("rechargeMoney",rechargeMoney);
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
