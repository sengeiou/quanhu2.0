package com.rz.sgt.jsbridge.core;


import com.rz.sgt.jsbridge.BaseParamsObject;

/**
 * 当服务完成后发送完成信息，该回调会由invokeId找到并触发。
 */

public abstract class Callback {

    public long invokeId;
    public String invokeName;

    public WebViewProxy webViewProxy;

    public BaseParamsObject initBaseParamsObject() {
        BaseParamsObject baseParamsObject = new BaseParamsObject();
        baseParamsObject.invokeId = invokeId;
        baseParamsObject.invokeName = invokeName;
        return baseParamsObject;
    }

    public Callback(WebViewProxy webViewProxy, long invokeId, String invokeName) {
        this.webViewProxy = webViewProxy;
        this.invokeId = invokeId;
        this.invokeName = invokeName;
    }

//    public void invoke(String data) {
//        ParamsObject paramsObject = new ParamsObject(invokeName, invokeId);
//        paramsObject.data = data;
//        webViewProxy.callbackInvoke(paramsObject.toJson());
//    }

    /**
     * 该方法实现主要完成业务数据封装成js需要的数据格式（参考jssdk接口文档）
     *
     * @param businessParams   服务组件的业务数据
     * @param baseParamsObject js回调需要的数据格式
     */
    public abstract void invoke(Object businessParams, BaseParamsObject baseParamsObject);

//    public boolean autoCancel() {
//        return true;
//    }

    public long getInvokeId() {
        return invokeId;
    }

    public void setInvokeId(long invokeId) {
        this.invokeId = invokeId;
    }

    public String getInvokeName() {
        return invokeName;
    }

    public void setInvokeName(String invokeName) {
        this.invokeName = invokeName;
    }
}
