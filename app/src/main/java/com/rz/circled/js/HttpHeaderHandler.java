package com.rz.circled.js;

import android.app.Activity;
import android.os.Build;
import android.util.Log;

import com.rz.circled.BuildConfig;
import com.rz.circled.application.QHApplication;
import com.rz.circled.js.model.HeaderModel;
import com.rz.common.cache.preference.Session;
import com.rz.common.utils.IntentUtil;
import com.rz.common.utils.SystemUtils;
import com.rz.sgt.jsbridge.BaseParamsObject;
import com.rz.sgt.jsbridge.JsEvent;
import com.rz.sgt.jsbridge.ServerHandler;
import com.rz.sgt.jsbridge.core.Callback;
import com.rz.sgt.jsbridge.core.ParamsObject;
import com.rz.sgt.jsbridge.core.WebViewProxy;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2017/3/29/029.
 */

public class HttpHeaderHandler extends ServerHandler {
    public HttpHeaderHandler(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public String getInvokeName() {
        return "httpHeader";
//        return "getNetWorkStatus";
    }

    @Override
    public void handle(String params, ParamsObject paramObj, Callback callback) {
        String paramStr = "";
        HeaderModel headerModel = new HeaderModel();
        headerModel.sign = "ceshi";
        headerModel.token = Session.getSessionKey();
        headerModel.devType = "2";
//        String sign = DesUtils.encrypt(act + "." + Session.getUserId() + "." + Session.getSessionKey()).replace("\\s", "").replace("\n", "");
//        headerModel.apiVersion ="V3.0.0";
        try {
            headerModel.devName = URLEncoder.encode(Build.MODEL, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        headerModel.appVersion = BuildConfig.VERSION_NAME;
        headerModel.devId = SystemUtils.getIMEI(QHApplication.getContext());
        headerModel.ip = SystemUtils.getIp(QHApplication.getContext());
        headerModel.net = IntentUtil.getNetType(QHApplication.getContext());
        headerModel.custId = Session.getUserId();
        headerModel.userId = Session.getJsUserId();
        headerModel.phone = Session.getUserPhone();
//        Gson gson = new Gson();
//        paramStr= gson.toJson(headerModel);
//        Log.e("fengan", "handle:paramStr== "+paramStr );
        JsEvent.callJsEvent(paramObj.getInvokeId(), headerModel, headerModel != null ? BaseParamsObject.RESULT_CODE_SUCRESS : BaseParamsObject.RESULT_CODE_FAILED);
    }

    @Override
    public Callback generateCallBack(WebViewProxy webViewProxy, ParamsObject paramsObject) {
        Callback callback = new Callback(webViewProxy, paramsObject.getInvokeId(), paramsObject.getInvokeName()) {
            @Override
            public void invoke(Object businessParms, BaseParamsObject baseParamsObject) {
//                String paramStr = (String) businessParms;
                baseParamsObject.data = businessParms;
                Log.e("fengan", "handle:baseParamsObject== " + baseParamsObject);
            }
        };
        return callback;
    }

    @Override
    public boolean isUi() {
        return false;
    }
}
