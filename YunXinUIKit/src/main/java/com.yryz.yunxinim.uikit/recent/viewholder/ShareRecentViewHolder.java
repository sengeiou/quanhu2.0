package com.yryz.yunxinim.uikit.recent.viewholder;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.team.model.Team;
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

public class ShareRecentViewHolder extends TViewHolder {

    protected HeadImageView head;

    protected TextView name;

    protected View line;

    RelativeLayout headLayout;

    RecentContact recent;

    @Override
    protected int getResId() {
        return R.layout.nim_contacts_item;
    }

    @Override
    protected void inflate() {
        headLayout = (RelativeLayout) view.findViewById(R.id.head_layout);
        head = (HeadImageView) view.findViewById(R.id.contacts_item_head);
        name = (TextView) view.findViewById(R.id.contacts_item_name);
        line = view.findViewById(R.id.bottom_line);
    }

    @Override
    protected void refresh(Object item) {
        recent = (RecentContact) item;
        loadPortrait();
        updateNickLabel(UserInfoHelper.getUserTitleName(recent.getContactId(), recent.getSessionType()));
        if (isLastItem())
            line.setVisibility(View.GONE);
        else
            line.setVisibility(View.VISIBLE);
    }

    void loadPortrait() {
        // 设置头像
        if (recent.getSessionType() == SessionTypeEnum.P2P) {
            head.loadBuddyAvatar(recent.getContactId());
        } else if (recent.getSessionType() == SessionTypeEnum.Team) {
            Team team = TeamDataCache.getInstance().getTeamById(recent.getContactId());
            head.loadTeamIconByTeam(team);
        }
    }

    void updateNickLabel(String nick) {
        int labelWidth = ScreenUtil.screenWidth;
        labelWidth -= ScreenUtil.dip2px(50 + 70); // 减去固定的头像和时间宽度

        if (labelWidth > 0) {
            name.setMaxWidth(labelWidth);
        }

        name.setText(nick);
    }
}