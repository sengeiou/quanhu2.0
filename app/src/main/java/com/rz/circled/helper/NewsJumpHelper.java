package com.rz.circled.helper;

import android.content.Context;
import android.content.Intent;

import com.rz.circled.R;
import com.rz.circled.constants.NewsTypeConstants;
import com.rz.circled.ui.activity.AccountDetailAty;
import com.rz.circled.ui.activity.ChooseProveIdentityActivity;
import com.rz.circled.ui.activity.CommonH5Activity;
import com.rz.circled.ui.activity.MyCouponsActivity;
import com.rz.circled.ui.activity.MyPrivateGroupActivity;
import com.rz.circled.ui.activity.NewsAnnounceDetailsActivity;
import com.rz.circled.ui.activity.UserInfoActivity;
import com.rz.circled.ui.activity.WebContainerActivity;
import com.rz.common.constant.Type;
import com.rz.httpapi.bean.NewsBean;
import com.rz.httpapi.bean.ProveStatusBean;

/**
 * Created by rzw2 on 2017/9/13.
 */

public class NewsJumpHelper {

    /**
     * @param context
     * @param data
     */
    public static void startAcceptActivity(Context context, NewsBean data) {
        startAcceptActivity(context, data, -1);
    }

    /**
     * @param context
     * @param data
     */
    public static void startAcceptActivity(Context context, NewsBean data, int flag) {
        if (data.getActionCode() == null) return;
        switch (data.getActionCode()) {
            case NewsTypeConstants.NONE:
                break;
            case NewsTypeConstants.ACCOUNT:
                AccountDetailAty.startAccountDetail(context, Type.TYPE_BALANCE, flag);
                break;
            case NewsTypeConstants.INTEGRAL:
                AccountDetailAty.startAccountDetail(context, Type.TYPE_SCORE, flag);
                break;
            case NewsTypeConstants.MYCARD:
                MyCouponsActivity.startMyCoupons(context, flag);
                break;
            case NewsTypeConstants.MYCOTERIE:
                MyPrivateGroupActivity.startMyPrivateGroup(context, 0, flag);
                break;
            case NewsTypeConstants.MYTALENT:
                ChooseProveIdentityActivity.startProveIdentity(context, new ProveStatusBean(), flag);
                break;
            case NewsTypeConstants.INNER_URL:
                WebContainerActivity.startActivity(context, data.getLink(), flag);
                break;
            case NewsTypeConstants.EXTERNAL_URL:
                CommonH5Activity.startCommonH5(context, "", data.getLink(), flag);
                break;
            case NewsTypeConstants.ANNOUNCEMENT_URL:
                CommonH5Activity.startCommonH5(context, context.getString(R.string.announcement_details), data.getLink(), flag);
                break;
            case NewsTypeConstants.USER_INFO_URL:
                UserInfoActivity.newFrindInfo(context, (String) data.getBody().get("custId"), flag);
                break;
            case NewsTypeConstants.COTERIE_HOME:
                CommonH5JumpHelper.startGroupHome(context, data.getCircleRoute(), data.getCoterieId(), flag);
                break;
            case NewsTypeConstants.TOPIC_HOME:
                CommonH5JumpHelper.startTopicHome(context, data.getCircleRoute(), data.getCoterieId(), data.getModuleEnum(), data.getResourceId(), flag);
                break;
            case NewsTypeConstants.REWARD_DETAIL:
                CommonH5JumpHelper.startRewardDetail(context, data.getResourceId(), flag);
                break;
            case NewsTypeConstants.COMMON_DETAIL:
                CommonH5JumpHelper.startResourceDetail(context, data.getCircleRoute(), data.getCoterieId(), data.getModuleEnum(), data.getResourceId(), flag);
                break;
            case NewsTypeConstants.APPLY_LIST:
                CommonH5JumpHelper.startMemberAudit(context, data.getCircleRoute(), data.getCoterieId(), flag);
                break;
            case NewsTypeConstants.ACTIVITY_SIGNUP:
                CommonH5JumpHelper.startSignActivity(context, data.getResourceId(), flag);
                break;
            case NewsTypeConstants.ACTIVITY_VOTE:
                CommonH5JumpHelper.startVoteActivity(context, data.getResourceId(), flag);
                break;
            case NewsTypeConstants.ACTIVITY_RESULT:
                CommonH5JumpHelper.startWinningScan(context, data.getResourceId(), flag);
                break;
        }
    }
}
