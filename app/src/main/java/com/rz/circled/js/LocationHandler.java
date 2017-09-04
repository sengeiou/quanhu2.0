package com.rz.circled.js;

import android.app.Activity;
import android.content.Intent;

import com.rz.circled.ui.activity.LocationActivity;
import com.rz.sgt.jsbridge.BaseParamsObject;
import com.rz.sgt.jsbridge.ServerHandler;
import com.rz.sgt.jsbridge.core.Callback;
import com.rz.sgt.jsbridge.core.ParamsObject;
import com.rz.sgt.jsbridge.core.WebViewProxy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/7 0007.
 */

public class LocationHandler extends ServerHandler {

    public LocationHandler(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public String getInvokeName() {
        return "getLocation";
    }

    @Override
    public void handle(String params, ParamsObject paramObj, Callback callback) {
        Intent intent = new Intent(mActivity, LocationActivity.class);
        mActivity.startActivity(intent);
    }

    @Override
    public Callback generateCallBack(WebViewProxy webViewProxy, ParamsObject paramsObject) {
        Callback callback = new Callback(webViewProxy, paramsObject.getInvokeId(), paramsObject.getInvokeName()) {
            @Override
            public void invoke(Object businessParms, BaseParamsObject baseParamsObject) {
                JSONObject json = (JSONObject) businessParms;
                Map<String, Object> map = new HashMap<>();
                try {
                    map.put("longitude", json.getDouble("longitude"));
                    map.put("latitude", json.getDouble("latitude"));
                    map.put("province", json.getString("province"));
                    map.put("city", json.getString("city"));
                    map.put("cityCode", json.getString("cityCode"));
                    map.put("region", json.getString("region"));
                    baseParamsObject.data = map;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        return callback;
    }

    @Override
    public boolean isUi() {
        return true;
    }
}
