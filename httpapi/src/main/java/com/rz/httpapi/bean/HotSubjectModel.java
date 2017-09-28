package com.rz.httpapi.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/2/002.
 */

public class HotSubjectModel implements Serializable {


    /**
     * audio : audio
     * circleId : t2uvj7cmjh8i
     * circleName : 创业达人圈
     * circleRoute : cyq
     * cityCode : 110100
     * completeTime : 85848
     * content : content5
     * coterieId : coterieId
     * coterieName : 测试内容e1w5
     * createTime : 68756
     * cust : {"custDesc":"测试内容kmp9","custId":"测试内容n006","custImg":"测试内容j5w8","custLevel":"测试内容54ns","custName":"测试内容1c21","custRole":80013,"custSex":"测试内容f8um","custSignature":"测试内容qhr2","nameNotes":"测试内容0y54"}
     * custId : yehao-test
     * custImg : 测试内容m66e
     * custNname : 测试内容6r8d
     * extjson : {"avtiviAddress":"湖北省武汉市洪山区"}
     * gps : 114.00265,36.325321
     * heat : 0
     * moduleEnum : 300-1001
     * orderby : 4
     * ownerIntro : 1
     * ownerName : 测试内容8462
     * partNum : 0
     * pics : https://www.baidu.com/logo.png,https://www.baidu.com/logo.png
     * readNum : 0
     * resourceId : 19441761d7ff48b0bd9930001617a0a3
     * resourceTag : 分类
     * resourceType : 1001
     * summary : summary5
     * talentType : 0
     * thumbnail : https://www.baidu.com/logo.png
     * title : title
     * updateTime : 18770
     * video : https://www.baidu.com/video.mp4
     * videoPic : https://www.baidu.com/logo.png
     */

    private String audio;
    private String circleId;
    private String circleName;
    public String circleRoute;
    private String cityCode;
    private long completeTime;
    private String content;
    private String coterieId;
    private String coterieName;
    private long createTime;
    private CustBean cust;
    private String custId;
    private String custImg;
    private String custNname;
    private String extjson;
    private String gps;
    private int heat;
    public String moduleEnum;
    private int orderby;
    private String ownerIntro;
    private String ownerName;
    private int partNum;
    private String pics;
    private int readNum;
    public String resourceId;
    private String resourceTag;
    private String resourceType;
    private String summary;
    private String talentType;
    private String thumbnail;
    private String title;
    private long updateTime;
    private String video;
    private String videoPic;

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
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

    public long getCompleteTime() {
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

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public CustBean getCust() {
        return cust;
    }

    public void setCust(CustBean cust) {
        this.cust = cust;
    }

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

    public String getCustNname() {
        return custNname;
    }

    public void setCustNname(String custNname) {
        this.custNname = custNname;
    }

    public String getExtjson() {
        return extjson;
    }

    public void setExtjson(String extjson) {
        this.extjson = extjson;
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

    public int getOrderby() {
        return orderby;
    }

    public void setOrderby(int orderby) {
        this.orderby = orderby;
    }

    public String getOwnerIntro() {
        return ownerIntro;
    }

    public void setOwnerIntro(String ownerIntro) {
        this.ownerIntro = ownerIntro;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
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

    public String getTalentType() {
        return talentType;
    }

    public void setTalentType(String talentType) {
        this.talentType = talentType;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(int updateTime) {
        this.updateTime = updateTime;
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

    public static class CustBean {
        /**
         * custDesc : 测试内容kmp9
         * custId : 测试内容n006
         * custImg : 测试内容j5w8
         * custLevel : 测试内容54ns
         * custName : 测试内容1c21
         * custRole : 80013
         * custSex : 测试内容f8um
         * custSignature : 测试内容qhr2
         * nameNotes : 测试内容0y54
         */

        private String custDesc;
        private String custId;
        private String custImg;
        private String custLevel;
        private String custNname;
        private int custRole;
        private String custSex;
        private String custSignature;
        private String nameNotes;

        public String getCustDesc() {
            return custDesc;
        }

        public void setCustDesc(String custDesc) {
            this.custDesc = custDesc;
        }

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

        public String getCustLevel() {
            return custLevel;
        }

        public void setCustLevel(String custLevel) {
            this.custLevel = custLevel;
        }

        public String getCustName() {
            return custNname;
        }

        public void setCustName(String custName) {
            this.custNname = custName;
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

        public String getCustSignature() {
            return custSignature;
        }

        public void setCustSignature(String custSignature) {
            this.custSignature = custSignature;
        }

        public String getNameNotes() {
            return nameNotes;
        }

        public void setNameNotes(String nameNotes) {
            this.nameNotes = nameNotes;
        }
    }
}
