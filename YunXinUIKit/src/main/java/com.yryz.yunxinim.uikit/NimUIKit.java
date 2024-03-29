package com.yryz.yunxinim.uikit;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.yryz.yunxinim.uikit.cache.DataCacheManager;
import com.yryz.yunxinim.uikit.cache.TeamDataCache;
import com.yryz.yunxinim.uikit.common.util.log.LogUtil;
import com.yryz.yunxinim.uikit.common.util.storage.StorageType;
import com.yryz.yunxinim.uikit.common.util.storage.StorageUtil;
import com.yryz.yunxinim.uikit.common.util.sys.ScreenUtil;
import com.yryz.yunxinim.uikit.contact.ContactEventListener;
import com.yryz.yunxinim.uikit.contact.ContactProvider;
import com.yryz.yunxinim.uikit.contact_selector.activity.ContactSelectActivity;
import com.yryz.yunxinim.uikit.session.SessionCustomization;
import com.yryz.yunxinim.uikit.session.SessionEventListener;
import com.yryz.yunxinim.uikit.session.activity.P2PMessageActivity;
import com.yryz.yunxinim.uikit.session.activity.TeamMessageActivity;
import com.yryz.yunxinim.uikit.session.emoji.StickerManager;
import com.yryz.yunxinim.uikit.session.module.MsgForwardFilter;
import com.yryz.yunxinim.uikit.session.module.MsgRevokeFilter;
import com.yryz.yunxinim.uikit.session.viewholder.MsgViewHolderBase;
import com.yryz.yunxinim.uikit.session.viewholder.MsgViewHolderFactory;
import com.yryz.yunxinim.uikit.team.activity.AdvancedTeamInfoActivity;
import com.yryz.yunxinim.uikit.team.activity.NormalTeamInfoActivity;
import com.yryz.yunxinim.uikit.uinfo.UserInfoHelper;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;

import java.util.List;

/**
 * UIKit能力输出类。
 */
public final class NimUIKit {

    // context
    private static Context context;

    // 自己的用户帐号
    private static String account;

    // 用户信息提供者
    private static UserInfoProvider userInfoProvider;

    // 通讯录信息提供者
    private static ContactProvider contactProvider;

    // 地理位置信息提供者
    private static LocationProvider locationProvider;

    // 图片加载、缓存与管理组件
    private static ImageLoaderKit imageLoaderKit;

    // 会话窗口消息列表一些点击事件的响应处理函数
    private static SessionEventListener sessionListener;

    // 通讯录列表一些点击事件的响应处理函数
    private static ContactEventListener contactEventListener;

    // 转发消息过滤器
    private static MsgForwardFilter msgForwardFilter;

    // 撤回消息过滤器
    private static MsgRevokeFilter msgRevokeFilter;

    // 自定义推送配置
    private static CustomPushContentProvider customPushContentProvider;

    // 特定消息筛选
    private static MessageFilterListener messageFilterListener;

    /**
     * 初始化UIKit，须传入context以及用户信息提供者
     *
     * @param context          上下文
     * @param userInfoProvider 用户信息提供者
     * @param contactProvider  通讯录信息提供者
     */
    public static void init(Context context, UserInfoProvider userInfoProvider, ContactProvider contactProvider) {
        NimUIKit.context = context.getApplicationContext();
        NimUIKit.userInfoProvider = userInfoProvider;
        NimUIKit.contactProvider = contactProvider;
        NimUIKit.imageLoaderKit = new ImageLoaderKit(context, null);

        // init data cache
        LoginSyncDataStatusObserver.getInstance().registerLoginSyncDataStatus(true);  // 监听登录同步数据完成通知
        DataCacheManager.observeSDKDataChanged(true);
        if (!TextUtils.isEmpty(getAccount())) {
            DataCacheManager.buildDataCache(); // build data cache on auto login
        }

        // init tools
        StorageUtil.init(context, null);
        ScreenUtil.init(context);
        StickerManager.getInstance().init();

        // init log
        String path = StorageUtil.getDirectoryByDirType(StorageType.TYPE_LOG);
        LogUtil.init(path, Log.DEBUG);
    }

    /**
     * 释放缓存，一般在注销时调用
     */
    public static void clearCache() {
        DataCacheManager.clearDataCache();
    }

    /**
     * 打开一个聊天窗口，开始聊天
     *
     * @param context       上下文
     * @param id            聊天对象ID（用户帐号account或者群组ID）
     * @param sessionType   会话类型
     * @param customization 定制化信息。针对不同的聊天对象，可提供不同的定制化。
     * @param anchor        跳转到指定消息的位置，不需要跳转填null
     */
    public static void startChatting(Context context, String id, SessionTypeEnum sessionType, SessionCustomization
            customization, IMMessage anchor) {
        if (sessionType == SessionTypeEnum.P2P) {
            P2PMessageActivity.start(context, id, customization, anchor);
        } else if (sessionType == SessionTypeEnum.Team) {
            TeamMessageActivity.start(context, id, customization, null, anchor);
        }
    }

