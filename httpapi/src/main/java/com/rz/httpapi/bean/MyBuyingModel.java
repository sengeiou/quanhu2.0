package com.rz.httpapi.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/20 0020.
 */

public class MyBuyingModel implements Serializable {


    /**
     * custId : 8cpvxjm7au
     * resourceId : 535101333936668672
     * resource : {"resourceId":"535101333936668672","custId":"24ien4w5m2","circleId":"k7yar8ng8u3q","circleRoute":"sjmwq","coterieId":"tvwobuw9rrfo","moduleEnum":"0210","resourceType":"1000","resourceTag":"陈琳的私圈","title":"这是我们的故事","summary":"应该很好好功夫郭红红火火滚滚滚古古怪怪个vv发个vv姐姐好看哈哈哈风风光光哥哥更丰富飞飞哥古古怪怪防城港个过函谷关古古怪怪嘎嘎嘎黄河鬼棺嘎嘎嘎好诡异","content":"应该很好好功夫郭红红火火滚滚滚古古怪怪个vv发个vv姐姐好看哈哈哈风风光光哥哥更丰富飞飞哥古古怪怪防城港个过函谷关古古怪怪嘎嘎嘎黄河鬼棺嘎嘎嘎好诡异","pics":"https://cdn.yryz.com/pic/opus/d83a51cc-e756-4d9a-9e6d-aab0b2916d6f.jpg","heat":0,"readNum":0,"partNum":0,"createTime":1505447947699,"updateTime":1505447797764,"orderby":0,"extjson":"{\"allowGeneralizeFlag\":0,\"allowShareFlag\":0,\"appName\":\"common\",\"classifyId\":0,\"content\":\"应该很好好功夫郭红红火火滚滚滚古古怪怪个vv发个vv姐姐好看哈哈哈风风光光哥哥更丰富飞飞哥古古怪怪防城港个过函谷关古古怪怪嘎嘎嘎黄河鬼棺嘎嘎嘎好诡异\",\"contentPrice\":0,\"contentSource\":\"[{\\\"text\\\":\\\"应该很好好功夫郭红红火火滚滚滚古古怪怪个vv发个vv姐姐好看哈哈哈风风光光哥哥更丰富飞飞哥古古怪怪防城港个过函谷关古古怪怪嘎嘎嘎黄河鬼棺嘎嘎嘎好诡异\\\"},{\\\"image\\\":\\\"https://cdn.yryz.com/pic/opus/d83a51cc-e756-4d9a-9e6d-aab0b2916d6f.jpg\\\"}]\",\"coterieId\":\"tvwobuw9rrfo\",\"createUserId\":\"58\",\"heat\":0,\"id\":244,\"imgUrl\":\"https://cdn.yryz.com/pic/opus/d83a51cc-e756-4d9a-9e6d-aab0b2916d6f.jpg\",\"lastUpdateUserId\":\"58\",\"moduleEnum\":\"0210\",\"resourceId\":\"535101333936668672\",\"revision\":1,\"tenantId\":\"k7yar8ng8u3q\",\"title\":\"这是我们的故事\",\"viewCount\":0}","publicState":0,"circleName":"舌尖美味圈","coterieName":"陈琳的私圈","custNname":"睡落枕的皮卡丘丘~","custImg":"https://cdn.yryz.com/pic/opus/684AAE72-FB98-4ADA-9457-DF932146F617_iOS.jpg","cust":{"custId":"24ien4w5m2","custNname":"睡落枕的皮卡丘丘~","custImg":"https://cdn.yryz.com/pic/opus/684AAE72-FB98-4ADA-9457-DF932146F617_iOS.jpg","custSignature":"233333北城国际很反感荷花皇冠夫妇给好好谈谈给聚聚GG毫","custDesc":"884598放飞机白菜豆腐骨灰盒斤斤计较","custLevel":"1","custRole":0,"custSex":"0"}}
     * amount : 120
     * createDate : 2017-09-16 10:55:12
     */

    private String custId;
    private String resourceId;
    private CircleDynamic resource;
    private int amount;
    private String createDate;

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public CircleDynamic getResource() {
        return resource;
    }

