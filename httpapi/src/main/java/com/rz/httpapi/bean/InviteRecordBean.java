package com.rz.httpapi.bean;

import java.util.List;

/**
 * Created by rzw2 on 2017/11/20.
 */

public class InviteRecordBean {

    /**
     * inviterDetail : [{"custNname":"昵称","inviterId":10,"regTime":13943959594}]
     * total : 10
     */

    private int total;
    private List<InviterDetailBean> inviterDetail;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<InviterDetailBean> getInviterDetail() {
        return inviterDetail;
    }

    public void setInviterDetail(List<InviterDetailBean> inviterDetail) {
        this.inviterDetail = inviterDetail;
    }

    public static class InviterDetailBean {
        /**
         * custNname : 昵称
         * inviterId : 10
         * regTime : 13943959594
         */

        private String custNname;
        private int inviterId;
        private long regTime;

        public String getCustNname() {
            return custNname;
        }

        public void setCustNname(String custNname) {
            this.custNname = custNname;
        }

        public int getInviterId() {
            return inviterId;
        }

        public void setInviterId(int inviterId) {
            this.inviterId = inviterId;
        }

        public long getRegTime() {
            return regTime;
        }

        public void setRegTime(long regTime) {
            this.regTime = regTime;
        }
    }
}
