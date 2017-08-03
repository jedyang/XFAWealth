/*
 * Copyright (c) 2009-2012 by fsll
 * All rights reserved.
 */

package com.fsll.common.util;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.util.CharacterUtils;

/**
 * @author 黄模建 E-mail:huangmojian@163.com
 * @version 1.0 Created On: Dec 16, 2010
 */
public class DateUtil {
	public final static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	public final static String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
    /**
     * 按照默认formatStr的格式，转化dateTimeStr为Date类型 dateTimeStr必须是formatStr的形式
     * 
     * @param dateTimeStr
     * @param formatStr
     * @return
     */
    public static Date getDate(String dateTimeStr, String formatStr) {
        try {
            if (dateTimeStr == null || "".equals(dateTimeStr)) {
                return null;
            }
            DateFormat sdf = new SimpleDateFormat(formatStr);
            java.util.Date d = sdf.parse(dateTimeStr);
            return d;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 按照默认formatStr的格式，转化dateTimeStr为Date类型 dateTimeStr必须是formatStr的形式
     * 
     * @param formatStr
     * @return
     */
    public static Date getDate(String dateTimeStr) {
    	return getDate(dateTimeStr, DEFAULT_DATE_FORMAT);
    }
    
    /**
     * 将YYYYMMDD转换成Date日期
     * 
     * @param date
     * @return
     * @throws BusinessException
     */
    public static Date transferDate(String date) throws Exception {
        if (date == null || date.length() < 1)
            return null;
        
        if (date.length() != 8)
            throw new Exception("日期格式错误");
        String con = "-";
        
        String yyyy = date.substring(0, 4);
        String mm = date.substring(4, 6);
        String dd = date.substring(6, 8);
        
        int month = Integer.parseInt(mm);
        int day = Integer.parseInt(dd);
        if (month < 1 || month > 12 || day < 1 || day > 31)
            throw new Exception("日期格式错误");
        
        String str = yyyy + con + mm + con + dd;
        return DateUtil.getDate(str, DEFAULT_DATE_FORMAT);
    }
    
    /**
     * 将YYYY－MM－DD日期转换成yyyymmdd格式字符串
     * 
     * @param date
     * @return
     */
    public static String getYYYYMMDDDate(Date date) {
        if (date == null)
            return null;
        String yyyy = Integer.toString(getYear(date));
        String mm = Integer.toString(getMonth(date));
        String dd = Integer.toString(getDay(date));
        mm = rightAlign(mm, 2, "0");
        dd = rightAlign(dd, 2, "0");
        return yyyy + mm + dd;
    }
    
    /**
     * 将YYYY－MM－DD日期转换成YYYYMMDDHHMMSS格式字符串
     * 
     * @param date
     * @return
     */
    public static String getYYYYMMDDHHMMSSDate(Date date) {
        if (date == null)
            return null;
        String yyyy = Integer.toString(getYear(date));
        String mm = Integer.toString(getMonth(date));
        String dd = Integer.toString(getDay(date));
        String hh = Integer.toString(getHour(date));
        String min = Integer.toString(getMin(date));
        String ss = Integer.toString(getSecond(date));
        
        mm = rightAlign(mm, 2, "0");
        dd = rightAlign(dd, 2, "0");
        hh = rightAlign(hh, 2, "0");
        min = rightAlign(min, 2, "0");
        ss = rightAlign(ss, 2, "0");
        
        return yyyy + mm + dd + hh + min + ss;
    }
    
    /**
     * 将YYYY－MM－DD日期转换成yyyymmdd格式字符串
     * 
     * @param date
     * @return
     */
    public static String getYYYYMMDDDate(String date) {
        return getYYYYMMDDDate(getDate(date, DEFAULT_DATE_FORMAT));
    }
    
    /**
     * 将Date转换成formatStr格式的字符串
     * 
     * @param date
     * @param formatStr
     * @return
     */
    public static String dateToDateString(Date date, String formatStr) {
        if (date == null) {
            return "";
        }
        DateFormat df = new SimpleDateFormat(formatStr);
        return df.format(date);
    }
    
    /**
     * 返回一个formatStr格式的日期时间字符串中的HH:mm:ss
     * 
     * @param dateTime
     * @param formatStr
     * @return
     */
    public static String getTimeString(String dateTime, String formatStr) {
        Date d = getDate(dateTime, formatStr);
        String s = dateToDateString(d, "yyyy-MM-dd HH:mm:ss");
        return s.substring("yyyy-MM-dd HH:mm:ss".indexOf('H'));
    }
    
    /**
     * 获取当前日期
     * 
     * @return
     */
    public static Date getCurDate() {
        return Calendar.getInstance().getTime();
    }
    
    /**
     * 获取当前日期yyyy-MM-dd的形式
     * 
     * @return
     */
    public static String getCurDateStr() {
        return dateToDateString(Calendar.getInstance().getTime(), DEFAULT_DATE_FORMAT);
    }
    
    /**
     * 获取当前日期yyyy年MM月dd日的形式
     * 
     * @return
     */
    public static String getCurZhCNDate() {
        return dateToDateString(new Date(), "yyyy年MM月dd日");
    }
    
    /**
     * 获取当前日期时间yyyy-MM-dd HH:mm:ss的形式
     * 
     * @return
     */
    public static String getCurDateTime() {
        return dateToDateString(new Date(), "yyyy-MM-dd HH:mm:ss");
    }
    
    /**
     * 获取当前日期时间yyyy年MM月dd日HH时mm分ss秒的形式
     * 
     * @return
     */
    public static String getCurZhCNDateTime() {
        return dateToDateString(new Date(), "yyyy年MM月dd日HH时mm分ss秒");
    }
    
    /**
     * 获取日期d的days天后的一个Date
     * 
     * @param d
     * @param days
     * @return
     */
    public static Date getInternalDateByDay(Date d, int days) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(d);
        now.add(Calendar.DATE, days);
        return now.getTime();
    }
    
    public static Date getInternalDateByMon(Date d, int months) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(d);
        now.add(Calendar.MONTH, months);
        return now.getTime();
    }
    
