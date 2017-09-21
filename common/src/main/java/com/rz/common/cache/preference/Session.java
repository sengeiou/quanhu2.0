package com.rz.common.cache.preference;

import android.text.TextUtils;

import java.util.Set;

public class Session {

    public static String user_beforeUserId;

    /**
     * 用户是否是第一次安装APP true 是的 false不是
     */
    public static boolean user_isfirstdownload;

    /**
     * 用户第一次安装APP 进入首页面时 操作提示 true是的 false不是
     */
    public static boolean user_firstremindhome;

    /**
     * 用户第一次安装APP 进入绑卡认证页面时 操作提示 true是的 false不是 默认为true
     */
    public static boolean user_first_Auth;

    /**
     * 用户第一次安装APP 进入信息详情页 操作提示 true是的 false不是
     */
    public static boolean user_firstInfoDetail;

    /**
     * 用户第一次安装APP 进入我的账户时 操作提示 true是的 false不是
     */
    public static boolean user_firstMyAccount;

    /**
     * 是否有更新，并点击取消更新，默认false
     */
    public static boolean update_app;
    /**
     * 有更新，用户点击取消更新，默认false
     */
    public static boolean update_click_cancel;

    /**
     * 用户是否登录 true 登录 false 未登录
     */
    public static boolean user_islogin;
    /**
     * 用户是否设置了密保问题
     */
    public static boolean user_SafetyProblem;
    /**
     * 用户是否设置了支付密码
     */
    public static boolean user_setpaypw;
    /**
     * SessionId 后面请求的时候回传
     */
    public static String sessionKey;
    /**
     * 是否开启免密支付
     */
    public static boolean isOpenGesture;
    /**
     * 用户设置的手势密码
     */
    public static String user_gesture;
    /**
     * 用户id
     */
    public static String user_id;
    /**
     * 用户头像网络地址
     */
    public static String user_pic_url;
    /**
     * 二维码图片
     */
    public static String user_loacal_url;
    /**
     * 用户真实姓名
     */
    public static String user_true_name;
    /**
     * 用户身份证号
     */
    public static String user_idcard;
    /**
     * 用户昵称
     */
    public static String user_name;
    /**
     * 用户登录账号
     */
    public static String user_account;
    /**
     * 用户手机号
     */
    public static String user_phone;
    /**
     * 用户等级
     */
    public static String user_level;
    /**
     * 用户认证状态
     */
    public static int user_identified;
    /**
     * 实名认证记录id
     */
    public static String identified_id;
    /**
     * 交易流水号
     */
    public static String orderserialno;
    /**
     * 手势密码验证机会
     */
    public static int user_gesture_chance;
    /**
     * 通讯录好友最后查询时间
     */
    public static String ContactsDate;
    /**
     * 通讯录关注的人最后查询时间
     */
    public static String CareFriendsDate;
    /**
     * 用户地区
     */
    public static String user_area;
    /**
     * 用户性别
     */
    public static String user_sex;
    /**
     * 用户简介
     */
    public static String user_desc;
    /**
     * 用户签名
     */
    public static String user_signatrue;
    /**
     * 用户账户可用余额
     */
    public static String user_money;
    /**
     * 用户账户余额是否冻结
     */
    public static boolean user_money_state;
    /**
     * 首页晒一晒是否有更新，提示红点用
     */
    public static boolean show_update;
    /**
     * 用户是否设置了登录密码
     */
    public static boolean user_login_pw;
    /**
     * 红包广告是否阅读协议
     */
    public static boolean redbag_rule;
    /**
     * 关注的数量
     */
    public static String user_focus_num;
    /**
     * 广告图片地址
     */
    public static String adv_pic_url;
    /**
     * 链接地址
     */
    public static String adv_url;
    /**
     * 上刊日期
     */
    public static String adv_upIngDate;
    /**
     * 过期日期
     */
    public static String adv_expireDate;
    /**
     * 记录是否关闭我的转发层级
     */
    public static boolean isCloseForward;
    /**
     * 记录我的转发层级
     */
    public static String is_aways_show;
    /**
     * 记录我的转发层级下标
     */
    public static String record_index;
    /**
     * 记录app版本号
     */
    public static String app_version;
    /**
     * 记录jpush tags
     */
    public static Set<String> jpush_tags;
    /**
     * 记录奖区数量
     */
    public static int lottery_area_num;
    /**
     * 用户取消登录提示，true取消 false未取消
     */
    private static boolean cancel_remind_login;
    /**
     * 我的动态未读数量
     */
    private static int my_tread_unread;
    /**
     * 好友动态未读数量
     */
    private static int fri_tread_unread;
    /**
     * 系统消息未读数量
     */
    private static int system_message_unread;
    /**
     * 账户安全消息未读数量
     */
    private static int account_safe_unread;
    /**
     * 用户登录方式
     */
    private static int login_way;
    /**
     * 记录最近播放的视频的URL
     */
    private static String last_play_video_url;
    /**
     * 记录最近播放的视频的播放位置
     */
    private static int last_play_video_position;

