package com.rz.sgt.jsbridge.core;

import com.rz.sgt.jsbridge.RequestParamsObject;

/**
 * Created by KF on 2017/5/22.
 */
public interface RequestJsBridgeCallBack {
    /**
     * 返回方法名
     *
     * @return
     */
    String getNativeEvent();

    /**
     * 调用方法完毕后的回调
     *
     * @param params       原始参数
     * @param paramsObject 解析参数
     */
    void onCallBack(String params, RequestParamsObject paramsObject);

    /**
     * 是否需要ui界面
     *
     * @return
     */
    boolean isUi();

}
