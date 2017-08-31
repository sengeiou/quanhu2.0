package com.yryz.yunxinim.session.viewholder;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.yryz.yunxinim.R;
import com.yryz.yunxinim.session.extension.LuckyAttachment;
import com.yryz.yunxinim.uikit.common.util.sys.ScreenUtil;
import com.yryz.yunxinim.uikit.session.viewholder.MsgViewHolderBase;

/**
 * Created by zhoujianghua on 2015/8/4.
 */
public class MsgViewHolderLucky extends MsgViewHolderBase {
    private TextView mTvTitle;
    private TextView mTvDesc;

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_lucky;
    }

    @Override
    protected void inflateContentView() {
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mTvDesc = (TextView) view.findViewById(R.id.tv_desc);
    }

    @Override
    protected void bindContentView() {
        LuckyAttachment attachment = ((LuckyAttachment) message.getAttachment());

        if (attachment == null) {
            return;
        }

        layoutDirection();

        mTvTitle.setText(attachment.getTitle());
        mTvDesc.setText(isReceivedMessage() ? "领取红包" : "查看红包");
    }

    private void layoutDirection() {
        View view = findViewById(R.id.layout_content);

        int width = ScreenUtil.getDisplayWidth() - ScreenUtil.dip2px(52) * 2;
        int height = ScreenUtil.dip2px(87);

        setLayoutParams(width, height, view);

        if (isReceivedMessage()) {
            view.setBackgroundResource(R.drawable.bg_lucky_other);
        } else {
            view.setBackgroundResource(R.drawable.bg_lucky_my);
        }
    }

    @Override
    protected void onItemClick() {
        LuckyAttachment attachment = ((LuckyAttachment) message.getAttachment());
        attachment.checkData();
        Intent intent = new Intent("com.rz.yryz.LUCKY_MONEY_IM");
        intent.putExtra("redbag_id", attachment.getId());
        intent.putExtra("lucky_message", attachment.getTitle());
        intent.putExtra("account_id", attachment.getUserId());
        intent.putExtra("account_name", attachment.getUserName());
        intent.putExtra("lucky_type", attachment.getLuckyType());
        intent.putExtra("account_head", attachment.getHeadUrl());
        intent.putExtra("scene", attachment.getScene());
        intent.putExtra("recentId", attachment.getRecentId());
        context.startActivity(intent);
    }

    @Override
    protected int leftBackground() {
        return 0;
    }

    @Override
    protected int rightBackground() {
        return 0;
    }

}