    /**
     * 微博用户的token
     */
    private static String sina_access_token;
    /**
     * 微博用户的token
     */
    private static String sina_refresh_access_token;
    /**
     * 第三方用户的openId
     */
    private static String user_open_id;

    /**
     * 存储js的userId
     */
    private static String js_user_id;

    /**
     * 用户个人资料的地址信息
     */
    private static String city_code;

    public static boolean isRedbag_rule() {
        return SysSharePres.getInstance().getUserReadBagRule();
    }

    public static void setRedbag_rule(boolean redbag_rule) {
        Session.redbag_rule = redbag_rule;
        SysSharePres.getInstance().setUserReadBagRule(redbag_rule);
    }

    public static boolean getUserIsFirstDownload() {
        return SysSharePres.getInstance().getUserIsfirstDownload();
    }

    public static void setUserIsFirstDownload(boolean user_isfirstdownload) {
        Session.user_isfirstdownload = user_isfirstdownload;
        SysSharePres.getInstance().setUserIsfirstDownload(user_isfirstdownload);
    }


    public static boolean getUserFirstRemindHome() {
        return SysSharePres.getInstance().getUserFirstRemindHome();
    }

    public static void setUserFirstRemindHome(boolean user_firstremindhome) {
        Session.user_firstremindhome = user_firstremindhome;
        SysSharePres.getInstance().setUserFirstRemindHome(user_firstremindhome);
    }

    public static boolean getUserFirstRemindAuth() {
        return SysSharePres.getInstance().getUserFirstRemindAuth();
    }

    public static void setUserFirstRemindAuth(boolean user_first_Auth) {
        Session.user_first_Auth = user_first_Auth;
        SysSharePres.getInstance().setUserFirstRemindAuth(user_first_Auth);
    }

    public static boolean getUserFirstInfoDetail() {
        return SysSharePres.getInstance().getUserFirstInfoDetail();
    }

    public static void setUserFirstInfoDetail(boolean user_firstInfoDetail) {
        Session.user_firstInfoDetail = user_firstInfoDetail;
        SysSharePres.getInstance().setUserFirstInfoDetail(user_firstInfoDetail);
    }

    public static boolean getUserFirstMyAccount() {
        return SysSharePres.getInstance().getUserFirstMyAccount();
    }

    public static void setUserFirstMyAccount(boolean user_firstMyAccount) {
        Session.user_firstMyAccount = user_firstMyAccount;
        SysSharePres.getInstance().setUserFirstMyAccount(user_firstMyAccount);
    }

    public static boolean getUpdateApp() {
        return SysSharePres.getInstance().getUpdateApp();
    }

    public static void setUpdateApp(boolean update_app) {
        Session.update_app = update_app;
        SysSharePres.getInstance().setUpdateApp(update_app);
    }

    public static boolean getClickCancel() {
        return SysSharePres.getInstance().getClickCancel();
    }

    public static void setClickCancel(boolean update_click_cancel) {
        Session.update_click_cancel = update_click_cancel;
        SysSharePres.getInstance().setClickCancel(update_click_cancel);
    }

    public static boolean getUserIsLogin() {
        return SysSharePres.getInstance().getUserIsLogin();
    }

    public static void setUserIsLogin(boolean user_islogin) {
        Session.user_islogin = user_islogin;
        SysSharePres.getInstance().setUserIsLogin(user_islogin);
    }


    public static boolean getCancelRemingLogin() {
        return SysSharePres.getInstance().getCancelRemingLogin();
    }

