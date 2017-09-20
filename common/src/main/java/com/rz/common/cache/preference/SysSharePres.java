package com.rz.common.cache.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.rz.common.application.BaseApplication;

import java.util.HashSet;
import java.util.Set;

/**
 * 数据保存
 */
public class SysSharePres {

    /**
     * sharedpreferences存储的名称
     */
    private static final String PRES_NAME = "system_share";

    private SharedPreferences mSharePres;

    /**
     * 用户是否是第一次安装APP true 是的 false不是
     */
    private static final String USER_ISFIRSTDOWNLOAD = "user_isfirstdownload";

    /**
     * 用户第一次安装APP 进入首页面时 操作提示 true是的 false不是
     */
    private static final String USER_FIRSTREMINDHOME = "user_firstremindhome";

    /**
     * 用户第一次安装APP 进入绑卡认证页面时 操作提示 true是的 false不是 默认为true
     */
    private static final String USER_FIRST_AUTH = "user_first_Auth";

    /**
     * 用户第一次安装APP 进入地图页面时 操作提示 true是的 false不是
     */
    private static final String USER_FIRSTINFODETAIL = "user_firstinfodetail";

    /**
     * 用户第一次安装APP 进入地图页面时 操作提示 true是的 false不是
     */
    private static final String USER_FIRSTMYACCOUNT = "user_firstrmyaccount";

    /**
     * 是否有更新，默认false
     */
    private static final String UPDATE_APP = "update_app";
    /**
     * 有更新，用户点击取消更新，默认false
     */
    private static final String UPDATE_CLICK_CANCEL = "update_click_cancel";

    /**
     * 用户是否登录 true 登录 false 未登录
     */
    private static final String USER_LOGIN = "user_login";

    /**
     * 用户取消登录提示，true取消 false未取消
     */
    private static final String CANCEL_REMIND_LOGIN = "cancel_remind_login";

    /**
     * 用户是否设置了密保问题
     */
    private static final String USER_SAFETYPROBLEM = "user_SafetyProblem";

    /**
     * 用户是否设置了支付密码
     */
    private static final String USER_SETPAYPW = "user_setpaypw";

    /**
     * SessionId 后面请求的时候回传
     */
    private static final String SESSIONKEY = "sessionKey";

    /**
     * 通讯录好友最后查询时间
     */
    private static final String CONTACTSDATE = "contactsdate";

    /**
     * 通讯录关注的人最后查询时间
     */
    private static final String CAREFRIENDSDATE = "carefriendsdate";

    /**
     * 是否打开手势密码
     */
    public static final String ISOPENGESTURE = "isOpenGesture";

    /**
     * 用户设置的手势密码
     */
    private static final String USER_GESTURE = "user_gesture";

    /**
     * 用户id
     */
    private static final String USER_ID = "user_id";

    /**
     * 上次登录的用户id
     */
    private static final String USER_BEFORE_ID = "user_before_id";

    /**
     * 用户真实姓名
     */
    private static final String USER_TRUENAME = "user_true_name";

    /**
     * 用户身份证号
     */
    private static final String USER_IDCARD = "user_idcard";

    /**
     * 用户昵称
     */
    private static final String USER_NAME = "user_name";

    /**
     * 用户登录账号
     */
    private static final String USER_ACCOUNT = "user_account";

    /**
     * 用户手机号
     */
    private static final String USER_PHONE = "user_phone";

    /**
     * 用户等级
     */
    private static final String USER_LEVEL = "user_level";

    /**
     * 用户认证状态
     */
    private static final String USER_IDENTIFIED = "user_identified";

    /**
     * 实名认证记录id
     */
    private static final String IDENTIFIED_ID = "identified_id";

    /**
     * 交易流水号
     */
    private static final String ORDERSERIALNO = "orderSerialNo";

    /**
     * 手势密码验证机会
     */
    private static final String USER_GESTURE_CHANCE = "user_gesture_chance";

    /**
     * 好友动态未读数量
     */
    private static final String FRI_TREAD_UNREAD = "fri_tread_unread";

    /**
     * 我的动态未读数量
     */
    private static final String MY_TREAD_UNREAD = "My_tread_unread";

    /**
     * 系统消息未读数量
     */
    private static final String SYSTEM_MESSAGE_UNREAD = "system_message_unread";

