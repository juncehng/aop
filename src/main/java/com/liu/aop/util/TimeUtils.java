package com.liu.aop.util;

import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * ClassName:TimeUtils <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:   TODO ADD REASON. <br/>
 * Date:     2017年9月7日 下午3:15:53 <br/>
 * @author   Administrator
 * @version
 * @since    JDK 1.6
 * @see
 */
public class TimeUtils {
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_DAY_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT_SQL = "yyyyMMddHHmmss";
    public static final String DATE_HOUR_FORMAT_SQL = "yyyyMMddHH";
    public static final String DATE_DAY_FORMAT_SQL = "yyyyMMdd";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String LONG_TIME_FORMAT = "yyyyMMddHHmmssSSS";
    public static final String DATE_MONTH_FORMAT_SQL = "yyyyMM";
    public static final String DATE_HOUR_MIN_FORMAT_SQL = "yyyyMMddHHmm";
    public static final String DATE_DAY_FORMAT_SQL_TASK = "yyyy/MM/dd";
    public static final String DATE_MONTH_FORMAT = "yyyy-MM";
    public static final String DATE_DAY_FORMAT_H = "yyyy/MM/dd HH:mm:ss";
    public static final String NEW_DATE_DAY_FORMAT_H = "yyyy-MM-dd HH:mm:ss";

    /**
     * @Title getDateByFormat
     * @Description 根据格式转换时间字符串为时间
     * @param timeString
     * @param format
     * @return
     * {@link }
     * @since 2017年9月8日 下午3:27:54
     */
    public static Date getDateByFormat(String timeString,String format) {
        if(StringUtils.isEmpty(timeString) || StringUtils.isEmpty(format)) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = formatter.parse(timeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * @Title getDateByFormat
     * @Description 根据格式转换时间
     * @param dateTime
     * @param format
     * @return
     * {@link }
     * @since 2017年9月8日 下午3:28:21
     */
    public static Date getDateByFormat(Date dateTime,String format)
    {
        if(dateTime == null || format == null)
        {
            return null;
        }

        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(dateTime);
        Date result = null;
        try
        {
            result = formatter.parse(dateString);
        }
        catch (ParseException e) {
        }
        return result;
    }

    /**
     * @Title getCurrentDayOfMonth
     * @Description 获取当前时间是几号
     * @return
     * {@link }
     * @since 2017年9月8日 下午3:28:35
     */
    public static int getCurrentDayOfMonth()
    {
        //获取当前日期
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * @Title getCurrentDayOfWeek
     * @Description 获取当前是周几
     * @return
     * {@link }
     * @since 2017年9月8日 下午3:28:48
     */
    public static int getCurrentDayOfWeek()
    {
        //获取当前日期
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-1;
        if(dayOfWeek == 0)
        {
            dayOfWeek = 7;
        }
        return dayOfWeek;
    }

    /**
     * @Title getCurrentDate
     * @Description 根据格式获取当前时间字符串
     * @param dateFormmat
     * @return
     * {@link }
     * @since 2017年9月8日 下午5:22:31
     */
    public static String getCurrentDateString(String dateFormmat)
    {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat(dateFormmat);
        return format.format(date);
    }

    /**
     * @Title getDateString
     * @Description 获取特定格式的时间字符串
     * @param date
     * @param formmat
     * @return
     * {@link }
     * @since 2017年10月31日 上午9:44:33
     */
    public static String getDateString(Date date, String formmat)
    {
        SimpleDateFormat format = new SimpleDateFormat(formmat);
        return format.format(date);
    }

    /**
     * @Title getCalendarFormmatTime
     * @Description 根据日期和格式化字符串，格式化时间格式
     * @param calendar
     * @param formmatStr
     * @return
     * {@link }
     * @since 2017年9月27日 上午10:55:40
     */
    public static String getCalendarFormmatTime(Calendar calendar,String formmatStr)
    {
        //时间格式
        SimpleDateFormat format = new SimpleDateFormat(formmatStr);
        return format.format(calendar.getTime());
    }

    public static String getDateStringFormmat(String dateString,String srcFormmat, String tarFormmat){
        Date date = getDateByFormat(dateString, srcFormmat);
        return getDateString(date,tarFormmat);
    }

    /**
    * @Description: 判断当前时间是否符合短信发送时间
    * @Param:  允许发送时段：早8点到12点   14点到18点
    * @return:
    * @Author: guohailong
    * @Date: 2018/9/28
    */
    public static boolean currentTimeIsInSendTime(){
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if(hour < 8){
            return false;
        }
        if(hour >= 18){
            return false;
        }
        if(hour >= 12 && hour < 14){
            return false;
        }
        return true;
    }

    /**
     * @Description: 根据格式计算时间
     * @Param:  time 初始时间   formmat 时间格式  calculateNum 时间计算量  type 时间类型（日/月/年）
     * @return:
     * @Author: guohailong
     * @Date: 2018/6/15
     */
    public static String calculateTime(String time, String formmat, int calculateNum, int type){
        Date date = getDateByFormat(time, formmat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(type, calculateNum);
        // 时间格式
        SimpleDateFormat format = new SimpleDateFormat(formmat);
        return format.format(calendar.getTime());
    }

    /**
     * 获取指定年月的第一天
     * @param year 年
     * @param month 月
     */
    public static String getFirstDayOfMonth(int year,int month,String format) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最小天数
        int firstDay = cal.getMinimum(Calendar.DATE);
        //设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH,firstDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(cal.getTime());
    }

    /**
     * 获取指定年月的最后一天
     * @param year 年
     * @param month 月
     */
    public static String getLastDayOfMonth(int year, int month,String format) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DATE);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(cal.getTime());
    }

    /**
     * 获取当前月所属季度
     * @param month 月份
     * @return 季度
     */
    public static int getQuarterNum(int month){
        if (month >= 1 && month < 4){
            return 1;
        }else if(month >= 4 && month < 7){
            return 2;
        }else if(month >= 7 && month < 10){
            return 3;
        }else {
            return 4;
        }
    }

    /**
     * 获取某月份所属季度的第一个月
     * @param month 月份
     * @return 月份
     */
    public static int getQuarterStartMonth(int month){
        if (month >= 1 && month < 4){
            return 1;
        }else if(month >= 4 && month < 7){
            return 4;
        }else if(month >= 7 && month < 10){
            return 7;
        }else {
            return 10;
        }
    }

    /**
     * 获取某月份所属季度的最后一个月
     * @param month 月份
     * @return 月份
     */
    public static int getQuarterEndMonth(int month){
        if (month >= 1 && month < 4){
            return 3;
        }else if(month >= 4 && month < 7){
            return 6;
        }else if(month >= 7 && month < 10){
            return 9;
        }else {
            return 12;
        }
    }
}

