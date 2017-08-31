package com.rz.circled.recorder;


public class RecorderContant {

    /**
     * 默认时长
     */
    public static float DEFAULT_DURATION_LIMIT = 60;

    /**
     * 晒一晒默认时长
     */
    public static float DEFAULT_DURATION_SHOW_LIMIT = 15;
    /**
     * 默认最小时长
     */
    public static float DEFAULT_MIN_DURATION_LIMIT = 5;
    /**
     * 默认码率
     */
    public static int DEFAULT_BITRATE = 2000 * 1000;

    public static final boolean hasMoreMusic = false;

    public static final int VIDEO_SIZE = 480;
    /**
     * 默认Video保存路径，请开发者自行指定
     */
    public static String VIDEOPATH = RecorderFileUtils.newOutgoingFilePath();
    /**
     * 默认缩略图保存路径，请开发者自行指定
     */
    public static String THUMBPATH = VIDEOPATH + ".jpg";
    /**
     * 水印本地路径，文件必须为rgba格式的PNG图片
     */
    public static String WATER_MARK_PATH = "assets://Qupai/watermark/qupai-logo.png";


//    public static final String APP_KEY = "20713707870378e";
//    public static final String APP_SECRET = "f363e3832846473baf6feebb27eb7402";

    public static final String APP_KEY = "20afda3fcb1d434";
    public static final String APP_SECRET = "a4469fcb358c41e5a671006cf2c6215f";

    public static String tags = "tags";
    public static String description = "description";
    public static int shareType = 0; //是否公开 0公开分享 1私有(default) 公开类视频不需要AccessToken授权

    public static String accessToken;//accessToken 通过调用授权接口得到

    public static final String space = "quanhu"; //存储目录 建议使用uid cid之类的信息,不要写死

    public static final String domain = "http://qupai-live.s.qupai.me";//当前TEST应用的域名。该地址每个应用都不同

    public final static String PREF_VIDEO_EXIST_USER = "Qupai_has_video_exist_in_user_list_pref";

    public static int RECORDE_SHOW = 10001;

    public static int RECORDE_OPUS_VIDEO = 10002;
}