    /**
     * 账户安全消息未读数量
     */
    private static final String ACCOUNT_SAFE_UNREAD = "account_safe_unread";

    /**
     * 用户头像URL
     */
    private static final String USER_PIC_URL = "user_pic_url";

    /**
     * 本地头像地址
     */
    private static final String USER_LOACAL_URL = "user_loacal_url";

    /**
     * 小额免支付密码
     */
    private static final String EASY_PAY = "easy_pay";


    /**
     * 用户地区
     */
    private static final String USER_AREA = "user_area";

    /**
     * 用户性别
     */
    private static final String USER_SEX = "user_sex";

    /**
     * 用户签名
     */
    private static final String USER_SIGNATRUE = "user_signature";

    /**
     * 用户简介
     */
    private static final String USER_DESC = "user_desc";
    /**
     * 用户登录方式
     */
    private static final String LOGIN_WAY = "login_way";

    /**
     * 用户账户可用余额
     */
    public static final String USER_MONEY = "user_money";

    /**
     * 用户账户余额是否冻结
     */
    public static final String USER_MONEY_STATE = "user_money_state";

    /**
     * 首页晒一晒是否有更新
     */
    public static final String SHOW_UPDATE = "show_update";

    /**
     * 用户是否设置了登录密码
     */
    public static final String USER_LOGIN_PW = "user_login_pw";

    /**
     * 用户是否阅读了广告协议
     */
    public static final String USER_READ_REABAG_RULE = "user_read_redbag_rule";

    /**
     * 关注的数量
     */
    public static final String USER_FOCUS_NUM = "user_focus_num";

    /**
     * 广告图片地址
     */
    public static final String ADV_PIC_URL = "adv_pic_url";

    /**
     * 链接地址
     */
    public static final String ADV_URL = "adv_url";

    /**
     * 上刊日期
     */
    public static final String ADV_UPINGDATE = "adv_upIngDate";

    /**
     * 过期日期
     */
    public static final String ADV_EXPIREDATE = "adv_expireDate";

    /**
     * 记录是否关闭我的转发层级
     */
    public static final String CLOSE_FORWARD = "is_close_forward";

    /**
     * 记录我的转发层级关系
     */
    public static final String IS_ALWAYS_SHOW = "is_always_show";

    /**
     * 记录我的转发层级关系的下标
     */
    public static final String RECORD_INDEX = "record_index";

    /**
     * 记录app版本号
     */
    public static final String APP_VERSION = "app_version";

    /**
     * 记录jpush tags
     */
    public static final String JPUSH_TAGS = "jpush_tags";

    /**
     * 记录奖区数量
     */
    public static final String LOTTERY_AREA_NUM = "lottery_area_num";

    /**
     * 记录用户最近一次播放的视频URL
     */
    public static final String LAST_PLAY_VIDEO_URL = "last_play_video_url";

    /**
     * 记录用户最近一次播放视频的位置
     */
    public static final String LAST_PLAY_VIDEO_POSITION = "last_play_video_position";

    /**
     * 新浪微博token
     */
    public static final String SINA_ACCESS_TOKEN = "sinaAccessToken";
    /**
     * 新浪微博refreshToken
     */
    public static final String SINA_REFRESH_ACCESS_TOKEN = "sinaRefreshAccessToken";
    /**
     * 第三方登录openId
     */
    public static final String USER_OPEN_ID = "userOpenId";
    /**
     * 最新公告时间
     */
    public static final String LATEST_ANNOUNCEMENT_TIME = "latestAnnouncementTime";
    /**
     * js的userId
     */
    public static final String JS_USER_ID = "jsuserId";

    /**
     * 极光Id
     */
    public static final String JPUSH_ID = "jpushId";

    /**
     * 极光Id
     */
    public static final String CACHE_SIZE = "cacheSize";

    /**
     * 用户个人资料地址
     */
    public static final String city_code = "cityCode";


    private static final String NEWS_ANNOUNCEMENT_NUM = "newsAnnouncementNum";

    private static final String NEWS_SYSTEM_INFORMATION_NUM = "newsSystemInformationNum";

    private static final String NEWS_COMMENT_NUM = "newsCommentNum";

    private static final String NEWS_QA_NUM = "newsQaNum";

    private static final String NEWS_GROUP_NUM = "newsGroupNum";