    public static void setCancelRemingLogin(boolean cancel_remind_login) {
        Session.cancel_remind_login = cancel_remind_login;
        SysSharePres.getInstance().setCancelRemingLogin(cancel_remind_login);
    }

    public static boolean getUserSafetyproblem() {
        return SysSharePres.getInstance().getUserSafetyproblem();
    }

    public static void setUserSafetyproblem(boolean user_SafetyProblem) {
        Session.user_SafetyProblem = user_SafetyProblem;
        SysSharePres.getInstance().setUserSafetyproblem(user_SafetyProblem);
    }

    public static boolean getUserSetpaypw() {
        return SysSharePres.getInstance().getUserSetpaypw();
    }

    public static void setUserSetpaypw(boolean user_setpaypw) {
        Session.user_setpaypw = user_setpaypw;
        SysSharePres.getInstance().setUserSetpaypw(user_setpaypw);
    }

    public static boolean getIsOpenGesture() {
        return SysSharePres.getInstance().getIsOpenGesture();
    }

    public static void setIsOpenGesture(boolean isOpenGesture) {
        Session.isOpenGesture = isOpenGesture;
        SysSharePres.getInstance().setIsOpenGesture(isOpenGesture);
    }

    public static String getSessionKey() {
        return SysSharePres.getInstance().getSessionKey();
    }

    public static void setSessionKey(String sessionKey) {
        Session.sessionKey = sessionKey;
        SysSharePres.getInstance().setSessionKey(sessionKey);
    }

    public static String getUserGesture() {
        return SysSharePres.getInstance().getUserGesture();
    }

    public static void setUserGesture(String user_gesture) {
        Session.user_gesture = user_gesture;
        SysSharePres.getInstance().setUserGesture(user_gesture);
    }

    public static String getUserId() {
        return SysSharePres.getInstance().getUserId();
    }

    public static void setUserId(String user_id) {
        Session.user_id = user_id;
        SysSharePres.getInstance().setUserId(user_id);
    }

    public static String getBeforeUserId() {
        return SysSharePres.getInstance().getBeforeUserId();
    }

    public static void setBeforeLoginId(String userId) {
        Session.user_beforeUserId = userId;
        SysSharePres.getInstance().setBeforeUserId(userId);
    }

    public static int getMyTreadUnread() {
        return SysSharePres.getInstance().getMyTreadUnread();
    }

    public static void setMyTreadUnread(int num) {
        Session.my_tread_unread = num;
        SysSharePres.getInstance().setMyTreadUnread(num);
    }

    public static int getFriTreadUnread() {
        return SysSharePres.getInstance().getFriTreadUnread();
    }

    public static void setFriTreadUnread(int num) {
        Session.fri_tread_unread = num;
        SysSharePres.getInstance().setFriTreadUnread(num);
    }

    public static int getAccount_safe_unread() {
        return SysSharePres.getInstance().getAccountSafeUnread();
    }

    public static void setAccount_safe_unread(int account_safe_unread) {
        Session.account_safe_unread = account_safe_unread;
        SysSharePres.getInstance().setAccountSafeUnread(account_safe_unread);
    }

    public static int getSystem_message_unread() {
        return SysSharePres.getInstance().getSystemMessageUnread();
    }

    public static void setSystem_message_unread(int system_message_unread) {
        Session.system_message_unread = system_message_unread;
        SysSharePres.getInstance().setSystemMessageUnread(system_message_unread);
    }

    public static String getUserPicUrl() {
        return SysSharePres.getInstance().getUserPicUrl();
    }

    public static void setUserPicUrl(String user_pic_url) {
        Session.user_pic_url = user_pic_url;
        SysSharePres.getInstance().setUserPicUrl(user_pic_url);
    }

    public static String getUserLocalUrl() {
        return SysSharePres.getInstance().getUserLocalUrl();
    }

    public static void setUserLocalUrl(String user_loacal_url) {
        Session.user_loacal_url = user_loacal_url;
        SysSharePres.getInstance().setUserLocalUrl(user_loacal_url);
    }

    public static String getUserTrueName() {
        return SysSharePres.getInstance().getUserTrueName();
    }

