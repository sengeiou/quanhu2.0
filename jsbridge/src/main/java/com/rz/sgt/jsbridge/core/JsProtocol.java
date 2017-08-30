package com.rz.sgt.jsbridge.core;

/**
 * jsbridge交互协议
 */

public interface JsProtocol {

    /**
     * @return 返回方法名（与jssdj接口文档方法名对应）
     */
    String getInvokeName();

    /**
     * 由js调用后的处理
     *
     * @param params   jssdk发出的原始参数数据
     * @param paramObj 解析数据格式后生成的数据
     * @param callback 生成的回调
     */
    void handle(String params, ParamsObject paramObj, Callback callback);

    /**
     * 生成一个callback用于js调用的处理结束后回调js
     *
     * @param webViewProxy webContainer
     * @param paramsObject jssdk发出的原始封装数据
     * @return
     */
    Callback generateCallBack(WebViewProxy webViewProxy, ParamsObject paramsObject);

    /**
     * 该界面是否提供Ui服务，jsBridge会根据是否为UI服务来屏蔽jssdk发送的多次ui服务调用
     * 只有一个UI服务结束后才能发起另一个UI服务
     *
     * @return
     */
    boolean isUi();
}
