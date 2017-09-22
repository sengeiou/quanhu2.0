package com.rz.circled.js;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.rz.circled.R;
import com.rz.circled.ui.activity.CommonH5Activity;
import com.rz.sgt.jsbridge.ServerHandler;
import com.rz.sgt.jsbridge.core.Callback;
import com.rz.sgt.jsbridge.core.ParamsObject;
import com.rz.sgt.jsbridge.core.WebViewProxy;

import org.json.JSONException;

/**
 * Created by KF on 2017/6/26.
 */
public class OpenAppHandler extends ServerHandler {
    public OpenAppHandler(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public String getInvokeName() {
        return "openAPP";
    }

    @Override
    public void handle(String params, ParamsObject paramObj, Callback callback) {
        Gson gson = new Gson();
        String dataJson = gson.toJson(paramObj.getData());
        String urlScheme = "";
        String downloadUrl = "";
        String extendParameter = "";
        try {
            org.json.JSONObject jsonObject = new org.json.JSONObject(dataJson);
            urlScheme = jsonObject.getString("urlScheme");
            downloadUrl = jsonObject.getString("downloadUrl");
            extendParameter = jsonObject.getString("extendParameter");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (haveApp(extendParameter)) {
            if (!TextUtils.isEmpty(urlScheme)) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlScheme));
                mActivity.startActivity(intent);
            }
        } else {
            CommonH5Activity.startCommonH5(mActivity, "", downloadUrl);
        }
    }

    private boolean haveApp(String packageName) {
        if (TextUtils.isEmpty(packageName))
            return false;
        try {
            mActivity.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @Override
    public Callback generateCallBack(WebViewProxy webViewProxy, ParamsObject paramsObject) {
        return null;
    }

    @Override
    public boolean isUi() {
        return false;
    }
}
