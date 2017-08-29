/*
 * Copyright (c) 2014,KJFrameForAndroid Open Source Project,张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rz.common.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.View;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 字符串操作工具包<br>
 * <p/>
 * <b>创建时间</b> 2014-8-14
 *
 * @author kymjs (https://github.com/kymjs)
 * @version 1.1
 */
public class StringUtils {
    private final static Pattern emailer = Pattern
            .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    private final static Pattern phone = Pattern
            .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
    private final static Pattern num = Pattern
            .compile("[0-9]*");

    /**
     * 判断给定字符串是否空白串 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     */
    public static boolean isEmpty(CharSequence input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断给定字符串是否空白串 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     */
    public static boolean isEmpty(CharSequence... strs) {
        for (CharSequence str : strs) {
            if (isEmpty(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是不是一个合法的电子邮件地址
     */
    public static boolean isEmail(CharSequence email) {
        if (isEmpty(email))
            return false;
        return emailer.matcher(email).matches();
    }

    /**
     * 判断是不是一个合法的手机号码
     */
    public static boolean isPhone(CharSequence phoneNum) {
        if (isEmpty(phoneNum))
            return false;
        return phone.matcher(phoneNum).matches();
    }

    /**
     * 判断是不是一个合法的手机号码
     */
    public static boolean isNum(CharSequence str) {
        if (isEmpty(str))
            return false;
        return num.matcher(str).matches();
    }

    /**
     * 返回当前系统时间
     */
    public static String getDataTime(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 对象转整
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * String转long
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * String转double
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static double toDouble(String obj) {
        try {
            return Double.parseDouble(obj);
        } catch (Exception e) {
        }
        return 0D;
    }

    /**
     * 字符串转布尔
     *
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 判断一个字符串是不是数字
     */
    public static boolean isNumber(CharSequence str) {
        try {
            Integer.parseInt(str.toString());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * byte[]数组转换为16进制的字符串。
     *
     * @param data 要转换的字节数组。
     * @return 转换后的结果。
     */
    public static final String byteArrayToHexString(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length * 2);
        for (byte b : data) {
            int v = b & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.getDefault());
    }

    /**
     * 16进制表示的字符串转换为字节数组。
     *
     * @param s 16进制表示的字符串
     * @return byte[] 字节数组
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] d = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
            d[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
                    .digit(s.charAt(i + 1), 16));
        }
        return d;
    }

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater3 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("MM-dd HH:mm");
        }
    };

    /**
     * 以友好的方式显示时间
     *
     * @param sdate
     * @return
     */
    public static String friendlyTime(String sdate) {
        Date time = null;

        if (isInEasternEightZones()) {
            time = toDate(sdate);
        } else {
            time = transformTime(toDate(sdate), TimeZone.getTimeZone("GMT+08"),
                    TimeZone.getDefault());
        }

        if (time == null) {
            return "Unknown";
        }
        String ftime = "";
        Calendar cal = Calendar.getInstance();

        // 判断是否是同一天
        String curDate = dateFormater2.get().format(cal.getTime());
        String paramDate = dateFormater2.get().format(time);
        if (curDate.equals(paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max(
                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "分钟前";
            else
                ftime = hour + "小时前";
            return ftime;
        }

        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max(
                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "分钟前";
            else
                ftime = hour + "小时前";
        } else if (days == 1) {
            ftime = "昨天";
        } else if (days == 2) {
            ftime = "前天 ";
        } else if (days > 2 && days < 31) {
            ftime = days + "天前";
        } else if (days >= 31 && days <= 2 * 31) {
            ftime = "一个月前";
        } else if (days > 2 * 31 && days <= 3 * 31) {
            ftime = "2个月前";
        } else if (days > 3 * 31 && days <= 4 * 31) {
            ftime = "3个月前";
        } else {
            ftime = dateFormater2.get().format(time);
        }
        return ftime;
    }

    public static boolean isYestoday(String sdate) {
        boolean isYestoday = false;
        Date time = null;

        if (isInEasternEightZones()) {
            time = toDate(sdate);
        } else {
            time = transformTime(toDate(sdate), TimeZone.getTimeZone("GMT+08"),
                    TimeZone.getDefault());
        }

        if (time == null) {
            return isYestoday;
        }
        Calendar cal = Calendar.getInstance();


        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 1) {
            return true;
        }
        return false;
    }

    /**
     * 格式化时间（输出类似于 刚刚, 4分钟前, 一小时前, 昨天这样的时间）
     *
     * @param time
     * @return time为null，或者时间格式不匹配，输出空字符""
     */
    public static String formatDisplayTime(long time) {
        return formatDisplayTime(time + "", "yyyy-MM-dd HH:mm:ss");
    }


    /**
     * 格式化时间（输出类似于 刚刚, 4分钟前, 一小时前, 昨天这样的时间）
     *
     * @param time    需要格式化的时间 如"2014-07-14 19:01:45"
     * @param pattern 输入参数time的时间格式 如:"yyyy-MM-dd HH:mm:ss"
     *                <p/>
     *                如果为空则默认使用"yyyy-MM-dd HH:mm:ss"格式
     * @return time为null，或者时间格式不匹配，输出空字符""
     */
    public static String formatDisplayTime(String time,
                                           String pattern) {
        String display = "";
        int tMin = 60 * 1000;
        int tHour = 60 * tMin;
        int tDay = 24 * tHour;

        if (time != null) {
            try {
                String mTime;
                if (isNumRic(time)) {
                    SimpleDateFormat format = new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss");
                    mTime = format.format(Long.parseLong(time));
                } else {
                    if (!TextUtils.isEmpty(time) && time.contains(".")) {
                        String[] timeSplit = time.split("\\.");
                        mTime = timeSplit[0];
                    } else {
                        mTime = time;
                    }
                }
                Date tDate = new SimpleDateFormat(pattern).parse(mTime);
                Date today = new Date();
                SimpleDateFormat thisYearDf = new SimpleDateFormat("yyyy");
                SimpleDateFormat todayDf = new SimpleDateFormat("yyyy-MM-dd");
                Date thisYear = new Date(thisYearDf.parse(
                        thisYearDf.format(today)).getTime());
                Date yesterday = new Date(todayDf.parse(todayDf.format(today))
                        .getTime());
                Date beforeYes = new Date(yesterday.getTime() - tDay);
                if (tDate != null) {
                    SimpleDateFormat halfDf = new SimpleDateFormat(
                            "MM-dd HH:mm");
                    long dTime = today.getTime() - tDate.getTime();
                    if (tDate.before(thisYear)) {
                        display = new SimpleDateFormat("yyyy-MM-dd HH:mm")
                                .format(tDate);
                    } else {

                        if (dTime < tMin) {
                            display = "刚刚";
                        } else if (dTime < tHour) {
                            display = (int) Math.ceil(dTime / tMin) + "分钟前";
                        } else if (dTime < tDay && tDate.after(yesterday)) {
                            display = (int) Math.ceil(dTime / tHour) + "小时前";
                        } else if (tDate.after(beforeYes)
                                && tDate.before(yesterday)) {
                            display = "昨天"
                                    + new SimpleDateFormat("HH:mm")
                                    .format(tDate);
                        } else {
                            display = halfDf.format(tDate);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return display;
    }

    public static boolean isYestory(String time,
                                    String pattern) {
        boolean isYestory = false;
        String display = "";
        int tMin = 60 * 1000;
        int tHour = 60 * tMin;
        int tDay = 24 * tHour;

        if (time != null) {
            try {
                SimpleDateFormat format = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                String mTime = format.format(Long.parseLong(time));

                Date tDate = new SimpleDateFormat(pattern).parse(mTime);
                Date today = new Date();
                SimpleDateFormat thisYearDf = new SimpleDateFormat("yyyy");
                SimpleDateFormat todayDf = new SimpleDateFormat("yyyy-MM-dd");
                Date thisYear = new Date(thisYearDf.parse(
                        thisYearDf.format(today)).getTime());
                Date yesterday = new Date(todayDf.parse(todayDf.format(today))
                        .getTime());
                Date beforeYes = new Date(yesterday.getTime() - tDay);
                if (tDate != null) {
                    SimpleDateFormat halfDf = new SimpleDateFormat(
                            "MM-dd HH:mm");
                    long dTime = today.getTime() - tDate.getTime();
                    if (tDate.after(beforeYes)
                            && tDate.before(yesterday)) {
                        return true;

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return isYestory;
    }

    public static Date toDateByDetail(String sdate) {
        return toDate(sdate, dateFormater3.get());
    }

    /**
     * 将字符串转位日期类型
     *
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        return toDate(sdate, dateFormater.get());
    }

    public static Date toDate(String sdate, SimpleDateFormat dateFormater) {
        try {
            return dateFormater.parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    public static boolean isYesterday(long when) {
        Time time = new Time();
        time.set(when);

        int thenYear = time.year;
        int thenMonth = time.month;
        int thenMonthDay = time.monthDay;

        time.set(System.currentTimeMillis());
        return (thenYear == time.year)
                && (thenMonth == time.month)
                && (thenMonthDay - 1 == time.monthDay);
    }

    public static boolean isYesterday(String when) {
        long times = 0;
        try {
            times = Long.valueOf(when);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Time time = new Time();
        time.set(times);

        int thenYear = time.year;
        int thenMonth = time.month;
        int thenMonthDay = time.monthDay;

        time.set(System.currentTimeMillis());
        return (thenYear == time.year)
                && (thenMonth == time.month)
                && (thenMonthDay == time.monthDay - 1);
    }

    public static boolean isToday(String when) {
        long times = 0;
        try {
            times = Long.valueOf(when);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Time time = new Time();
        time.set(times);
        int thenYear = time.year;
        int thenMonth = time.month;
        int thenMonthDay = time.monthDay;

        time.set(System.currentTimeMillis());
        Log.d("test", "isToday when " + when + "  timeStamp" + time.toString());
        return (thenYear == time.year)
                && (thenMonth == time.month)
                && (thenMonthDay == time.monthDay);
    }

    public static boolean isToday(long when) {
        Time time = new Time();
        time.set(when);

        int thenYear = time.year;
        int thenMonth = time.month;
        int thenMonthDay = time.monthDay;

        time.set(System.currentTimeMillis());
        return (thenYear == time.year)
                && (thenMonth == time.month)
                && (thenMonthDay == time.monthDay);
    }

    public static boolean isToyear(long when) {
        Time time = new Time();
        time.set(when);

        int thenYear = time.year;

        time.set(System.currentTimeMillis());
        return (thenYear == time.year);
    }

    public static String getDetailStrYear(String createDate) {
        if (StringUtils.isEmpty(createDate) || TextUtils.equals("null", createDate)) {
            return "";
        }
        try {
            long time = Long.valueOf(createDate);
            Date date = new Date(time);
            SimpleDateFormat sdf = null;
//            if (isToday(time)) {
//                sdf = new SimpleDateFormat("HH:mm");
//            } else
//            if (isToyear(time)) {
////                sdf = new SimpleDateFormat("MM-dd HH:mm");
//                sdf = new SimpleDateFormat("MM-dd");
//            } else {
            sdf = new SimpleDateFormat("yyyy.MM.dd");
//            }
            String tim = sdf.format(date);
            return tim;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDetailStr(String createDate) {
        if (StringUtils.isEmpty(createDate) || TextUtils.equals("null", createDate)) {
            return "";
        }
        try {
            long time = Long.valueOf(createDate);
            Date date = new Date(time);
            SimpleDateFormat sdf = null;
            if (isToday(time)) {
                sdf = new SimpleDateFormat("HH:mm");
            } else if (isToyear(time)) {
//                sdf = new SimpleDateFormat("MM-dd HH:mm");
                sdf = new SimpleDateFormat("MM-dd");
            } else {
                sdf = new SimpleDateFormat("yyyy-MM-dd");
            }
            String tim = sdf.format(date);
            return tim;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDetailStr1(long createDate) {
        if (createDate == 0) {
            return "";
        }
        try {
            long time = Long.valueOf(createDate);
            Date date = new Date(time);
            SimpleDateFormat sdf = null;
            if (isToday(time)) {
                sdf = new SimpleDateFormat("HH:mm");
            } else if (isToyear(time)) {
                sdf = new SimpleDateFormat("MM-dd");
            } else {
                sdf = new SimpleDateFormat("yyyy-MM-dd");
            }
            String tim = sdf.format(date);
            return tim;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDetailStr1(String createDate) {
        if (StringUtils.isEmpty(createDate) || TextUtils.equals("null", createDate)) {
            return "";
        }
        try {
            long time = Long.valueOf(createDate);
            Date date = new Date(time);
            SimpleDateFormat sdf = null;
            if (isToday(time)) {
                sdf = new SimpleDateFormat("HH:mm");
            } else if (isToyear(time)) {
                sdf = new SimpleDateFormat("MM-dd");
            } else {
                sdf = new SimpleDateFormat("yyyy-MM-dd");
            }
            String tim = sdf.format(date);
            return tim;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDetailStr1(Long createDate) {
        if (createDate == 0l) {
            return "";
        }
        try {
            Date date = new Date(createDate);
            SimpleDateFormat sdf = null;
            if (isToday(createDate)) {
                sdf = new SimpleDateFormat("HH:mm");
            } else if (isToyear(createDate)) {
                sdf = new SimpleDateFormat("MM-dd");
            } else {
                sdf = new SimpleDateFormat("yyyy-MM-dd");
            }
            String tim = sdf.format(date);
            return tim;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String getMonth(String createDate) {
        if (StringUtils.isEmpty(createDate) || TextUtils.equals("null", createDate)) {
            return "";
        }
        try {
            long time = Long.valueOf(createDate);
            Date date = new Date(time);

            int tim = date.getMonth();

            return tim + "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDay(String createDate) {
        if (StringUtils.isEmpty(createDate) || TextUtils.equals("null", createDate)) {
            return "";
        }
        try {
            long time = Long.valueOf(createDate);
            Date date = new Date(time);

            int tim = date.getDay();

            return tim + "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Long StrToTimeStamp(String time) {
        Date date = new Date();
        try {
            //Date或者String转化为时间戳
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = format.parse(time);

        } catch (Exception e) {

        }

        return date.getTime();
    }


    public static String getCurrentDay(long createDate) {
        try {
            Date date = new Date(createDate);
            SimpleDateFormat sdf = null;
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String tim = sdf.format(date);
            return tim;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 判断用户的设备时区是否为东八区（中国） 2014年7月31日
     *
     * @return
     */
    public static boolean isInEasternEightZones() {
        boolean defaultVaule = true;
        if (TimeZone.getDefault() == TimeZone.getTimeZone("GMT+08"))
            defaultVaule = true;
        else
            defaultVaule = false;
        return defaultVaule;
    }

    /**
     * 根据不同时区，转换时间 2014年7月31日
     */
    public static Date transformTime(Date date, TimeZone oldZone,
                                     TimeZone newZone) {
        Date finalDate = null;
        if (date != null) {
            int timeOffset = oldZone.getOffset(date.getTime())
                    - newZone.getOffset(date.getTime());
            finalDate = new Date(date.getTime() - timeOffset);
        }
        return finalDate;
    }

    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        // p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
        // p = Pattern
        // .compile("^(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$");
//        p = Pattern.compile("^[1][3578][0-9]{9}$");
        p = Pattern.compile("^((13|14|15|17|18)\\d{9})$");
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    /**
     * 判断是否是纯数字
     *
     * @param str
     * @return
     */
    public static boolean isNumRic(String str) {
        return str.matches("[0-9]*");
    }

    /**
     * 判断是否是纯字母
     *
     * @param str
     * @return
     */
    public static boolean isLetterRic(String str) {
        return str.matches("[a-zA-Z]+");
    }


    public static String formatTime(long ms) {
        if (ms == 0) {
            return "00:00:00";
        }
        // 将毫秒数换算成x天x时x分x秒x毫秒
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second
                * ss;

        String strDay = day < 10 ? "0" + day : "" + day;
        String strHour = hour < 10 ? "0" + hour : "" + hour;
        String strMinute = minute < 10 ? "0" + minute : "" + minute;
        String strSecond = second < 10 ? "0" + second : "" + second;
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : ""
                + milliSecond;
        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : ""
                + strMilliSecond;
        return strHour + ":" + strMinute + ":" + strSecond + " ";
    }

    public static String formatDuration(long ms) {
        if (ms == 0) {
            return "00:00:00";
        }
        // 将毫秒数换算成x天x时x分x秒x毫秒
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second
                * ss;

        String strDay = day < 10 ? "0" + day : "" + day;
        String strHour = hour < 10 ? "0" + hour : "" + hour;
        String strMinute = minute < 10 ? "0" + minute : "" + minute;
        String strSecond = second < 10 ? "0" + second : "" + second;
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : ""
                + milliSecond;
        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : ""
                + strMilliSecond;
        if (hour == 0) {
            return strMinute + ":" + strSecond;
        }
        return strHour + ":" + strMinute + ":" + strSecond + " ";
    }


    public static String retrunSecondsTime(long ms) {
        if (ms == 0) {
            return "00:00:00";
        }
        // 将毫秒数换算成x天x时x分x秒x毫秒
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second
                * ss;

        String strDay = day < 10 ? "0" + day : "" + day;
        String strHour = hour < 10 ? "0" + hour : "" + hour;
        String strMinute = minute < 10 ? "0" + minute : "" + minute;
        String strSecond = second < 10 ? "0" + second : "" + second;
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : ""
                + milliSecond;
        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : ""
                + strMilliSecond;
        return strSecond + " ";
    }


    public static String returnMiniteTime(long ms) {
        if (ms == 0) {
            return "00:00:00";
        }
        // 将毫秒数换算成x天x时x分x秒x毫秒
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second
                * ss;

        String strDay = day < 10 ? "0" + day : "" + day;
        String strHour = hour < 10 ? "0" + hour : "" + hour;
        String strMinute = minute < 10 ? "0" + minute : "" + minute;
        String strSecond = second < 10 ? "0" + second : "" + second;
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : ""
                + milliSecond;
        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : ""
                + strMilliSecond;
        return strMinute;
    }

    /**
     * 根据时间来命名图片名字 如：15028998329.jpg
     *
     * @return
     */
    public static String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    /**
     * 字符串隐藏
     *
     * @param str
     * @param start
     * @param end
     * @return
     */
    public static String replaceSubString(String str, int start, int end) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        String newStr = "";
        String s = "";
        String e = "";
        if (start != 0) {
            s = str.substring(0, start);
        }
        if (end != str.length()) {
            e = str.substring(str.length() - end, str.length());
        }
        StringBuffer sb = new StringBuffer();
        for (int i = start; i < str.length() - end; i++) {
            sb = sb.append("*");
        }
        newStr = s + sb.toString() + e;
        return newStr;
    }

    /**
     * 固定长度16位字符串隐藏
     *
     * @param str
     * @param start 首部保留几位
     * @param end   末尾保留几位
     * @return
     */
    public static String replace16Sub(String str, int start, int end) {
        String newStr = "";
        String s = "";
        String e = "";
        if (start != 0) {
            s = str.substring(0, start);
        }
        if (end != str.length()) {
            e = str.substring(end, str.length());
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 16 - s.length() - e.length(); i++) {
            sb = sb.append("*");
        }
        newStr = s + sb.toString() + e;
        return newStr;
    }

    /**
     * 银行卡 *隐藏
     *
     * @param str
     * @param end
     * @return
     */
    public static String replaceBankString(String str, int end) {
        String card = replace16Sub(str, 0, end);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < card.length(); i++) {
            if (i == 0) {
                sb.append(card.charAt(i));
            } else {
                if (i % 4 == 0) {
                    sb.append(" " + card.charAt(i));
                } else {
                    sb.append(card.charAt(i));
                }
            }
        }
        return sb.toString();
    }

    /**
     * 显示本地图片
     *
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 用来获取手机拨号上网（包括CTWAP和CTNET）时由PDSN分配给手机终端的源IP地址。
     *
     * @return
     * @author SHANHY
     */
    public static String getPsdnIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        // if (!inetAddress.isLoopbackAddress() && inetAddress
                        // instanceof Inet6Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
        }
        return "";
    }

    public static String getPrintSize(long size) {
        //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return String.valueOf(size) + "B";
        } else {
            size = size / 1024;
        }
        //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        //因为还没有到达要使用另一个单位的时候
        //接下去以此类推
        if (size < 1024) {
            return String.valueOf(size) + "KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            //因为如果以MB为单位的话，要保留最后1位小数，
            //因此，把此数乘以100之后再取余
            size = size * 100;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "MB";
        } else {
            //否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "GB";
        }
    }

    /**
     * 获得实际的宽度
     *
     * @param view
     * @return
     */
    public static int getMeasureWidth(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        int width = view.getMeasuredWidth();
        return width;
    }

    /**
     * 获得实际的高度
     *
     * @param view
     * @return
     */
    public static int getMeasureHeight(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        int height = view.getMeasuredHeight();
        return height;
    }


    /*
    * 将时间戳转换为时间
    */
    public static String stampToDate(String s, String formart) {
        if (StringUtils.isEmpty(s)) {
            return "";
        }
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formart);
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /*
   * 将时间戳转换为时间
   */
    public static String stampToDate(Long s, String formart) {
        if (s == 0l) {
            return "";
        }
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formart);
        Date date = new Date(s);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static final int SECONDS_IN_DAY = 60 * 60 * 24;
    public static final long MILLIS_IN_DAY = 1000L * SECONDS_IN_DAY;

    public static boolean isSameDayOfMillis(final String ms1, final String ms2) {
        final long interval = Long.parseLong(ms1) - Long.parseLong(ms2);
        return interval < MILLIS_IN_DAY
                && interval > -1L * MILLIS_IN_DAY
                && toDay(Long.parseLong(ms1)) == toDay(Long.parseLong(ms2));
    }

    private static long toDay(long millis) {
        return (millis + TimeZone.getDefault().getOffset(millis)) / MILLIS_IN_DAY;
    }


    public static int getMonthByTimeStamp(long timeStamp) {
        String date = timeStampToDate(timeStamp);
        String month = date.substring(5, 7);
        return Integer.parseInt(month);
    }


    public static String timeStampToDate(long timeStamp) {
        Date date = new Date(timeStamp);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }

    public static int getDayByTimeStamp(long timeStamp) {
        String date = timeStampToDate(timeStamp);
        String day = date.substring(8, 10);
        return Integer.parseInt(day);
    }

    private static final String MAC_NAME = "HmacSHA1";
    private static final String ENCODING = "UTF-8";

    /*
     * 展示了一个生成指定算法密钥的过程 初始化HMAC密钥
     * @return
     * @throws Exception
     *
      public static String initMacKey() throws Exception {
      //得到一个 指定算法密钥的密钥生成器
      KeyGenerator KeyGenerator keyGenerator =KeyGenerator.getInstance(MAC_NAME);
      //生成一个密钥
      SecretKey secretKey =keyGenerator.generateKey();
      return null;
      }
     */

    /**
     * 使用 HMAC-SHA1 签名方法对对encryptText进行签名
     *
     * @param encryptText 被签名的字符串
     * @param encryptKey  密钥
     * @return
     * @throws Exception
     */
    public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception {
        byte[] data = encryptKey.getBytes(ENCODING);
        //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
        SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
        //生成一个指定 Mac 算法 的 Mac 对象
        Mac mac = Mac.getInstance(MAC_NAME);
        //用给定密钥初始化 Mac 对象
        mac.init(secretKey);

        byte[] text = encryptText.getBytes(ENCODING);
        //完成 Mac 操作
        return mac.doFinal(text);
    }

//    public static CharSequence getGreen(Context context, String source) {
//        if (!TextUtils.isEmpty(source)) {
//            SpannableString spannableString = new SpannableString(source);
//            spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color_main)), 0, source.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            return spannableString;
//        }
//        return "";
//    }
//
//    public static CharSequence getRed(Context context, String source) {
//        if (!TextUtils.isEmpty(source)) {
//            SpannableString spannableString = new SpannableString(source);
//            spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color_ff2f2f)), 0, source.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            return spannableString;
//        }
//        return "";
//    }
//
//    public static CharSequence getPurple(Context context, String source) {
//        if (!TextUtils.isEmpty(source)) {
//            SpannableString spannableString = new SpannableString(source);
//            spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color_FA63A3)), 0, source.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            return spannableString;
//        }
//        return "";
//    }
//
//    public static CharSequence getBlue(Context context, String source) {
//        if (!TextUtils.isEmpty(source)) {
//            SpannableString spannableString = new SpannableString(source);
//            spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color_1B97D0)), 0, source.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            return spannableString;
//        }
//        return "";
//    }
//
//    public static CharSequence getYellow(Context context, String source) {
//        if (!TextUtils.isEmpty(source)) {
//            SpannableString spannableString = new SpannableString(source);
//            spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color_f49923)), 0, source.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            return spannableString;
//        }
//        return "";
//    }

    //判断是否是数字开头如果是就截取
    public static List<String> getNumbers(String content) {
        Pattern pattern = Pattern.compile("^(\\d+)[^\\d]*");
        Matcher matcher = pattern.matcher(content);
        List<String> list = new ArrayList<>();
        if (matcher.find()) {
            list.add(matcher.group(1));
            if (!TextUtils.equals("content", list.get(0))) {
                list.add(content.replace(list.get(0), ""));
            }
        } else {
            list.add(content);
        }
        return list;
    }

    //毫秒转化为时分秒
    public static String formatTime(Long ms) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        StringBuffer sb = new StringBuffer();
        if (day > 0) {
            sb.append(day + "天");
        }
        if (hour > 0) {
            sb.append(hour + "小时");
        }
        if (minute > 0) {
            sb.append(minute + "分");
        }
        if (second > 0) {
            sb.append(second + "秒");
        }
        if (milliSecond > 0) {
            sb.append(milliSecond + "毫秒");
        }
        return sb.toString();
    }

    /**
     * 获取时间间隔
     *
     * @param millisecond
     * @return
     */
    public static String getSpaceTime(Long millisecond) {
//        Calendar calendar = Calendar.getInstance();
//        Long currentMillisecond = calendar.getTimeInMillis();

        //间隔秒
//        Long spaceSecond = (currentMillisecond - millisecond) / 1000;
        Long spaceSecond = (millisecond) / 1000;
        //一分钟之内
        if (spaceSecond >= 0 && spaceSecond < 60) {
            return spaceSecond + "秒";
        }
        //一小时之内
        else if (spaceSecond / 60 > 0 && spaceSecond / 60 < 60) {
            return spaceSecond / 60 + "分钟";
        }
        //一天之内
        else if (spaceSecond / (60 * 60) > 0 && spaceSecond / (60 * 60) < 24) {
            return spaceSecond / (60 * 60) + "小时";
        }
        //3天之内
        else if (spaceSecond / (60 * 60 * 24) > 0 && spaceSecond / (60 * 60 * 24) < 3) {
            return spaceSecond / (60 * 60 * 24) + "天";
        } else {
            return getDateTimeFromMillisecond(millisecond);
        }
    }

    /**
     * 首页动态条目时间显示格式
     * 获取时间间隔
     *
     * @param startTime   开始时间
     * @param millisecond 结束(当前)时间 - 开始时间 = 时间间隔
     * @return
     */
    public static String getSpaceTimeNo1(Long millisecond, Long startTime) {
//        Calendar calendar = Calendar.getInstance();
//        Long currentMillisecond = calendar.getTimeInMillis();

        //间隔秒
//        Long spaceSecond = (currentMillisecond - millisecond) / 1000;
        Long spaceSecond = (millisecond) / 1000;
        //一分钟之内
        if (spaceSecond >= 0 && spaceSecond < 60) {
//            return spaceSecond + "秒";
            return spaceSecond + "刚刚";//1分钟以内，显示：刚刚
        }
        //一小时之内
        else if (spaceSecond / 60 > 0 && spaceSecond / 60 < 60) {
            return spaceSecond / 60 + "分钟前";//1小时内，显示：xx分钟前
        }
        //一天之内
        else if (spaceSecond / (60 * 60) > 0 && spaceSecond / (60 * 60) < 24) {
            return spaceSecond / (60 * 60) + "小时前";//小于24小时，显示：xx小时前
        }
        //超过24小时
        else if (spaceSecond / (60 * 60) >= 24 && spaceSecond / (60 * 60) < 48) {
//            return spaceSecond / (60 * 60 * 24) + "天";
            return "昨天" + getDateTimeFromMillisecondNo1(startTime);//间隔超过24小时，显示：昨天xx:xx
        }
        //超过48小时
        else if (spaceSecond / (60 * 60) >= 48 && spaceSecond / (60 * 60) < 365 * 24) {
//            return spaceSecond / (60 * 60 * 24) + "天";
            return getDateTimeFromMillisecondNo2(startTime);//间隔超过48小时，显示：xx月xx日 xx:xx
        }
//        //3天之内
//        else if (spaceSecond / (60 * 60 * 24) > 0 && spaceSecond / (60 * 60 * 24) < 3) {
//            return spaceSecond / (60 * 60 * 24) + "天";
//        }
        else {
            return getDateTimeFromMillisecond3(millisecond);//间隔跨自然年，显示：xxxx年xx月xx日 xx:xx
        }
    }


    /**
     * 将毫秒转化成固定格式的时间
     * 时间格式:fomat
     *
     * @param millisecond
     * @return
     */
    public static String getDateTimeFromMillisecond(Long millisecond, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = new Date(millisecond);
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }

    /**
     * 将毫秒转化成固定格式的时间
     * 时间格式: yyyy-MM-dd HH:mm:ss
     *
     * @param millisecond
     * @return
     */
    public static String getDateTimeFromMillisecond(Long millisecond) {//new SimpleDateFormat("HH:mm")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(millisecond);
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }

    /**
     * 将毫秒转化成固定格式的时间
     * 时间格式: HH:mm
     *
     * @param millisecond
     * @return
     */
    public static String getDateTimeFromMillisecondNo1(Long millisecond) {//new SimpleDateFormat("HH:mm")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date(millisecond);
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }

    /**
     * 将毫秒转化成固定格式的时间
     * 时间格式: HH:mm
     *
     * @param millisecond
     * @return
     */
    public static String getDateTimeFromMillisecondNo2(Long millisecond) {//new SimpleDateFormat("HH:mm")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日 HH:mm");
        Date date = new Date(millisecond);
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }

    /**
     * 将毫秒转化成固定格式的时间
     * 时间格式: yyyy-MM-dd HH:mm:ss
     *
     * @param millisecond
     * @return
     */
    public static String getDateTimeFromMillisecond3(Long millisecond) {//new SimpleDateFormat("HH:mm")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        Date date = new Date(millisecond);
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }
}
