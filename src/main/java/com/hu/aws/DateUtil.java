package com.hu.aws;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static java.util.Calendar.*;

/**
 * @Author hutiantian
 * @Date 2018/11/22 15:44:42
 */
@Slf4j
public class DateUtil {

    /**
     * date格式日期转换成string格式
     * @param date 日期
     * @param pattern 模式枚举
     * @return String类型日期
     */
    public static String toString(Date date, DateFormatEnum pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern.toString());
        return sdf.format(date);
    }

    /**
     * String格式的日期转换成date格式
     * @param time String类型日期
     * @param pattern 模板枚举
     * @return Date类型日期
     */
    public static Date toDate(String time, DateFormatEnum pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern.toString());
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            log.error("[Parse Exception]: the time string doesn't match for pattern.");
        }
        return date;
    }

    /**
     * 获取当前日期的前后几天
     * nextDays 正数则往后数，负数往前数
     */
    public static Date nextDays(Date date, int nextDays) {
        GregorianCalendar c1 = new GregorianCalendar();
        c1.setTime(date);
        GregorianCalendar cloneCalendar = (GregorianCalendar) c1.clone();
        cloneCalendar.add(Calendar.DATE, nextDays);
        return cloneCalendar.getTime();
    }

    /**
     * 获取两个日期之间的间隔天数
     */
    public static long dayDiff(Date d1, Date d2) {
        Calendar c1 = new GregorianCalendar();
        Calendar c2 = new GregorianCalendar();
        c1.setTime(d1);
        c2.setTime(d2);
        Calendar c1Copy = new GregorianCalendar(c1.get(YEAR), c1.get(MONTH), c1.get(DATE));
        Calendar c2Copy = new GregorianCalendar(c2.get(YEAR), c2.get(MONTH), c2.get(DATE));
        long diffMillis = c1Copy.getTimeInMillis() - c2Copy.getTimeInMillis();
        long dayMills = 24L * 60L * 60L * 1000L;
        long diffDays = diffMillis / dayMills;
        return diffDays;
    }
}

