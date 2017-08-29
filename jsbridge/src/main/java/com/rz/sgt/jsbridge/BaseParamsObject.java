package com.rz.sgt.jsbridge;

/**
 * jsbridge数据格式
 */

public class BaseParamsObject {

    public Long invokeId;
    public String invokeName;
    public int resultCode;
    public Object data;

    public static final int RESULT_CODE_SUCRESS = 1001;

    public static final int RESULT_CODE_FAILED = 1002;

    public static final int RESULT_CODE_CANCEL = 1003;

    @Override
    public String toString() {
        return "BaseParamsObject{" +
                "invokeId=" + invokeId +
                ", invokeName='" + invokeName + '\'' +
                ", resultCode=" + resultCode +
                ", data=" + data +
                '}';
    }
}
