package com.yryz.yunxinim.uikit.session.helper;

import com.yryz.yunxinim.uikit.NimUIKit;
import com.yryz.yunxinim.uikit.R;
import com.yryz.yunxinim.uikit.cache.TeamDataCache;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;

/**
 * Created by hzxuwen on 2016/8/19.
 */
public class MessageHelper {

    public static MessageHelper getInstance() {
        return InstanceHolder.instance;
    }

    static class InstanceHolder {
        final static MessageHelper instance = new MessageHelper();
    }

    // 消息撤回
    public void onRevokeMessage(IMMessage item) {
        if (item == null) {
            return;
        }

        IMMessage message = MessageBuilder.createTipMessage(item.getSessionId(), item.getSessionType());
        String nick = "";
        if (item.getSessionType() == SessionTypeEnum.Team) {
            nick = TeamDataCache.getInstance().getTeamMemberDisplayNameYou(item.getSessionId(), item.getFromAccount());
        } else if (item.getSessionType() == SessionTypeEnum.P2P) {
            nick = item.getFromAccount().equals(NimUIKit.getAccount()) ? NimUIKit.getContext().getString(R.string.default_you) : NimUIKit.getContext().getString(R.string.other_side);
        }
        message.setContent(nick + NimUIKit.getContext().getString(R.string.withdraw_the_message));
        message.setStatus(MsgStatusEnum.success);
        CustomMessageConfig config = new CustomMessageConfig();
        config.enableUnreadCount = false;
        message.setConfig(config);
        NIMClient.getService(MsgService.class).saveMessageToLocalEx(message, true, item.getTime());
    }
}
