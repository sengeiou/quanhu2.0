package com.rz.httpapi.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/18 0018.
 */

public class MyRewardBean implements Serializable {

    /**
     * id : 187
     * custId : 0m8xwqgrxd
     * content : dfgdfghxdh
     * pics :
     * duration : 1
     * complete : 1
     * terminalTime : 2017-09-17 00:00:00
     * location :
     * replyNum : 2
     * nowTime : 2017-09-18 20:32:00
     * user : {"custId":"0m8xwqgrxd","custNname":"悠然号1765075","custLevel":"1","custRole":0}
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

    public static class UserBean {
        /**
         * custId : 0m8xwqgrxd
         * custNname : 悠然号1765075
         * custLevel : 1
         * custRole : 0
         */

        private String custId;
        private String custNname;
        private String custLevel;
        private int custRole;

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
