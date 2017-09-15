package com.rz.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/9/15 0015.
 */

public class TimeUtil {


    public static String getTime(String createTiem,String closeTime){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = sdf.parse(createTiem);
            date2 = sdf.parse(closeTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long l = date2.getTime() - date1.getTime();
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        return (day + "天" + hour + "小时" + min +"分" + s + "秒");
    }
}
