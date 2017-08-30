package com.rz.sgt.jsbridge.core;

import com.google.gson.Gson;
import com.rz.sgt.jsbridge.BaseParamsObject;

/**
 * JSSDK交互数据格式
 */

public class ParamsObject {

    public long invokeId;
    public String invokeName;
    public String errMsg;
    public String circleId;

    public Object data;

    public ParamsObject(String invokeName, long invokeId, String errMsg) {
        this.invokeId = invokeId;
        this.invokeName = invokeName;
        this.errMsg = errMsg;
    }

    public ParamsObject(BaseParamsObject baseParamsObject) {
        this.invokeId = baseParamsObject.invokeId;
        this.invokeName = baseParamsObject.invokeName;
        this.data = baseParamsObject.data;
        if (baseParamsObject.resultCode == BaseParamsObject.RESULT_CODE_SUCRESS) {
            this.errMsg = invokeName + ":success";
        } else if (baseParamsObject.resultCode == BaseParamsObject.RESULT_CODE_CANCEL) {
            this.errMsg = invokeName + ":cancel";
        } else if (baseParamsObject.resultCode == BaseParamsObject.RESULT_CODE_FAILED) {
            this.errMsg = invokeName + ":error";
        }
    }


    public static ParamsObject Parse(String json) {
        Gson gson = new Gson();
        ParamsObject object = gson.fromJson(json, ParamsObject.class);
        return object;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public Long getInvokeId() {
        return invokeId;
    }

    public void setInvokeId(Long invokeId) {
        this.invokeId = invokeId;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getInvokeName() {
        return invokeName;
    }

    public void setInvokeName(String invokeName) {
        this.invokeName = invokeName;
    }

    public Object getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ParamsObject{" +
                "invokeId=" + invokeId +
                ", invokeName='" + invokeName + '\'' +
                ", errMsg='" + errMsg + '\'' +
                ", data=" + data +
                '}';
    }
}
