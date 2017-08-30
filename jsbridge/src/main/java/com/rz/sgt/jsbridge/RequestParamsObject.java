package com.rz.sgt.jsbridge;

import com.google.gson.Gson;

/**
 * Created by KF on 2017/6/7.
 */
public class RequestParamsObject {
    public Long invokeId;
    public String nativeEvent;
    public String errorMsg;
    public int resultCode;
    public Object data;

    public RequestParamsObject() {
    }

    public RequestParamsObject(Long invokeId, String nativeEvent, String errorMsg, int resultCode) {
        this.invokeId = invokeId;
        this.nativeEvent = nativeEvent;
        this.errorMsg = errorMsg;
        this.resultCode = resultCode;
    }

    public static final int RESULT_CODE_SUCRESS = 1001;

    public static final int RESULT_CODE_FAILED = 1002;

    public static final int RESULT_CODE_CANCEL = 1003;

    public static RequestParamsObject Parse(String json) {
        Gson gson = new Gson();
        RequestParamsObject object = gson.fromJson(json, RequestParamsObject.class);
        return object;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
