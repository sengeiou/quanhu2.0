package com.rz.circled.js;

import android.app.Activity;
import android.content.Context;
import android.text.ClipboardManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.rz.sgt.jsbridge.BaseParamsObject;
import com.rz.sgt.jsbridge.JsEvent;
import com.rz.sgt.jsbridge.ServerHandler;
import com.rz.sgt.jsbridge.core.Callback;
import com.rz.sgt.jsbridge.core.ParamsObject;
import com.rz.sgt.jsbridge.core.WebViewProxy;

import org.json.JSONException;


/**
 * Created by Administrator on 2017/4/15 0015.
 */

public class PasteBoardHandler extends ServerHandler {


    public PasteBoardHandler(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public String getInvokeName() {
        return "pasteboard";
    }

    @Override
    public void handle(String params, ParamsObject paramObj, Callback callback) {
        String txt = null;
        try {
            String s = new Gson().toJson(paramObj.data);
            org.json.JSONObject jsonObject = new org.json.JSONObject(s);
            txt = jsonObject.getString("text");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ClipboardManager cm = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
            cm.setText(txt);
        }
        JsEvent.callJsEvent(paramObj.getInvokeId(), null, TextUtils.isEmpty(txt) ? BaseParamsObject.RESULT_CODE_FAILED : BaseParamsObject.RESULT_CODE_SUCRESS);
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
        return false;
    }
}
