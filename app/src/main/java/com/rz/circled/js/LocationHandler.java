package com.rz.circled.js;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.rz.circled.presenter.impl.PersonInfoPresenter;
import com.rz.circled.ui.activity.LocationActivity;
import com.rz.common.cache.preference.Session;
import com.rz.sgt.jsbridge.BaseParamsObject;
import com.rz.sgt.jsbridge.ServerHandler;
import com.rz.sgt.jsbridge.core.Callback;
import com.rz.sgt.jsbridge.core.ParamsObject;
import com.rz.sgt.jsbridge.core.WebViewProxy;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/7 0007.
 */

public class LocationHandler extends ServerHandler {

    private boolean isGetCityCode = false;

    public LocationHandler(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public String getInvokeName() {
        return "getLocation";
    }

    @Override
    public void handle(String params, ParamsObject paramObj, Callback callback) {
        Gson gson = new Gson();
        String dataJson = gson.toJson(paramObj.getData());
        try {
            org.json.JSONObject jsonObject = new org.json.JSONObject(dataJson);
            isGetCityCode = jsonObject.getInt("isGetCityCode") == 1 ? true : false;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("jsonException = ", e.toString());
        }
        Intent intent = new Intent(mActivity, LocationActivity.class);
        mActivity.startActivity(intent);
    }

    @Override
    public Callback generateCallBack(WebViewProxy webViewProxy, ParamsObject paramsObject) {
        Callback callback = new Callback(webViewProxy, paramsObject.getInvokeId(), paramsObject.getInvokeName()) {
            @Override
            public void invoke(Object businessParms, BaseParamsObject baseParamsObject) {
                HashMap<String, Object> hashMap = (HashMap<String, Object>) businessParms;
                Map<String, Object> map = new HashMap<>();
                baseParamsObject.data = hashMap;
                if (isGetCityCode) {//去修改用户资料
                    PersonInfoPresenter personInfoPresenter = new PersonInfoPresenter();
                    personInfoPresenter.initForLocation(mActivity);
                    personInfoPresenter.savePersonInfo(Session.getUserId(), "location", (String) hashMap.get("province") + (String) hashMap.get("city"), (String) hashMap.get("cityCode"));
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