    /**
     * 打开一个聊天窗口（用于从聊天信息中创建群聊时，打开群聊）
     *
     * @param context       上下文
     * @param id            聊天对象ID（用户帐号account或者群组ID）
     * @param sessionType   会话类型
     * @param customization 定制化信息。针对不同的聊天对象，可提供不同的定制化。
     * @param backToClass   返回的指定页面
     * @param anchor        跳转到指定消息的位置，不需要跳转填null
     */
    public static void startChatting(Context context, String id, SessionTypeEnum sessionType, SessionCustomization customization,
                                     Class<? extends Activity> backToClass, IMMessage anchor) {
        if (sessionType == SessionTypeEnum.Team) {
            TeamMessageActivity.start(context, id, customization, backToClass, anchor);
        }
    }

    /**
     * 打开联系人选择器
     *
     * @param context     上下文（Activity）
     * @param option      联系人选择器可选配置项
     * @param requestCode startActivityForResult使用的请求码
     */
    public static void startContactSelect(Context context, ContactSelectActivity.Option option, int requestCode) {
        ContactSelectActivity.startActivityForResult(context, option, requestCode);
    }

    /**
     * 打开讨论组或高级群资料页
     *
     * @param context 上下文
     * @param teamId  群id
     */
    public static void startTeamInfo(Context context, String teamId) {
        Team team = TeamDataCache.getInstance().getTeamById(teamId);
        if (team == null) {
            return;
        }
        if (team.getType() == TeamTypeEnum.Advanced) {
            AdvancedTeamInfoActivity.start(context, teamId); // 启动固定群资料页
        } else if (team.getType() == TeamTypeEnum.Normal) {
            NormalTeamInfoActivity.start(context, teamId); // 启动讨论组资料页
        }
    }

    public static Context getContext() {
        return context;
    }

    public static String getAccount() {
        return account;
    }

    public static UserInfoProvider getUserInfoProvider() {
        return userInfoProvider;
    }

    public static ContactProvider getContactProvider() {
        return contactProvider;
    }

    public static LocationProvider getLocationProvider() {
        return locationProvider;
    }

    public static ImageLoaderKit getImageLoaderKit() {
        return imageLoaderKit;
    }

    public static void setLocationProvider(LocationProvider locationProvider) {
        NimUIKit.locationProvider = locationProvider;
    }

    /**
     * 根据消息附件类型注册对应的消息项展示ViewHolder
     *
     * @param attach     附件类型
     * @param viewHolder 消息ViewHolder
     */
    public static void registerMsgItemViewHolder(Class<? extends MsgAttachment> attach, Class<? extends MsgViewHolderBase> viewHolder) {
        MsgViewHolderFactory.register(attach, viewHolder);
    }

    /**
     * 注册Tip类型消息项展示ViewHolder
     *
     * @param viewHolder Tip消息ViewHolder
     */
    public static void registerTipMsgViewHolder(Class<? extends MsgViewHolderBase> viewHolder) {
        MsgViewHolderFactory.registerTipMsgViewHolder(viewHolder);
    }

    /**
     * 设置当前登录用户的帐号
     *
     * @param account 帐号
     */
    public static void setAccount(String account) {
        NimUIKit.account = account;
    }

    /**
     * 获取聊天界面事件监听器
     *
     * @return
     */
    public static SessionEventListener getSessionListener() {
        return sessionListener;
    }

    /**
     * 设置聊天界面的事件监听器
     *
     * @param sessionListener
     */
    public static void setSessionListener(SessionEventListener sessionListener) {
        NimUIKit.sessionListener = sessionListener;
    }

    /**
     * 获取通讯录列表的事件监听器
     *
     * @return
     */
    public static ContactEventListener getContactEventListener() {
        return contactEventListener;
    }

    /**
     * 设置通讯录列表的事件监听器
     *
     * @param contactEventListener
     */
    public static void setContactEventListener(ContactEventListener contactEventListener) {
        NimUIKit.contactEventListener = contactEventListener;
    }

    /**
     * 当用户资料发生改动时，请调用此接口，通知更新UI
     *
     * @param accounts 有用户信息改动的帐号列表
     */
    public static void notifyUserInfoChanged(List<String> accounts) {
        UserInfoHelper.notifyChanged(accounts);
    }

    /**
     * 设置转发消息过滤的监听器
     *
     * @param msgForwardFilter
     */
    public static void setMsgForwardFilter(MsgForwardFilter msgForwardFilter) {
        NimUIKit.msgForwardFilter = msgForwardFilter;
    }

    /**
     * 获取转发消息过滤的监听器
     *
     * @return
     */
    public static MsgForwardFilter getMsgForwardFilter() {
        return msgForwardFilter;
    }

    /**
     * 设置消息撤回的监听器
     *
     * @param msgRevokeFilter
     */
    public static void setMsgRevokeFilter(MsgRevokeFilter msgRevokeFilter) {
        NimUIKit.msgRevokeFilter = msgRevokeFilter;
    }

    /**
     * 获取消息撤回的监听器
     *
     * @return
     */
    public static MsgRevokeFilter getMsgRevokeFilter() {
        return msgRevokeFilter;
    }

    /**
     * @return
     */
    public static CustomPushContentProvider getCustomPushContentProvider() {
        return customPushContentProvider;
    }

    /**
     * 注册自定义推送文案
     *
     * @return
     */
    public static void CustomPushContentProvider(CustomPushContentProvider mixPushCustomConfig) {
        NimUIKit.customPushContentProvider = mixPushCustomConfig;
    }

    public static MessageFilterListener getMessageFilterListener() {
        return messageFilterListener;
    }

    public static void setMessageFilterListener(MessageFilterListener messageFilterListener) {
        NimUIKit.messageFilterListener = messageFilterListener;
    }
}
