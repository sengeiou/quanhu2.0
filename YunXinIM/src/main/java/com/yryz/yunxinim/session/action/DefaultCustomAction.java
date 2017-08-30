package com.yryz.yunxinim.session.action;

import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.yryz.yunxinim.R;
import com.yryz.yunxinim.session.extension.DefaultCustomAttachment;
import com.yryz.yunxinim.uikit.session.actions.BaseAction;

import java.util.HashMap;
import java.util.Map;

/**
 * Tip类型消息测试
 * Created by hzxuwen on 2016/3/9.
 */
public class DefaultCustomAction extends BaseAction {

    public DefaultCustomAction() {
        super(R.drawable.message_plus_tip_selector, R.string.input_panel_tip);
    }

    @Override
    public void onClick() {
        DefaultCustomAttachment attachment = new DefaultCustomAttachment();
        IMMessage msg = MessageBuilder.createCustomMessage(getAccount(), getSessionType(), attachment);
        Map<String, Object> data = new HashMap<>();
        data.put("key1", "ext data");
        data.put("key2", 2015);
        msg.setRemoteExtension(data); // 设置服务器扩展字段

        CustomMessageConfig config = new CustomMessageConfig();
        config.enablePush = false; // 不推送
        msg.setConfig(config);

        sendMessage(msg);
    }
}
