package com.yryz.yunxinim.uikit.recent.viewholder;

import android.text.TextUtils;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.yryz.yunxinim.uikit.NimUIKit;
import com.yryz.yunxinim.uikit.cache.TeamDataCache;
import com.netease.nimlib.sdk.msg.attachment.NotificationAttachment;
import com.yryz.yunxinim.uikit.session.constant.Extras;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TeamRecentViewHolder extends CommonRecentViewHolder {

    @Override
    protected String getContent() {
        String content = descOfMsg();

        String fromId = recent.getFromAccount();
        if (!TextUtils.isEmpty(fromId)
                && !fromId.equals(NimUIKit.getAccount())
                && recent.getMsgType() != MsgTypeEnum.custom) {
            String tid = recent.getContactId();
            String teamNick = getTeamUserDisplayName(tid, fromId);
            content = teamNick + ": " + content;
        }

        if (TextUtils.isEmpty(content)) {
            String msgId = recent.getRecentMessageId();
            List<String> uuids = new ArrayList<>(1);
            uuids.add(msgId);
            List<IMMessage> msgs = NIMClient.getService(MsgService.class).queryMessageListByUuidBlock(uuids);
            if (msgs != null && !msgs.isEmpty()) {
                IMMessage msg = msgs.get(0);
                content = msg.getContent();
            }
        }

        return content;
    }

    private String getTeamUserDisplayName(String tid, String account) {
        return TeamDataCache.getInstance().getTeamMemberDisplayName(tid, account);
    }

}
