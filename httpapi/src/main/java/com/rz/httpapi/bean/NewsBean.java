package com.rz.httpapi.bean;

/**
 * Created by rzw2 on 2017/9/5.
 */

public class NewsBean {

    /**
     * messageId : 900ec35e4add4844b1951efe0701ce1a
     * type : 1
     * label : 0
     * toCust : r3l7bbgi
     * viewCode : 3000
     * actionCode : 1
     * title : title0
     * content : content0
     * img : https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png
     * link : https://m.yryz.com
     * createTime : "2017-09-09 16:37:51"
     * body : {"serialVersionUID":{"$numberLong":"-7378469458462122189"},"circleId":"t2uvj7cmjh8i","circleName":"创业达人圈","coterieId":"y993kg5o9kd2","coterieName":"孙悟dd"}
     */

    private String messageId;
    private int type;
    private int label;
    private String toCust;
    private String viewCode;
    private String actionCode;
    private String title;
    private String content;
    private String img;
    private String link;
    private String createTime;
    private BodyBean body;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public String getToCust() {
        return toCust;
    }

    public void setToCust(String toCust) {
        this.toCust = toCust;
    }

    public String getViewCode() {
        return viewCode;
    }

    public void setViewCode(String viewCode) {
        this.viewCode = viewCode;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public static class BodyBean {
        /**
         * serialVersionUID : {"$numberLong":"-7378469458462122189"}
         * circleId : t2uvj7cmjh8i
         * circleName : 创业达人圈
         * coterieId : y993kg5o9kd2
         * coterieName : 孙悟dd
         */

        private SerialVersionUIDBean serialVersionUID;
        private String circleId;
        private String circleName;
        private String coterieId;
        private String coterieName;

        public SerialVersionUIDBean getSerialVersionUID() {
            return serialVersionUID;
        }

        public void setSerialVersionUID(SerialVersionUIDBean serialVersionUID) {
            this.serialVersionUID = serialVersionUID;
        }

        public String getCircleId() {
            return circleId;
        }

        public void setCircleId(String circleId) {
            this.circleId = circleId;
        }

        public String getCircleName() {
            return circleName;
        }

        public void setCircleName(String circleName) {
            this.circleName = circleName;
        }

        public String getCoterieId() {
            return coterieId;
        }

        public void setCoterieId(String coterieId) {
            this.coterieId = coterieId;
        }

        public String getCoterieName() {
            return coterieName;
        }

        public void setCoterieName(String coterieName) {
            this.coterieName = coterieName;
        }

        public static class SerialVersionUIDBean {
            /**
             * $numberLong : -7378469458462122189
             */

            private String $numberLong;

            public String get$numberLong() {
                return $numberLong;
            }

            public void set$numberLong(String $numberLong) {
                this.$numberLong = $numberLong;
            }
        }
    }
}
