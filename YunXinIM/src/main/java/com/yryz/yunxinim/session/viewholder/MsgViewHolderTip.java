package com.yryz.yunxinim.session.viewholder;

import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yryz.yunxinim.session.extension.LuckyAttachment;
import com.yryz.yunxinim.session.extension.LuckyTipAttachment;
import com.yryz.yunxinim.uikit.NimUIKit;
import com.yryz.yunxinim.uikit.session.emoji.MoonUtil;
import com.yryz.yunxinim.uikit.session.viewholder.MsgViewHolderBase;
import com.yryz.yunxinim.R;

import java.util.Map;

/**
 * Created by huangjun on 2015/11/25.
 * Tip类型消息ViewHolder
 */
public class MsgViewHolderTip extends MsgViewHolderBase {

    protected TextView notificationTextView;

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_notification;
    }

    @Override
    protected void inflateContentView() {
        notificationTextView = (TextView) view.findViewById(R.id.message_item_notification_label);
    }

    @Override
    protected void bindContentView() {
        String text = "未知通知提醒";
        if (TextUtils.isEmpty(message.getContent())) {
            Map<String, Object> content = message.getRemoteExtension();
            if (content != null && !content.isEmpty()) {
                text = (String) content.get("content");
            }
        } else {
            text = message.getContent();
        }

        if (message.getAttachment() instanceof LuckyTipAttachment && null != message.getAttachment()) {
            LuckyTipAttachment attachment = ((LuckyTipAttachment) message.getAttachment());
            readReceiptTextView.setVisibility(View.GONE);
            if (TextUtils.equals(attachment.getUserId(), NimUIKit.getAccount())) {
                if (!TextUtils.equals(attachment.getReceiveId(), NimUIKit.getAccount()))
                    text = attachment.getReceiveName() + "领取了你的红包";
                else
                    text = "你领取了自己的红包";
            } else if (TextUtils.equals(attachment.getReceiveId(), NimUIKit.getAccount())) {
                text = "你领取了" + attachment.getUserName() + "的红包";
            } else {
                text = message.getContent();
            }
        }

        handleTextNotification(text);
    }

    private void handleTextNotification(String text) {
        MoonUtil.identifyFaceExpressionAndATags(context, notificationTextView, text, ImageSpan.ALIGN_BOTTOM);
        notificationTextView.setMovementMethod(LinkMovementMethod.getInstance());

        if (message.getAttachment() instanceof LuckyTipAttachment) {

            final LuckyTipAttachment attachment = ((LuckyTipAttachment) message.getAttachment());

            if (attachment == null) {
                return;
            }

            SpannableString spannableString = new SpannableString(text);
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#ff5400")), text.length() - 2, text.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            notificationTextView.setText(spannableString);
            notificationTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent("com.rz.yryz.LUCKY_MONEY_DETAILS");
                    intent.putExtra("redbag_id", attachment.getId());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    protected boolean isMiddleItem() {
        return true;
    }
}