    public static void setUserTrueName(String user_true_name) {
        Session.user_true_name = user_true_name;
        SysSharePres.getInstance().setUserTrueName(user_true_name);
    }

    public static String getUserIdCard() {
        return SysSharePres.getInstance().getUserIdCard();
    }

    public static void setUserIdCard(String user_idcard) {
        Session.user_idcard = user_idcard;
        SysSharePres.getInstance().setUserIdCard(user_idcard);
    }

    public static String getUserName() {
        return SysSharePres.getInstance().getUserName();
    }

    public static void setUserName(String user_name) {
        Session.user_name = user_name;
        SysSharePres.getInstance().setUserName(user_name);
    }
    public static void setMessFree(boolean isChecked) {
        SysSharePres.getInstance().setMessFree(isChecked);
    }
    public static boolean getMessFree() {
       return SysSharePres.getInstance().getMessFree();
    }

    public static String getUserAccount() {
        return SysSharePres.getInstance().getUserAccount();
    }

    public static void setUserAccount(String user_account) {
        Session.user_account = user_account;
        SysSharePres.getInstance().setUserAccount(user_account);
    }

    public static String getUserPhone() {
        return SysSharePres.getInstance().getUserPhone();
    }

    public static void setUserPhone(String user_phone) {
        Session.user_phone = user_phone;
        SysSharePres.getInstance().setUserPhone(user_phone);
    }

    public static String getUserLevel() {
        return SysSharePres.getInstance().getUserLevel();
    }

    public static void setUserLevel(String user_level) {
        Session.user_level = user_level;
        SysSharePres.getInstance().setUserLevel(user_level);
    }

    public static int getUserIdentified() {
        return SysSharePres.getInstance().getUserIdentified();
    }

    public static void setUserIdentified(int user_identified) {
        Session.user_identified = user_identified;
        SysSharePres.getInstance().setUserIdentified(user_identified);
    }

    public static String getIdentifiedId() {
        return SysSharePres.getInstance().getIdentifiedId();
    }

    public static void setIdentifiedId(String identified_id) {
        Session.identified_id = identified_id;
        SysSharePres.getInstance().setIdentifiedId(identified_id);
    }

    public static void setContactsdate(String contactsdate) {
        Session.ContactsDate = contactsdate;
        SysSharePres.getInstance().setContactsDate(contactsdate);
    }

    public static String getContacksdate() {
        return SysSharePres.getInstance().getContactsDate();
    }

    public static String getCareFriendsDate() {
        return SysSharePres.getInstance().getCareFriendsDate();
    }

    public static void setCareFriendsDate(String CareFriendsDate) {
        Session.CareFriendsDate = CareFriendsDate;
        SysSharePres.getInstance().setCareFriendsDate(CareFriendsDate);
    }

    public static String getOrderserialno() {
        return SysSharePres.getInstance().getOrderserialno();
    }

    public static void setOrderserialno(String orderserialno) {
        Session.orderserialno = orderserialno;
        SysSharePres.getInstance().setOrderserialno(orderserialno);
    }

    public static int getUserGestureChance() {
        return SysSharePres.getInstance().getUserGestureChance();
    }

    public static void setUserGestureChance(int user_gesture_chance) {
        Session.user_gesture_chance = user_gesture_chance;
        SysSharePres.getInstance().setUserGestureChance(user_gesture_chance);
    }

    public static void setEasyPay(boolean easyPay) {
        SysSharePres.getInstance().setEasyPay(easyPay);
    }

    public static boolean getIsEasyPay() {
        return SysSharePres.getInstance().getIsEasyPay();
    }


    public static String getUser_signatrue() {
        return SysSharePres.getInstance().getUserSignatrue();
    }

    public static void setUser_signatrue(String user_signatrue) {
        SysSharePres.getInstance().setUserSignatrue(user_signatrue);
    }

    public static String getUser_desc() {
        return SysSharePres.getInstance().getUserDesc();
    }

    public static void setUser_desc(String user_desc) {
        Session.user_desc = user_desc;
        SysSharePres.getInstance().setUserDesc(user_desc);
    }

    public static int getLoginWay() {
        return SysSharePres.getInstance().getLoginWay();
    }

    public static void setLoginWay(int login_way) {
        Session.login_way = login_way;
        SysSharePres.getInstance().setLoginWay(login_way);
    }

