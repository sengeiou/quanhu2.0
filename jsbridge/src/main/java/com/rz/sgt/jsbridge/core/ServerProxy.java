package com.rz.sgt.jsbridge.core;

import android.util.Log;
import android.webkit.JavascriptInterface;

import com.google.gson.Gson;
import com.rz.sgt.jsbridge.RequestParamsObject;
import com.rz.sgt.jsbridge.ServerHandler;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * JS调用本地的注入对象。
 */

public class ServerProxy {

    public static final String JAVA_BRIDGE = "JavascriptBridge";
    public Map<Long, Callback> callbacks = new HashMap<>();
    private Map<String, ServerHandler> handlers = new HashMap<>();
    private WebViewProxy webViewProxy;
    public boolean currentUI = false;
    public long uiHandlingInvokeId;
    public long uiRequestInvokeId;
    private int blockTimes;


    private Map<Long, RequestJsServerHandler> requestHandlers = new HashMap<>();
    private boolean requestCurrentUi = false;


    public ServerProxy(WebViewProxy webViewProxy) {
        this.webViewProxy = webViewProxy;
    }

    /**
     * js端调用native统一方法入口
     */
    @JavascriptInterface
    public final void invoke(String params) {
        Log.d("JsBridge", "js invoke -- " + params);

        processRequestOrCallBack(params);

    }


    private void processRequestOrCallBack(String params) {
        try {
            org.json.JSONObject jsonObject = new org.json.JSONObject(params);
            if (jsonObject.has("nativeEvent")) {
                processRequest(params);
            } else if (jsonObject.has("invokeName")) {
                processCallBack(params);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void processRequest(String params) {
        RequestParamsObject paramsObject = RequestParamsObject.Parse(params);
        toRequestCallback(params, paramsObject);
    }

    private void processCallBack(String params) {
        ParamsObject paramsObject = ParamsObject.Parse(params);
        dispatchCallbackInvoke(params, paramsObject);
    }

    /**
     * 在webview加载结束后统一调用register注册支持的方法
     *
     * @param serverHandler
     */
    public void register(ServerHandler serverHandler) {
        handlers.put(serverHandler.getInvokeName(), serverHandler);
    }

    /**
     * 加载全部的原生注册对象
     *
     * @param handlers
     */
    public void registerAll(Map<String, ServerHandler> handlers) {
        if (handlers != null) {
            this.handlers = handlers;
        }

    }

    /**
     * js调用java根據js參數生成一個callback,屏蔽多次的UI组件服务请求。调用服务的启动方法
     */
    private void dispatchCallbackInvoke(String params, ParamsObject paramsObject) {
        ServerHandler serverHandler = handlers.get(paramsObject.getInvokeName());
        if (serverHandler != null) {
            Callback callback = serverHandler.generateCallBack(webViewProxy, paramsObject);
            if (callback != null) {
                callbacks.put(paramsObject.getInvokeId(), callback);
            }
            if (serverHandler.isUi()) {
                if (currentUI) {
                    blockTimes++;
                    //TODO 返回失败，当前界面已经有UI服务正在进行
                    if (blockTimes >= 2) {
                        blockTimes = 0;
                        currentUI = true;
                        uiHandlingInvokeId = paramsObject.invokeId;
                        webViewProxy.autoCancelUiHandler();
                    } else {
                        return;
                    }
                } else {
                    blockTimes = 0;
                    currentUI = true;
                    uiHandlingInvokeId = paramsObject.invokeId;
                }
            }

            serverHandler.handle(params, paramsObject, callback);
        } else {
            Log.w("dispatchDirectInvoke", "no ServerHandler:" + paramsObject.getInvokeName());
            webViewProxy.callbackInvoke(new Gson().toJson(paramsObject));
        }
    }

    public void destroy() {
        handlers.clear();
        callbacks.clear();
        currentUI = false;
        uiHandlingInvokeId = 0;

        requestHandlers.clear();
        requestCurrentUi = false;
        uiRequestInvokeId = 0;
    }

    public void reload() {
        callbacks.clear();
        currentUI = false;
        uiHandlingInvokeId = 0;

        requestHandlers.clear();
        requestCurrentUi = false;
        uiRequestInvokeId = 0;
    }

    public Callback removeCallback(long invokeId) {
        return callbacks.remove(invokeId);
    }

    public void removeRequestCallback(long invokeId) {
        requestHandlers.remove(invokeId);
    }

    public Callback getCallbackByInvokeId(long invokeId) {
        return callbacks.get(invokeId);
    }

    public void changeUIStatus(long invokeId) {
        if (invokeId != 0 && uiHandlingInvokeId == invokeId) {
            currentUI = false;
            uiHandlingInvokeId = 0;
        }
    }

    public void changeRequestUiStatus(long invokeId) {
        if (invokeId != 0 && uiRequestInvokeId == invokeId) {
            requestCurrentUi = false;
            uiRequestInvokeId = 0;
        }
    }

    public Callback getCurrentHandingUiHandlerCallback() {
        if (currentUI) {
            return getCallbackByInvokeId(uiHandlingInvokeId);
        } else {
            return null;
        }
    }

    public RequestJsServerHandler getCurrentHandingRequestUiHandler() {
        if (requestCurrentUi) {
            return requestHandlers.get(uiRequestInvokeId);
        } else {
            return null;
        }
    }

    /***
     * 返回是否添加成功
     *
     * @param jsServerHandler
     * @return
     */
    public boolean addRequestCallback(RequestJsServerHandler jsServerHandler) {
        if (jsServerHandler != null) {
            long invokeId = jsServerHandler.invokeId;
            if (jsServerHandler.isUi()) {//需要ui支持
                if (requestCurrentUi) {
                    //返回失败，当前界面已经有UI服务正在进行
                    requestCurrentUi = true;
                    RequestParamsObject cancel = getRequestResultData(jsServerHandler.getNativeEvent(), invokeId, "cancel");
                    jsServerHandler.onCallBack(cancel.toJson(), cancel);
                    return false;
                } else {
                    requestHandlers.put(invokeId, jsServerHandler);
                    requestCurrentUi = true;
                    uiRequestInvokeId = invokeId;
                    return true;
                }
            } else {
                requestHandlers.put(invokeId, jsServerHandler);
                return true;
            }
        }
        return false;
    }

    private void toRequestCallback(String params, RequestParamsObject paramsObject) {
        Long invokeId = paramsObject.invokeId;
        if (requestHandlers.containsKey(invokeId)) {
            RequestJsServerHandler requestJsServerHandler = requestHandlers.get(invokeId);
            requestJsServerHandler.onCallBack(params, paramsObject);
            changeRequestUiStatus(invokeId);
            requestHandlers.remove(invokeId);
        }
    }

    private RequestParamsObject getRequestResultData(String nativeEvent, long invokeId, String errorMsg) {
        return new RequestParamsObject(invokeId, nativeEvent, errorMsg, RequestParamsObject.RESULT_CODE_CANCEL);
    }

    public RequestJsServerHandler removeRequestHandler(long invokeId) {
        return requestHandlers.remove(invokeId);
    }
}
