package com.rz.httpapi.bean;

/**
 * Created by Administrator on 2017/9/7/007.
 */

public class StarListBean {
    /**
     * custInfo : {"custId":"","custImg":"","custLevel":1,"custNname":"","nameNotes":""}
     * starInfo : {"tradeField":""}
     */

    public CustInfoBean custInfo;
    public StarInfoBean starInfo;

    public static class CustInfoBean {

        /**
         * custId : 11y3giw7os
         * custPhone : 13400000004
         * custNname : 哈哈一笑
         * custImg : https://cdn.yryz.com/pic/headImage/5a12e599-0a79-4fee-8599-163774245cea.jpg
         * custSignature : 🌻🌻🌻
         * custQr : http://yryz-resources-mo.oss-cn-hangzhou.aliyuncs.com/pic/qr/11y3giw7os_qr.png
         * custSex : 0
         * custLocation : 北京市 北京市
         * cityCode : 110100
         * custDevId : 160a3797c831daaff8e
         * custIdcard : 420114198508253625
         * custDesc : 我爱烊烊
         * createDate : 2017-04-19 17:11:06.0
         * custStatus : 1
         * custRole : 0
         * heatWeight : 1034540
         * custPoints : 0
         * starRecommendStatus : 0
         * starWeight : 0
         */

        private String custId;
        private String custPhone;
        private String custNname;
        private String custImg;
        private String custSignature;
        private String custQr;
        private String custSex;
        private String custLocation;
        private String cityCode;
        private String custDevId;
        private String custIdcard;
        private String custDesc;
        private String createDate;
        private String custStatus;
        private int custRole;
        private int heatWeight;
        private int custPoints;
        private int starRecommendStatus;
        private int starWeight;
        private String custLevel;

        public String getCustId() {
            return custId;
        }

        public void setCustId(String custId) {
            this.custId = custId;
        }

        public String getCustPhone() {
            return custPhone;
        }

        public void setCustPhone(String custPhone) {
            this.custPhone = custPhone;
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

        public String getCustQr() {
            return custQr;
        }

        public void setCustQr(String custQr) {
            this.custQr = custQr;
        }

        public String getCustSex() {
            return custSex;
        }

        public void setCustSex(String custSex) {
            this.custSex = custSex;
        }

        public String getCustLocation() {
            return custLocation;
        }

        public void setCustLocation(String custLocation) {
            this.custLocation = custLocation;
        }

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }

        public String getCustDevId() {
            return custDevId;
        }

        public void setCustDevId(String custDevId) {
            this.custDevId = custDevId;
        }

        public String getCustIdcard() {
            return custIdcard;
        }

        public void setCustIdcard(String custIdcard) {
            this.custIdcard = custIdcard;
        }

        public String getCustDesc() {
            return custDesc;
        }

        public String getCustLevel() {
            return custLevel;
        }

        public void setCustLevel(String custLevel) {
            this.custLevel = custLevel;
        }

        public void setCustDesc(String custDesc) {
            this.custDesc = custDesc;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getCustStatus() {
            return custStatus;
        }

        public void setCustStatus(String custStatus) {
            this.custStatus = custStatus;
        }

        public int getCustRole() {
            return custRole;
        }

        public void setCustRole(int custRole) {
            this.custRole = custRole;
        }

        public int getHeatWeight() {
            return heatWeight;
        }

        public void setHeatWeight(int heatWeight) {
            this.heatWeight = heatWeight;
        }

        public int getCustPoints() {
            return custPoints;
        }

        public void setCustPoints(int custPoints) {
            this.custPoints = custPoints;
        }

        public int getStarRecommendStatus() {
            return starRecommendStatus;
        }

        public void setStarRecommendStatus(int starRecommendStatus) {
            this.starRecommendStatus = starRecommendStatus;
        }

        public int getStarWeight() {
            return starWeight;
        }

        public void setStarWeight(int starWeight) {
            this.starWeight = starWeight;
        }
    }

    public static class StarInfoBean {
        /**
         * tradeField :
         */

        private String tradeField;
        public int id;

        public String getTradeField() {
            return tradeField;
        }

        public void setTradeField(String tradeField) {
            this.tradeField = tradeField;
        }
    }
}
