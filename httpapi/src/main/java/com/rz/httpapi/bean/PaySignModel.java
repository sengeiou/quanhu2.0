package com.rz.httpapi.bean;

import java.io.Serializable;

/**
 * Created by xiayumo on 16/8/24.
 * 支付返回值
 */
public class PaySignModel implements Serializable {


    /**
     * 交易流水号
     */
    public String orderId;

    /**
     * 商品名字
     */
    public String productName;

    /**
     * 订单提交时间
     */
    public String orderDatetime;

    /**
     * 订单金额
     */
    public String orderAmount;

    /**
     * 币种
     */
    public String orderCurrency;

    /**
     * 验签字符串
     * 扩展字段（手机微信支付或支付宝支付时，会有此值）
     */
    public Sign ext;

    public class Sign {

        //手机端调用支付api需要的订单信息，里面已完全拼装好各字段，包括签名
        public String orderStr;

        public String appid;

        public String noncestr;

//        public String package;

        public String partnerid;

        public String prepayid;

        public String sign;

        public String timestamp;
    }
}
