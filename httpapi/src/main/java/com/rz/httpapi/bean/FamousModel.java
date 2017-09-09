package com.rz.httpapi.bean;

/**
 * Created by Administrator on 2017/6/8/008.
 */

public class FamousModel {

    /**
     * custInfo : {"custId":"","custImg":"","custLevel":1,"custNname":"","nameNotes":""}
     * starInfo : {"tradeField":""}
     */

    public CustInfoBean custInfo;
    public StarInfoBean starInfo;

    public class CustInfoBean {
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
        public int custRole;
        private String custNname;
        private String nameNotes;
        public String custDesc;

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
