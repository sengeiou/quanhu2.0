package com.yryz.yunxinim.uikit.session.viewholder;

import com.yryz.yunxinim.uikit.R;
import com.yryz.yunxinim.uikit.session.activity.WatchMessagePictureActivity;

/**
 * Created by zhoujianghua on 2015/8/4.
 */
public class MsgViewHolderPicture extends MsgViewHolderThumbBase {

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_picture;
    }

    @Override
    protected void onItemClick() {
        WatchMessagePictureActivity.start(context, message);
    }

    @Override
    protected String thumbFromSourceFile(String path) {
        return path;
    }
}