    private static final String NEWS_ACTIVITY_NUM = "newsActivityNum";

    private static final String NEWS_RECOMMEND_NUM = "newsRecommendNum";

    private static final String NEWS_ACCOUNT_INFORMATION_NUM = "newsAccountInformationNum";

    //是否显示全聊及其入口
    private static final String NEED_TEAM = "needTeam";

    //当前请求act
    private static final String NOW_ACT = "nowAct";

    public SysSharePres() {
        System.out.println(BaseApplication.getInstance().getContext());
        mSharePres = BaseApplication.getInstance().getContext().getSharedPreferences(PRES_NAME,
                Context.MODE_PRIVATE);
    }

    private static class SysSharePresHolder {

        static final SysSharePres INSTANCE = new SysSharePres();
    }

    public static SysSharePres getInstance() {
        return SysSharePresHolder.INSTANCE;
    }

    public void setUserIsfirstDownload(boolean user_isfirstdownload) {
        mSharePres.edit()
                .putBoolean(USER_ISFIRSTDOWNLOAD, user_isfirstdownload)
                .apply();
    }

    public boolean getUserIsfirstDownload() {
        return mSharePres.getBoolean(USER_ISFIRSTDOWNLOAD, true);
    }

    public boolean getIsEasyPay() {
        return mSharePres.getBoolean(EASY_PAY, true);
    }

    public void setEasyPay(boolean easyPay) {
        mSharePres.edit()
                .putBoolean(EASY_PAY, easyPay)
                .commit();

    }

    public void setUserFirstRemindHome(boolean user_firstremindhome) {
        mSharePres.edit()
                .putBoolean(USER_FIRSTREMINDHOME, user_firstremindhome)
                .commit();
    }

    public boolean getUserFirstRemindHome() {
        return mSharePres.getBoolean(USER_FIRSTREMINDHOME, true);
    }

    public void setUserFirstRemindAuth(boolean user_first_Auth) {
        mSharePres.edit().putBoolean(USER_FIRST_AUTH, user_first_Auth).commit();
    }

    public boolean getUserFirstRemindAuth() {
        return mSharePres.getBoolean(USER_FIRST_AUTH, true);
    }

    public void setUserFirstInfoDetail(boolean user_firstinfodetail) {
        mSharePres.edit()
                .putBoolean(USER_FIRSTINFODETAIL, user_firstinfodetail)
                .commit();
    }

    public boolean getUserFirstInfoDetail() {
        return mSharePres.getBoolean(USER_FIRSTINFODETAIL, true);
    }

    public void setUserFirstMyAccount(boolean user_firstaccount) {
        mSharePres.edit().putBoolean(USER_FIRSTMYACCOUNT, user_firstaccount)
                .commit();
    }

    public boolean getUserFirstMyAccount() {
        return mSharePres.getBoolean(USER_FIRSTMYACCOUNT, true);
    }

    public void setUpdateApp(boolean update_app) {
        mSharePres.edit().putBoolean(UPDATE_APP, update_app).commit();
    }

    public boolean getUpdateApp() {
        return mSharePres.getBoolean(UPDATE_APP, false);
    }

    public void setClickCancel(boolean update_click_cancel) {
        mSharePres.edit().putBoolean(UPDATE_CLICK_CANCEL, update_click_cancel)
                .commit();
    }

    public boolean getClickCancel() {
        return mSharePres.getBoolean(UPDATE_CLICK_CANCEL, false);
    }

    public void setUserIsLogin(boolean user_login) {
        mSharePres.edit().putBoolean(USER_LOGIN, user_login).commit();
    }

    public boolean getUserIsLogin() {
        return mSharePres.getBoolean(USER_LOGIN, false);
    }

    public void setCancelRemingLogin(boolean user_login) {
        mSharePres.edit().putBoolean(CANCEL_REMIND_LOGIN, user_login).commit();
    }

    public boolean getCancelRemingLogin() {
        return mSharePres.getBoolean(CANCEL_REMIND_LOGIN, false);
    }

    public void setUserSafetyproblem(boolean user_SafetyProblem) {
        mSharePres.edit().putBoolean(USER_SAFETYPROBLEM, user_SafetyProblem)
                .commit();
    }

    public boolean getUserSafetyproblem() {
        return mSharePres.getBoolean(USER_SAFETYPROBLEM, false);
    }

