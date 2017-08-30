package com.rz.circled.js;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.rz.circled.application.QHApplication;
import com.rz.circled.js.http.APIWebService;
import com.rz.circled.js.http.WebHttp;
import com.rz.circled.js.model.SendVeifyModel;
import com.rz.sgt.jsbridge.BaseParamsObject;
import com.rz.sgt.jsbridge.JsEvent;
import com.rz.sgt.jsbridge.ServerHandler;
import com.rz.sgt.jsbridge.core.Callback;
import com.rz.sgt.jsbridge.core.ParamsObject;
import com.rz.sgt.jsbridge.core.WebViewProxy;

/**
 * Created by Administrator on 2017/3/29/029.
 */

public class SendVerifyCodeHandler extends ServerHandler {

    public SendVerifyCodeHandler(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public String getInvokeName() {
        return "sendVerifyCode";
//        return "getNetWorkStatus";
    }

    @Override
    public void handle(String params, final ParamsObject paramObj, Callback callback) {
//        params = "{\n" +
//                "    \"data\": {\n" +
//                "        \"code\": \"1\",\n" +
//                "        \"phone\": \"15321361713\",\n" +
//                "        \"type\": \"1\"\n" +
//                "    },\n" +
//                "    \"invokeId\": 1490779502297,\n" +
//                "    \"invokeName\": \"circleShortcut\"\n" +
//                "}";

        Log.e("fengan", "handle: params==" + params);
        Gson gson = new Gson();
        SendVeifyModel sendVeifyModel = null;
        try {
            String dataJson = gson.toJson(paramObj.getData());
            sendVeifyModel = gson.fromJson(dataJson, SendVeifyModel.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            Log.e("fengan", "参数错误");
            Log.e("fengan", "paramObj===" + "paramObj");
            JsEvent.callJsEvent(paramObj.getInvokeId(), "", BaseParamsObject.RESULT_CODE_FAILED);
        }
        //需要的三个参数
        String type = sendVeifyModel.getType();
        String code = sendVeifyModel.getCode();
        String phone = sendVeifyModel.getPhone();
        //访问的接口
        APIWebService mUserService = WebHttp.getWebService(QHApplication.getContext(), null);
//        Call<JsonObject> call = mUserService.sendVeriCodeJs(1001, phone, type, code);//old
//        CallManager.add(call);
//        call.enqueue(new retrofit2.Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                if (response.isSuccessful()) {
//                    JsonObject body = response.body();
//                    String json = body.toString();
//                    Log.e("fengan", "onResponse: response.body() " + response.body());
//                    Log.e("fengan", "onResponse: json " + json);
//                    JsEvent.callJsEvent(paramObj.getInvokeId(), json, json == "" ? BaseParamsObject.RESULT_CODE_SUCRESS : BaseParamsObject.RESULT_CODE_FAILED);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                Log.e("fengan", "onFailure1: " + call);
//                t.printStackTrace();
//                JsEvent.callJsEvent(paramObj.getInvokeId(), "", BaseParamsObject.RESULT_CODE_FAILED);
//            }
//        });

    }

    @Override
    public Callback generateCallBack(WebViewProxy webViewProxy, ParamsObject paramsObject) {
        Callback callback = new Callback(webViewProxy, paramsObject.getInvokeId(), paramsObject.getInvokeName()) {
            @Override
            public void invoke(Object businessParms, BaseParamsObject baseParamsObject) {
                String json = (String) businessParms;
                baseParamsObject.data = json;
                Log.e("fengan", "generateCallBack: " + baseParamsObject);
                Log.e("fengan", "generateCallBack: baseParamsObject.data" + baseParamsObject.data);
            }
        };
        return callback;
    }

    @Override
    public boolean isUi() {
        return false;
    }


}
