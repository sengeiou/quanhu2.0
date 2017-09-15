package com.rz.circled.application;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
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
import com.rz.circled.js.BackHandler;
import com.rz.circled.js.BackHomeHandler;
import com.rz.circled.js.CreateTeamHandler;
import com.rz.circled.js.EditorHandler;
import com.rz.circled.js.EditorTwoHandler;
import com.rz.circled.js.FinishLoadingHandler;
import com.rz.circled.js.FriendInfoHandler;
import com.rz.circled.js.HttpHeaderHandler;
import com.rz.circled.js.HttpRequestHandler;
import com.rz.circled.js.JoinTeamHandler;
import com.rz.circled.js.JumpUrlHandler;
import com.rz.circled.js.LocationHandler;
import com.rz.circled.js.LoginHandler;
import com.rz.circled.js.NavigateHandler;
import com.rz.circled.js.NetStatusHandler;
import com.rz.circled.js.OpenAppHandler;
import com.rz.circled.js.OpenCircleListHandler;
import com.rz.circled.js.OpenUrlHandler;
import com.rz.circled.js.PasteBoardHandler;
import com.rz.circled.js.PlayVideoHandler;
import com.rz.circled.js.RechargeHandler;
import com.rz.circled.js.ReportHandler;
import com.rz.circled.js.SaveImageHandler;
import com.rz.circled.js.SendVerifyCodeHandler;
import com.rz.circled.js.ShortCutHandler;
import com.rz.circled.js.SocializationShareHandler;
import com.rz.circled.js.StartP2pMessageHandler;
import com.rz.circled.js.StartTeamMessageHandler;
import com.rz.circled.js.StatusBarHandler;
import com.rz.circled.js.TransferHandler;
import com.rz.circled.js.UploadAudioHandler;
import com.rz.circled.js.UploadPicHandler;
import com.rz.circled.js.UploadVideoHandler;
import com.rz.common.application.BaseApplication;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.utils.IntentUtil;
import com.rz.common.utils.SystemUtils;
import com.rz.httpapi.api.Http;
import com.rz.sgt.jsbridge.RegisterList;
import com.tencent.bugly.Bugly;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
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
        configBugly();
//        configRecord();
        registerJsServerInterface();
        configFresco();
        configJpush();
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void configFresco() {
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(instance)
                .setMaxCacheSize(2 * 1024 * 1024)//最大缓存
                .setBaseDirectoryName("imageCache")//子目录
                .setBaseDirectoryPathSupplier(new Supplier<File>() {
                    @Override
                    public File get() {
                        return instance.getCacheDir();//还是推荐缓存到应用本身的缓存文件夹,这样卸载时能自动清除,其他清理软件也能扫描出来
                    }
                })
                .build();
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(instance)
                .setMainDiskCacheConfig(diskCacheConfig)
                .setDownsampleEnabled(true)
                //Downsampling，要不要向下采样,它处理图片的速度比常规的裁剪scaling更快，
                // 并且同时支持PNG，JPG以及WEP格式的图片，非常强大,与ResizeOptions配合使用
                .setBitmapsConfig(Bitmap.Config.RGB_565)
                //如果不是重量级图片应用,就用这个省点内存吧.默认是RGB_888
                .build();
        Fresco.initialize(instance, config);
    }

    private void registerJsServerInterface() {
        //RegisterList.registerServerHandlerClass(TestHandler.class);
        RegisterList.registerServerHandlerClass(UploadPicHandler.class);
        RegisterList.registerServerHandlerClass(UploadVideoHandler.class);
        RegisterList.registerServerHandlerClass(UploadAudioHandler.class);
        //充值
        RegisterList.registerServerHandlerClass(RechargeHandler.class);
        //网络状态
        RegisterList.registerServerHandlerClass(NetStatusHandler.class);
        RegisterList.registerServerHandlerClass(SocializationShareHandler.class);
        RegisterList.registerServerHandlerClass(BackHomeHandler.class);
        RegisterList.registerServerHandlerClass(BackHandler.class);
        RegisterList.registerServerHandlerClass(FriendInfoHandler.class);
        RegisterList.registerServerHandlerClass(LocationHandler.class);
        //生成快捷方式
        RegisterList.registerServerHandlerClass(ShortCutHandler.class);
        RegisterList.registerServerHandlerClass(EditorHandler.class);
        //网络访问
        RegisterList.registerServerHandlerClass(HttpRequestHandler.class);
        //获取公共头信息
        RegisterList.registerServerHandlerClass(HttpHeaderHandler.class);
        //发送短信验证码
        RegisterList.registerServerHandlerClass(SendVerifyCodeHandler.class);
        //修改状态栏颜色
        RegisterList.registerServerHandlerClass(StatusBarHandler.class);
        //创建圈子
        RegisterList.registerServerHandlerClass(CreateTeamHandler.class);
        //加入圈子
        RegisterList.registerServerHandlerClass(JoinTeamHandler.class);
        //单聊会话
        RegisterList.registerServerHandlerClass(StartP2pMessageHandler.class);
        //群聊会话
        RegisterList.registerServerHandlerClass(StartTeamMessageHandler.class);
        //剪切板
        RegisterList.registerServerHandlerClass(PasteBoardHandler.class);
        //举报
        RegisterList.registerServerHandlerClass(ReportHandler.class);
        //转发
        RegisterList.registerServerHandlerClass(TransferHandler.class);
        //打开url
        RegisterList.registerServerHandlerClass(OpenUrlHandler.class);
        //本地播放视频
        RegisterList.registerServerHandlerClass(PlayVideoHandler.class);
        //发布
        RegisterList.registerServerHandlerClass(EditorTwoHandler.class);
        //悠然广场
        RegisterList.registerServerHandlerClass(OpenCircleListHandler.class);
        //登录
        RegisterList.registerServerHandlerClass(LoginHandler.class);
        //跳转融众财富
        RegisterList.registerServerHandlerClass(OpenAppHandler.class);
        //导航
        RegisterList.registerServerHandlerClass(NavigateHandler.class);
        //关闭loading页面
        RegisterList.registerServerHandlerClass(FinishLoadingHandler.class);
        //保存图片
        RegisterList.registerServerHandlerClass(SaveImageHandler.class);
        //跳转
        RegisterList.registerServerHandlerClass(JumpUrlHandler.class);
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

    private void configBugly() {
//        Constants.Bugly.init(this, Constants.Bugly.APP_ID, BuildConfig.DEBUG);
        Bugly.init(this, Constants.Bugly.APP_ID, BuildConfig.DEBUG);
    }

    /**
     * 极光推送初始化
     */
    private void configJpush() {
        JPushInterface.setDebugMode(BuildConfig.DEBUG);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
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
