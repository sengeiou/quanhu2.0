package com.rz.common.utils;

import android.content.Context;

import com.rz.common.R;
import com.rz.common.application.BaseApplication;

/**
 * 币种换算
 */
public class Currency {

    /**
     * 人民币
     */
    public static final String RMB = "156";

    /**
     * 美元
     */
    public static final String USD = "840";

    /**
     * 港币
     */
    public static final String HK = "344";

    /**
     * 日元
     */
    public static final String JPY = "392";

    /**
     * 欧元
     */
    public static final String EURO = "978";

    /**
     * 马来西亚吉特
     */
    public static final String Ringgit = "458";

    /**
     * 澳元
     */
    public static final String AUD = "032";

    /**
     * @param currency 币种
     * @param dollar   钱
     * @param flag     0表示钱，1表示收益
     * @return 返回转化后的钱
     */
    public static String returnDollar(String currency, String dollar, int flag) {
        Context context = BaseApplication.getContext();
        String money = "";
        if (StringUtils.isEmpty(dollar) || Double.parseDouble(dollar) == 0) {
            money = "0.00";
        } else {
            // DecimalFormat df = new DecimalFormat("#.00");
            // money = df.format(d);
            double d = Double.parseDouble(dollar) / 100;
            money = String.format("%.2f", d);
        }
        if (flag == 1) {
            money += context.getString(R.string.rmb_yuan) + " ";
        } else if (flag == 0) {
            if (StringUtils.isEmpty(currency)) {
                currency = "156";
            }
            //悠然币替换了以前币种,暂时不用
//            if (TextUtils.equals(currency, RMB)) {
//                // money = context.getString(R.string.rmb) + " ";
//                money += context.getString(R.string.rmb) + "";
//            } else if (TextUtils.equals(currency, USD)) {
//                money += context.getString(R.string.dollar) + "";
//            } else if (TextUtils.equals(currency, HK)) {
//                money += context.getString(R.string.hk) + "";
//            } else if (TextUtils.equals(currency, JPY)) {
//                money += context.getString(R.string.yen) + "";
//            } else if (TextUtils.equals(currency, EURO)) {
//                money += context.getString(R.string.euro) + "";
//            } else if (TextUtils.equals(currency, Ringgit)) {
//                money += context.getString(R.string.Ringgit) + "";
//            } else if (TextUtils.equals(currency, AUD)) {
//                money += context.getString(R.string.dollar) + "";
//            }
        }
        return money;
    }

    public static String returnDollarNoUnits(String currency, String dollar, int flag) {
        Context context = BaseApplication.getContext();
        String money = "";
        if (StringUtils.isEmpty(dollar) || Double.parseDouble(dollar) == 0) {
            money = "0.00";
        } else {
            // DecimalFormat df = new DecimalFormat("#.00");
            // money = df.format(d);
            double d = Double.parseDouble(dollar) / 100;
            money = String.format("%.2f", d);
        }
        if (flag == 1) {
            money += context.getString(R.string.rmb_yuan) + " ";
        } else if (flag == 0) {
            if (StringUtils.isEmpty(currency)) {
                currency = "156";
            }
        }
        return money;
    }


    public static String returnDollar(String dollar) {
        if (StringUtils.isEmpty(dollar)) {
            return "0悠然币";
        }
        Context context = BaseApplication.getContext();
        String money = "";
        int d = Integer.parseInt(dollar) / 100;
        money = d + context.getString(R.string.rmb_yuan) + "";
        return money;
    }
}