    public static Date getInternalDateByYear(Date d, int years) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(d);
        now.add(Calendar.YEAR, years);
        return now.getTime();
    }
    
    public static Date getInternalDateBySec(Date d, int sec) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(d);
        now.add(Calendar.SECOND, sec);
        return now.getTime();
    }
    
    public static Date getInternalDateByMin(Date d, int min) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(d);
        now.add(Calendar.MINUTE, min);
        return now.getTime();
    }
    
    public static Date getInternalDateByHour(Date d, int hours) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(d);
        now.add(Calendar.HOUR_OF_DAY, hours);
        return now.getTime();
    }
    
    /**
     * 根据一个日期字符串，返回日期格式，目前支持4种 如果都不是，则返回null
     * 
     * @param DateString
     * @return
     */
    public static String getFormateStr(String dateString) {
        String patternStr1 = "[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}"; // "yyyy-MM-dd"
        String patternStr2 = "[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}\\s[0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}"; // "yyyy-MM-dd
        
        String patternStr3 = "[0-9]{4}年[0-9]{1,2}月[0-9]{1,2}日";// "yyyy年MM月dd日"
        String patternStr4 = "[0-9]{4}年[0-9]{1,2}月[0-9]{1,2}日[0-9]{1,2}时[0-9]{1,2}分[0-9]{1,2}秒";// "yyyy年MM月dd日HH时mm分ss秒"
        
        Pattern p = Pattern.compile(patternStr1);
        Matcher m = p.matcher(dateString);
        boolean b = m.matches();
        if (b)
            return "yyyy-MM-dd";
        p = Pattern.compile(patternStr2);
        m = p.matcher(dateString);
        b = m.matches();
        if (b)
            return "yyyy-MM-dd HH:mm:ss";
        
        p = Pattern.compile(patternStr3);
        m = p.matcher(dateString);
        b = m.matches();
        if (b)
            return "yyyy年MM月dd日";
        
        p = Pattern.compile(patternStr4);
        m = p.matcher(dateString);
        b = m.matches();
        if (b)
            return "yyyy年MM月dd日HH时mm分ss秒";
        return null;
    }
    
    /**
     * 将一个"yyyy-MM-dd HH:mm:ss"字符串，转换成"yyyy年MM月dd日HH时mm分ss秒"的字符串
     * 
     * @param dateStr
     * @return
     */
    public static String getZhCNDateTime(String dateStr) {
        Date d = getDate(dateStr, "yyyy-MM-dd HH:mm:ss");
        return dateToDateString(d, "yyyy年MM月dd日HH时mm分ss秒");
    }
    
    /**
     * 将一个"yyyy-MM-dd"字符串，转换成"yyyy年MM月dd日"的字符串
     * 
     * @param dateStr
     * @return
     */
    public static String getZhCNDate(String dateStr) {
        Date d = getDate(dateStr, DEFAULT_DATE_FORMAT);
        return dateToDateString(d, "yyyy年MM月dd日");
    }
    
    /**
     * 将dateStr从fmtFrom转换到fmtTo的格式
     * 
     * @param dateStr
     * @param fmtFrom
     * @param fmtTo
     * @return
     */
    public static String getDateStr(String dateStr, String fmtFrom, String fmtTo) {
        Date d = getDate(dateStr, fmtFrom);
        return dateToDateString(d, fmtTo);
    }
    
    /**
     * 将日期转换为fmtTo格式的字符串
     * 
     * @param d
     * @param fmtTo
     * @return
     */
    public static String getDateStr(Date d, String fmtTo) {
        return dateToDateString(d, fmtTo);
    }

    /**
     * 将dateStr从fmtFrom转换到fmtTo的格式
     * 
     * @param dateStr
     * @param fmtFrom
     * @param fmtTo
     * @return
     */
    public static String getDateStr(Date date) {
        return dateToDateString(date, DEFAULT_DATE_FORMAT);
    }
    
    /**
     * 比较两个"yyyy-MM-dd HH:mm:ss"格式的日期，之间相差多少毫秒,time2-time1
     * 
     * @param time1
     * @param time2
     * @return
     */
    public static long compareDateStr(String time1, String time2) {
        Date d1 = getDate(time1, "yyyy-MM-dd HH:mm:ss");
        Date d2 = getDate(time2, "yyyy-MM-dd HH:mm:ss");
        return d2.getTime() - d1.getTime();
    }
    

    /**
     * 比较两个"yyyy-MM-dd"格式的日期，之间相差多少天,eTime-sTime
     * @author zxtan
     * @param sTime
     * @param eTime
     * @return
     */
    public static long getDaysOfTwoDate(String sTime, String eTime) {
    	
//        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        //跨年不会出现问题
        Date sDate = getDate(sTime, "yyyy-MM-dd");//sdf.parse(sTime);
        Date eDate = getDate(eTime, "yyyy-MM-dd");//sdf.parse(eTime);
        long days=(eDate.getTime()-sDate.getTime())/(1000*3600*24);
        return days;
    }
    
    /**
     * 将小时数换算成返回以毫秒为单位的时间
     * 
     * @param hours
     * @return
     */
    public static long getMicroSec(BigDecimal hours) {
        BigDecimal bd;
        bd = hours.multiply(new BigDecimal(3600 * 1000));
        return bd.longValue();
    }
    
    /**
     * 获取Date中的分钟
     * 
     * @param d
     * @return
     */
    public static int getMin(Date d) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(d);
        return now.get(Calendar.MINUTE);
    }
    
    /**
     * 获取Date中的小时(24小时)
     * 
     * @param d
     * @return
     */
    public static int getHour(Date d) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(d);
        return now.get(Calendar.HOUR_OF_DAY);
    }
    
    /**
     * 获取Date中的秒
     * 
     * @param d
     * @return
     */
    public static int getSecond(Date d) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(d);
        return now.get(Calendar.SECOND);
    }
    
    /**
     * 获取xxxx-xx-xx的日
     * 
     * @param d
     * @return
     */
    public static int getDay(Date d) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(d);
        return now.get(Calendar.DAY_OF_MONTH);
    }
    
    /**
     * 获取月份，1-12月
     * 
     * @param d
     * @return
     */
    public static int getMonth(Date d) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(d);
        return now.get(Calendar.MONTH) + 1;
    }
    
    /**
     * 获取19xx,20xx形式的年
     * 
     * @param d
     * @return
     */
    public static int getYear(Date d) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(d);
        return now.get(Calendar.YEAR);
    }
    
    /**
     * 得到d的上个月的年份+月份,如201005
     * 
     * @param d
     * @return
     */
    public static String getYearMonthOfLastMon(Date d) {
        Date newdate = getInternalDateByMon(d, -1);
        String year = String.valueOf(getYear(newdate));
        String month = String.valueOf(getMonth(newdate));
        return year + month;
    }
    
    /**
     * 将字符串转换成日期格式
     * 
     * @param date_s
     * @param rule
     * @return
     */
    public static Date StringToDateFormat(String dateS, String rule) {
        
        SimpleDateFormat df = new SimpleDateFormat(rule);
        Date date = null;
        try {
            date = (Date) df.parse(dateS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return date;
    }
    
    /**
     * 得到当前日期的年和月如200509
     * 
     * @return String
     */
    public static String getCurYearMonth() {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        String dateFormat = "yyyyMM";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(dateFormat);
        sdf.setTimeZone(TimeZone.getDefault());
        return (sdf.format(now.getTime()));
    }
    
    /**
     * @param year
     * @param month
     * @return
     */
    public static Date getNextMonth(String year, String month) {
        String datestr = year + "-" + month + "-01";
        Date date = getDate(datestr, DEFAULT_DATE_FORMAT);
        return getInternalDateByMon(date, 1);
    }
    
    /**
     * @param year
     * @param month
     * @return
     */
    public static Date getLastMonth(String year, String month) {
        String datestr = year + "-" + month + "-01";
        Date date = getDate(datestr, DEFAULT_DATE_FORMAT);
        return getInternalDateByMon(date, -1);
    }
    
    /**
     * 得到日期d，按照页面日期控件格式，如"2001-3-16"
     * 
     * @param d
     * @return
     */
    public static String getSingleNumDate(Date d) {
        return dateToDateString(d, DEFAULT_DATE_FORMAT);
    }
    
    /**
     * 得到d半年前的日期,"yyyy-MM-dd"
     * 
     * @param d
     * @return
     */
    public static String getHalfYearBeforeStr(Date d) {
        return dateToDateString(getInternalDateByMon(d, -6), DEFAULT_DATE_FORMAT);
    }
    
    /**
     * 得到当前日期D的月底的前/后若干天的时间,<0表示之前，>0表示之后
     * 
     * @param d
     * @param days
     * @return
     */
    public static String getInternalDateByLastDay(Date d, int days) {
        
        return dateToDateString(getInternalDateByDay(d, days), DEFAULT_DATE_FORMAT);
    }
    
    /**
     * 日期中的年月日相加
     * 
     * @param field int 需要加的字段 年 月 日
     * @param amount int 加多少
     * @return String
     */
    public static String addDate(int field, int amount) {
        return dateToDateString(addDate(new Date(), field, amount), DEFAULT_DATE_FORMAT);
    }
    
    /**
     * 日期中的年月日相加
     * 
     * @param field int 需要加的字段 年 月 日
     * @param amount int 加多少
     * @return String
     */
    public static Date addDate(Date d, int field, int amount) {
        
        try {
            Calendar cal = Calendar.getInstance(TimeZone.getDefault());
            cal.setTime(d);
            cal.add(field, amount);
            return cal.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 日期中的年月日相加
     * @param d 日期字符串（yyyy-MM-dd）
     * @param field int 需要加的字段 年 月 日
     * @param amount int 加多少
     * @return String
     */
    public static Date addDate(String d, int field, int amount) {
    	Date date = StringToDate(d, DEFAULT_DATE_FORMAT);
        return addDate(date, field, amount);
    }
    
    /**
     * 日期中的年月日相加
     * @param d 日期字符串（yyyy-MM-dd）
     * @param field int 需要加的字段 年 月 日
     * @param amount int 加多少
     * @return String
     */
    public static String addDateToString(String d, int field, int amount) {
    	Date date = StringToDate(d, DEFAULT_DATE_FORMAT);
        return getDateStr(addDate(date, field, amount));
    }
    
    /**
     * 获得系统当前月份的天数
     * 
     * @return
     */
    public static int getCurentMonthDay() {
        Date date = Calendar.getInstance().getTime();
        return getMonthDay(date);
    }
    
    /**
     * 获得指定日期月份的天数
     * 
     * @param date
     * @return
     */
    public static int getMonthDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
    
    /**
     * 获得指定日期月份的天数 yyyy-mm-dd
     * 
     * @param date
     * @return
     */
    public static int getMonthDay(String date) {
        Date strDate = getDate(date, DEFAULT_DATE_FORMAT);
        return getMonthDay(strDate);
    }
    
    /**
     * @param cal
     * @return
     */
    public static String getStringDate(Calendar cal) {
        
        SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        return format.format(cal.getTime());
    }
    
    /**
     * @param source
     * @param length
     * @param fit
     * @return
     */
    public static String rightAlign(String source, int length, String fit) {
        String result = "";
        int div = length - source.length();
        String tmp = "";
        if (div > 0) {
            for (int i = 0; i < div; i++) {
                tmp += fit;
            }
        }
        result = tmp + source;
        return result;
    }
    
    /**
     * 把yyyyMMdd格式的整型日期转换为Date类型的日期
     * 
     * @author hechuan
     * @param dateInt
     * @return
     */
    public static Date getYYYYMMddDateFromInt(int dateInt) {
        if (dateInt == 0)
            return null;
        return DateUtil.getDate(Integer.toString(dateInt), "yyyyMMdd");
    }
    
    /**
     * 把yyyyMM格式的整型日期转换为Date类型的月份
     * 
     * @author hechuan
     * @param dateInt
     * @return
     */
    public static Date getYYYYMMMonthFromInt(int dateInt) {
        if (dateInt == 0)
            return null;
        return DateUtil.getDate(Integer.toString(dateInt), "yyyyMM");
    }
    
    /**
     * 把Date转换成yyyyMM的整数
     * 
     * @author hechuan
     * @param dateInt
     * @return
     */
    public static int getMonthToInt(Date date) {
        return Integer.parseInt(DateUtil.dateToDateString(date, "yyyyMM"));
    }
    
    /**
     * 把Date转换成yyyyMMdd的整数
     * 
     * @author hechuan
     * @param date
     * @return
     */
    public static int getDateToInt(Date date) {
        return Integer.parseInt(DateUtil.dateToDateString(date, "yyyyMMdd"));
    }
    
    /**
     * 把Date转换成yyyyMMddHHmmssSSS的长整数
     * 
     * @author hechuan
     * @param date
     * @return
     */
    public static long getTimeToLong(Date date) {
        return Long.parseLong(DateUtil.dateToDateString(date, "yyyyMMddHHmmssSSS"));
    }
    
    /**
     * 把字符串转换为Date类型
     * 
     * @param number
     * @param format
     * @return
     * @throws Exception
     * @throws BusinessException
     */
    public static Date StringToDate(String str, String format) {
        if (str == null || "".equals(str) || "null".equals(str))
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date;
        try {
            date = new Date(sdf.parse(str).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("数据转换失败 StringToDate String = " + str + "; " + e.getMessage());
        }
        return date;
    }
    
    public static int getQuanter(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(Calendar.MONTH);
        int quanter = (month + 3) / 3;
        return quanter;
    }
    
    /**
     * 获取某年某月第一天
     * 
     * @Title: getFirstDayOfMonth
     * @param year
     * @param month
     * @return String
     */
    public static String getFirstDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getMinimum(Calendar.DATE));
        return new SimpleDateFormat("yyyy-MM-dd ").format(cal.getTime());
    }
    
    /**
     * 获取某月的最后一天
     * 
     * @Title:getLastDayOfMonth
     * @Description:
     * @param:@param year
     * @param:@param month
     * @param:@return
     * @return:String
     * @throws
     */
    public static String getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance(); // 设置年份
        cal.set(Calendar.YEAR, year);// 设置月份
        cal.set(Calendar.MONTH, month - 1);// 获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);// 设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay); // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        String lastDayOfMonth = sdf.format(cal.getTime());
        return lastDayOfMonth;
    }
    
    /**
     * 转化Timestamp object为Date类型
     * 
     * @param fromTimestamp
     * @return
     */
    public static java.util.Date getDate(Object fromTimestamp) {
        try {
            if (fromTimestamp == null) {
                return null;
            }
            return (new java.util.Date(((java.sql.Timestamp)fromTimestamp).getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 获取当前日期前n个单位日期yyyy-MM-dd的形式字符串
     * @param field 时间字段：日，月，年...
     * @param amount 数量
     * @return
     */
    public static Date getCurDate(int field, int amount) {
    	Calendar calendar = Calendar.getInstance();   
    	calendar.add(field, amount);
        return calendar.getTime();
    }
    
    /**
     * 获取当前日期前n个单位日期yyyy-MM-dd的形式字符串
     * @param field 时间字段：日，月，年...
     * @param amount 数量
     * @return
     */
    public static String getCurDateStr(int field, int amount) {
        return dateToDateString(getCurDate(field, amount), DEFAULT_DATE_FORMAT);
    }
    
    /** 
     * 格式化日期 
     * @param date 日期对象 
     * @return String 日期字符串 
     */  
    public static String formatDate(Date date){  
        SimpleDateFormat f = new SimpleDateFormat(DEFAULT_DATE_FORMAT);  
        String sDate = f.format(date);  
        return sDate;  
    }  
      
    /** 
     * 获取当年的第一天 
     * @param year 
     * @return 
     */  
    public static Date getCurrYearFirst(){  
        Calendar currCal=Calendar.getInstance();    
        int currentYear = currCal.get(Calendar.YEAR);  
        return getYearFirst(currentYear);  
    }  
      
    /** 
     * 获取当年的最后一天 
     * @param year 
     * @return 
     */  
    public static Date getCurrYearLast(){  
        Calendar currCal=Calendar.getInstance();    
        int currentYear = currCal.get(Calendar.YEAR);  
        return getYearLast(currentYear);  
    }  
      
    /** 
     * 获取某年第一天日期 
     * @param year 年份 
     * @return Date 
     */  
    public static Date getYearFirst(int year){  
        Calendar calendar = Calendar.getInstance();  
        calendar.clear();  
        calendar.set(Calendar.YEAR, year);  
        Date currYearFirst = calendar.getTime();  
        return currYearFirst;  
    }  
      
    /** 
     * 获取某年最后一天日期 
     * @param year 年份 
     * @return Date 
     */  
    public static Date getYearLast(int year){  
        Calendar calendar = Calendar.getInstance();  
        calendar.clear();  
        calendar.set(Calendar.YEAR, year);  
        calendar.roll(Calendar.DAY_OF_YEAR, -1);  
        Date currYearLast = calendar.getTime();  
          
        return currYearLast;  
    }  
    
    /** 
     * 将指定XML日期对象转换成格式化字符串 
     *  
     * @param xmlDate 
     *            Date XML日期对象 
     * @param datePattern 
     *            String 日期格式 
     * @return String 
     */  
    public static String getFormattedString(XMLGregorianCalendar xmlDate,  
            String datePattern) {  
        SimpleDateFormat sd = new SimpleDateFormat(datePattern);  
  
        Calendar calendar = xmlDate.toGregorianCalendar();  
  
        return sd.format(calendar.getTime());  
    }  
  
    /** 
     * 将指定XML日期对象转换成日期对象 
     *  
     * @param xmlDate 
     *            Date XML日期对象 
     * @param datePattern 
     *            String 日期格式 
     * @return Date 
     */  
    public static Date xmlGregorianCalendar2Date(XMLGregorianCalendar xmlDate) {  
        return xmlDate.toGregorianCalendar().getTime();  
    }  
  
    public static String getThisYear() {  
        // 获得当前日期  
        Calendar cldCurrent = Calendar.getInstance();  
        // 获得年月日  
        String strYear = String.valueOf(cldCurrent.get(Calendar.YEAR));  
        return strYear;  
    }  
  
    public static XMLGregorianCalendar convert2XMLCalendar(Calendar calendar) {  
        try {  
            DatatypeFactory dtf = DatatypeFactory.newInstance();              
            return dtf.newXMLGregorianCalendar(  
                    calendar.get(Calendar.YEAR),  
                    calendar.get(Calendar.MONTH)+1,  
                    calendar.get(Calendar.DAY_OF_MONTH),  
                    calendar.get(Calendar.HOUR),  
                    calendar.get(Calendar.MINUTE),  
                    calendar.get(Calendar.SECOND),  
                    calendar.get(Calendar.MILLISECOND),  
                    calendar.get(Calendar.ZONE_OFFSET)/(1000*60));  
                  
        } catch (DatatypeConfigurationException e) {  
            e.printStackTrace();  
            return null;  
        }  
    }  
  
    // 获取当天时间  
    public static java.sql.Timestamp getNowTime(String dateformat) {  
        Date now = new Date();  
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);// 可以方便地修改日期格式  
        String dateString = dateFormat.format(now);  
        SimpleDateFormat sd = new SimpleDateFormat(dateformat);  
        Date dateFormt = sd.parse(dateString, new java.text.ParsePosition(0));  
        java.sql.Timestamp dateTime = new java.sql.Timestamp(dateFormt  
                .getTime());  
  
        return dateTime;  
    }  
  
    // 获取指定时间  
    public static java.sql.Timestamp getNowNewTime(String date,String dateformat){      
        //Date   now   =   new   Date();         
        SimpleDateFormat   dateFormat   =   new   SimpleDateFormat(dateformat);//可以方便地修改日期格式     
        dateFormat.parse(date, new java.text.ParsePosition(0));  
          
      //  String  dateString= dateFormat.format(date);   
       Date dateFormt= dateFormat.parse(date, new java.text.ParsePosition(0));  
       java.sql.Timestamp dateTime = new java.sql.Timestamp(dateFormt.getTime());  
       
         
       return dateTime;  
    }  
    /** 
     * @param 含有yyyy-MM-dd'T'hh:mm:ss.SSS格式的时间转换. 
     * @return 
     */  
    public static String getTFormatString(String tdate) {  
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");  
        String str ="";  
        try {  
            java.util.Date date = format1.parse(tdate);  
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
             str = format2.format(date);  
        } catch (ParseException e) {  
            e.printStackTrace();  
        }  
        return str;  
    }  
    
//    /**
//     * 将未指定格式的日期字符串转化成java.util.Date类型日期对象 <br>
//     * @param date 待转换的日期字符串
//     * @return
//     * @throws ParseException
//     */
//    public static Date parseStringToDate(String date) throws ParseException{
//        Date result=null;
//        String parse=date;
//        parse=parse.replaceFirst("^[0-9]{4}([^0-9])", "yyyy$1");
//        parse=parse.replaceFirst("^[0-9]{2}([^0-9])", "yy$1");
//        parse=parse.replaceFirst("([^0-9])[0-9]{1,2}([^0-9])", "$1MM$2");
//        parse=parse.replaceFirst("([^0-9])[0-9]{1,2}( ?)", "$1dd$2");
//        parse=parse.replaceFirst("( )[0-9]{1,2}([^0-9])", "$1HH$2");
//        parse=parse.replaceFirst("([^0-9])[0-9]{1,2}([^0-9])", "$1mm$2");
//        parse=parse.replaceFirst("([^0-9])[0-9]{1,2}([^0-9]?)", "$1ss$2");
//         
//        DateFormat format=new SimpleDateFormat(parse);
// 
//        result=format.parse(date);
//         
//        return result;
//    }
    
    public static void main(String args[]) throws ParseException {
        //System.out.println(">>>:" + DateUtil.getLastDayOfMonth(2013, 1) + "  >>>: " + DateUtil.getFirstDayOfMonth(2013, 1));
        //Date date = DateUtil.getDate("2014-09", "yyyy-MM");
        //System.out.println("year:" + DateUtil.getYear(date) + "  month:" + DateUtil.getMonth(date));
    }
    
    /**
     * 显示日期方法 根据不同的已逝时间，显示不同值，刚刚，x分钟前，X小时前，X天前，具体日期 【可自定义配置】
     * author : 林文伟
     * @param date 日期时间
     * @param lang 多语言标识
     * @param dateTimeFormat 当计算结果是直接显示日期时，如果调用方法者想对显示日期时间进行自定义格式化
     * 						 比如【yyyy年MM月dd日】，则传递"yyyy年MM月dd日"进来，如果为空，则默认显示"yyyy-MM-dd HH:m:s"格式
     * @return
     */
	public static String getDateTimeGoneFormatStr(Date date, String lang,String dateTimeFormat) {
    	 long oneMinute = 60000L;	//1分钟转换60000豪秒  
         long oneHour = 3600000L;  //1小时转换3600000豪秒  
//         long oneDay = 86400000L;  //1天转换86400000豪秒  
          //默认简体
         String oneSecondAgo = "秒前";  
         String oneMinuteAgo = "分钟前";  
         String oneHourAgo = "小时前"; 
         String secondsAgo = "秒前";  
         String minutesAgo = "分钟前";  
         String hoursAgo = "小时前"; 
//         String oneDayAgo = "天前";  
         String yesterdayAgo = "昨天";  
         if(StringUtils.isBlank(lang))lang="en";
         if("en".equals(lang)){
        	 oneSecondAgo =" Second Ago";  
        	 oneMinuteAgo = " Minute Ago";  
        	 oneHourAgo = " Hour Ago";   
        	 secondsAgo =" Seconds Ago"; 
        	 minutesAgo = " Minutes Ago";  
        	 hoursAgo = " Hours Ago"; 
//        	 oneDayAgo = " Day Ago";  
        	 yesterdayAgo = " Yesterday";
         }
         else if("tc".equals(lang)){
        	 oneSecondAgo ="秒前";  
        	 oneMinuteAgo = "分鐘前";  
        	 oneHourAgo = "小時前"; 
        	 secondsAgo ="秒前";   
        	 minutesAgo = "分鐘前";  
        	 hoursAgo = "小時前"; 
//        	 oneDayAgo = "天前";  
        	 yesterdayAgo = "昨天";
         }
        //用于比较的日期时间值与当前的日期时间值豪秒级数据比较
     	long timeDifference = new Date().getTime() - date.getTime(); 
         Date dtCurrent = new Date();
         int daysBetween;
         try {
			daysBetween = daysBetween(date,dtCurrent);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			daysBetween = 0;
		}
		
		if(daysBetween == 0){
	    	//运算值可能比较大时，后缀加L进行Long对象包装
	        if (timeDifference < 1L * oneMinute) {  
	            long seconds = timeDifference / 1000L;//toSeconds(delta);  
	            return seconds <= 1 ? 1 + oneSecondAgo : seconds + secondsAgo;  
	        }  
	        //计算多少分钟前
	        if (timeDifference < 45L * oneMinute) {  
	            long minutes = (timeDifference / 1000L) / 60L;//toMinutes(delta);  
	            return minutes <= 1 ? 1 + oneMinuteAgo : minutes + minutesAgo;  
	        }  
	        //计算多少小时前
	        if (timeDifference < 24L * oneHour) {  
	            long hours = ((timeDifference / 1000L) / 60L) / 60L;//toHours(delta);  
	            return hours <= 1 ? 1 + oneHourAgo : hours + hoursAgo;  
	        } 
		}else if(daysBetween == 1) {
	        //计算 是否昨天
//	        if (timeDifference < 48L * oneHour) {  
//	            return yesterdayAgo;  
//	        } 
	        return yesterdayAgo;
		}
        //计算 多少天前
//        if (timeDifference < 8L * oneDay) {  
//            long days = (((timeDifference / 1000L) / 60L) / 60L)/24L;//toDays(delta);  
//            return (days <= 0 ? 1 : days) + oneDayAgo;  
//        }
        //大于7天，则直接显示日期
		if (StringUtils.isNotBlank(dateTimeFormat)) {
        	dateTimeFormat = dateTimeFormat + " " +  com.fsll.common.CommonConstants.FORMAT_TIME;
        	return (new SimpleDateFormat(dateTimeFormat)).format(date);
        }
        else
        	return (new SimpleDateFormat(com.fsll.common.CommonConstants.FORMAT_DATE_TIME)).format(date);                  
    }
	
	/**
     * 显示日期方法的特殊处理 当天的不显示日期 昨天的显示昨天+时间 ，其它的显示日期+时间
     * author : 林文伟
     * @param date 日期时间
     * @param lang 多语言标识
     * @param dateTimeFormat 当计算结果是直接显示日期时，如果调用方法者想对显示日期时间进行自定义格式化
     * 						 比如【yyyy年MM月dd日】，则传递"yyyy年MM月dd日"进来，如果为空，则默认显示"yyyy-MM-dd HH:m:s"格式
     * @return
     */
	public static String getDateTimeGoneFormatStr2(Date date, String lang,String dateTimeFormat) {
    	 //long oneMinute = 60000L;	//1分钟转换60000豪秒  
         //long oneHour = 3600000L;  //1小时转换3600000豪秒  
//         long oneDay = 86400000L;  //1天转换86400000豪秒  
          //默认简体
         //String oneSecondAgo = "秒前";  
         //String oneMinuteAgo = "分钟前";  
         //String oneHourAgo = "小时前"; 
         //String secondsAgo = "秒前";  
         //String minutesAgo = "分钟前";  
         //String hoursAgo = "小时前"; 
//         String oneDayAgo = "天前";  
         String yesterdayAgo = "昨天";  
         if(StringUtils.isBlank(lang))lang="en";
         if("en".equals(lang)){
        	 //oneSecondAgo =" Second Ago";  
        	 //oneMinuteAgo = " Minute Ago";  
        	 //oneHourAgo = " Hour Ago";   
        	 //secondsAgo =" Seconds Ago"; 
        	 //minutesAgo = " Minutes Ago";  
        	 //hoursAgo = " Hours Ago"; 
//        	 oneDayAgo = " Day Ago";  
        	 yesterdayAgo = " Yesterday";
         }
         else if("tc".equals(lang)){
        	 //oneSecondAgo ="秒前";  
        	 //oneMinuteAgo = "分鐘前";  
        	 //oneHourAgo = "小時前"; 
        	 //secondsAgo ="秒前";   
        	 //minutesAgo = "分鐘前";  
        	 //hoursAgo = "小時前"; 
//        	 oneDayAgo = "天前";  
        	 yesterdayAgo = "昨天";
         }
        //用于比较的日期时间值与当前的日期时间值豪秒级数据比较
     	//long timeDifference = new Date().getTime() - date.getTime(); 
         Date dtCurrent = new Date();
         int daysBetween;
         try {
			daysBetween = daysBetween(date,dtCurrent);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			daysBetween = 0;
		}
		
		if(daysBetween == 0){ //显示时间即可
			 SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
			 String timeString = formatter.format(date);
			 return timeString;
		}else if(daysBetween == 1) {
	        //计算 是否昨天
//	        if (timeDifference < 48L * oneHour) {  
//	            return yesterdayAgo;  
//	        } 
			 SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
			 String timeString = formatter.format(date);
	        return yesterdayAgo + " " + timeString;
		}
        //计算 多少天前
//        if (timeDifference < 8L * oneDay) {  
//            long days = (((timeDifference / 1000L) / 60L) / 60L)/24L;//toDays(delta);  
//            return (days <= 0 ? 1 : days) + oneDayAgo;  
//        }
        //大于7天，则直接显示日期
        if(!"".equals(dateTimeFormat)){
        	dateTimeFormat = dateTimeFormat + " " +  com.fsll.common.CommonConstants.FORMAT_TIME;
        	return (new SimpleDateFormat(dateTimeFormat)).format(date);
        }
        else
        	return (new SimpleDateFormat(com.fsll.common.CommonConstants.FORMAT_DATE_TIME)).format(date);                  
    }
    
    /**  
     * 计算两个日期之间相差的天数  
     * @param smdate 较小的时间 
     * @param bdate  较大的时间 
     * @return 相差天数 
     * @throws ParseException  
     */    
    public static int daysBetween(Date smdate,Date bdate) throws ParseException    
    {    
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
        smdate=sdf.parse(sdf.format(smdate));  
        bdate=sdf.parse(sdf.format(bdate));  
        Calendar cal = Calendar.getInstance();    
        cal.setTime(smdate);    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(bdate);    
        long time2 = cal.getTimeInMillis();         
        long betweenDays=(time2-time1)/(1000*3600*24);  
            
       return Integer.parseInt(String.valueOf(betweenDays));           
    }    
        

    /**
     * 获取某时间段内的所有日期
     * @author mqzou 2017-01-10
     * @param beginDate
     * @param endDate
     * @return
     */
    public static List<String> getDateList(Date beginDate,Date endDate,String dateFormat){
    	List<String> list=new ArrayList<String>();
    	Calendar calBegin = Calendar.getInstance();  
    	calBegin.setTime(beginDate);
    	while (endDate.after(calBegin.getTime())) {
    		list.add(getDateStr(calBegin.getTime(),dateFormat));
    		calBegin.add(Calendar.DATE, 1);
		}
    	
    	return list;
    }
    
    /**
     *  计算下个周期的某一天（例下次定投时间）
     * @author wwluo 2017-02-24
     * @param execCycle 时间周期，w：周，b:双周，m:月
     * @param timeDistance 时间间隔 ， 1~31
     * @return
     */
    public static String getNextCycleTime(Date date,String execCycle,Integer timeDistance){
    	String nextTime = null;
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	if (StringUtils.isNotBlank(execCycle) 
    			&& date != null 
    				&& timeDistance != null) {
    		Calendar calendar = Calendar.getInstance();
        	calendar.setTime(date);
        	if("w".equals(execCycle)){
            	calendar.add(Calendar.DATE, -calendar.get(Calendar.DAY_OF_WEEK));
        		calendar.add(Calendar.DATE, 7);
        		calendar.add(Calendar.DATE, timeDistance);
        	}else if("b".equals(execCycle)){
        		calendar.add(Calendar.DATE, -calendar.get(Calendar.DAY_OF_WEEK));
        		calendar.add(Calendar.DATE, 14);
        		calendar.add(Calendar.DATE, timeDistance);
        	}else if("m".equals(execCycle)){
        		calendar.add(Calendar.DATE, -calendar.get(Calendar.DAY_OF_MONTH));
        		calendar.add(Calendar.MONTH, 1);
        		calendar.add(Calendar.DATE, timeDistance);
        	}
        	nextTime = dateFormat.format(calendar.getTime());
		}
    	return nextTime;
    }
    
    
    
    
    
    
}
