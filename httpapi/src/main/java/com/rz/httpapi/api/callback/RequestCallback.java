package com.rz.httpapi.api.callback;

/**
 * Created by JS01 on 2016/6/15.
 * 使用Http.get() 和Http.post()时的请求回调接口
 */
public abstract class RequestCallback<T> {

    public static final int ERROR_THROWABLE = -1001;

    private static final String TAG = "RequestCallback";

    public abstract void onSuccess(T t);

    public abstract void onFailure(int code, String errorInfo);
}
