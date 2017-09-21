package com.rz.httpapi.bean;

/**
 * Created by Administrator on 2017/9/21/021.
 */

public class CollectionAnswerBean {

    /**
     * question : {"revision":1,"createUserId":"60","createDate":1505902758000,"lastUpdateUserId":null,"lastUpdateDate":1505902833000,"appName":"common","tenantId":"k7yar8ng8u3q","coterieId":"i9a8n67l8i6w","id":576,"title":null,"content":"好多假的看考场看考场","contentSource":"[{\"text\":\"好多假的看考场看考场\"}]","imgUrl":null,"targetId":"73","type":1,"heat":88567,"recommend":0,"recommendDate":null,"shelveFlag":0,"delFlag":0,"moduleEnum":"0240","answerCount":1,"likeCount":0,"viewCount":2,"commentCount":0,"favoriteCount":0,"shareCount":0,"answerId":190,"lastAnswerDate":1505902833000,"cityCode":"","gps":"114.3936869223736,30.50986292774837","resourceId":"542917504401440768","validTime":48,"chargeAmount":1,"isOnlyShowMe":0,"isAnonymity":1,"isValid":0,"refundFlag":0,"password":null,"orderFlag":0,"orderId":"2017092010878056","refundOrderId":null,"questionContent":null,"userName":"TEXT","userImg":"https://cdn.yryz.com/pic/opus/91F7E71C-E418-4251-83EE-1FA60F4E5AC8_iOS.jpg","nickName":"yryz1337869","targetUserName":"TEXT","targetUserImg":"https://cdn.yryz.com/pic/opus/D53C8B89-E369-48F9-B741-25C841650901_iOS.jpg","targetNickName":"我爱度假","myAnswerId":null,"questionType":null,"roleFlag":0,"targetRoleFlag":0,"likeFlag":0,"currentUserId":null,"currentUser":null,"revisionNext":2}
     * answer : {"revision":1,"createUserId":"73","createDate":1505902833000,"lastUpdateUserId":null,"lastUpdateDate":1505902833000,"appName":"common","tenantId":"k7yar8ng8u3q","coterieId":"i9a8n67l8i6w","id":190,"questionId":576,"content":"我来帮你揉揉腿了？","contentSource":"[{\"text\":\"我来帮你揉揉腿了？\"},{\"audio\":{\"url\":\"https://cdn.yryz.com/audio/sjmwq/6823ED00-E198-431C-B122-2279A568B011_iOS.mp3\",\"audioTime\":10000,\"size\":49,\"time\":10000}}]","imgUrl":"","heat":0,"shelveFlag":0,"delFlag":0,"moduleEnum":"0241","likeCount":0,"commentCount":0,"favoriteCount":0,"shareCount":0,"type":1,"cityCode":"","gps":"114.3936869223736,30.50986292774837","resourceId":"542918792891629568","answerAudio":"https://cdn.yryz.com/audio/sjmwq/6823ED00-E198-431C-B122-2279A568B011_iOS.mp3","answerContent":null,"audioLength":10000,"orderFlag":0,"orderId":null,"userName":"TEXT","userImg":"https://cdn.yryz.com/pic/opus/D53C8B89-E369-48F9-B741-25C841650901_iOS.jpg","nickName":"我爱度假","roleFlag":0,"likeFlag":-1,"currentUserId":null,"currentUser":null,"revisionNext":2}
     */

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
