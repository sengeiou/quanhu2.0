package com.rz.circled.helper;

import android.content.Context;

import com.rz.circled.constants.NewsTypeConstants;
import com.rz.circled.ui.activity.CommonH5Activity;
import com.rz.httpapi.bean.NewsBean;

/**
 * Created by rzw2 on 2017/9/13.
 */

public class NewsJumpHelper {
    /**
     * @param context
     * @param data
     */
    public static void startAcceptActivity(Context context, NewsBean data) {
        switch (data.getActionCode()) {
            case NewsTypeConstants.NONE:
                break;
            case NewsTypeConstants.ACCOUNT:
                break;
            case NewsTypeConstants.INTEGRAL:
                break;
            case NewsTypeConstants.MYCARD:
                break;
            case NewsTypeConstants.MYCOTERIE:
                break;
            case NewsTypeConstants.MYTALENT:
                break;
            case NewsTypeConstants.INNER_URL:
                break;
            case NewsTypeConstants.EXTERNAL_URL:
                CommonH5Activity.startCommonH5(context, "", data.getLink());
                break;
            case NewsTypeConstants.COTERIE_HOME:
                NewsH5JumpHelper.startGroupHome(context, data.getCircleRoute(), data.getCoterieId());
                break;
            case NewsTypeConstants.TOPIC_HOME:
                NewsH5JumpHelper.startTopicHome(context, data.getCircleRoute(), data.getResourceId());
                break;
            case NewsTypeConstants.REWARD_DETAIL:
                NewsH5JumpHelper.startRewardDetail(context, data.getResourceId());
                break;
            case NewsTypeConstants.COMMON_DETAIL:
                NewsH5JumpHelper.startResourceDetail(context, data.getCircleRoute(), data.getCoterieId(), data.getModuleEnum(), data.getResourceId());
                break;
            case NewsTypeConstants.APPLY_LIST:
                NewsH5JumpHelper.startMemberAudit(context, data.getCircleRoute(), data.getCoterieId());
                break;
            case NewsTypeConstants.ACTIVITY_SIGNUP:
                NewsH5JumpHelper.startSignActivity(context, data.getResourceId());
                break;
            case NewsTypeConstants.ACTIVITY_VOTE:
                NewsH5JumpHelper.startVoteActivity(context, data.getResourceId());
                break;
            case NewsTypeConstants.ACTIVITY_RESULT:
                NewsH5JumpHelper.startWinningScan(context, data.getResourceId());
                break;
        }
    }
}
