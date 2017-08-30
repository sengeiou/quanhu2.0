package com.yryz.yunxinim.uikit.contact.core.provider;

import com.yryz.yunxinim.uikit.NimUIKit;
import com.yryz.yunxinim.uikit.UIKitLogTag;
import com.yryz.yunxinim.uikit.cache.FriendDataCache;
import com.yryz.yunxinim.uikit.common.util.log.LogUtil;
import com.yryz.yunxinim.uikit.contact.core.item.AbsContactItem;
import com.yryz.yunxinim.uikit.contact.core.item.ContactItem;
import com.yryz.yunxinim.uikit.contact.core.item.ItemTypes;
import com.yryz.yunxinim.uikit.contact.core.query.TextQuery;
import com.yryz.yunxinim.uikit.contact.core.util.ContactHelper;
import com.netease.nimlib.sdk.friend.model.Friend;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class UserDataProvider {
    public static final List<AbsContactItem> provide(TextQuery query) {
        List<UserInfoProvider.UserInfo> sources = query(query);
        List<AbsContactItem> items = new ArrayList<>(sources.size());
        for (UserInfoProvider.UserInfo u : sources) {
            items.add(new ContactItem(ContactHelper.makeContactFromUserInfo(u), ItemTypes.FRIEND));
        }

        LogUtil.i(UIKitLogTag.CONTACT, "contact provide data size =" + items.size());
        return items;
    }

    private static final List<UserInfoProvider.UserInfo> query(TextQuery query) {
        if (query != null) {
            List<UserInfoProvider.UserInfo> users = NimUIKit.getContactProvider().getUserInfoOfMyFriends();
            UserInfoProvider.UserInfo user;
            for (Iterator<UserInfoProvider.UserInfo> iter = users.iterator(); iter.hasNext(); ) {
                user = iter.next();
                Friend friend = FriendDataCache.getInstance().getFriendByAccount(user.getAccount());
                boolean hit = ContactSearch.hitUser(user, query) || (friend != null && ContactSearch.hitFriend(friend, query));
                if (!hit) {
                    iter.remove();
                }
            }
            return users;
        } else {
            return NimUIKit.getContactProvider().getUserInfoOfMyFriends();
        }
    }
}