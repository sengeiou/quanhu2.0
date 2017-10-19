package com.rz.httpapi.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/20 0020.
 */

public class MineRewardBean implements Serializable {


    /**
     * rewardId : 535259405913030656
     * rewardPrice : 700
     * createTime : 1505456998000
     * giftInfo : {"giftId":"ffhhh154sdg1","image":"http://imgwx1.2345.com/xiaoimg/api_images/6/55900_b.jpg","name":"大炮"}
     * resourceInfo : {"resourceId":"535101333936668672","circleRoute":"sjmwq","moduleEnum":"0210","resourceType":"1000","resourceTag":"陈琳的私圈","title":"这是我们的故事","summary":"应该很好好功夫郭红红火火滚滚滚古古怪怪个vv发个vv姐姐好看哈哈哈风风光光哥哥更丰富飞飞哥古古怪怪防城港个过函谷关古古怪怪嘎嘎嘎黄河鬼棺嘎嘎嘎好诡异","content":"应该很好好功夫郭红红火火滚滚滚古古怪怪个vv发个vv姐姐好看哈哈哈风风光光哥哥更丰富飞飞哥古古怪怪防城港个过函谷关古古怪怪嘎嘎嘎黄河鬼棺嘎嘎嘎好诡异","pics":"https://cdn.yryz.com/pic/opus/d83a51cc-e756-4d9a-9e6d-aab0b2916d6f.jpg","heat":0,"readNum":0,"partNum":0,"extjson":"{\"allowGeneralizeFlag\":0,\"allowShareFlag\":0,\"appName\":\"common\",\"classifyId\":0,\"content\":\"应该很好好功夫郭红红火火滚滚滚古古怪怪个vv发个vv姐姐好看哈哈哈风风光光哥哥更丰富飞飞哥古古怪怪防城港个过函谷关古古怪怪嘎嘎嘎黄河鬼棺嘎嘎嘎好诡异\",\"contentPrice\":0,\"contentSource\":\"[{\\\"text\\\":\\\"应该很好好功夫郭红红火火滚滚滚古古怪怪个vv发个vv姐姐好看哈哈哈风风光光哥哥更丰富飞飞哥古古怪怪防城港个过函谷关古古怪怪嘎嘎嘎黄河鬼棺嘎嘎嘎好诡异\\\"},{\\\"image\\\":\\\"https://cdn.yryz.com/pic/opus/d83a51cc-e756-4d9a-9e6d-aab0b2916d6f.jpg\\\"}]\",\"coterieId\":\"tvwobuw9rrfo\",\"createUserId\":\"58\",\"heat\":0,\"id\":244,\"imgUrl\":\"https://cdn.yryz.com/pic/opus/d83a51cc-e756-4d9a-9e6d-aab0b2916d6f.jpg\",\"lastUpdateUserId\":\"58\",\"moduleEnum\":\"0210\",\"resourceId\":\"535101333936668672\",\"revision\":1,\"tenantId\":\"k7yar8ng8u3q\",\"title\":\"这是我们的故事\",\"viewCount\":0}"}
     * user : {"custId":"36b5ne0f3q","custNname":"夏天1310015m","custImg":"http://yryz-resources.oss-cn-hangzhou.aliyuncs.com/pic%2FheadImage%2F20170428023206472-364.jpg","custSignature":"123456要❤️6464","custLevel":"1","custRole":0,"custDesc":"","custSex":"1"}
     */

    private String rewardId;
    private int rewardPrice;
    private long createTime;
    private GiftInfoBean giftInfo;
    private ResourceInfoBean resourceInfo;
    private UserBean user;

    public String getRewardId() {
        return rewardId;
    }

    public void setRewardId(String rewardId) {
        this.rewardId = rewardId;
    }

    public int getRewardPrice() {
        return rewardPrice;
    }

