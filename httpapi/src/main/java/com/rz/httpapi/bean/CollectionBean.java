package com.rz.httpapi.bean;

/**
 * Created by Administrator on 2017/9/15/015.
 */

public class CollectionBean {

    /**
     * cid : 100
     * circleInfo : {"appId":"","circleName":"育儿童星圈","circleUrl":""}
     * coterieInfo : {"coterieId":"","name":"","qrUrl":""}
     * createTime : 4345949000
     * resourceInfo : {"audio":"","circleRoute":"","cityCode":"","completeTime":1,"content":"","extjson":"{}","gps":"","heat":100,"moduleEnum":"","partNum":20,"pics":"","readNum":100,"resourceId":"","resourceTag":"","resourceType":"","summary":"","thumnail":"","title":"","video":"","videoPic":""}
     * user : {"custId":"测试内容e25u","custImg":"测试内容ku81","custLevel":1,"custNname":"测试内容tmdv","custRole":0,"custSex":"测试内容xodc","nameNotes":"测试内容4fp6"}
     */

    public int cid;
    public CircleInfoBean circleInfo;
    public CoterieInfoBean coterieInfo;
    public long createTime;
    public ResourceInfoBean resourceInfo;
    public UserBean user;
    public boolean isSelect;

    public static class CircleInfoBean {
        /**
         * appId :
         * circleName : 育儿童星圈
         * circleUrl :
         */

        private String appId;
        private String circleName;
        private String circleUrl;

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getCircleName() {
            return circleName;
        }

        public void setCircleName(String circleName) {
            this.circleName = circleName;
        }

        public String getCircleUrl() {
            return circleUrl;
        }

        public void setCircleUrl(String circleUrl) {
            this.circleUrl = circleUrl;
        }
    }

    public static class CoterieInfoBean {
        /**
         * coterieId :
         * name :
         * qrUrl :
         */

        private String coterieId;
        private String name;
        private String qrUrl;

        public String getCoterieId() {
            return coterieId;
        }

        public void setCoterieId(String coterieId) {
            this.coterieId = coterieId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getQrUrl() {
            return qrUrl;
        }

        public void setQrUrl(String qrUrl) {
            this.qrUrl = qrUrl;
        }
    }

    public static class ResourceInfoBean {
        /**
         * audio :
         * circleRoute :
         * cityCode :
         * completeTime : 1
         * content :
         * extjson : {}
         * gps :
         * heat : 100
         * moduleEnum :
         * partNum : 20
         * pics :
         * readNum : 100
         * resourceId :
         * resourceTag :
         * resourceType :
         * summary :
         * thumnail :
         * title :
         * video :
         * videoPic :
         */

        private String audio;
        private String circleRoute;
        private String cityCode;
        private int completeTime;
        private String content;
        public String extjson;
        private String gps;
        private int heat;
        private String moduleEnum;
        private int partNum;
        private String pics;
        private int readNum;
        private String resourceId;
        private String resourceTag;
        private String resourceType;
        private String summary;
        private String thumnail;
        private String title;
        private String video;
        private String videoPic;

        public String getAudio() {
            return audio;
        }

        public void setAudio(String audio) {
            this.audio = audio;
        }

        public String getCircleRoute() {
            return circleRoute;
        }

        public void setCircleRoute(String circleRoute) {
            this.circleRoute = circleRoute;
        }

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }

        public int getCompleteTime() {
            return completeTime;
        }

        public void setCompleteTime(int completeTime) {
            this.completeTime = completeTime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getGps() {
            return gps;
        }

        public void setGps(String gps) {
            this.gps = gps;
        }

        public int getHeat() {
            return heat;
        }

        public void setHeat(int heat) {
            this.heat = heat;
        }

        public String getModuleEnum() {
            return moduleEnum;
        }

        public void setModuleEnum(String moduleEnum) {
            this.moduleEnum = moduleEnum;
        }

        public int getPartNum() {
            return partNum;
        }

        public void setPartNum(int partNum) {
            this.partNum = partNum;
        }

        public String getPics() {
            return pics;
        }

        public void setPics(String pics) {
            this.pics = pics;
        }

        public int getReadNum() {
            return readNum;
        }

        public void setReadNum(int readNum) {
            this.readNum = readNum;
        }

        public String getResourceId() {
            return resourceId;
        }

        public void setResourceId(String resourceId) {
            this.resourceId = resourceId;
        }

        public String getResourceTag() {
            return resourceTag;
        }

        public void setResourceTag(String resourceTag) {
            this.resourceTag = resourceTag;
        }

        public String getResourceType() {
            return resourceType;
        }

        public void setResourceType(String resourceType) {
            this.resourceType = resourceType;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getThumnail() {
            return thumnail;
        }

        public void setThumnail(String thumnail) {
            this.thumnail = thumnail;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public String getVideoPic() {
            return videoPic;
        }

        public void setVideoPic(String videoPic) {
            this.videoPic = videoPic;
        }
    }

    public static class UserBean {
        /**
         * custId : 测试内容e25u
         * custImg : 测试内容ku81
         * custLevel : 1
         * custNname : 测试内容tmdv
         * custRole : 0
         * custSex : 测试内容xodc
         * nameNotes : 测试内容4fp6
         */

        private String custId;
        private String custImg;
        private int custLevel;
        private String custNname;
        private int custRole;
        private String custSex;
        private String nameNotes;

        public String getCustId() {
            return custId;
        }

        public void setCustId(String custId) {
            this.custId = custId;
        }

        public String getCustImg() {
            return custImg;
        }

        public void setCustImg(String custImg) {
            this.custImg = custImg;
        }

        public int getCustLevel() {
            return custLevel;
        }

        public void setCustLevel(int custLevel) {
            this.custLevel = custLevel;
        }

        public String getCustNname() {
            return custNname;
        }

        public void setCustNname(String custNname) {
            this.custNname = custNname;
        }

        public int getCustRole() {
            return custRole;
        }

        public void setCustRole(int custRole) {
            this.custRole = custRole;
        }

        public String getCustSex() {
            return custSex;
        }

        public void setCustSex(String custSex) {
            this.custSex = custSex;
        }

        public String getNameNotes() {
            return nameNotes;
        }

        public void setNameNotes(String nameNotes) {
            this.nameNotes = nameNotes;
        }
    }
}
