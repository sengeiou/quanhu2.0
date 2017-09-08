package com.rz.httpapi.bean;

import java.io.Serializable;

public class PayModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 应用ID
	 */
	public String appid;

	/**
	 * 随机字符串
	 */
	public String noncestr;

	/**
	 * 商户号
	 */
	public String partnerid;

	/**
	 * 预支付交易会话ID
	 */
	public String prepayid;

	/**
	 * 签名
	 */
	public String sign;

	/**
	 * 时间戳
	 */
	public String timestamp;

	/**
	 * 支付宝返回字段
	 */
	public String orderStr;
}
