package com.common.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by taomingzhu on 2018/6/25.
 */
public class DateUtil {

    //比较两个日期相差几个月
    public static int getDiffBetweenMonths(Date date1, Date date2) {
        int result = 0;
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(date1);
        c2.setTime(date2);
        result = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
        int month = (c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR)) * 12;
        return Math.abs(result + month);
    }

    public static Date getNextMonthDate(Date date, int afterMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + afterMonth);
        Date next = calendar.getTime();
        return next;
    }

    public static Date dayAddAndSub(Date date, int currentDay, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(currentDay, day);
        return calendar.getTime();
    }

    public static Date getFirstDate(String string) throws ParseException {
        if (StringUtils.isEmpty(string)) {
            return null;
        }
        string += "-01 00:00:00";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = simpleDateFormat.parse(string);
        return date;
    }

    //日期后的第afterday天
    public static String getNextDateStr(Date date, int afterDay) {
        Date nextDate = getNextDate(date, afterDay);
        String dateStr = getDateStr(nextDate, "yyyy-MM-dd");
        return dateStr;
    }

    //日期后的第afterday天
    public static Date getNextDate(Date date, int afterDay) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        Integer day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, day + afterDay);
        Date newDate = calendar.getTime();
        return newDate;
    }

    //获取日期yyyymmdd
    public static String getDateStr(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        String s = simpleDateFormat.format(date);
        return s;
    }

    public static Date parseStrToDate(String string, String format) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date parse = simpleDateFormat.parse(string);
        return parse;
    }

    public static int getMonth(String string) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = simpleDateFormat.parse(string);
        int month = parse.getMonth() + 1;
        return month;
    }

    public static void main(String[] args) throws ParseException {
        Date a = dayAddAndSub(new Date(), Calendar.HOUR, -8);
        System.out.print(a);
    }

    public static int differentDays(Date beforeDate, Date currentDate) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(beforeDate);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(currentDate);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2)   //同一年
        {
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0)    //闰年
                {
                    timeDistance += 366;
                } else    //不是闰年
                {
                    timeDistance += 365;
                }
            }
            return timeDistance + (day2 - day1);
        } else    //不同年
        {
            System.out.println("判断day2 - day1 : " + (day2 - day1));
            return day2 - day1;
        }
    }

    /**
     * 完整日期去掉时分秒
     */
    public static Date getDateNotHms(Date date) throws ParseException {
        if (date == null) {
            return null;
        }
        return parseStrToDate(getDateStr(date, "yyyy-MM-dd"), "yyyy-MM-dd");
    }

    /**
     * @param date
     * @return
     */
    public static Date getAfterDay(Date date, int afterDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + afterDay);
        return calendar.getTime();
    }
}
