package com.rz.httpapi.bean;

/**
 * Created by rzw2 on 2017/9/15.
 */

public class PayOrderInfoBean {

    /**
     * callback : http://yehao-callback
     * cost : 10000
     * orderDesc : 测试订单10000
     * orderId : 2017091410480025
     * orderState : 1
     * orderType : 3
     * productDesc : 测试产品描述
     * productId : 1000
     * productType : 1000
     * type : 3
     */

    private String callback;
    private int cost;
    private String orderDesc;
    private String orderId;
    private int orderState;
    private int orderType;
    private String productDesc;
    private String productId;
    private int productType;
    private int type;
    private String notifyStatus;

    public String getNotifyStatus() {
        return notifyStatus;
    }

    public void setNotifyStatus(String notifyStatus) {
        this.notifyStatus = notifyStatus;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