    public static String getUser_sex() {
        return SysSharePres.getInstance().getUserSex();
    }

    public static void setUser_sex(String user_sex) {
        SysSharePres.getInstance().setUserSex(user_sex);
    }

    public static String getUser_area() {
        return SysSharePres.getInstance().getUserArea();
    }

    public static void setUser_area(String user_area) {
        SysSharePres.getInstance().setUserArea(user_area);
    }

    public static String getUserMoney() {
        return SysSharePres.getInstance().getUserMoney();
    }

    public static void setUserMoney(String user_money) {
        Session.user_money = user_money;
        SysSharePres.getInstance().setUserMoney(user_money);
    }

    public static boolean getUserMoneyState() {
        return SysSharePres.getInstance().getUserMoneyState();
    }

    public static void setUserMoneyState(boolean user_money_state) {
        Session.user_money_state = user_money_state;
        SysSharePres.getInstance().setUserMoneyState(user_money_state);
    }

    public static boolean getShowUpdate() {
        return SysSharePres.getInstance().getShowUpdate();
    }

    public static void setShowUpdate(boolean showUpdate) {
        Session.show_update = showUpdate;
        SysSharePres.getInstance().setShowUpdate(showUpdate);
    }

    public static boolean getUserLoginPw() {
        return SysSharePres.getInstance().getUserLoginPw();
    }

    public static void setUserLoginPw(boolean user_login_pw) {
        Session.user_login_pw = user_login_pw;
        SysSharePres.getInstance().setUserLoginPw(user_login_pw);
    }

    public static String getUserFocusNum() {
        return SysSharePres.getInstance().getUserFocusNum();
    }

    public static void setUserFocusNum(String user_focus_num) {
        Session.user_focus_num = user_focus_num;
        SysSharePres.getInstance().setUserFocusNum(user_focus_num);
    }

    public static String getAdv_pic_url() {
        return SysSharePres.getInstance().getAdvPicUrl();
    }

    public static void setAdv_pic_url(String adv_pic_url) {
        Session.adv_pic_url = adv_pic_url;
        SysSharePres.getInstance().setAdvPicUrl(adv_pic_url);
    }

    public static String getAdv_url() {
        return SysSharePres.getInstance().getAdvUrl();
    }

    public static void setAdv_url(String adv_url) {
        Session.adv_url = adv_url;
        SysSharePres.getInstance().setAdvUrl(adv_url);
    }

    public static String getAdv_upIngDate() {
        return SysSharePres.getInstance().getAdvUpingdate();
    }

    public static void setAdv_upIngDate(String adv_upIngDate) {
        Session.adv_upIngDate = adv_upIngDate;
        SysSharePres.getInstance().setAdvUpingdate(adv_upIngDate);
    }

    public static String getAdv_expireDate() {
        return SysSharePres.getInstance().getAdvExpiredate();
    }

    public static void setAdv_expireDate(String adv_expireDate) {
        Session.adv_expireDate = adv_expireDate;
        SysSharePres.getInstance().setAdvExpiredate(adv_expireDate);
    }

    public static boolean getCloseForward() {
        return SysSharePres.getInstance().getCloseForward();
    }

    public static void setCloseForward(boolean isCloseForward) {
        Session.isCloseForward = isCloseForward;
        SysSharePres.getInstance().setCloseForward(isCloseForward);
    }

    public static String getIsAwaysShow() {
        return SysSharePres.getInstance().getIsAwaysShow();
    }

    public static void setIsAwaysShow(String is_aways_show) {
        Session.is_aways_show = is_aways_show;
        SysSharePres.getInstance().setIsAwaysShow(is_aways_show);
    }

    public static String getRecordIndex() {
        return SysSharePres.getInstance().getRecordIndex();
    }

    public static void setRecordIndex(String record_index) {
        Session.record_index = record_index;
        SysSharePres.getInstance().setRecordIndex(record_index);
    }

    public static String getAppVersion() {
        return SysSharePres.getInstance().getAppVersion();
    }

    public static void setAppVersion(String app_version) {
        Session.app_version = app_version;
        SysSharePres.getInstance().setAppVersion(app_version);
    }

    public static Set<String> getJpushTags() {
        return SysSharePres.getInstance().getJpushTags();
    }

