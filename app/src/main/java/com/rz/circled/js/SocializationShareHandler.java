package com.rz.circled.js;


import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.rz.circled.R;
import com.rz.circled.ui.activity.SocializationShareAty;
import com.rz.sgt.jsbridge.BaseParamsObject;
import com.rz.sgt.jsbridge.ServerHandler;
import com.rz.sgt.jsbridge.core.Callback;
import com.rz.sgt.jsbridge.core.ParamsObject;
import com.rz.sgt.jsbridge.core.WebViewProxy;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/7 0007.
 */

public class SocializationShareHandler extends ServerHandler {

    public SocializationShareHandler(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public String getInvokeName() {
        return "shareToOtherPlat";
    }

    @Override
    public void handle(String params, ParamsObject paramObj, Callback callback) {
        Log.d("zxw", "handle: paramObj" + paramObj.toJson());
        Gson gson = new Gson();
        String dataJson = gson.toJson(paramObj.getData());
        SocializationShareModel socializationShareModel = gson.fromJson(dataJson, SocializationShareModel.class);
        try {
            SocializationShareAty.start(mActivity,
                    "",
                    socializationShareModel.getCircleKey(),
                    !TextUtils.isEmpty(socializationShareModel.getTitle()) ? URLDecoder.decode(URLEncoder.encode(socializationShareModel.getTitle(), "utf-8").replaceAll("%ef%bf%bc", "").replaceAll("%EF%BF%BC", ""), "utf-8") : mActivity.getResources().getString(R.string.share_title1),
                    !TextUtils.isEmpty(socializationShareModel.getContent()) ? URLDecoder.decode(URLEncoder.encode(socializationShareModel.getContent(), "utf-8").replaceAll("%ef%bf%bc", "").replaceAll("%EF%BF%BC", ""), "utf-8") : mActivity.getResources().getString(R.string.share_desc1),
                    socializationShareModel.getPic(),
                    socializationShareModel.getUrl(),
                    socializationShareModel.getPlat());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        SocializationShareAty.start(mActivity,
//                "",
//                "这里是标题",
//                "这里是内容",
//                "http://tse1.mm.bing.net/th?id=OIP.Q9Cy7oATyq-sPtRlb1pqAADkEs&w=186&h=244&c=7&qlt=90&o=4&pid=1.7",
//                "http://www.baidu.com",
//                object.getJSONObject("data").getString("plat"));
    }

    @Override
    public Callback generateCallBack(WebViewProxy webViewProxy, ParamsObject paramsObject) {
        Callback callback = new Callback(webViewProxy, paramsObject.getInvokeId(), paramsObject.getInvokeName()) {
            @Override
            public void invoke(Object businessParms, BaseParamsObject baseParamsObject) {
                Map<String, String> map = new HashMap<>();
                map.put("code", businessParms + "");
                baseParamsObject.data = map;
            }
        };
        return callback;
    }

    @Override
    public boolean isUi() {
        return true;
    }

    class SocializationShareModel {
        private String title;
        private String circleKey;
        private String content;
        private String pic;
        private String url;
        private String plat;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCircleKey() {
            return circleKey;
        }

        public void setCircleKey(String circleKey) {
            this.circleKey = circleKey;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPlat() {
            return plat;
        }

        public void setPlat(String plat) {
            this.plat = plat;
        }
    }
}
