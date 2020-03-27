package com.start.demo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间转换工具类
 */
public class TimeUtil {


    //13位时间戳转换yyyyMMddHHmmss日期格式
    public static String dateConversion(Date date){
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sf.format(date);
    }

    //13位时间戳转换yyyy-MM-dd HH:mm:ss日期格式
    public static String dateConversion2(Date date){
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sf.format(date);
    }

    //13位时间戳转换yyyyMMdd 日期格式
    public static String dateConversion3(Date date){
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        return sf.format(date);
    }
}