    public static void setJpushTags(Set<String> tags) {
        Session.jpush_tags = tags;
        SysSharePres.getInstance().setJpushTags(tags);
    }

    public static int getLottery_area_num() {
        return SysSharePres.getInstance().getLotteryAreaNum();
    }

    public static void setLottery_area_num(int lottery_area_num) {
        Session.lottery_area_num = lottery_area_num;
        SysSharePres.getInstance().setLotteryAreaNum(lottery_area_num);
    }

    public static int getLastPlayVideoPosition() {
        return SysSharePres.getInstance().getLastPlayVideoPosition();
    }

    public static void setLastPlayVideoPosition(int last_play_video_position) {
        Session.last_play_video_position = last_play_video_position;
        SysSharePres.getInstance().setLastPlayVideoPosition(last_play_video_position);
    }

    public static String getLastPlayVideoUrl() {
        return SysSharePres.getInstance().getLastPlayVideoUrl();
    }

    public static void setLastPlayVideoUrl(String last_play_video_url) {
        Session.last_play_video_url = last_play_video_url;
        SysSharePres.getInstance().setLastPlayVideoUrl(last_play_video_url);
    }

    public static String getSinaAccessToken() {
        return SysSharePres.getInstance().getSinaAccessToken();
    }

    public static void setSinaAccessToken(String sinaAccessToken) {
        Session.sina_access_token = sinaAccessToken;
        SysSharePres.getInstance().setSinaAccessToken(sinaAccessToken);
    }

    public static String getSinaRefreshAccessToken() {
        return SysSharePres.getInstance().getSinaRefreshAccessToken();
    }

    public static void setSinaRefreshAccessToken(String sinaRefreshAccessToken) {
        Session.sina_refresh_access_token = sinaRefreshAccessToken;
        SysSharePres.getInstance().setSinaRefreshAccessToken(sinaRefreshAccessToken);
    }

    public static String getOpenId() {
        return SysSharePres.getInstance().getOpenId();
    }

    public static void setOpenId(String openId) {
        Session.user_open_id = openId;
        SysSharePres.getInstance().setOpenId(openId);
    }

    public static String getLatestAnnouncementTime() {
        return SysSharePres.getInstance().getLatestAnnouncementTime();
    }

    public static void setLatestAnnouncementTime(String time) {
        SysSharePres.getInstance().setLatestAnnouncementTime(time);
    }

    public static String getJsUserId() {
        return SysSharePres.getInstance().getOpenId();
    }

    public static void setJsUserId(String userId) {
        Session.js_user_id = userId;
        SysSharePres.getInstance().setOpenId(userId);
    }

    public static String getJpushId() {
        return SysSharePres.getInstance().getJpushId();
    }

    public static void setJpushId(String userId) {
        SysSharePres.getInstance().setJpushId(userId);
    }

    public static int getCache() {
        return SysSharePres.getInstance().getCacheSize();
    }

    public static void setCache(int size) {
        SysSharePres.getInstance().setCache(size);
    }

    public static int getNewsAnnouncementNum() {
        return SysSharePres.getInstance().getNewsAnnouncementNum();
    }

    public static void setNewsAnnouncementNum(int newsAnnouncementNum) {
        SysSharePres.getInstance().setNewsAnnouncementNum(newsAnnouncementNum);
    }

    public static int getNewsSystemInformationNum() {
        return SysSharePres.getInstance().getNewsSystemInformationNum();
    }

    public static void setNewsSystemInformationNum(int newsSystemInformationNum) {
        SysSharePres.getInstance().setNewsSystemInformationNum(newsSystemInformationNum);
    }

    public static int getNewsCommentNum() {
        return SysSharePres.getInstance().getNewsCommentNum();
    }

    public static void setNewsCommentNum(int newsCommentNum) {
        SysSharePres.getInstance().setNewsCommentNum(newsCommentNum);
    }

    public static int getNewsQaNum() {
        return SysSharePres.getInstance().getNewsQaNum();
    }

    public static void setNewsQaNum(int newsQaNum) {
        SysSharePres.getInstance().setNewsQaNum(newsQaNum);
    }

    public static int getNewsGroupNum() {
        return SysSharePres.getInstance().getNewsGroupNum();
    }

