package com.cn.xmf.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间转换处理类
 *
 * @author rufei.cn
 **/
@SuppressWarnings("all")
public class DateUtil extends org.apache.commons.lang3.time.DateUtils {


    private static String[] parsePatterns = {"yyyy-MM-dd",
            "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", "yyyy/MM/dd",
            "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd",
            "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String formatDate(Date date, Object... pattern) {
        String formatDate = null;
        try {
            if (date == null) {
                return formatDate;
            }
            if (pattern != null && pattern.length > 0) {
                formatDate = DateFormatUtils.format(date, pattern[0].toString());
            } else {
                formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatDate;
    }

    /**
     * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String formatDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String getNowDateTime() {
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 得到当天日期和时间字符串 格式（yyyy-MM-dd）
     */
    public static String getTodayDate() {
        return formatDate(new Date(), "yyyy-MM-dd");
    }

    /**
     * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss:SSSSSS）
     */
    public static String getTimeNow() {
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss:SSSSSS");
    }

    /**
     * stampToDate:(将时间戳转换为时间)
     *
     * @param s
     * @return
     * @author rufei.cn
     */
    public static Date stampToDate(String s, String paten) {
        Date dt = new Date();
        try {
            if (StringUtil.isBlank(s)) {
                return dt;
            }
            if (StringUtil.isBlank(paten)) {
                paten = "yyyy-MM-dd HH:mm:ss";
            }
            String res;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(paten);
            long lt = new Long(s);
            Date date = new Date(lt);
            res = simpleDateFormat.format(date);
            if (!StringUtil.isBlank(res)) {
                dt = toDate(res, paten);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return dt;
    }

    /**
     * stampToString:(将时间戳转换为时间)
     *
     * @param s
     * @return
     * @author rufei.cn
     */
    public static String stampToString(String s, String paten) {
        Date dt = new Date();
        String res = "";
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(paten);
            long lt = new Long(s);
            Date date = new Date(lt);
            res = simpleDateFormat.format(date);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return res;
    }


    /**
     * 日期格式化
     *
     * @param format
     * @param date
     * @return
     */
    public static Date toDate(Date date, String format) {
        Date dt = new Date();
        if (date == null) {
            return dt;
        }
        String dateStr = formatDate(date, format);
        if (StringUtil.isBlank(dateStr)) {
            return dt;
        }
        SimpleDateFormat formater = new SimpleDateFormat(format);
        try {
            dt = formater.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dt;
    }

    /**
     * 转换为日期
     *
     * @param dateStr
     * @return
     */
    public static Date toDate(String dateStr, String format) {
        Date date = new Date();
        if (StringUtils.isEmpty(dateStr)) {
            return date;
        }
        SimpleDateFormat formater = new SimpleDateFormat(format);
        try {
            date = formater.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 转换为日期字符串
     *
     * @param dateStr
     * @return
     */
    public static String toDateStr(String dateStr, String format) {
        Date date = new Date();
        if (StringUtils.isEmpty(dateStr)) {
            dateStr = formatDate(date, format);
            return dateStr;
        }
        SimpleDateFormat formater = new SimpleDateFormat(format);
        try {
            date = formater.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dateStr = formatDate(date, format);
        return dateStr;
    }

    /**
     * afterDayDateTime:(取得给定日期数天前（后）的日期函数)
     *
     * @param strDate
     * @param day
     * @return
     * @author rufei.cn
     */
    public static Date afterDayDateTime(Date strDate, int day) {
        Date date = null;
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(strDate);
            cal.add(Calendar.DAY_OF_MONTH, day);
            date = cal.getTime();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return date;
    }


    /**
     * afterMinDateTime:(取得给定日期数分钟前（后）的日期函数)
     *
     * @param strDate
     * @param min
     * @return
     * @author rufei.cn
     */
    public static Date afterMinDateTime(Date strDate, int min) {
        Date date = null;
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(strDate);
            cal.add(Calendar.MINUTE, min);
            date = cal.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * afterMonthDateTime:(取得给定日期数月前（后）的日期函数)
     *
     * @param strDate
     * @param month
     * @return
     * @author rufei.cn
     */
    public static Date afterMonthDateTime(Date strDate, int month) {
        Date date = null;
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(strDate);
            cal.add(Calendar.MONTH, month);
            date = cal.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 计算传入日期据今天的天数
     *
     * @param beginDate 今日日期
     * @param endDate   未来日期
     * @return integer
     * @throws ParseException the parse exception
     */
    public static Integer getDateIntervalDay(String beginDate, String endDate) {
        if (StringUtil.isBlank(endDate)) {
            return null;
        }
        if (StringUtil.isBlank(beginDate)) {
            return null;
        }
        int count = 0;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date endDateStr = format.parse(endDate);
            Date beginDateStr = format.parse(beginDate);
            count = (int) ((endDateStr.getTime() - beginDateStr.getTime()) / (1000 * 3600 * 24));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * 计算当前日期到目的日期的天数
     *
     * @param endDate
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static int calculateDays(String endDate, String pattern) {
        int day = 0;
        String beginDate = formatDate(new Date(), pattern);
        try {
            Integer days = getDateIntervalDay(beginDate, endDate);
            if (days != null) {
                day = days;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return day;
    }

    /**
     * 两日期的间隔秒数
     *
     * @param strDateBegin the str date begin
     * @param strDateEnd   the str date end
     * @return 间隔秒数 （int）
     * @throws ParseException the parse exception
     */
    public static long diffSecondPattern(String strDateBegin, String strDateEnd, String pattern) throws ParseException {
        long milliSencods = 100000000;
        if (StringUtil.isBlank(strDateBegin)) {
            return milliSencods;
        }
        if (StringUtil.isBlank(strDateEnd)) {
            return milliSencods;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date dateBegin = sdf.parse(strDateBegin);
        Date dateEnd = sdf.parse(strDateEnd);
        milliSencods = dateEnd.getTime() - dateBegin.getTime();
        return milliSencods / 1000;
    }


    /**
     * 两日期的间隔分钟
     *
     * @param strDateBegin the str date begin
     * @param strDateEnd   the str date end
     * @return 间隔秒数 （int）
     * @throws ParseException the parse exception
     */
    public static long diffMinutesPattern(String strDateBegin, String strDateEnd, String pattern) throws ParseException {
        long milliSencods = 100000000;
        if (StringUtil.isBlank(strDateBegin)) {
            return milliSencods;
        }
        if (StringUtil.isBlank(strDateEnd)) {
            return milliSencods;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date dateBegin = sdf.parse(strDateBegin);
        Date dateEnd = sdf.parse(strDateEnd);
        milliSencods = dateEnd.getTime() - dateBegin.getTime();
        return milliSencods / 1000 / 60;
    }
}