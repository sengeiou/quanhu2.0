package com.rz.circled.js;

import android.app.Activity;
import android.util.Log;

import com.rz.sgt.jsbridge.BaseParamsObject;
import com.rz.sgt.jsbridge.ServerHandler;
import com.rz.sgt.jsbridge.core.Callback;
import com.rz.sgt.jsbridge.core.ParamsObject;
import com.rz.sgt.jsbridge.core.WebViewProxy;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rzw2 on 2017/4/6.
 */

public class CreateTeamHandler extends ServerHandler {

    public CreateTeamHandler(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public String getInvokeName() {
        return "createCircleTeam";
    }

    @Override
    public void handle(String params, ParamsObject paramObj, Callback callback) {
        Log.d("zxw", "handle: params" + params);
//        JSONObject object = JSON.parseObject(params);
//        if (object.containsKey("data") && object.getJSONObject("data").containsKey("circleKey")) {
//            if (object.getJSONObject("data").containsKey("circleName"))
//                CreateTeamActivity.createTeam(webContainerAty, object.getJSONObject("data").getString("circleKey"), object.getJSONObject("data").getString("circleName"), paramObj.getInvokeId());
//            else
//                CreateTeamActivity.createTeam(webContainerAty, object.getJSONObject("data").getString("circleKey"), "育儿圈", paramObj.getInvokeId());
//        } else
//            JsEvent.callJsEvent(paramObj.getInvokeId(), null, BaseParamsObject.RESULT_CODE_FAILED);
    }

    @Override
    public Callback generateCallBack(WebViewProxy webViewProxy, ParamsObject paramsObject) {
        Callback callback = new Callback(webViewProxy, paramsObject.getInvokeId(), paramsObject.getInvokeName()) {
            @Override
            public void invoke(Object businessParms, BaseParamsObject baseParamsObject) {
                if (businessParms == null) return;

                String teamId = (String) businessParms;
                Map<String, Object> map = new HashMap<>();
                map.put("teamId", teamId);
                baseParamsObject.data = map;
            }
        };
        return callback;
    }

    @Override
    public boolean isUi() {
        return false;
    }
}
