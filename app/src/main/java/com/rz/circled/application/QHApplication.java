package com.rz.circled.application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
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
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.NimStrings;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.model.AVChatAttachment;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.rts.RTSManager;
import com.netease.nimlib.sdk.rts.model.RTSData;
import com.netease.nimlib.sdk.team.constant.TeamFieldEnum;
import com.netease.nimlib.sdk.team.model.IMMessageFilter;
import com.netease.nimlib.sdk.team.model.UpdateTeamAttachment;
import com.rz.circled.BuildConfig;
import com.rz.circled.R;
import com.rz.circled.helper.YunXinHelper;
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
import com.rz.circled.js.PayOrderHandler;
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
import com.rz.circled.pay.DesUtils;
import com.rz.circled.ui.activity.MainActivity;
import com.rz.common.application.BaseApplication;
import com.rz.common.cache.preference.Session;
import com.rz.common.constant.CommonCode;
import com.rz.common.constant.Constants;
import com.rz.common.utils.IntentUtil;
import com.rz.common.utils.SystemUtils;
import com.rz.httpapi.api.Http;
import com.rz.sgt.jsbridge.RegisterList;
import com.tencent.bugly.Bugly;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.yryz.yunxinim.DemoCache;
import com.yryz.yunxinim.avchat.AVChatProfile;
import com.yryz.yunxinim.avchat.activity.AVChatActivity;
import com.yryz.yunxinim.common.util.crash.AppCrashHandler;
import com.yryz.yunxinim.config.ExtraOptions;
import com.yryz.yunxinim.config.preference.Preferences;
import com.yryz.yunxinim.config.preference.UserPreferences;
import com.yryz.yunxinim.contact.ContactHelper;
import com.yryz.yunxinim.rts.activity.RTSActivity;
import com.yryz.yunxinim.session.NimDemoLocationProvider;
import com.yryz.yunxinim.session.SessionHelper;
import com.yryz.yunxinim.session.extension.LuckyTipAttachment;
import com.yryz.yunxinim.uikit.MessageFilterListener;
import com.yryz.yunxinim.uikit.NimUIKit;
import com.yryz.yunxinim.uikit.contact.core.query.PinYin;
import com.yryz.yunxinim.uikit.session.viewholder.MsgViewHolderThumbBase;
import com.zhuge.analysis.stat.ZhugeSDK;

import java.io.File;
import java.io.IOException;
import java.util.Map;
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
        //初始化诸葛io
        ZhugeSDK.getInstance().openLog();
        ZhugeSDK.getInstance().init(this);
        configOkHttp();
        configExo();
        configBugly();
