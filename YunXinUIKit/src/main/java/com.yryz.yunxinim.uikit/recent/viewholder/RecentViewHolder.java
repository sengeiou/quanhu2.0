package com.yryz.yunxinim.uikit.recent.viewholder;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.team.model.Team;
import com.yryz.yunxinim.uikit.NimUIKit;
import com.yryz.yunxinim.uikit.R;
import com.yryz.yunxinim.uikit.cache.TeamDataCache;
import com.yryz.yunxinim.uikit.common.adapter.TViewHolder;
import com.yryz.yunxinim.uikit.common.ui.drop.DropFake;
import com.yryz.yunxinim.uikit.common.ui.drop.DropManager;
import com.yryz.yunxinim.uikit.common.ui.imageview.HeadImageView;
import com.yryz.yunxinim.uikit.common.util.sys.ScreenUtil;
import com.yryz.yunxinim.uikit.common.util.sys.TimeUtil;
import com.yryz.yunxinim.uikit.recent.RecentContactsCallback;
import com.yryz.yunxinim.uikit.recent.RecentContactsFragment;
import com.yryz.yunxinim.uikit.session.constant.Extras;
import com.yryz.yunxinim.uikit.session.emoji.MoonUtil;
import com.yryz.yunxinim.uikit.uinfo.UserInfoHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class RecentViewHolder extends TViewHolder implements OnClickListener {

    protected FrameLayout portraitPanel;

    protected HeadImageView imgHead;

    protected TextView tvNickname;

    protected TextView tvMessage;

    protected TextView tvDatetime;

    // 消息发送错误状态标记，目前没有逻辑处理
    protected ImageView imgMsgStatus;

    protected RecentContact recent;

    protected View bottomLine;

    protected View topLine;

    // 未读红点（一个占坑，一个全屏动画）
    protected DropFake tvUnread;

    private ImageView imgUnreadExplosion;

    protected abstract String getContent();

    public void refresh(Object item) {
        boolean flag = recent != null && recent.getUnreadCount() > 0;

        recent = (RecentContact) item;

        flag = flag && recent.getUnreadCount() == 0; // 未读数从N->0执行爆裂动画

        updateBackground();

        loadPortrait();

        updateNickLabel(UserInfoHelper.getUserTitleName(recent.getContactId(), recent.getSessionType()));

        updateMsgLabel();

        updateNewIndicator();

        if (flag) {
            Object o = DropManager.getInstance().getCurrentId();
            if (o instanceof String && o.equals("0")) {
                imgUnreadExplosion.setImageResource(R.drawable.explosion);
                imgUnreadExplosion.setVisibility(View.VISIBLE);
                ((AnimationDrawable) imgUnreadExplosion.getDrawable()).start();
            }
        }
    }

    public void refreshCurrentItem() {
        if (recent != null) {
            refresh(recent);
        }
    }

    private void updateBackground() {
        topLine.setVisibility(isFirstItem() ? View.GONE : View.VISIBLE);
        bottomLine.setVisibility(isLastItem() ? View.VISIBLE : View.GONE);
        if ((recent.getTag() & RecentContactsFragment.RECENT_TAG_STICKY) == 0) {
            view.setBackgroundResource(R.drawable.nim_list_item_selector);
        } else {
            view.setBackgroundResource(R.drawable.nim_recent_contact_sticky_selecter);
        }
    }

    protected void loadPortrait() {
        // 设置头像
        if (recent.getSessionType() == SessionTypeEnum.P2P) {
            imgHead.loadBuddyAvatar(recent.getContactId());
        } else if (recent.getSessionType() == SessionTypeEnum.Team) {
            Team team = TeamDataCache.getInstance().getTeamById(recent.getContactId());
            imgHead.loadTeamIconByTeam(team);
        }
    }

    private void updateNewIndicator() {
        int unreadNum = recent.getUnreadCount();
        tvUnread.setVisibility(unreadNum > 0 ? View.VISIBLE : View.GONE);
        tvUnread.setText(unreadCountShowRule(unreadNum));
    }

    private void updateMsgLabel() {
        // 显示消息具体内容
        MoonUtil.identifyFaceExpressionAndTags(context, tvMessage, getContent(), ImageSpan.ALIGN_BOTTOM, 0.45f);

        if (recent.getExtension() != null
                && recent.getExtension().containsKey(Extras.EXTRA_TYPE)
                && (boolean) recent.getExtension().get(Extras.EXTRA_TYPE)
                && recent.getUnreadCount() > 0) {
            SpannableString spannableString = new SpannableString(context.getString(R.string.viewholder_at));
            spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, 7,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//把固定字符串转换为imagespan
            CharSequence msg = tvMessage.getText();
            tvMessage.setText(spannableString);
            tvMessage.append(msg);
        }

        if (getContent().startsWith("[红包] ") || getContent().contains("[红包] ")) {
            SpannableString spannableString = new SpannableString(getContent().substring(getContent().indexOf("[红包] ")));
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#ff6060")), 0, 4,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//把固定字符串转换为imagespan
            tvMessage.setText(spannableString);
        }

        MsgStatusEnum status = recent.getMsgStatus();
        switch (status) {
            case fail:
                imgMsgStatus.setImageResource(R.drawable.nim_g_ic_failed_small);
                imgMsgStatus.setVisibility(View.VISIBLE);
                break;
            case sending:
                imgMsgStatus.setImageResource(R.drawable.nim_recent_contact_ic_sending);
                imgMsgStatus.setVisibility(View.VISIBLE);
                break;
            default:
                imgMsgStatus.setVisibility(View.GONE);
                break;
        }

        String timeString = TimeUtil.getTimeShowString(recent.getTime(), true);
        tvDatetime.setText(timeString);
    }

    protected void updateNickLabel(String nick) {
        int labelWidth = ScreenUtil.screenWidth;
        labelWidth -= ScreenUtil.dip2px(50 + 70); // 减去固定的头像和时间宽度

        if (labelWidth > 0) {
            tvNickname.setMaxWidth(labelWidth);
        }

        tvNickname.setText(nick);
    }

    protected int getResId() {
        return R.layout.nim_recent_contact_list_item;
    }

    public void inflate() {
        this.portraitPanel = (FrameLayout) view.findViewById(R.id.portrait_panel);
        this.imgHead = (HeadImageView) view.findViewById(R.id.img_head);
        this.tvNickname = (TextView) view.findViewById(R.id.tv_nickname);
        this.tvMessage = (TextView) view.findViewById(R.id.tv_message);
        this.tvUnread = (DropFake) view.findViewById(R.id.unread_number_tip);
        this.imgUnreadExplosion = (ImageView) view.findViewById(R.id.unread_number_explosion);
        this.tvDatetime = (TextView) view.findViewById(R.id.tv_date_time);
        this.imgMsgStatus = (ImageView) view.findViewById(R.id.img_msg_status);
        this.bottomLine = view.findViewById(R.id.bottom_line);
        this.topLine = view.findViewById(R.id.top_line);

        this.tvUnread.setClickListener(new DropFake.ITouchListener() {
            @Override
            public void onDown() {
                DropManager.getInstance().setCurrentId(recent);
                DropManager.getInstance().getDropCover().down(tvUnread, tvUnread.getText());
            }

            @Override
            public void onMove(float curX, float curY) {
                DropManager.getInstance().getDropCover().move(curX, curY);
            }

            @Override
            public void onUp() {
                DropManager.getInstance().getDropCover().up();
            }
        });
    }

    protected String unreadCountShowRule(int unread) {
//        unread = Math.min(unread, 99);
        return unread > 99 ? "99+" : String.valueOf(unread);
    }

    protected RecentContactsCallback getCallback() {
        return ((RecentContactAdapter) getAdapter()).getCallback();
    }

    @Override
    public void onClick(View v) {

    }
}
