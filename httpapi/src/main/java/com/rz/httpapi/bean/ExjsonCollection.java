package com.rz.httpapi.bean;

/**
 * Created by Administrator on 2017/9/16/016.
 */

public class ExjsonCollection {

    public QuestionBean question;
    public AnswerBean answer;

    public static class QuestionBean {

        public int revision;
        public String createUserId;
        public long createDate;
        public Object lastUpdateUserId;
        public long lastUpdateDate;
        public String appName;
        public String tenantId;
        public String coterieId;
        public int id;
        public Object title;
        public String content;
        public String contentSource;
        public Object imgUrl;
        public String targetId;
        public int type;
        public int heat;
        public int recommend;
        public Object recommendDate;
        public int shelveFlag;
        public int delFlag;
        public String moduleEnum;
        public int answerCount;
        public int likeCount;
        public int viewCount;
        public int commentCount;
        public int favoriteCount;
        public int shareCount;
        public int answerId;
        public long lastAnswerDate;
        public String cityCode;
        public String gps;
        public String resourceId;
        public int validTime;
        public double chargeAmount;
        public int isOnlyShowMe;
        public int isAnonymity;
        public int isValid;
        public int refundFlag;
        public Object password;
        public int orderFlag;
        public String orderId;
        public Object refundOrderId;
        public Object questionContent;
        public String userName;
        public String userImg;
        public String nickName;
        public String targetUserName;
        public String targetUserImg;
        public String targetNickName;
        public Object myAnswerId;
        public Object questionType;
        public int roleFlag;
        public int targetRoleFlag;
        public int likeFlag;
        public Object currentUserId;
        public Object currentUser;
        public int revisionNext;


    }

    public static class AnswerBean {
        public int revision;
        public String createUserId;
        public long createDate;
        public Object lastUpdateUserId;
        public long lastUpdateDate;
        public String appName;
        public String tenantId;
        public String coterieId;
        public int id;
        public int questionId;
        public String content;
        public String contentSource;
        public String imgUrl;
        public int heat;
        public int shelveFlag;
        public int delFlag;
        public String moduleEnum;
        public int likeCount;
        public int commentCount;
        public int favoriteCount;
        public int shareCount;
        public int type;
        public String cityCode;
        public String gps;
        public String resourceId;
        public String answerAudio;
        public Object answerContent;
        public int audioLength;
        public int orderFlag;
        public Object orderId;
        public String userName;
        public String userImg;
        public String nickName;
        public int roleFlag;
        public int likeFlag;
        public Object currentUserId;
        public Object currentUser;
        public int revisionNext;
    }

   
}
