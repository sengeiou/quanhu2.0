package com.rz.httpapi.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/18 0018.
 */

public class MyRewardBean implements Serializable {


    /**
     * id : 168
     * custId : 4qqcxnbnht
     * content : 11111111111
     * pics :
     * duration : 1
     * complete : 2
     * terminalTime : 2017-09-15 23:59:59
     * location :
     * replyNum : 3
     * nowTime : 2017-09-18 20:32:00
     * user : {"custId":"4qqcxnbnht","custNname":"Miss余","custImg":"http://yryz-resources.oss-cn-hangzhou.aliyuncs.com/pic%2FheadImage%2F20170427103754172-6739.jpg","custSignature":"签名是个什么梗","custDesc":"Yes or No","custLevel":"1","custRole":0}
     */

    private int id;
    private String custId;
    private String content;
    private String pics;
    private int duration;
    private int complete;
    private String terminalTime;
    private String location;
    private int replyNum;
    private String nowTime;
    private UserBean user;
    private String price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getComplete() {
        return complete;
    }

    public void setComplete(int complete) {
        this.complete = complete;
    }

    public String getTerminalTime() {
        return terminalTime;
    }

    public void setTerminalTime(String terminalTime) {
        this.terminalTime = terminalTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(int replyNum) {
        this.replyNum = replyNum;
    }

    public String getNowTime() {
        return nowTime;
    }

    public void setNowTime(String nowTime) {
        this.nowTime = nowTime;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public static class UserBean {
        /**
         * custId : 4qqcxnbnht
         * custNname : Miss余
         * custImg : http://yryz-resources.oss-cn-hangzhou.aliyuncs.com/pic%2FheadImage%2F20170427103754172-6739.jpg
         * custSignature : 签名是个什么梗
         * custDesc : Yes or No
         * custLevel : 1
         * custRole : 0
         */

        private String custId;
        private String custNname;
        private String custImg;
        private String custSignature;
        private String custDesc;
        private String custLevel;
        private int custRole;
        private String nameNotes;
        private int relation;

        public String getNameNotes() {
            return nameNotes;
        }

        public void setNameNotes(String nameNotes) {
            this.nameNotes = nameNotes;
        }

        public int getRelation() {
            return relation;
        }

        public void setRelation(int relation) {
            this.relation = relation;
        }

        public String getCustId() {
            return custId;
        }

        public void setCustId(String custId) {
            this.custId = custId;
        }

        public String getCustNname() {
            return custNname;
        }

        public void setCustNname(String custNname) {
            this.custNname = custNname;
        }

        public String getCustImg() {
            return custImg;
        }

        public void setCustImg(String custImg) {
            this.custImg = custImg;
        }

        public String getCustSignature() {
            return custSignature;
        }

        public void setCustSignature(String custSignature) {
            this.custSignature = custSignature;
        }

        public String getCustDesc() {
            return custDesc;
        }

        public void setCustDesc(String custDesc) {
            this.custDesc = custDesc;
        }

        public String getCustLevel() {
            return custLevel;
        }

        public void setCustLevel(String custLevel) {
            this.custLevel = custLevel;
        }

        public int getCustRole() {
            return custRole;
        }

        public void setCustRole(int custRole) {
            this.custRole = custRole;
        }
    }
}
