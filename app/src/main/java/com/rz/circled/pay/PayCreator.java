package com.rz.circled.pay;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PayCreator {

    public static JSONObject randomPaa(String orderNo, String orderAmount,
                                       String orderDatetime, String productName, String cardNo) {

        JSONObject paaParams = new JSONObject();
        try {
            paaParams.put("inputCharset", PayCommon.inputCharset);
            paaParams.put("receiveUrl", PayCommon.receiveUrl);
            paaParams.put("version", PayCommon.version);
            paaParams.put("signType", PayCommon.signType);
            paaParams.put("merchantId", PayCommon.merchantId);
            paaParams.put("orderNo", orderNo);
            paaParams.put("orderAmount", orderAmount);
            paaParams.put("orderCurrency", PayCommon.orderCurrency);
            paaParams.put("orderDatetime", orderDatetime);
            paaParams.put("productName", productName);
            paaParams.put("payType", PayCommon.payType);
            if (!TextUtils.isEmpty(cardNo)) {
                paaParams.put("cardNo", cardNo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] paaParamsArray = {PayCommon.inputCharset, "inputCharset",
                PayCommon.receiveUrl, "receiveUrl", PayCommon.version,
                "version", PayCommon.signType, "signType",
                PayCommon.merchantId, "merchantId", orderNo, "orderNo",
                orderAmount, "orderAmount", PayCommon.orderCurrency,
                "orderCurrency", orderDatetime, "orderDatetime", productName,
                "productName", PayCommon.payType, "payType", "1234567890",
                "key",};

        String paaStr = "";
        for (int i = 0; i < paaParamsArray.length; i++) {
            paaStr += paaParamsArray[i + 1] + "=" + paaParamsArray[i] + "&";
            i++;
        }
        String md5Str1 = md5(paaStr.substring(0, paaStr.length() - 1));
        try {
            paaParams.put("signMsg", md5Str1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return paaParams;
    }

    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        return hexString(hash);
    }

    public static final String hexString(byte[] bytes) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            buffer.append(hexString(bytes[i]));
        }
        return buffer.toString();
    }

    public static final String hexString(byte byte0) {
        char ac[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
                'B', 'C', 'D', 'E', 'F'};
        char ac1[] = new char[2];
        ac1[0] = ac[byte0 >>> 4 & 0xf];
        ac1[1] = ac[byte0 & 0xf];
        String s = new String(ac1);
        return s;
    }
}
