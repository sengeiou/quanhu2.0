package com.rz.circled.constants;

/**
 * Created by rzw2 on 2017/9/5.
 */

public class JPushTypeConstants {
    //通知公告
    public static final class Announcement {
        public static final int TYPE = 1;

        public static final int ANNOUNCEMENT = 1001;
    }

    //系统消息
    public static final class SystemInformation {
        public static final int TYPE = 2;

        public static final int ANNOUNCEMENT = 2001;
        public static final int ACOUNT_SAFE = 2002;
        public static final int WARNING = 2003;
        public static final int REVIEW = 2004;
        public static final int OFF_THE_SHELF = 2005;
    }

    //账户消息
    public static final class AccoutNotification {
        public static final int TYPE = 3;

        public static final int RECHARGE = 3001;
        public static final int PAY = 3002;
        public static final int WITHDRAW = 3003;
        public static final int RETURN = 3004;
        public static final int TO_THE_ACCOUNT = 3005;
    }

    //互动消息
    public static final class InteractiveMessage {
        public static final int TYPE = 4;

        public static final int REWARD = 4001;
        public static final int PRIVATE_GROUP = 4002;
        public static final int COMMENT = 4003;
        public static final int NOTICE = 4004;
    }

    //推荐、活动
    public static final class RecommendedActivities {
        public static final int TYPE =5;

        public static final int HOT = 5001;
        public static final int ACTIVITY = 5002;
    }
}
