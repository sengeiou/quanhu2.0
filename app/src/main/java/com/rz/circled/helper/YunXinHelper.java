package com.rz.circled.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.netease.nimlib.sdk.msg.MessageNotifierCustomization;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.rz.circled.R;
import com.yryz.yunxinim.session.extension.LuckyAttachment;
import com.yryz.yunxinim.uikit.ImageLoaderKit;
import com.yryz.yunxinim.uikit.NimUIKit;
import com.yryz.yunxinim.uikit.cache.FriendDataCache;
import com.yryz.yunxinim.uikit.cache.NimUserInfoCache;
import com.yryz.yunxinim.uikit.cache.TeamDataCache;
import com.yryz.yunxinim.uikit.contact.ContactProvider;
import com.yryz.yunxinim.uikit.session.constant.Extras;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by rzw2 on 2017/9/16.
 */

public class YunXinHelper {

    private static volatile YunXinHelper sInst = null;  // <<< 这里添加了 volatile

    public static YunXinHelper getInstance(Context context) {
        YunXinHelper inst = sInst;  // <<< 在这里创建临时变量
        if (inst == null) {
            synchronized (YunXinHelper.class) {
                inst = sInst;
                if (inst == null) {
                    inst = new YunXinHelper(context);
                    sInst = inst;
                }
            }
        }
        return inst;  // <<< 注意这里返回的是临时变量
    }

    private Context context;

    private YunXinHelper(Context context) {
        this.context = context;
    }

    public UserInfoProvider getInfoProvider() {
        return infoProvider;
    }

    public ContactProvider getContactProvider() {
        return contactProvider;
    }

    public MessageNotifierCustomization getMessageNotifierCustomization() {
        return messageNotifierCustomization;
    }

    private UserInfoProvider infoProvider = new UserInfoProvider() {
        @Override
        public UserInfo getUserInfo(String account) {
            UserInfo user = NimUserInfoCache.getInstance().getUserInfo(account);
            if (user == null) {
                NimUserInfoCache.getInstance().getUserInfoFromRemote(account, null);
            }

            return user;
        }

        @Override
        public int getDefaultIconResId() {
            return R.mipmap.ic_default_avatar_big;
        }

        @Override
        public Bitmap getTeamIcon(String teamId) {
            /**
             * 注意：这里最好从缓存里拿，如果读取本地头像可能导致UI进程阻塞，导致通知栏提醒延时弹出。
             */
            // 从内存缓存中查找群头像
            Team team = TeamDataCache.getInstance().getTeamById(teamId);
            if (team != null) {
                Bitmap bm = ImageLoaderKit.getNotificationBitmapFromCache(team.getIcon());
                if (bm != null) {
                    return bm;
                }
            }

            // 默认图
            Drawable drawable = context.getResources().getDrawable(R.drawable.ic_default_avatar_team);
            if (drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable) drawable).getBitmap();
            }

            return null;
        }

        @Override
        public Bitmap getAvatarForMessageNotifier(String account) {
            /**
             * 注意：这里最好从缓存里拿，如果读取本地头像可能导致UI进程阻塞，导致通知栏提醒延时弹出。
             */
            UserInfo user = getUserInfo(account);
            return (user != null) ? ImageLoaderKit.getNotificationBitmapFromCache(user.getAvatar()) : null;
        }

        @Override
        public String getDisplayNameForMessageNotifier(String account, String sessionId, SessionTypeEnum sessionType) {
            String nick = null;
            if (sessionType == SessionTypeEnum.P2P) {
                nick = NimUserInfoCache.getInstance().getAlias(account);
            } else if (sessionType == SessionTypeEnum.Team) {
                nick = TeamDataCache.getInstance().getTeamNick(sessionId, account);
                if (TextUtils.isEmpty(nick)) {
                    nick = NimUserInfoCache.getInstance().getAlias(account);
                }
            }
            // 返回null，交给sdk处理。如果对方有设置nick，sdk会显示nick
            if (TextUtils.isEmpty(nick)) {
                return null;
            }

            return nick;
        }
    };

    private ContactProvider contactProvider = new ContactProvider() {
        @Override
        public List<UserInfoProvider.UserInfo> getUserInfoOfMyFriends() {
            List<NimUserInfo> nimUsers = NimUserInfoCache.getInstance().getAllUsersOfMyFriend();
            List<UserInfoProvider.UserInfo> users = new ArrayList<>(nimUsers.size());
            if (!nimUsers.isEmpty()) {
                users.addAll(nimUsers);
            }

            return users;
        }

        @Override
        public int getMyFriendsCount() {
            return FriendDataCache.getInstance().getMyFriendCounts();
        }

        @Override
        public String getUserDisplayName(String account) {
            return NimUserInfoCache.getInstance().getUserDisplayName(account);
        }
    };

    private MessageNotifierCustomization messageNotifierCustomization = new MessageNotifierCustomization() {
        @Override
        public String makeNotifyContent(String nick, IMMessage message) {

            switch (message.getMsgType()) {
                case text:
                    Map<String, Object> map = message.getRemoteExtension();
                    if (null != map && map.containsKey(Extras.EXTRA_AT_LIST)) {
                        List<String> atList = (List<String>) map.get(Extras.EXTRA_AT_LIST);
                        if (atList.contains(NimUIKit.getAccount())) {
                            return context.getString(R.string.app_had_at) + nick + ":" + message.getContent();
                        }
                    }
            }

            if (message.getAttachment() instanceof LuckyAttachment) {
                LuckyAttachment a = (LuckyAttachment) message.getAttachment();
                return context.getString(R.string.app_lucky_money) + a.getUserName() + ":" + a.getTitle();
            }

            return null; // 采用SDK默认文案
        }

        @Override
        public String makeTicker(String nick, IMMessage message) {
            return null; // 采用SDK默认文案
        }
    };
}
