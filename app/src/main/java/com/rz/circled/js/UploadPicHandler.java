package com.rz.circled.js;

import android.app.Activity;
import android.content.Intent;

import com.rz.circled.ui.activity.UploadPicActivity;
import com.rz.circled.ui.activity.WebContainerActivity;
import com.rz.common.constant.IntentKey;
import com.rz.sgt.jsbridge.BaseParamsObject;
import com.rz.sgt.jsbridge.JsEvent;
import com.rz.sgt.jsbridge.ServerHandler;
import com.rz.sgt.jsbridge.core.Callback;
import com.rz.sgt.jsbridge.core.ParamsObject;
import com.rz.sgt.jsbridge.core.WebViewProxy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/7 0007.
 */

public class UploadPicHandler extends ServerHandler {

    public UploadPicHandler(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public String getInvokeName() {
        return "uploadPics";
    }

    @Override
    public void handle(String params, ParamsObject paramObj, Callback callback) {
        final Intent i = new Intent(mActivity, UploadPicActivity.class);
        int pos = 0;
        boolean isCrop = false;
        double scaleY = 1;
        try {
            JSONObject jsonObject = new JSONObject(String.valueOf(paramObj.data));
            if (jsonObject.has("picNum")) {
                pos = jsonObject.getInt("picNum");
            }
            if (jsonObject.has("isCrop")) {
                isCrop = jsonObject.getBoolean("isCrop");
            }
            if (jsonObject.has("scaleY")) {
                scaleY = jsonObject.getDouble("scaleY");

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (pos < 0 || pos > 30) {
            JsEvent.callJsEvent(paramObj.getInvokeId(), null, BaseParamsObject.RESULT_CODE_FAILED);
        } else {
            if (isCrop) {
                pos = 1;
            }
            i.putExtra(IntentKey.EXTRA_NUM, pos);
            i.putExtra(IntentKey.EXTRA_BOOLEAN, isCrop);
            i.putExtra(UploadPicActivity.EXTRA_SCALEY, scaleY);
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mActivity instanceof WebContainerActivity)
                        i.putExtra(IntentKey.EXTRA_URL, ((WebContainerActivity) mActivity).getOssDir());
                    mActivity.startActivity(i);
                }
            });
        }

    }

    @Override
    public Callback generateCallBack(WebViewProxy webViewProxy, ParamsObject paramsObject) {
        Callback callback = new Callback(webViewProxy, paramsObject.getInvokeId(), paramsObject.getInvokeName()) {
            @Override
            public void invoke(Object businessParms, BaseParamsObject baseParamsObject) {
                Map<String, List<String>> map = new HashMap<>();
                map.put("picUrls", (List<String>) businessParms);
                baseParamsObject.data = map;
            }
        };
        return callback;
    }

    @Override
    public boolean isUi() {
        return true;
    }
}