    public void setUserSetpaypw(boolean user_setpaypw) {
        mSharePres.edit().putBoolean(USER_SETPAYPW, user_setpaypw).commit();
    }

    public boolean getUserSetpaypw() {
        return mSharePres.getBoolean(USER_SETPAYPW, false);
    }

    public void setSessionKey(String sessionKey) {
        mSharePres.edit().putString(SESSIONKEY, sessionKey).commit();
    }

    public String getSessionKey() {
        return mSharePres.getString(SESSIONKEY, "");
    }

    public void setUserGesture(String user_gesture) {
        mSharePres.edit().putString(USER_GESTURE, user_gesture).commit();
    }

    public String getContactsDate() {
        return mSharePres.getString(CONTACTSDATE, "");
    }

    public void setContactsDate(String user_gesture) {
        mSharePres.edit().putString(CONTACTSDATE, user_gesture).commit();
    }

    public String getCareFriendsDate() {
        return mSharePres.getString(CAREFRIENDSDATE, "");
    }

    public void setCareFriendsDate(String carefriendsdate) {
        mSharePres.edit().putString(CAREFRIENDSDATE, carefriendsdate).commit();
    }

    public String getUserGesture() {
        return mSharePres.getString(USER_GESTURE, "");
    }

    public void setIsOpenGesture(boolean isopengesture) {
        mSharePres.edit().putBoolean(ISOPENGESTURE, isopengesture).commit();
    }

    public boolean getIsOpenGesture() {
        return mSharePres.getBoolean(ISOPENGESTURE, false);
    }

    public void setUserId(String user_id) {
        mSharePres.edit().putString(USER_ID, user_id).commit();
    }

    public void setBeforeUserId(String userId) {
        mSharePres.edit().putString(USER_BEFORE_ID, userId).commit();
    }

    public String getBeforeUserId() {
        return mSharePres.getString(USER_BEFORE_ID, "");
    }

    public String getUserId() {
        return mSharePres.getString(USER_ID, "");
    }

    public void setUserPicUrl(String user_pic_url) {
        mSharePres.edit().putString(USER_PIC_URL, user_pic_url).commit();
    }

    public String getUserPicUrl() {
        return mSharePres.getString(USER_PIC_URL, "");
    }

    public void setUserLocalUrl(String user_loacal_url) {
        mSharePres.edit().putString(USER_LOACAL_URL, user_loacal_url).commit();
    }

    public String getUserLocalUrl() {
        return mSharePres.getString(USER_LOACAL_URL, "");
    }

    public void setUserTrueName(String user_true_name) {
        mSharePres.edit().putString(USER_TRUENAME, user_true_name).commit();
    }

    public String getUserTrueName() {
        return mSharePres.getString(USER_TRUENAME, "");
    }

    public void setUserIdCard(String user_idcard) {
        mSharePres.edit().putString(USER_IDCARD, user_idcard).commit();
    }

    public String getUserIdCard() {
        return mSharePres.getString(USER_IDCARD, "");
    }

    public void setUserName(String user_name) {
        mSharePres.edit().putString(USER_NAME, user_name).commit();
    }

    public String getUserName() {
        return mSharePres.getString(USER_NAME, "");
    }

    public void setUserAccount(String user_account) {
        mSharePres.edit().putString(USER_ACCOUNT, user_account).commit();
    }

    public String getUserAccount() {
        return mSharePres.getString(USER_ACCOUNT, "");
    }

    public void setUserPhone(String user_phone) {
        mSharePres.edit().putString(USER_PHONE, user_phone).commit();
    }

    public String getUserPhone() {
        return mSharePres.getString(USER_PHONE, "");
    }

    public void setUserLevel(String user_level) {
        mSharePres.edit().putString(USER_LEVEL, user_level).commit();
    }

    public String getUserLevel() {
        return mSharePres.getString(USER_LEVEL, "");
    }

    public void setUserIdentified(int user_identified) {
        mSharePres.edit().putInt(USER_IDENTIFIED, user_identified).commit();
    }

    public int getUserIdentified() {
        return mSharePres.getInt(USER_IDENTIFIED, 0);
    }

    public void setIdentifiedId(String identified_id) {
        mSharePres.edit().putString(IDENTIFIED_ID, identified_id).commit();
    }