    public void setResource(CircleDynamic resource) {
        this.resource = resource;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public static class ResourceBean {
        /**
         * resourceId : 535101333936668672
         * custId : 24ien4w5m2
         * circleId : k7yar8ng8u3q
         * circleRoute : sjmwq
         * coterieId : tvwobuw9rrfo
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
         * createTime : 1505447947699
         * updateTime : 1505447797764
         * orderby : 0
         * extjson : {"allowGeneralizeFlag":0,"allowShareFlag":0,"appName":"common","classifyId":0,"content":"应该很好好功夫郭红红火火滚滚滚古古怪怪个vv发个vv姐姐好看哈哈哈风风光光哥哥更丰富飞飞哥古古怪怪防城港个过函谷关古古怪怪嘎嘎嘎黄河鬼棺嘎嘎嘎好诡异","contentPrice":0,"contentSource":"[{\"text\":\"应该很好好功夫郭红红火火滚滚滚古古怪怪个vv发个vv姐姐好看哈哈哈风风光光哥哥更丰富飞飞哥古古怪怪防城港个过函谷关古古怪怪嘎嘎嘎黄河鬼棺嘎嘎嘎好诡异\"},{\"image\":\"https://cdn.yryz.com/pic/opus/d83a51cc-e756-4d9a-9e6d-aab0b2916d6f.jpg\"}]","coterieId":"tvwobuw9rrfo","createUserId":"58","heat":0,"id":244,"imgUrl":"https://cdn.yryz.com/pic/opus/d83a51cc-e756-4d9a-9e6d-aab0b2916d6f.jpg","lastUpdateUserId":"58","moduleEnum":"0210","resourceId":"535101333936668672","revision":1,"tenantId":"k7yar8ng8u3q","title":"这是我们的故事","viewCount":0}
         * publicState : 0
         * circleName : 舌尖美味圈
         * coterieName : 陈琳的私圈
         * custNname : 睡落枕的皮卡丘丘~
         * custImg : https://cdn.yryz.com/pic/opus/684AAE72-FB98-4ADA-9457-DF932146F617_iOS.jpg
         * cust : {"custId":"24ien4w5m2","custNname":"睡落枕的皮卡丘丘~","custImg":"https://cdn.yryz.com/pic/opus/684AAE72-FB98-4ADA-9457-DF932146F617_iOS.jpg","custSignature":"233333北城国际很反感荷花皇冠夫妇给好好谈谈给聚聚GG毫","custDesc":"884598放飞机白菜豆腐骨灰盒斤斤计较","custLevel":"1","custRole":0,"custSex":"0"}
         */

        private String resourceId;
        private String custId;
        private String circleId;
        private String circleRoute;
        private String coterieId;
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
        private long createTime;
        private long updateTime;
        private int orderby;
        private String extjson;
        private int publicState;
        private String circleName;
        private String coterieName;
        private String custNname;
        private String custImg;
        private CustBean cust;

        public String getResourceId() {
            return resourceId;
        }

        public void setResourceId(String resourceId) {
            this.resourceId = resourceId;
        }

        public String getCustId() {
            return custId;
        }

        public void setCustId(String custId) {
            this.custId = custId;
        }

        public String getCircleId() {
            return circleId;
        }

        public void setCircleId(String circleId) {
            this.circleId = circleId;
        }

        public String getCircleRoute() {
            return circleRoute;
        }

        public void setCircleRoute(String circleRoute) {
            this.circleRoute = circleRoute;
        }

        public String getCoterieId() {
            return coterieId;
        }

        public void setCoterieId(String coterieId) {
            this.coterieId = coterieId;
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

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public int getOrderby() {
            return orderby;
        }

        public void setOrderby(int orderby) {
            this.orderby = orderby;
        }

        public String getExtjson() {
            return extjson;
        }

        public void setExtjson(String extjson) {
            this.extjson = extjson;
        }

        public int getPublicState() {
            return publicState;
        }

        public void setPublicState(int publicState) {
            this.publicState = publicState;
        }

        public String getCircleName() {
            return circleName;
        }

        public void setCircleName(String circleName) {
            this.circleName = circleName;
        }

        public String getCoterieName() {
            return coterieName;
        }

        public void setCoterieName(String coterieName) {
            this.coterieName = coterieName;
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

        public CustBean getCust() {
            return cust;
        }

        public void setCust(CustBean cust) {
            this.cust = cust;
        }

        public static class CustBean {
            /**
             * custId : 24ien4w5m2
             * custNname : 睡落枕的皮卡丘丘~
             * custImg : https://cdn.yryz.com/pic/opus/684AAE72-FB98-4ADA-9457-DF932146F617_iOS.jpg
             * custSignature : 233333北城国际很反感荷花皇冠夫妇给好好谈谈给聚聚GG毫
             * custDesc : 884598放飞机白菜豆腐骨灰盒斤斤计较
             * custLevel : 1
             * custRole : 0
             * custSex : 0
             */

            private String custId;
            private String custNname;
            private String custImg;
            private String custSignature;
            private String custDesc;
            private String custLevel;
            private int custRole;
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

            public String getCustSex() {
                return custSex;
            }

            public void setCustSex(String custSex) {
                this.custSex = custSex;
            }
        }
    }
}
