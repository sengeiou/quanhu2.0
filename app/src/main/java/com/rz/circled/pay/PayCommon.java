package com.rz.circled.pay;

/**
 * 付款常量类
 */
public class PayCommon {

	/** 1 代表 UTF-8；2 代表 GBK；3 代表 GB2312 */
	public static String inputCharset = "1";
	/** 支付通知结果以此为准，后台通知商户网站支付结果的 url 地址 */
	public static String receiveUrl = "http://rrzchina.com/rrz/app/pay/receive";
	/** 协议版本 */
	public static String version = "v1.0";
	/** 指定支付揑件当前显示诧言，仅国际卡支付有效。 固定选择值：1、2、3 1代表简体中文、2代表繁体中文、3代表英文 */
	public String language = "1";
	/** 签名类型 signType，不可空，0 表示客户用 MD5 验签，1 表示客户用证书验签 */
	public static String signType = "0";
	/** 调用通联支付服务的商户号 */
	public static String merchantId = "100020091218001";
	/** 生成的订单号 */
	public String orderNo = "";
	/** 整型数字 金额与币种有关单位是分，即 10 元提交时金额应为 1000 */
	public int orderAmount = 0;
	/** 订单金额币种 0 或 156–人民币 840–美元 344–港币 */
	public static String orderCurrency = "156";
	/** 订单生成的时间 */
	public String orderDatatime = "";
	/** 商品名称 */
	public String productName = "";
	/**
	 * 支付类型 填 0，表示向持卡人展示所有可用支付类型，并由 持卡人选择。
	 * 填27，则表示直接跳到输卡号界面，可支持借记卡和信用卡的讣证或快捷支付（适用于v2.0或以上版的控件）。 若商户此时开通跨境支付则支持跨境支付。
	 * 填30，则表示直接使用国际卡的支付方式（适用于v2.4.0或以上版的控件）
	 */
	public static String payType = "27";
	/**
	 * 为防止非法篡改要求商户对请求内容进行签名，供服务端进行校验；签名串生成机制：按上述顺序所有非空参数与密钥 key 组合，经加密后生成
	 * signMsg
	 */
	public String signMsg = "";

}
