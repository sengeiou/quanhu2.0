package com.rz.httpapi.bean;

/**
 * Created by rzw2 on 2017/4/10.
 */

public class CircleMemberModel {
    private String appId;
    private User user;
    private long insideTime;

    public class User {
        private String custId;
        private String custNname;
        private String custImg;
        private String custNameNote;

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

        public String getCustNameNote() {
            return custNameNote;
        }

        public void setCustNameNote(String custNameNote) {
            this.custNameNote = custNameNote;
        }

        @Override
        public String toString() {
            return "User{" +
                    "custId='" + custId + '\'' +
                    ", custNname='" + custNname + '\'' +
                    ", custImg='" + custImg + '\'' +
                    ", custNameNote='" + custNameNote + '\'' +
                    '}';
        }
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getInsideTime() {
        return insideTime;
    }

    public void setInsideTime(long insideTime) {
        this.insideTime = insideTime;
    }

    @Override
    public String toString() {
        return "CircleMemberModel{" +
                "appId='" + appId + '\'' +
                ", user=" + user +
                ", insideTime=" + insideTime +
                '}';
    }
}
