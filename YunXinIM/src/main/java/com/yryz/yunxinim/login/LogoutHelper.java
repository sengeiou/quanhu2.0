package com.yryz.yunxinim.login;

import com.yryz.yunxinim.DemoCache;
import com.yryz.yunxinim.chatroom.helper.ChatRoomHelper;
import com.yryz.yunxinim.uikit.LoginSyncDataStatusObserver;
import com.yryz.yunxinim.uikit.NimUIKit;
import com.yryz.yunxinim.uikit.common.ui.drop.DropManager;

/**
 * 注销帮助类
 * Created by huangjun on 2015/10/8.
 */
public class LogoutHelper {
    public static void logout() {
        // 清理缓存&注销监听&清除状态
        NimUIKit.clearCache();
        ChatRoomHelper.logout();
        DemoCache.clear();
        LoginSyncDataStatusObserver.getInstance().reset();
        DropManager.getInstance().destroy();
    }
}