    public String getIdentifiedId() {
        return mSharePres.getString(IDENTIFIED_ID, "");
    }

    public void setOrderserialno(String orderserialno) {
        mSharePres.edit().putString(ORDERSERIALNO, orderserialno).commit();
    }

    public String getOrderserialno() {
        return mSharePres.getString(ORDERSERIALNO, "");
    }

    public void setUserGestureChance(int user_gesture_chance) {
        mSharePres.edit().putInt(USER_GESTURE_CHANCE, user_gesture_chance)
                .commit();
    }

    public int getUserGestureChance() {
        return mSharePres.getInt(USER_GESTURE_CHANCE, 5);
    }


    public String getUserDesc() {
        return mSharePres.getString(USER_DESC, "");
    }

    public void setUserDesc(String userDesc) {
        mSharePres.edit().putString(USER_DESC, userDesc).commit();
    }

    public int getLoginWay() {
        return mSharePres.getInt(LOGIN_WAY, 0);
    }

    public void setLoginWay(int login_way) {
        mSharePres.edit().putInt(LOGIN_WAY, login_way).commit();
    }

    public String getUserArea() {
        return mSharePres.getString(USER_AREA, "");
    }

    public void setUserArea(String userArea) {
        mSharePres.edit().putString(USER_AREA, userArea).commit();
    }

    public String getUserSignatrue() {
        return mSharePres.getString(USER_SIGNATRUE, "");
    }

    public void setMyTreadUnread(int num) {
        mSharePres.edit().putInt(MY_TREAD_UNREAD, num).apply();
    }

    public int getMyTreadUnread() {
        return mSharePres.getInt(MY_TREAD_UNREAD, 0);
    }

    public void setFriTreadUnread(int num) {
        Log.e("zxw", "setFriTreadUnread: " + num);
        mSharePres.edit().putInt(FRI_TREAD_UNREAD, num).apply();
    }

    public int getFriTreadUnread() {
        Log.e("zxw", "getFriTreadUnread: " + mSharePres.getInt(FRI_TREAD_UNREAD, 0));
        return mSharePres.getInt(FRI_TREAD_UNREAD, 0);
    }

    public int getSystemMessageUnread() {
        return mSharePres.getInt(SYSTEM_MESSAGE_UNREAD, 0);
    }

    public void setSystemMessageUnread(int num) {
        mSharePres.edit().putInt(SYSTEM_MESSAGE_UNREAD, num).apply();
    }

    public int getAccountSafeUnread() {
        return mSharePres.getInt(ACCOUNT_SAFE_UNREAD, 0);
    }

    public void setAccountSafeUnread(int num) {
        mSharePres.edit().putInt(ACCOUNT_SAFE_UNREAD, num).apply();
    }

    public void setUserSignatrue(String userSignatrue) {
        mSharePres.edit().putString(USER_SIGNATRUE, userSignatrue).commit();
    }

    public String getUserSex() {
        return mSharePres.getString(USER_SEX, "");
    }

    public void setUserSex(String userSex) {
        mSharePres.edit().putString(USER_SEX, userSex).commit();
    }

    public String getUserMoney() {
        return mSharePres.getString(USER_MONEY, "");
    }

    public void setUserMoney(String user_money) {
        mSharePres.edit().putString(USER_MONEY, user_money).commit();
    }

    public boolean getUserMoneyState() {
        return mSharePres.getBoolean(USER_MONEY_STATE, false);
    }

    public void setUserMoneyState(boolean user_money_state) {
        mSharePres.edit().putBoolean(USER_MONEY_STATE, user_money_state).commit();
    }

    public boolean getShowUpdate() {
        return mSharePres.getBoolean(SHOW_UPDATE, false);
    }

    public void setShowUpdate(boolean showupdate) {
        mSharePres.edit().putBoolean(SHOW_UPDATE, showupdate).commit();
    }


    public boolean getUserLoginPw() {
        return mSharePres.getBoolean(USER_LOGIN_PW, false);
    }


    public void setUserLoginPw(boolean user_login_pw) {
        mSharePres.edit().putBoolean(USER_LOGIN_PW, user_login_pw).commit();
    }

    public boolean getUserReadBagRule() {
        return mSharePres.getBoolean(USER_READ_REABAG_RULE, false);
    }

