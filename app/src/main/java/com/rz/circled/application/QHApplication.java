package com.rz.circled.application;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;
import com.rz.circled.BuildConfig;
import com.rz.common.application.BaseApplication;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.utils.IntentUtil;
import com.rz.common.utils.SystemUtils;
import com.rz.httpapi.api.Http;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Gsm on 2017/8/24.
 */
public class QHApplication extends BaseApplication {

    private static QHApplication instance;
    public static String userAgent;

    public static int isFlag = BuildConfig.isFlag;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        init();
    }

    public static QHApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        if (instance != null) {
            return instance.getApplicationContext();
        }
        return null;
    }

    private void init() {
        configOkHttp();
        configExo();
    }

    public void configExo() {
        userAgent = Util.getUserAgent(this, "rz.sgt");
    }

    public DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory(this, bandwidthMeter,
                buildHttpDataSourceFactory(bandwidthMeter));
    }

    public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter);
    }

    private void configOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        setLoggingInterceptor(builder);
        setCookieJar(builder);
        setHeaderInterceptor(this, builder);
        //setParamsInterceptor(builder);
        setCacheDirectory(builder);
        setCacheInterceptor(builder);
        setTimeout(builder);
        //setCertificates(builder,context);
        Http.initClient(builder.build(), BuildConfig.BaseUrl);
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
                    Request originalRequest = chain.request();
                    Request.Builder requestBuilder = originalRequest.newBuilder()
                            .header("devType", "2")
                            .header("sign", "1");
                    Log.d("token", "setHeaderInterceptor headToken is " + Session.getSessionKey());
                    if (!TextUtils.isEmpty(Session.getSessionKey())) {
                        requestBuilder.header("token", Session.getSessionKey());
                    }
                    requestBuilder.header("appVersion", BuildConfig.VERSION_NAME);
                    requestBuilder.header("apiVersion", "V1.0.0");
                    requestBuilder.header("appId", CommonCode.App.APP_ID);
                    requestBuilder.header("appSecret", CommonCode.App.APP_SECRET);
//                    requestBuilder.header("devId", SystemUtils.getIMEI(mContent));
                    requestBuilder.header("devId", Session.getJpushId());
                    Log.d("token", "setHeaderInterceptor " + " /  appId = " + CommonCode.App.APP_ID + " / appSecret = " + CommonCode.App.APP_SECRET
                            + " / deviceId = " + Session.getJpushId());
                    requestBuilder.header("ip", SystemUtils.getIp(mContent));
                    requestBuilder.header("net", IntentUtil.getNetType(mContent));
                    try {
                        requestBuilder.header("devName", Build.MODEL);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    requestBuilder.method(originalRequest.method(), originalRequest.body());
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            };
            builder.addInterceptor(headerInterceptor);
        }
    }

    /**
     * 设置CookieJar
     *
     * @param builder
     */
    private static void setCookieJar(OkHttpClient.Builder builder) {
        Context context = getContext();
        if (context != null) {
            ClearableCookieJar cookieJar = new PersistentCookieJar(
                    new SetCookieCache(),
                    new SharedPrefsCookiePersistor(context));
            builder.cookieJar(cookieJar);
        }
    }

    /**
     * 设置缓存路径
     *
     * @param builder
     */
    public static void setCacheDirectory(OkHttpClient.Builder builder) {
        //设置缓存路径
        Context context = getContext();
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
//        if (builder != null) {
//            Interceptor cacheInterceptor = new Interceptor() {
//                @Override
//                public Response intercept(Chain chain) throws IOException {
//                    Request request = chain.request();
//                    Context context = getContext();
//                    if (context != null) {
//                        if (!Network.isAvailable(context)) {
//                            request = request.newBuilder()
//                                    .cacheControl(CacheControl.FORCE_CACHE)
//                                    .build();
//                        }
//
//                        Response response = chain.proceed(request);
//                        if (Network.isAvailable(context)) {
//                            int maxAge = 0;
//                            // 有网络时,设置缓存超时时间0个小时
//                            response.newBuilder()
//                                    .header("Cache-Control", "public, max-age=" + maxAge)
//                                    .removeHeader("Pragma")
//                                    .build();
//                        } else {
//                            // 无网络时,设置超时为4周
//                            int maxStale = 60 * 60 * 24 * 3;
//                            response.newBuilder()
//                                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
//                                    .removeHeader("Pragma")
//                                    .build();
//                        }
//                        return response;
//                    } else {
//                        //默认当作有网络处理
//                        int maxAge = 0;
//                        Response response = chain.proceed(request);
//                        response.newBuilder()
//                                .header("Cache-Control", "public, max-age=" + maxAge)
//                                .removeHeader("Pragma")
//                                .build();
//                        return response;
//                    }
//                }
//            };
//            builder.addInterceptor(cacheInterceptor);
//        }
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

}
