package com.rz.httpapi.bean;

/**
 * Created by xiayumo on 16/9/5.
 */
public class FriendRelationModel {
    private int relation;
    private String custId;

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }
}
