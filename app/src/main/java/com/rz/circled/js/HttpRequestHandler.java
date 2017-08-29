package com.rz.circled.js;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rz.circled.application.QHApplication;
import com.rz.circled.js.http.WebHttp;
import com.rz.httpapi.api.CallManager;
import com.rz.sgt.jsbridge.BaseParamsObject;
import com.rz.sgt.jsbridge.JsEvent;
import com.rz.sgt.jsbridge.ServerHandler;
import com.rz.sgt.jsbridge.core.Callback;
import com.rz.sgt.jsbridge.core.ParamsObject;
import com.rz.sgt.jsbridge.core.WebViewProxy;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/3/22/022.
 */

public class HttpRequestHandler extends ServerHandler {
    public HttpRequestHandler(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public String getInvokeName() {
        return "httpRequest";
//        return "getNetWorkStatus";
    }


    public Map jsonToObject(String jsonStr) throws Exception {
        JSONObject jsonObj = new JSONObject(jsonStr);
        Iterator<String> nameItr = jsonObj.keys();
        String name;
        Map<String, String> outMap = new HashMap<String, String>();
        while (nameItr.hasNext()) {
            name = nameItr.next();
            outMap.put(name, jsonObj.getString(name));
        }
        return outMap;
    }
    @Override
    public void handle(String params, final ParamsObject paramObj, Callback callback) { try {
        if (TextUtils.isEmpty(params)) {
            JsEvent.callJsEvent(paramObj.getInvokeId(),"", BaseParamsObject.RESULT_CODE_FAILED);
            return;
        }

//        params = "{\n" +
//                "    \"data\": {\n" +
//                "        \"body\": {\n" +
//                "            \"l\": \"39.938133,116.395739\",\n" +
//                "            \"type\": \"001\"\n" +
//                "        },\n" +
//                "        \"headers\": {\n" +
//                "            \"Content-type\": \"application/json; charset=UTF-8\"\n" +
//                "        },\n" +
//                "        \"method\": \"post\",\n" +
//                "        \"url\": \"http://gc.ditu.aliyun.com/regeocoding\"\n" +
//                "    },\n" +
//                "    \"invokeId\": 1490779502297,\n" +
//                "    \"invokeName\": \"circleShortcut\"\n" +
//                "}";
        Gson gson = new Gson();
        String dataJson = gson.toJson(paramObj.getData());
        Map data4Map = jsonToObject(dataJson);
        String bodyJson = (String) data4Map.get("body");
        String headerJson = (String) data4Map.get("headers");
        String method = (String) data4Map.get("method");
        String url = (String) data4Map.get("url");
        Log.e("fengan", "handle:url==== "+url );
        Log.e("fengan", "handle:method==== "+method );
        Log.e("fengan", "handle:bodyJson==== "+bodyJson );
        Log.e("fengan", "handle:headerJson==== "+headerJson );
        Map headersMap = new HashMap();
        try {
            headersMap = jsonToObject(headerJson);
            Log.e("fengan", "handle: "+"头数据数据正常" );
            Log.e("fengan", "handle:headersMap==== "+headersMap );
        } catch (Exception e) {
            Log.e("fengan", "handle: "+"头数据数据异常" );
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), bodyJson);
        Call<JsonObject> call = null;
        if (method.equalsIgnoreCase("post")) {
            Log.e("fengan", "onResponse:post " + "postpostpostpostpost");
            call = WebHttp.getWebService(QHApplication.getContext(),headersMap).reQuestNetPost(url, requestBody,headersMap);
        } else if(method.equalsIgnoreCase("get")){
            try {
                Log.e("fengan", "handle: "+"开始map解析" );
                Map map = jsonToObject(bodyJson);
                Log.e("fengan", "handle: "+"结束map解析" );
                Log.e("fengan", "handle: "+map );
                if (map.size()>0) {
                    call = WebHttp.getWebService(QHApplication.getContext(),headersMap).reQuestNetGetMap(url, map,headersMap);
                }else {
                    call = WebHttp.getWebService(QHApplication.getContext(),headersMap).reQuestNetGet(url,headersMap);
                }
            } catch (Exception e) {
                Log.e("fengan", "handle: "+"map解析失败,说明无请求参数" );
                Log.e("fengan", "handle: "+"null" );
                call = WebHttp.getWebService(QHApplication.getContext(),headersMap).reQuestNetGet(url,headersMap);
                e.printStackTrace();
            }
        }else if(method.equalsIgnoreCase("put")){
            call = WebHttp.getWebService(QHApplication.getContext(),headersMap).reQuestNetPut(url, requestBody,headersMap);
        }else if(method.equalsIgnoreCase("delete")){
            call = WebHttp.getWebService(QHApplication.getContext(),headersMap).reQuestNetDelete(url, requestBody,headersMap);
        }else if(method.equalsIgnoreCase("patch")){
            call = WebHttp.getWebService(QHApplication.getContext(),headersMap).reQuestNetPatch(url, requestBody,headersMap);
        }

        CallManager.add(call);
        call.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject body = response.body();
                    String json =  body.toString();
                    Log.e("fengan", "onResponse: response.body() " + response.body());
                    Log.e("fengan", "onResponse: json " + json);
                    JsEvent.callJsEvent(paramObj.getInvokeId(),json, json==""? BaseParamsObject.RESULT_CODE_SUCRESS:BaseParamsObject.RESULT_CODE_FAILED);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("fengan", "onFailure1: " + call);
                t.printStackTrace();
                JsEvent.callJsEvent(paramObj.getInvokeId(),"", BaseParamsObject.RESULT_CODE_FAILED);
            }
        });
    } catch (Exception e) {
        Log.e("fengan", "onFailure2: " + "onFailure");
        JsEvent.callJsEvent(paramObj.getInvokeId(),"", BaseParamsObject.RESULT_CODE_FAILED);
        e.printStackTrace();
    }
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
