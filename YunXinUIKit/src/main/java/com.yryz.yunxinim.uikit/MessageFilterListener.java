package com.yryz.yunxinim.uikit;

import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;

/**
 * Created by rzw2 on 2017/2/28.
 */

public interface MessageFilterListener {
    boolean messageFilter(MsgAttachment recent);
}
