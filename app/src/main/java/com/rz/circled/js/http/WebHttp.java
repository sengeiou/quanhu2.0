package com.rz.circled.js.http;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.litesuits.common.assist.Network;
import com.rz.circled.BuildConfig;
import com.rz.circled.application.QHApplication;
import com.rz.common.cache.preference.Session;
import com.rz.common.utils.IntentUtil;
import com.rz.common.utils.SystemUtils;
import com.rz.httpapi.api.API;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/3/24/024.
 */

public class WebHttp {
    private static OkHttpClient.Builder builder;
    private static OkHttpClient client;
    private static APIWebService mWebService;

    public static APIWebService getWebService(Context context, Map map) {
        if (client == null || builder == null) {
            builder = initBuilder(context);
            client = builder.build();
        }
//        if (null != map && map.size() > 0) {
//            setWebHeaderInterceptor(context, builder, map);
//            client = builder.build();
//        }
        if (mWebService == null) {
            String base_url = "";
            if (QHApplication.isFlag == 1) {
                base_url = API.URL_APP_USER_RELEASE + (BuildConfig.addServerPrefix ? "app-user/" : "");
            } else if (QHApplication.isFlag == 2) {
                base_url = API.URL_APP_USER_MO + (BuildConfig.addServerPrefix ? "app-user/" : "");
//                base_url = API.URL_APP_USER;
            } else if (QHApplication.isFlag == 3) {
                base_url = API.URL_APP_USER_TEST + (BuildConfig.addServerPrefix ? "app-user/" : "");
            } else if (QHApplication.isFlag == 4) {
                base_url = API.URL_DEBUG + (BuildConfig.addServerPrefix ? "app-user/" : "");
            } else if (QHApplication.isFlag == 5) {
                base_url = API.URL_APP_USER_17 + (BuildConfig.addServerPrefix ? "app-user/" : "");
            }
            Retrofit retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(base_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            mWebService = retrofit.create(APIWebService.class);
        }
        return mWebService;
    }


    private static OkHttpClient.Builder initBuilder(Context context) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        if (!App.isDebug) {
        //开启日志拦截器
        setLoggingInterceptor(builder);
//        }
        setCookieJar(builder);
        setHeaderInterceptor(context, builder);
        //setParamsInterceptor(builder);
        setCacheDirectory(builder);
        setCacheInterceptor(builder);
        setTimeout(builder);
        //setCertificates(builder,context);
        return builder;
    }


    /**
     * 初始化OKHttpClient
     */
    private static void initClient(Context context) {
        client = initBuilder(context).build();
    }

    /**
     * 日志拦截器
     *
     * @param builder
     */
    private static void setLoggingInterceptor(OkHttpClient.Builder builder) {
        if (builder != null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }
    }

    /**
     * 设置CookieJar
     *
     * @param builder
     */
    private static void setCookieJar(OkHttpClient.Builder builder) {
        Context context = QHApplication.getContext();
        if (context != null) {
            ClearableCookieJar cookieJar = new PersistentCookieJar(
                    new SetCookieCache(),
                    new SharedPrefsCookiePersistor(context));
            builder.cookieJar(cookieJar);
        }
    }


    /**
     * 头部拦截器
     *
     * @param builder
     */
    private static void setHeaderInterceptor(final Context mContent, OkHttpClient.Builder builder) {
        Log.d("token", "setHeaderInterceptor");
        if (builder != null) {
            Interceptor headerInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    String headToken = Session.getSessionKey();
                    Log.d("token", "setHeaderInterceptor Session.getSessionKey()  " + headToken + "-------");
                    Request originalRequest = chain.request();
                    Request.Builder requestBuilder = originalRequest.newBuilder()
                            .header("devType", "2")
//                            .header("Content-Type", "application/json")
//                            .header("Accept", "application/json")
//                            .header("token", "1")
                            .header("sign", "1");
                    Log.d("token", "setHeaderInterceptor headToken is " + headToken);
//                    if (!TextUtils.isEmpty(headToken)) {
                    requestBuilder.header("token", headToken);
//                    }
                    requestBuilder.header("appVersion", BuildConfig.VERSION_NAME);
                    requestBuilder.header("apiVersion", "2.0.0");
                    requestBuilder.header("devName", Build.MODEL);
                    requestBuilder.header("devId", SystemUtils.getIMEI(mContent));
                    requestBuilder.header("ip", SystemUtils.getIp(mContent));
                    requestBuilder.header("net", IntentUtil.getNetType(mContent));
                    requestBuilder.method(originalRequest.method(), originalRequest.body());
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            };
            builder.addInterceptor(headerInterceptor);
        }
    }

    /**
     * 设置缓存路径
     *
     * @param builder
     */
    public static void setCacheDirectory(OkHttpClient.Builder builder) {
        //设置缓存路径
        Context context = QHApplication.getContext();
        if (context != null) {
            String path = context.getCacheDir().getPath();
            File httpCacheDirectory = new File(path, "responses");
            Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
            builder.cache(cache);
        }
    }

    /**
     * 缓存拦截器
     *
     * @param builder
     */
    private static void setCacheInterceptor(OkHttpClient.Builder builder) {
        if (builder != null) {
            Interceptor cacheInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Context context = QHApplication.getContext();
                    if (context != null) {
                        if (!Network.isAvailable(context)) {
                            request = request.newBuilder()
                                    .cacheControl(CacheControl.FORCE_CACHE)
                                    .build();
                        }

                        Response response = chain.proceed(request);
                        if (Network.isAvailable(context)) {
                            int maxAge = 0;
                            // 有网络时,设置缓存超时时间0个小时
                            response.newBuilder()
                                    .header("Cache-Control", "public, max-age=" + maxAge)
                                    .removeHeader("Pragma")
                                    .build();
                        } else {
                            // 无网络时,设置超时为4周
                            int maxStale = 60 * 60 * 24 * 3;
                            response.newBuilder()
                                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                                    .removeHeader("Pragma")
                                    .build();
                        }
                        return response;
                    } else {
                        //默认当作有网络处理
                        int maxAge = 0;
                        Response response = chain.proceed(request);
                        response.newBuilder()
                                .header("Cache-Control", "public, max-age=" + maxAge)
                                .removeHeader("Pragma")
                                .build();
                        return response;
                    }
                }
            };
            builder.addInterceptor(cacheInterceptor);
        }
    }

    /**
     * 设置超时和重试
     *
     * @param builder
     */
    private static void setTimeout(OkHttpClient.Builder builder) {
        //设置超时
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(15, TimeUnit.SECONDS);
        builder.writeTimeout(15, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
    }


    /**
     * web自定义头部拦截器
     *
     * @param builder
     */
    private static void setWebHeaderInterceptor(final Context mContent, OkHttpClient.Builder builder, final Map map) {
        if (builder != null) {
            Interceptor headerInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request originalRequest = chain.request();
                    Request.Builder requestBuilder = originalRequest.newBuilder();
                    Iterator entries = map.entrySet().iterator();
                    while (entries.hasNext()) {
                        Map.Entry entry = (Map.Entry) entries.next();
                        String key = (String) entry.getKey();
                        String value = (String) entry.getValue();
                        Log.e("fengan", "intercept: 头信息key==" + key);
                        Log.e("fengan", "intercept: 头信息value==" + value);
                        requestBuilder.header(key, value);
                    }
                    requestBuilder.method(originalRequest.method(), originalRequest.body());
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            };
            builder.addInterceptor(headerInterceptor);
        }
    }


}
