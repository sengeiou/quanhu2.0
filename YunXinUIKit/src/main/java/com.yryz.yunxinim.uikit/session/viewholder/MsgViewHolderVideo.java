package com.yryz.yunxinim.uikit.session.viewholder;

import com.yryz.yunxinim.uikit.R;
import com.yryz.yunxinim.uikit.common.util.media.BitmapDecoder;
import com.yryz.yunxinim.uikit.session.activity.WatchVideoActivity;
import com.netease.nimlib.sdk.msg.attachment.VideoAttachment;

/**
 * Created by zhoujianghua on 2015/8/5.
 */
public class MsgViewHolderVideo extends MsgViewHolderThumbBase {

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_video;
    }

    @Override
    protected void onItemClick() {
        WatchVideoActivity.start(context, message);
    }

    @Override
    protected String thumbFromSourceFile(String path) {
        VideoAttachment attachment = (VideoAttachment) message.getAttachment();
        String thumb = attachment.getThumbPathForSave();
        return BitmapDecoder.extractThumbnail(path, thumb) ? thumb : null;
    }
}
