package com.rz.circled.pay;

/**
 * 支付信息
 */
public class WXPayResult {


	static WXPayCallback payCallback;


	public void setPayCallback(WXPayCallback payCallback){

		this.payCallback=payCallback;

	}


	public static void setPayStatus(int payStatus) {

		if(payCallback!=null)
			payCallback.callbackState(payStatus);
	}

	public interface WXPayCallback{

		void callbackState(int status);

	}


}
