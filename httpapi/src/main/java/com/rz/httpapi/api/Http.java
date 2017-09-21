package com.rz.httpapi.api;

import android.content.Context;

import com.rz.httpapi.api.callback.RequestCallback;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by JS01 on 2016/6/7.
 */
public class Http {

    private static OkHttpClient client;
    private static Map<String, Object> serviceMap = new HashMap<>();
    private static String BaseUrl;

    /**
     * APIService的对象，访问接口
     */
    public static <T> T getApiService(Class<T> service) {
        return getApiService(service, false);
    }

    public static <T> T getApiService(Class<T> service, boolean pay) {
        Object object = serviceMap.get(service.getCanonicalName());
        if (object == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(BaseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            object = retrofit.create(service);
            serviceMap.put(service.getCanonicalName(), object);
        } else {
            object = serviceMap.get(service.getCanonicalName());
        }
        return (T) object;
    }

    public static void initClient(OkHttpClient client1, String baseUrl) {
        client = client1;
        BaseUrl = baseUrl;
    }

    /**
     * GET请求
     *
     * @param url      请求链接
     * @param params   请求参数
     * @param callback 请求回调 为防止内存泄漏，callback用弱引用包装
     */
    public static <T> void get(String url, Map<String, String> params, final RequestCallback<T> callback) {
        final WeakReference<RequestCallback<T>> weakReference = new WeakReference<>(callback);
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                urlBuilder.addQueryParameter(key, value);
            }
        }
        HttpUrl httpUrl = urlBuilder.build();
        final Request request = new Request.Builder()
                .url(httpUrl)
                .cacheControl(CacheControl.FORCE_NETWORK)
                .get()
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                RequestCallback<T> callback1 = weakReference.get();
                if (callback1 != null) {
                    int code = -1;
                    String error = e.getLocalizedMessage();
                    callback1.onFailure(code, error);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                RequestCallback<T> callback1 = weakReference.get();
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    T t = (T) body.string();
                    if (callback1 != null) {
                        callback1.onSuccess(t);
                    }
                } else {
                    int code = response.code();
                    String error = response.message();
                    if (callback1 != null) {
                        callback1.onFailure(code, error);
                    }
                }
            }
        });
    }

    /**
     * POST请求
     *
     * @param url      请求链接
     * @param params   请求参数
     * @param callback 请求回调 为防止内存泄漏，callback用弱引用包装
     */
    public static <T> void post(String url, Map<String, String> params, final RequestCallback<T> callback) {
        final WeakReference<RequestCallback<T>> weakReference = new WeakReference<>(callback);
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                bodyBuilder.add(key, value);
            }
        }
        FormBody formBody = bodyBuilder.build();

        Request request = new Request.Builder()
                .cacheControl(CacheControl.FORCE_NETWORK)
                .url(url)
                .post(formBody)
                .build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                RequestCallback<T> callback1 = weakReference.get();
                if (callback1 != null) {
                    int code = -1;
                    String error = e.getLocalizedMessage();
                    callback1.onFailure(code, error);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                RequestCallback<T> callback1 = weakReference.get();
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    T t = (T) body.string();
                    if (callback1 != null) {
                        callback1.onSuccess(t);
                    }
                } else {
                    int code = response.code();
                    String error = response.message();
                    if (callback1 != null) {
                        callback1.onFailure(code, error);
                    }
                }
            }
        });
    }

    /**
     * POST请求
     *
     * @param url      请求链接
     * @param params   请求参数
     * @param callback 请求回调 为防止内存泄漏，callback用弱引用包装
     */
    public static <T> void post(String url, List<Map<String, String>> params, final RequestCallback<T> callback) {
        final WeakReference<RequestCallback<T>> weakReference = new WeakReference<>(callback);
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        for (Map<String, String> map : params) {
            if (map != null && !map.isEmpty()) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    bodyBuilder.add(key, value);
                }
            }
        }
        FormBody formBody = bodyBuilder.build();

        Request request = new Request.Builder()
                .cacheControl(CacheControl.FORCE_NETWORK)
                .url(url)
                .post(formBody)
                .build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                RequestCallback<T> callback1 = weakReference.get();
                if (callback1 != null) {
                    int code = -1;
                    String error = e.getLocalizedMessage();
                    callback1.onFailure(code, error);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                RequestCallback<T> callback1 = weakReference.get();
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    T t = (T) body.string();
                    if (callback1 != null) {
                        callback1.onSuccess(t);
                    }
                } else {
                    int code = response.code();
                    String error = response.message();
                    if (callback1 != null) {
                        callback1.onFailure(code, error);
                    }
                }
            }
        });
    }
}
