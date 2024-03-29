package com.yryz.yunxinim.uikit.session.fragment;

import android.widget.Toast;

import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.yryz.yunxinim.uikit.R;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.model.Team;

/**
 * Created by zhoujianghua on 2015/9/10.
 */
public class TeamMessageFragment extends MessageFragment {

    private Team team;

    @Override
    public boolean isAllowSendMessage(IMMessage message) {
        if (team == null || !team.isMyTeam()) {
            if (team.getType() == TeamTypeEnum.Advanced)
                Toast.makeText(getActivity(), R.string.team_send_message_not_allow, Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getActivity(), R.string.team_normal_send_message_not_allow, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