    public void setUserReadBagRule(boolean user_read_redbag_rule) {
        mSharePres.edit().putBoolean(USER_READ_REABAG_RULE, user_read_redbag_rule).commit();
    }

    public String getUserFocusNum() {
        return mSharePres.getString(USER_FOCUS_NUM, "");
    }

    public void setUserFocusNum(String user_focus_num) {
        mSharePres.edit().putString(USER_FOCUS_NUM, user_focus_num).commit();
    }

    public String getAdvPicUrl() {
        return mSharePres.getString(ADV_PIC_URL, "");
    }

    public void setAdvPicUrl(String adv_pic_url) {
        mSharePres.edit().putString(ADV_PIC_URL, adv_pic_url).commit();
    }

    public String getAdvUrl() {
        return mSharePres.getString(ADV_URL, "");
    }

    public void setAdvUrl(String adv_url) {
        mSharePres.edit().putString(ADV_URL, adv_url).commit();
    }

    public String getAdvUpingdate() {
        return mSharePres.getString(ADV_UPINGDATE, "");
    }

    public void setAdvUpingdate(String adv_upingdate) {
        mSharePres.edit().putString(ADV_UPINGDATE, adv_upingdate).commit();
    }

    public String getAdvExpiredate() {
        return mSharePres.getString(ADV_EXPIREDATE, "");
    }

    public void setAdvExpiredate(String adv_expiredate) {
        mSharePres.edit().putString(ADV_EXPIREDATE, adv_expiredate).commit();
    }

    public boolean getCloseForward() {
        return mSharePres.getBoolean(CLOSE_FORWARD, false);
    }

    public void setCloseForward(boolean close_forward) {
        mSharePres.edit().putBoolean(CLOSE_FORWARD, close_forward).commit();
    }

    public String getIsAwaysShow() {
        return mSharePres.getString(IS_ALWAYS_SHOW, "");
    }

    public void setIsAwaysShow(String is_always_show) {
        mSharePres.edit().putString(IS_ALWAYS_SHOW, is_always_show).commit();
    }

    public String getRecordIndex() {
        return mSharePres.getString(RECORD_INDEX, "");
    }

    public void setRecordIndex(String record_index) {
        mSharePres.edit().putString(RECORD_INDEX, record_index).commit();
    }

    public String getAppVersion() {
        return mSharePres.getString(APP_VERSION, "");
    }

    public void setAppVersion(String app_version) {
        mSharePres.edit().putString(APP_VERSION, app_version).commit();
    }

    public Set<String> getJpushTags() {
        return mSharePres.getStringSet(JPUSH_TAGS, new HashSet<String>());
    }

    public void setJpushTags(Set tags) {
        mSharePres.edit().putStringSet(JPUSH_TAGS, tags).commit();
    }

    public int getLotteryAreaNum() {
        return mSharePres.getInt(LOTTERY_AREA_NUM, 0);
    }

    public void setLotteryAreaNum(int num) {
        mSharePres.edit().putInt(LOTTERY_AREA_NUM, num).commit();
    }

    public String getLastPlayVideoUrl() {
        return mSharePres.getString(LAST_PLAY_VIDEO_URL, "");
    }

    public void setLastPlayVideoUrl(String last_play_video_url) {
        mSharePres.edit().putString(LAST_PLAY_VIDEO_URL, last_play_video_url).commit();
    }

    public int getLastPlayVideoPosition() {
        return mSharePres.getInt(LAST_PLAY_VIDEO_POSITION, 0);
    }

    public void setLastPlayVideoPosition(int last_play_video_position) {
        mSharePres.edit().putInt(LAST_PLAY_VIDEO_POSITION, last_play_video_position).commit();
    }

    public String getSinaAccessToken() {
        return mSharePres.getString(SINA_ACCESS_TOKEN, "");
    }

    public void setSinaAccessToken(String sinaAccessToken) {
        mSharePres.edit().putString(SINA_ACCESS_TOKEN, sinaAccessToken).commit();
    }

    public String getSinaRefreshAccessToken() {
        return mSharePres.getString(SINA_REFRESH_ACCESS_TOKEN, "");
    }

    public void setSinaRefreshAccessToken(String sinaAccessToken) {
        mSharePres.edit().putString(SINA_REFRESH_ACCESS_TOKEN, sinaAccessToken).commit();
    }

    public String getOpenId() {
        return mSharePres.getString(USER_OPEN_ID, "");
    }