    public void setRewardPrice(int rewardPrice) {
        this.rewardPrice = rewardPrice;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public GiftInfoBean getGiftInfo() {
        return giftInfo;
    }

    public void setGiftInfo(GiftInfoBean giftInfo) {
        this.giftInfo = giftInfo;
    }

    public ResourceInfoBean getResourceInfo() {
        return resourceInfo;
    }

    public void setResourceInfo(ResourceInfoBean resourceInfo) {
        this.resourceInfo = resourceInfo;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class GiftInfoBean {
        /**
         * giftId : ffhhh154sdg1
         * image : http://imgwx1.2345.com/xiaoimg/api_images/6/55900_b.jpg
         * name : 大炮
         */

        private String giftId;
        private String image;
        private String name;

        public String getGiftId() {
            return giftId;
        }

        public void setGiftId(String giftId) {
            this.giftId = giftId;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class ResourceInfoBean {
        /**
         * resourceId : 535101333936668672
         * circleRoute : sjmwq
         * moduleEnum : 0210
         * resourceType : 1000
         * resourceTag : 陈琳的私圈
         * title : 这是我们的故事
         * summary : 应该很好好功夫郭红红火火滚滚滚古古怪怪个vv发个vv姐姐好看哈哈哈风风光光哥哥更丰富飞飞哥古古怪怪防城港个过函谷关古古怪怪嘎嘎嘎黄河鬼棺嘎嘎嘎好诡异
         * content : 应该很好好功夫郭红红火火滚滚滚古古怪怪个vv发个vv姐姐好看哈哈哈风风光光哥哥更丰富飞飞哥古古怪怪防城港个过函谷关古古怪怪嘎嘎嘎黄河鬼棺嘎嘎嘎好诡异
         * pics : https://cdn.yryz.com/pic/opus/d83a51cc-e756-4d9a-9e6d-aab0b2916d6f.jpg
         * heat : 0
         * readNum : 0
         * partNum : 0
         * extjson : {"allowGeneralizeFlag":0,"allowShareFlag":0,"appName":"common","classifyId":0,"content":"应该很好好功夫郭红红火火滚滚滚古古怪怪个vv发个vv姐姐好看哈哈哈风风光光哥哥更丰富飞飞哥古古怪怪防城港个过函谷关古古怪怪嘎嘎嘎黄河鬼棺嘎嘎嘎好诡异","contentPrice":0,"contentSource":"[{\"text\":\"应该很好好功夫郭红红火火滚滚滚古古怪怪个vv发个vv姐姐好看哈哈哈风风光光哥哥更丰富飞飞哥古古怪怪防城港个过函谷关古古怪怪嘎嘎嘎黄河鬼棺嘎嘎嘎好诡异\"},{\"image\":\"https://cdn.yryz.com/pic/opus/d83a51cc-e756-4d9a-9e6d-aab0b2916d6f.jpg\"}]","coterieId":"tvwobuw9rrfo","createUserId":"58","heat":0,"id":244,"imgUrl":"https://cdn.yryz.com/pic/opus/d83a51cc-e756-4d9a-9e6d-aab0b2916d6f.jpg","lastUpdateUserId":"58","moduleEnum":"0210","resourceId":"535101333936668672","revision":1,"tenantId":"k7yar8ng8u3q","title":"这是我们的故事","viewCount":0}
         */

        private String resourceId;
        private String circleRoute;
        private String moduleEnum;
        private String resourceType;
        private String resourceTag;
        private String title;
        private String summary;
        private String content;
        private String pics;
        private int heat;
        private int readNum;
        private int partNum;
        private String coterieId;
        private String extjson;
        private String videoPic;

        public String getVideoPic() {
            return videoPic;
        }

        public void setVideoPic(String videoPic) {
            this.videoPic = videoPic;
        }

        public String getResourceId() {
            return resourceId;
        }

        public void setResourceId(String resourceId) {
            this.resourceId = resourceId;
        }

        public String getCircleRoute() {
            return circleRoute;
        }

        public void setCircleRoute(String circleRoute) {
            this.circleRoute = circleRoute;
        }

        public String getModuleEnum() {
            return moduleEnum;
        }

        public void setModuleEnum(String moduleEnum) {
            this.moduleEnum = moduleEnum;
        }

        public String getResourceType() {
            return resourceType;
        }

        public void setResourceType(String resourceType) {
            this.resourceType = resourceType;
        }

        public String getResourceTag() {
            return resourceTag;
        }

        public void setResourceTag(String resourceTag) {
            this.resourceTag = resourceTag;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
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

        public int getHeat() {
            return heat;
        }

        public void setHeat(int heat) {
            this.heat = heat;
        }

        public int getReadNum() {
            return readNum;
        }

        public void setReadNum(int readNum) {
            this.readNum = readNum;
        }

        public int getPartNum() {
            return partNum;
        }

        public void setPartNum(int partNum) {
            this.partNum = partNum;
        }

        public String getCoterieId() {
            return coterieId;
        }

        public void setCoterieId(String coterieId) {
            this.coterieId = coterieId;
        }

        public String getExtjson() {
            return extjson;
        }

        public void setExtjson(String extjson) {
            this.extjson = extjson;
        }
    }

    public static class UserBean {
        /**
         * custId : 36b5ne0f3q
         * custNname : 夏天1310015m
         * custImg : http://yryz-resources.oss-cn-hangzhou.aliyuncs.com/pic%2FheadImage%2F20170428023206472-364.jpg
         * custSignature : 123456要❤️6464
         * custLevel : 1
         * custRole : 0
         * custDesc :
         * custSex : 1
         */

        private String custId;
        private String custNname;
        private String custImg;
        private String custSignature;
        private String custLevel;
        private int custRole;
        private String custDesc;
        private String custSex;

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

        public String getCustDesc() {
            return custDesc;
        }

        public void setCustDesc(String custDesc) {
            this.custDesc = custDesc;
        }

        public String getCustSex() {
            return custSex;
        }

        public void setCustSex(String custSex) {
            this.custSex = custSex;
        }
    }
}
