package com.rz.circled.js;

import android.app.Activity;
<<<<<<< HEAD

import com.google.gson.Gson;
import com.rz.circled.ui.activity.JsPayOrderActivity;
=======
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.rz.circled.presenter.impl.PersonInfoPresenter;
import com.rz.circled.ui.activity.JsPayOrderActivity;
import com.rz.circled.ui.activity.LocationActivity;
import com.rz.common.cache.preference.Session;
>>>>>>> 2540931ec03580503cb88e4fe7ef18497de3b69c
import com.rz.sgt.jsbridge.BaseParamsObject;
import com.rz.sgt.jsbridge.ServerHandler;
import com.rz.sgt.jsbridge.core.Callback;
import com.rz.sgt.jsbridge.core.ParamsObject;
import com.rz.sgt.jsbridge.core.WebViewProxy;

<<<<<<< HEAD
=======
import org.json.JSONException;

>>>>>>> 2540931ec03580503cb88e4fe7ef18497de3b69c
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rzw2 on 2017/9/18.
 */

public class PayOrderHandler extends ServerHandler {

    public PayOrderHandler(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public String getInvokeName() {
        return "pay";
    }

    @Override
    public void handle(String params, ParamsObject paramObj, Callback callback) {
        Gson gson = new Gson();
        String dataJson = gson.toJson(paramObj.getData());
        JsPayOrderActivity.startJsPay(mActivity, dataJson);
    }

    @Override
    public Callback generateCallBack(WebViewProxy webViewProxy, ParamsObject paramsObject) {
        Callback callback = new Callback(webViewProxy, paramsObject.getInvokeId(), paramsObject.getInvokeName()) {
            @Override
            public void invoke(Object businessParms, BaseParamsObject baseParamsObject) {
                HashMap<String, Object> hashMap = (HashMap<String, Object>) businessParms;
                Map<String, Object> map = new HashMap<>();
                baseParamsObject.data = hashMap;
            }
        };
        return callback;
    }

    @Override
    public boolean isUi() {
        return true;
    }
}