//        configRecord();
        registerJsServerInterface();
        configFresco();
        configJpush();
        configYunXin();
        configUmeng();
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
        //支付
        RegisterList.registerServerHandlerClass(PayOrderHandler.class);

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
        Bugly.init(this, Constants.Bugly.APP_ID, BuildConfig.DEBUG);
    }


    /**
     * 友盟配置
     */
    public void configUmeng() {
        UMShareConfig config = new UMShareConfig();
        config.isOpenShareEditActivity(false);
        UMShareAPI.get(this).setShareConfig(config);

        UMShareAPI.get(this);

        Config.DEBUG = false;
        com.umeng.socialize.utils.Log.LOG = false;

        //微信 appid appsecret
        PlatformConfig.setWeixin(Constants.WeiXin.APP_ID, Constants.WeiXin.APP_SECRET);

        //新浪微博 appkey appsecret
        PlatformConfig.setSinaWeibo(Constants.Sina.APP_KEY, Constants.Sina.APP_SECRET, Constants.Sina.REDIRECT_URL);

        // QQ和Qzone appid appkey
        PlatformConfig.setQQZone(Constants.QQ.APP_ID, Constants.QQ.APP_KEY);
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
        setHeaderInterceptor(this, builder, DesUtils.encrypt(Session.getNowAct() + "." + Session.getUserId() + "." + Session.getSessionKey()).replace("\\s", "").replace("\n", ""));
        setCacheDirectory(builder);
        setCacheInterceptor(builder);
        setTimeout(builder);
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
    private static void setHeaderInterceptor(final Context mContent, OkHttpClient.Builder builder, final String sign) {
        Log.d("token", "setHeaderInterceptor");
        if (builder != null) {
            Interceptor headerInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request originalRequest = chain.request();
                    Request.Builder requestBuilder = originalRequest.newBuilder()
                            .header("devType", "2")
                            .header("sign", sign);
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

    private void configYunXin() {
        DemoCache.setContext(this);
        com.yryz.yunxinim.uikit.Constants.topBarHeight = getResources().getDimension(R.dimen.px146);
        com.yryz.yunxinim.uikit.Constants.topBarTitleSize = getResources().getDimension(R.dimen.px55);
        // SDK初始化（启动后台服务，若已经存在用户登录信息， SDK 将完成自动登录）
        NIMClient.init(this, loginInfo(), options());
        ExtraOptions.provide();
        // crash handler
        AppCrashHandler.getInstance(this);
        if (SystemUtils.inMainProcess(instance)) {
            // 注意：以下操作必须在主进程中进行
            // 1、UI相关初始化操作
            // 2、相关Service调用

            // init pinyin
            PinYin.init(this);
            PinYin.validate();

            // 初始化UIKit模块
            initUIKit();

            // 注册通知消息过滤器
            registerIMMessageFilter();

            // 初始化消息提醒
            NIMClient.toggleNotification(UserPreferences.getNotificationToggle());

            // 注册网络通话来电
            enableAVChat();

            // 注册白板会话
            enableRTS();

            // 注册语言变化监听
            registerLocaleReceiver(true);
        }
    }

    // 如果返回值为 null，则全部使用默认参数。
    private SDKOptions options() {
        SDKOptions options = new SDKOptions();

        // 如果将新消息通知提醒托管给SDK完成，需要添加以下配置。
        StatusBarNotificationConfig config = UserPreferences.getStatusConfig();
        if (config == null) {
            config = new StatusBarNotificationConfig();
        }
        // 点击通知需要跳转到的界面
        config.notificationEntrance = MainActivity.class;
        config.notificationSmallIconId = R.mipmap.icon_logo;

        // 通知铃声的uri字符串
        config.notificationSound = "android.resource://com.netease.nim.demo/raw/msg";

        // 呼吸灯配置
        config.ledARGB = Color.GREEN;
        config.ledOnMs = 1000;
        config.ledOffMs = 1500;

        options.statusBarNotificationConfig = config;
        DemoCache.setNotificationConfig(config);
        UserPreferences.setStatusConfig(config);

        // 配置保存图片，文件，log等数据的目录
        String sdkPath = Environment.getExternalStorageDirectory() + "/" + getPackageName() + "/nim";
        options.sdkStorageRootPath = sdkPath;

        // 配置数据库加密秘钥
        options.databaseEncryptKey = "NETEASE";

        // 配置是否需要预下载附件缩略图
        options.preloadAttach = true;

        // 配置附件缩略图的尺寸大小，
        options.thumbnailSize = MsgViewHolderThumbBase.getImageMaxEdge();

        // 用户信息提供者
        options.userInfoProvider = YunXinHelper.getInstance(this).getInfoProvider();

        // 定制通知栏提醒文案（可选，如果不定制将采用SDK默认文案）
        options.messageNotifierCustomization = YunXinHelper.getInstance(this).getMessageNotifierCustomization();

        // 在线多端同步未读数
        options.sessionReadAck = true;

        return options;
    }

    // 如果已经存在用户登录信息，返回LoginInfo，否则返回null即可
    private LoginInfo loginInfo() {
        // 从本地读取上次登录成功时保存的用户登录信息
        String account = Preferences.getUserAccount();
        String token = Preferences.getUserToken();

        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            DemoCache.setAccount(account.toLowerCase());
            return new LoginInfo(account, token);
        } else {
            return null;
        }
    }

    /**
     * 通知消息过滤器（如果过滤则该消息不存储不上报）
     */
    private void registerIMMessageFilter() {
        NIMClient.getService(MsgService.class).registerIMMessageFilter(new IMMessageFilter() {
            @Override
            public boolean shouldIgnore(IMMessage message) {
                if (UserPreferences.getMsgIgnore() && message.getAttachment() != null) {
                    if (message.getAttachment() instanceof UpdateTeamAttachment) {
                        UpdateTeamAttachment attachment = (UpdateTeamAttachment) message.getAttachment();
                        for (Map.Entry<TeamFieldEnum, Object> field : attachment.getUpdatedFields().entrySet()) {
                            if (field.getKey() == TeamFieldEnum.ICON) {
                                return true;
                            }
                        }
                    } else if (message.getAttachment() instanceof AVChatAttachment) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    /**
     * 音视频通话配置与监听
     */
    private void enableAVChat() {
        registerAVChatIncomingCallObserver(true);
    }

    private void registerAVChatIncomingCallObserver(boolean register) {
        AVChatManager.getInstance().observeIncomingCall(new Observer<AVChatData>() {
            @Override
            public void onEvent(AVChatData data) {
                String extra = data.getExtra();
                Log.e("Extra", "Extra Message->" + extra);
                // 有网络来电打开AVChatActivity
                AVChatProfile.getInstance().setAVChatting(true);
                AVChatActivity.launch(DemoCache.getContext(), data, AVChatActivity.FROM_BROADCASTRECEIVER);
            }
        }, register);
    }

    /**
     * 白板实时时会话配置与监听
     */
    private void enableRTS() {
        registerRTSIncomingObserver(true);
    }

    private void registerRTSIncomingObserver(boolean register) {
        RTSManager.getInstance().observeIncomingSession(new Observer<RTSData>() {
            @Override
            public void onEvent(RTSData rtsData) {
                RTSActivity.incomingSession(DemoCache.getContext(), rtsData, RTSActivity.FROM_BROADCAST_RECEIVER);
            }
        }, register);
    }

    private void registerLocaleReceiver(boolean register) {
        if (register) {
            updateLocale();
            IntentFilter filter = new IntentFilter(Intent.ACTION_LOCALE_CHANGED);
            registerReceiver(localeReceiver, filter);
        } else {
            unregisterReceiver(localeReceiver);
        }
    }

    private BroadcastReceiver localeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_LOCALE_CHANGED)) {
                updateLocale();
            }
        }
    };

    private void updateLocale() {
        NimStrings strings = new NimStrings();
        strings.status_bar_multi_messages_incoming = getString(R.string.nim_status_bar_multi_messages_incoming);
        strings.status_bar_image_message = getString(R.string.nim_status_bar_image_message);
        strings.status_bar_audio_message = getString(R.string.nim_status_bar_audio_message);
        strings.status_bar_custom_message = getString(R.string.nim_status_bar_custom_message);
        strings.status_bar_file_message = getString(R.string.nim_status_bar_file_message);
        strings.status_bar_location_message = getString(R.string.nim_status_bar_location_message);
        strings.status_bar_notification_message = getString(R.string.nim_status_bar_notification_message);
        strings.status_bar_ticker_text = getString(R.string.nim_status_bar_ticker_text);
        strings.status_bar_unsupported_message = getString(R.string.nim_status_bar_unsupported_message);
        strings.status_bar_video_message = getString(R.string.nim_status_bar_video_message);
        strings.status_bar_hidden_message_content = getString(R.string.nim_status_bar_hidden_msg_content);
        NIMClient.updateStrings(strings);
    }

    private void initUIKit() {
        // 初始化，需要传入用户信息提供者
        NimUIKit.init(this, YunXinHelper.getInstance(this).getInfoProvider(), YunXinHelper.getInstance(this).getContactProvider());

        // 设置地理位置提供者。如果需要发送地理位置消息，该参数必须提供。如果不需要，可以忽略。
        NimUIKit.setLocationProvider(new NimDemoLocationProvider());

        NimUIKit.setMessageFilterListener(new MessageFilterListener() {
            @Override
            public boolean messageFilter(MsgAttachment attachment) {
                if (attachment instanceof LuckyTipAttachment) {
                    LuckyTipAttachment lucky = (LuckyTipAttachment) attachment;
                    if (!TextUtils.equals(lucky.getUserId(), NimUIKit.getAccount()) && !TextUtils.equals(lucky.getReceiveId(), NimUIKit.getAccount())) {
                        return true;
                    }
                }
                return false;
            }
        });

        // 会话窗口的定制初始化。
        SessionHelper.init();

        // 通讯录列表定制初始化
        ContactHelper.init();

        // 添加自定义推送文案以及选项，请开发者在各端（Android、IOS、PC、Web）消息发送时保持一致，以免出现通知不一致的情况
        NimUIKit.CustomPushContentProvider(null);
    }


}
