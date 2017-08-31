package com.rz.httpapi.api;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by JS01 on 2016/6/14.
 * 使用Retrofit2 Call异步请求时的回调
 */
public abstract class BaseCallback<T> implements Callback<T> {

    private static final String TAG = "BaseCallback";

    public static Class<? extends interceptorResponse> cls;

    private interceptorResponse responseBase;

    public BaseCallback() {
        if (cls != null) {
            try {
                this.responseBase = cls.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public interface interceptorResponse {
        void handleReson(Response response);
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        Log.d(TAG, "code:" + response.code());
        if (null != responseBase) {
            responseBase.handleReson(response);
        }
        CallManager.remove(call);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.d(TAG, "error:" + t.getLocalizedMessage());
        CallManager.remove(call);
    }
}