    public void setOpenId(String openId) {
        mSharePres.edit().putString(USER_OPEN_ID, openId).commit();
    }

    public String getLatestAnnouncementTime() {
        return mSharePres.getString(LATEST_ANNOUNCEMENT_TIME, "");
    }

    public void setLatestAnnouncementTime(String time) {
        mSharePres.edit().putString(LATEST_ANNOUNCEMENT_TIME, time).apply();
    }

    public String getJsUserId() {
        return mSharePres.getString(JS_USER_ID, "");
    }

    public void setJsUserId(String userId) {
        mSharePres.edit().putString(JS_USER_ID, userId).apply();
    }

    public String getJpushId() {
        return mSharePres.getString(JPUSH_ID, "");
    }

    public void setJpushId(String userId) {
        mSharePres.edit().putString(JPUSH_ID, userId).apply();
    }

    public int getCacheSize() {
        return mSharePres.getInt(CACHE_SIZE, 12);
    }

    public void setCache(int size) {
        mSharePres.edit().putInt(CACHE_SIZE, size).apply();
    }

    public int getNewsAnnouncementNum() {
        return mSharePres.getInt(NEWS_ANNOUNCEMENT_NUM, 0);
    }

    public void setNewsAnnouncementNum(int newsAnnouncementNum) {
        mSharePres.edit().putInt(NEWS_ANNOUNCEMENT_NUM, newsAnnouncementNum).apply();
    }

    public int getNewsSystemInformationNum() {
        return mSharePres.getInt(NEWS_SYSTEM_INFORMATION_NUM, 0);
    }

    public void setNewsSystemInformationNum(int newsSystemInformationNum) {
        mSharePres.edit().putInt(NEWS_SYSTEM_INFORMATION_NUM, newsSystemInformationNum).apply();
    }

    public int getNewsCommentNum() {
        return mSharePres.getInt(NEWS_COMMENT_NUM, 0);
    }

    public void setNewsCommentNum(int newsCommentNum) {
        mSharePres.edit().putInt(NEWS_COMMENT_NUM, newsCommentNum).apply();
    }

    public int getNewsQaNum() {
        return mSharePres.getInt(NEWS_QA_NUM, 0);
    }

    public void setNewsQaNum(int newsQaNum) {
        mSharePres.edit().putInt(NEWS_QA_NUM, newsQaNum).apply();
    }

    public int getNewsGroupNum() {
        return mSharePres.getInt(NEWS_GROUP_NUM, 0);
    }

    public void setNewsGroupNum(int newsGroupNum) {
        mSharePres.edit().putInt(NEWS_GROUP_NUM, newsGroupNum).apply();
    }

    public int getNewsActivityNum() {
        return mSharePres.getInt(NEWS_ACTIVITY_NUM, 0);
    }

    public void setNewsActivityNum(int newsActivityNum) {
        mSharePres.edit().putInt(NEWS_ACTIVITY_NUM, newsActivityNum).apply();
    }

    public int getNewsRecommendNum() {
        return mSharePres.getInt(NEWS_RECOMMEND_NUM, 0);
    }

    public void setNewsRecommendNum(int newsRecommendNum) {
        mSharePres.edit().putInt(NEWS_RECOMMEND_NUM, newsRecommendNum).apply();
    }

    public int getNewsAccountInformationNum() {
        return mSharePres.getInt(NEWS_ACCOUNT_INFORMATION_NUM, 0);
    }

    public void setNewsAccountInformationNum(int newsAccountInformationNum) {
        mSharePres.edit().putInt(NEWS_ACCOUNT_INFORMATION_NUM, newsAccountInformationNum).apply();
    }

    public String getCityCode() {
        return mSharePres.getString(city_code, "");
    }

    public void setCityCode(String cityCode) {
        mSharePres.edit().putString(city_code, cityCode).apply();
    }

    public boolean isNeedTeam() {
        return mSharePres.getBoolean(NEED_TEAM, true);
    }

    public void setNeedTeam(boolean needTeam) {
        mSharePres.edit().putBoolean(NEED_TEAM, needTeam).apply();
    }

    public String getNowAct() {
        return mSharePres.getString(NOW_ACT, "");
    }

    public void setNowAct(String act) {
        mSharePres.edit().putString(NOW_ACT, act).apply();
    }
}
