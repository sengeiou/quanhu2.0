package com.yryz.yunxinim.session.viewholder;

import android.widget.TextView;

import com.yryz.yunxinim.R;
import com.yryz.yunxinim.session.extension.RTSAttachment;
import com.yryz.yunxinim.uikit.session.viewholder.MsgViewHolderBase;

public class MsgViewHolderRTS extends MsgViewHolderBase {

    private TextView textView;

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_rts;
    }

    @Override
    protected void inflateContentView() {
        textView = (TextView) view.findViewById(R.id.rts_text);
    }

    @Override
    protected void bindContentView() {
        RTSAttachment attachment = (RTSAttachment) message.getAttachment();
        textView.setText(attachment.getContent());
    }

    @Override
    protected int leftBackground() {
        return R.drawable.nim_message_left_white_bg;
    }

    @Override
    protected int rightBackground() {
        return R.drawable.nim_message_right_blue_bg;
    }
}