    public static void setNewsGroupNum(int newsGroupNum) {
        SysSharePres.getInstance().setNewsGroupNum(newsGroupNum);
    }

    public static int getNewsActivityNum() {
        return SysSharePres.getInstance().getNewsActivityNum();
    }

    public static void setNewsActivityNum(int newsActivityNum) {
        SysSharePres.getInstance().setNewsActivityNum(newsActivityNum);
    }

    public static int getNewsRecommendNum() {
        return SysSharePres.getInstance().getNewsRecommendNum();
    }

    public static void setNewsRecommendNum(int newsRecommendNum) {
        SysSharePres.getInstance().setNewsRecommendNum(newsRecommendNum);
    }

    public static int getNewsAccountInformationNum() {
        return SysSharePres.getInstance().getNewsAccountInformationNum();
    }

    public static void setNewsAccountInformationNum(int newsAccountInformationNum) {
        SysSharePres.getInstance().setNewsAccountInformationNum(newsAccountInformationNum);
    }

    public static String getCityCode() {
        return SysSharePres.getInstance().getCityCode();
    }

    public static void setCityCode(String cityCode) {
        SysSharePres.getInstance().setCityCode(cityCode);
    }

    public static String getCustPoints(){
       return SysSharePres.getInstance().getCustPoint();
    }

    public static void setCustPoints(String custPoints) {
        SysSharePres.getInstance().setCustPoint(custPoints);
    }

    public static String getCustRole(){
        return SysSharePres.getInstance().getCustRole();
    }

    public static void setCustRole(String custRole) {
        SysSharePres.getInstance().setCustRole(custRole);
    }

    public static boolean isNeedTeam() {
        return SysSharePres.getInstance().isNeedTeam();
    }

    public static void setNeedTeam(boolean needTeam) {
        SysSharePres.getInstance().setNeedTeam(needTeam);
    }


    public static void setNowAct(String act) {
        SysSharePres.getInstance().setNowAct(act);
    }


    public static String getNowAct() {
        return SysSharePres.getInstance().getNowAct();
    }

    /**
     * 清除数据
     */
    public static void clearShareP() {
        Session.setUserIsLogin(false);
        Session.setIsOpenGesture(false);
        Session.setUserSetpaypw(false);
        Session.setUserPicUrl("");
//        Session.setCancelRemingLogin(false);
//        Session.setCareFriendsDate("");
//        Session.setClickCancel(false);
//        Session.setContactsdate("");
        Session.setEasyPay(false);
//        Session.setLoginWay(-1);
        Session.setSessionKey("");
        Session.setOrderserialno("");
//        Session.setUpdateApp(false);
        Session.setUser_area("");
        Session.setUser_desc("");
        Session.setUser_sex("");
        Session.setUser_signatrue("");
        Session.setUserAccount("");
//        Session.setUserFirstInfoDetail(false);
//        Session.setUserFirstRemindAuth(false);
        if (!TextUtils.isEmpty(Session.getUserId()))
            Session.setBeforeLoginId(Session.getUserId());
        Session.setUserId("");
        Session.setUserLocalUrl("");
        Session.setUserMoney("");
        Session.setUserMoneyState(false);
        Session.setUserName("");
        Session.setIsAwaysShow("");
        Session.setAdv_expireDate("");
        Session.setAdv_upIngDate("");
        Session.setAdv_url("");
        Session.setAdv_pic_url("");
        //     Session.setFriTreadUnread(0);
        //       Session.setMyTreadUnread(0);
//        Session.setUserPhone("");
        Session.setLastPlayVideoUrl("");
        Session.setLastPlayVideoPosition(0);
        Session.setSinaAccessToken("");
        Session.setMyTreadUnread(0);
        Session.setFriTreadUnread(0);
        Session.setAccount_safe_unread(0);
        Session.setSystem_message_unread(0);
        Session.setUserFocusNum("");
        Session.setSinaAccessToken("");
        Session.setSinaRefreshAccessToken("");
        Session.setOpenId("");
        Session.setCityCode("");

    }

    //版本更新時清理数据
    public static void clearMust() {
        Session.getUserIsFirstDownload();
        Session.setIsAwaysShow("");
        Session.setRecordIndex("");
    }
}
