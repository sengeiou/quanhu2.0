package com.yryz.yunxinim.uikit.team.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yryz.yunxinim.uikit.NimUIKit;
import com.yryz.yunxinim.uikit.R;
import com.yryz.yunxinim.uikit.cache.TeamDataCache;
import com.yryz.yunxinim.uikit.common.adapter.TViewHolder;
import com.yryz.yunxinim.uikit.common.ui.imageview.HeadImageView;
import com.yryz.yunxinim.uikit.team.adapter.TeamMemberAdapter;

public class TeamMemberSwitchHolder extends TViewHolder {

    private HeadImageView headImageView;

    private TextView nameTextView;

    private TeamMemberAdapter.TeamMemberItem memberItem;

    protected TeamMemberAdapter getAdapter() {
        return (TeamMemberAdapter) super.getAdapter();
    }

    @Override
    protected int getResId() {
        return R.layout.nim_team_member_switch_item;
    }

    @Override
    protected void inflate() {
        headImageView = (HeadImageView) view.findViewById(R.id.imageViewHeader);
        nameTextView = (TextView) view.findViewById(R.id.textViewName);
    }

    @Override
    protected void refresh(Object item) {
        memberItem = (TeamMemberAdapter.TeamMemberItem) item;
        headImageView.resetImageView();

        // show team member
        refreshTeamMember(memberItem, false);
    }

    private void refreshTeamMember(final TeamMemberAdapter.TeamMemberItem item, boolean deleteMode) {
        nameTextView.setText(TeamDataCache.getInstance().getTeamMemberDisplayName(item.getTid(), item.getAccount()));
        headImageView.loadBuddyAvatar(item.getAccount());
    }

}
