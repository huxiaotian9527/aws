package com.hu.aws;

/**
 * @Author hutiantian
 * @Date 2018/11/22 15:46:07
 */
public enum DateFormatEnum {
    YEAR_MONTH_DAY_HH_MM_SS("yyyy-MM-dd HH:mm:ss"),
    YEAR_MONTH("yyyy-MM"),
    SPLIT_CHAR("-"),
    YEAR_MONTH_DAY("yyyy-MM-dd"),
    MONTH_DAY("MM-dd"),
    YEAR_MONTH_EU("yyyy/MM"),
    MONTH_DAY_YEAR_EU("MM/dd/yyyy"),
    YEAR_MONTH_DAY_EU("yyyy/MM/dd"),
    YEAR_MONTH_DAY_H_M_S_EU("yyyy/MM/dd HH:mm:ss"),
    YEARMONTHDAYHHMMSS("yyyyMMddHHmmss"),
    YEARMONTHDAY("yyyyMMdd"),
    YEARMONTHDAY_HH_MM("yyyyMMdd/HH:mm"),
    YEAR_MONTH_DAY_HH_MM("yyyy-MM-dd HH:mm");

    private String str;

    DateFormatEnum(String s) {
        this.str = s;
    }

    @Override
    public String toString() {
        return str;
    }

}