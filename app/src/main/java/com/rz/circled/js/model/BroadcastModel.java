package com.rz.circled.js.model;

/**
 * Created by Gsm on 2017/8/17.
 */
public class BroadcastModel {
    private String type;
    private BroadcastDataModel data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BroadcastDataModel getData() {
        return data;
    }

    public void setData(BroadcastDataModel data) {
        this.data = data;
    }

    public class BroadcastDataModel {
        private String userId;
        private String custId;

        public String getCustId() {
            return custId;
        }

        public void setCustId(String custId) {
            this.custId = custId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
