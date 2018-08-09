package com.qianfeng.common.DealWithFile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ligui
 * 验证日期合法性:2008-02-23
 */
public class CheckOfDate {
    public static boolean validate(String date) {
        //验证长度
        Pattern p = Pattern.compile("\\d{4}+[-]+\\d{1,2}+[-]+\\d{1,2}");
        Matcher m = p.matcher(date);
        if (!m.matches()) {
            return false;
        }
        //验证月份天数
        String[] arrDate = date.split("-");
        int year = Integer.valueOf(arrDate[0]);
        int month = Integer.valueOf(arrDate[1]);
        int day = Integer.valueOf(arrDate[2]);
        if (month < 1 || month > 12) {
            return false;
        }
        switch (month) {
            case 4:
            case 6:
            case 9:
            case 11:
                return (day == 30) ? true : false;
            case 2: {
                if (isLeapYear(year)) {
                    return (day == 28) ? true : false;

                } else {
                    return (day == 29) ? true : false;
                }
            }
            default:
                return (day == 31) ? true : false;
        }
    }

    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 == 0 || year % 400 == 0);
    }

    //测试
    public static void main(String[] args) {
        // System.out.println(validate("2000-2-28"));
    }
}
