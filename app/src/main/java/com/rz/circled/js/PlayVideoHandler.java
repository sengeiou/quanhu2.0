package com.rz.circled.js;

import android.app.Activity;
import android.content.Intent;

import com.google.gson.Gson;
import com.rz.circled.ui.activity.MediaActivity;
import com.rz.common.constant.IntentKey;
import com.rz.sgt.jsbridge.BaseParamsObject;
import com.rz.sgt.jsbridge.ServerHandler;
import com.rz.sgt.jsbridge.core.Callback;
import com.rz.sgt.jsbridge.core.ParamsObject;
import com.rz.sgt.jsbridge.core.WebViewProxy;

import org.json.JSONException;

/**
 * Created by KF on 2017/5/27.
 */
public class PlayVideoHandler extends ServerHandler {
    public PlayVideoHandler(Activity mActivity) {
        super(mActivity);
    }


    @Override
    public String getInvokeName() {
        return "playVideo";
    }

    @Override
    public void handle(String params, ParamsObject paramObj, Callback callback) {
        Gson gson = new Gson();
        String dataJson = gson.toJson(paramObj.getData());
        String videoUrl = "";
        try {
            org.json.JSONObject jsonObject = new org.json.JSONObject(dataJson);
            videoUrl = jsonObject.getString("videoUrl");
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        Intent intent = new Intent(mActivity, FullScreenVideoActivity.class);
        Intent intent = new Intent(mActivity, MediaActivity.class);
        intent.putExtra(IntentKey.EXTRA_PATH, videoUrl);
        intent.putExtra(IntentKey.EXTRA_BOOLEAN, false);
        mActivity.startActivity(intent);
    }

    @Override
    public Callback generateCallBack(WebViewProxy webViewProxy, ParamsObject paramsObject) {
        return new Callback(webViewProxy, paramsObject.invokeId, paramsObject.invokeName) {
            @Override
            public void invoke(Object businessParams, BaseParamsObject baseParamsObject) {

            }
        };
    }

    @Override
    public boolean isUi() {
        return true;
    }
}
