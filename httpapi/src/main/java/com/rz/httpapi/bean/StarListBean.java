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
         * custId :
         * custImg :
         * custLevel : 1
         * custNname :
         * nameNotes :
         */

        private String custId;
        private String custImg;
        private int custLevel;
        private String custNname;
        private String nameNotes;

        private int custRole;
        private String custSignature;

        public int getCustRole() {
            return custRole;
        }

        public void setCustRole(int custRole) {
            this.custRole = custRole;
        }

        public String getCustSignature() {
            return custSignature;
        }

        public void setCustSignature(String custSignature) {
            this.custSignature = custSignature;
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

        public String getNameNotes() {
            return nameNotes;
        }

        public void setNameNotes(String nameNotes) {
            this.nameNotes = nameNotes;
        }
    }

    public static class StarInfoBean {
        /**
         * tradeField :
         */

        private String tradeField;

        public String getTradeField() {
            return tradeField;
        }

        public void setTradeField(String tradeField) {
            this.tradeField = tradeField;
        }
    }
}
