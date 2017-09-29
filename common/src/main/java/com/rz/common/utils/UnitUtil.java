package com.rz.common.utils;


import com.rz.common.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xiayumo on 16/8/29.
 */
public class UnitUtil {


    /**
     * 将元为单位的转换为分 替换小数点，支持以逗号区分的金额
     *
     * @param amount
     * @return
     */
    public static long changeY2F(String amount) {
        String currency = amount.replaceAll("\\$|\\￥|\\,", "");  //处理包含, ￥ 或者$的金额
        int index = currency.indexOf(".");
        int length = currency.length();
        Long amLong = 0l;
        if (index == -1) {
            amLong = Long.valueOf(currency + "00");
        } else if (length - index >= 3) {
            amLong = Long.valueOf((currency.substring(0, index + 3)).replace(".", ""));
        } else if (length - index == 2) {
            amLong = Long.valueOf((currency.substring(0, index + 2)).replace(".", "") + 0);
        } else {
            amLong = Long.valueOf((currency.substring(0, index + 1)).replace(".", "") + "00");
        }
        return amLong;
    }

    /**
     *  分转换元
     *
     * @param amount
     * @return
     */
    public static String changeF2Y(Long amount) {
        int flag = 0;
        String amString = amount.toString();
        if (amString.charAt(0) == '-') {
            flag = 1;
            amString = amString.substring(1);
        }
        StringBuffer result = new StringBuffer();
        if (amString.length() == 1) {
            result.append("0.0").append(amString);
        } else if (amString.length() == 2) {
            result.append("0.").append(amString);
        } else {
            String intString = amString.substring(0, amString.length() - 2);
            for (int i = 1; i <= intString.length(); i++) {
                if ((i - 1) % 3 == 0 && i != 1) {
                    result.append(",");
                }
                result.append(intString.substring(intString.length() - i, intString.length() - i + 1));
            }
            result.reverse().append(".").append(amString.substring(amString.length() - 2));
        }
        if (flag == 1) {
            return "-" + result.toString();
        } else {
            return result.toString();
        }
    }

    // date类型转换为String类型
    // formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    // data Date类型的时间
    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }

    // long类型转换为String类型
    // currentTime要转换的long类型的时间
    // formatType要转换的string类型的时间格式
    public static String longToString(long currentTime, String formatType) {
        Date date = longToDate(currentTime, formatType); // long类型转成Date类型
        String strTime = dateToString(date, formatType); // date类型转成String
        return strTime;
    }

    // string类型转换为date类型
    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        try {
            date = formatter.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    // long转换为Date类型
    // currentTime要转换的long类型的时间
    // formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    public static Date longToDate(long currentTime, String formatType) {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }

    // string类型转换为long类型
    // strTime要转换的String类型的时间
    // formatType时间格式
    // strTime的时间格式和formatType的时间格式必须相同
    public static long stringToLong(String strTime, String formatType) {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }

    // date类型转换为long类型
    // date要转换的date类型的时间
    public static long dateToLong(Date date) {
        return date.getTime();
    }


    public static int checkBankLogo(String bankCode) {
        int logo = R.drawable.ic_height_bg;
        if (StringUtils.isEmpty(bankCode)) {
            return logo;
        }

        if (bankCode.contains("工商")) {
            logo = R.drawable.gongshang;
        } else if (bankCode.contains("光大")) {
            logo = R.drawable.guangda;
        } else if (bankCode.contains("广发")) {
            logo = R.drawable.guangfa;
        } else if (bankCode.contains("汉口")) {
            logo = R.drawable.hankou;
        } else if (bankCode.contains("华夏")) {
            logo = R.drawable.huaxia;
        } else if (bankCode.contains("建设")) {
            logo = R.drawable.jianshe;
        } else if (bankCode.contains("交通")) {
            logo = R.drawable.jiaotong;
        } else if (bankCode.contains("民生")) {
            logo = R.drawable.mingsheng;
        } else if (bankCode.contains("农业") || bankCode.contains("农")) {
            logo = R.drawable.nongye;
        } else if (bankCode.contains("平安")) {
            logo = R.drawable.pingan;
        } else if (bankCode.contains("浦发")) {
            logo = R.drawable.pufa;
        } else if (bankCode.contains("齐商")) {
            logo = R.drawable.qishang;
        } else if (bankCode.contains("上海")) {
            logo = R.drawable.shanghai;
        } else if (bankCode.contains("泰安")) {
            logo = R.drawable.taian;
        } else if (bankCode.contains("兴业")) {
            logo = R.drawable.xingye;
        } else if (bankCode.contains("枣庄")) {
            logo = R.drawable.zaozhuang;
        } else if (bankCode.contains("招商")) {
            logo = R.drawable.zhaoshang;
        } else if (bankCode.contains("邮政") || bankCode.contains("邮")) {
            logo = R.drawable.youzheng;
        } else if (bankCode.contains("中信")) {
            logo = R.drawable.zhongxin;
        } else if (bankCode.contains("中国银行") || bankCode.contains("中国")) {
            logo = R.drawable.zhongguo;
        }
        return logo;
    }
}
