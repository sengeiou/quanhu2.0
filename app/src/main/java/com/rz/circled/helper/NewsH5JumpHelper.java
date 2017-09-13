package com.rz.circled.helper;

import android.content.Context;

import com.rz.circled.ui.activity.WebContainerActivity;

import java.util.Formatter;

/**
 * Created by rzw2 on 2017/9/13.
 */

public class NewsH5JumpHelper {

    //私圈首页 /:sjmwq/coterie/:coterieId
    private static final String PRIVATE_GROUP_HOME = "/%s/coterie/%s";
    //话题主页 /:sjmwq/topic/home/:id
    private static final String TOPIC_HOME = "/%s/topic/home/%s";
    //悬赏详情页 	/activity/reward/detail/:id
    private static final String REWARD_DETAIL = "/activity/reward/detail/%s";
    //详情页 /:sjmwq/redirect/coterie/:coterieId/:moduleEnum/:id
    private static final String RESOURCE_DETAIL = "/%s/redirect/coterie/%s/%s/%s";
    //成员审核页 /:sjmwq/coterie/:coterieId/edit/applyList
    private static final String MEMBER_AUDIT = "/%s/coterie/%s/edit/applyList";
    //活动详情页：报名类 	/activity/platform-activity/signup/:activityId
    private static final String SIGN_ACTIVITY = "/activity/platform-activity/signup/%s";
    //活动详情页：投票类 /activity/platform-activity/vote/:activityId
    private static final String VOTE_ACTIVITY = "/activity/platform-activity/vote/%s";
    //投票活动中奖公布页 	/activity/platform-activity/vote/:activityId/tab/3
    private static final String WINNING_SCAN = "/activity/platform-activity/vote/%s/tab/3";

    private static Formatter formatter = new Formatter();

    public static void startGroupHome(Context context, String circleRoute, String coterieId) {
        String url = formatter.format(PRIVATE_GROUP_HOME, circleRoute, coterieId).toString();
        WebContainerActivity.startActivity(context, url);
    }

    public static void startTopicHome(Context context, String circleRoute, String resourceId) {
        String url = formatter.format(TOPIC_HOME, circleRoute, resourceId).toString();
        WebContainerActivity.startActivity(context, url);
    }

    public static void startRewardDetail(Context context, String resourceId) {
        String url = formatter.format(REWARD_DETAIL, resourceId).toString();
        WebContainerActivity.startActivity(context, url);
    }

    public static void startResourceDetail(Context context, String circleRoute, String coterieId, String moduleEnum, String resourceId) {
        String url = formatter.format(RESOURCE_DETAIL, circleRoute, coterieId, moduleEnum, resourceId).toString();
        WebContainerActivity.startActivity(context, url);
    }

    public static void startMemberAudit(Context context, String circleRoute, String coterieId) {
        String url = formatter.format(MEMBER_AUDIT, circleRoute, coterieId).toString();
        WebContainerActivity.startActivity(context, url);
    }

    public static void startSignActivity(Context context, String resourceId) {
        String url = formatter.format(SIGN_ACTIVITY, resourceId).toString();
        WebContainerActivity.startActivity(context, url);
    }

    public static void startVoteActivity(Context context, String resourceId) {
        String url = formatter.format(VOTE_ACTIVITY, resourceId).toString();
        WebContainerActivity.startActivity(context, url);
    }

    public static void startWinningScan(Context context, String resourceId) {
        String url = formatter.format(WINNING_SCAN, resourceId).toString();
        WebContainerActivity.startActivity(context, url);
    }
}
