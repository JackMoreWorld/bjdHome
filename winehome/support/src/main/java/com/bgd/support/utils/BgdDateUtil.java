package com.bgd.support.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("deprecation")
public class BgdDateUtil {
    private static final Logger logger = LoggerFactory.getLogger(BgdDateUtil.class);
    public static final String SDF_DATE_FORMAT_SSS = "yyyy-MM-dd HH:mm:ss.SSSSSS";
    public static final String SDF_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String YMD_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_HOUSE_FORMAT = "yyyy-MM-dd HH";
    public static final String DATE_HOUSE_NOSPLIT_FORMAT = "yyyyMMddHH";

    private static final long TIME_MILLS_ONE_DAY = 1000 * 60 * 60 * 24;
    private static final long STARDARD_FROM_TIMEMILLS = parse("1970-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss").getTime();

    public static int daysBetween(Date date1, Date date2) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        long time1 = cal.getTimeInMillis();
        cal.setTime(date2);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        between_days = between_days + 1;
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 比较两个时间相差分钟数
     *
     * @param now         当前时间
     * @param compareDate 比较时间
     * @return 相差分钟数
     */
    public static long compareMinutes(Date now, Date compareDate) {
        return compareSeconds(now, compareDate) / 60;
    }

    public static long compareSeconds(Date now, Date compareDate) {
        return (long) (now.getTime() - compareDate.getTime()) / 1000;
    }

    public static String compareTimeWithDate(Date now, Date compareDate) {
        return compareTimeWithDate((long) Math.abs(compareSeconds(now, compareDate)));
    }

    public static String compareTimeWithDate(long difftime) {
        StringBuilder msg = new StringBuilder();
        DecimalFormat df = new DecimalFormat("00");
        long day = difftime / 60 / 60 / 24; // day
        String day_str = df.format(day);
        long hour = difftime % (3600 * 24) / 3600;
        String hour_str = df.format(hour);
        long min = difftime % 3600 / 60;
        String min_str = df.format(min);
        long second = difftime % 60;
        String second_str = df.format(second);
        if (difftime >= 60 * 60 * 24) {
            msg.append(day_str + " " + hour_str + ":" + min_str + ":" + second_str);
        } else if (difftime >= 60 * 60) {
            msg.append(hour_str + ":" + min_str + ":" + second_str);
        } else if (difftime >= 60) {
            msg.append(min_str + ":" + second_str);
        } else {
            msg.append("00" + ":" + second_str);
        }
        return msg.toString();
    }

    public static Long getTimeBeforeDaybreak() {
        Date currentDate = new Date();
        Date beforeDaybreak = getDate(24, 0, 0, 0);
        return beforeDaybreak.getTime() - currentDate.getTime();
    }

    public static Date getDate(int hour, int minute, int second, int milliSecond) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.MILLISECOND, milliSecond);
        Date date = cal.getTime();
        return date;
    }

    public static Date addMonth(Date date, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, month);
        return calendar.getTime();
    }

    /**
     * 计算月数 返回这某个月的第一天或则最后一天
     *
     * @param date  传入的参数必须包含月份
     * @param month
     * @return 返回这某个月的第一天或则最后一天
     */
    public static String getStartOrEndByAddMonth(String date, int month) {
        String resultDate = null;
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parseDate(date));
            if (calendar.get(Calendar.DAY_OF_MONTH) != 1) {
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.add(Calendar.DAY_OF_MONTH, -1);
            } else {
                calendar.add(Calendar.MONTH, month);
            }
            resultDate = formatSdf(calendar.getTime());
        } catch (Exception e) {
            logger.error("getStartOrEndByAddMonth error", e);
        }

        return resultDate;
    }

    /**
     * @param date
     * @param day
     * @return
     * @Method Name: addMonthReduceDay
     * @Return : Date
     */
    public static String addMonthDay(String datestr, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        Date date = parseDate1(datestr);
        calendar.setTime(date);
        // 加month月
        calendar.add(Calendar.MONTH, month);
        // 加day天
        calendar.add(Calendar.DAY_OF_YEAR, day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String resultDateStr = sdf.format(calendar.getTime());
        return resultDateStr;
    }

    public static Date parseDate(String dateStr, String format) {
        if (StringUtils.isEmpty(dateStr))
            return null;
        if (dateStr.indexOf("T") != -1) {
            dateStr = dateStr.replace("T", " ");
        }
        Date date = null;
        try {
            java.text.DateFormat df = new SimpleDateFormat(format);
            String dt = dateStr;
            if ((!dt.equals("")) && (dt.length() < format.length())) {
                dt += format.substring(dt.length()).replaceAll("[YyMmDdHhSs]", "0");
            }
            date = (Date) df.parse(dt);
        } catch (Exception e) {
            logger.error("parseDate data error!", e);
        }
        return date;
    }

    public static Date parseDate(String str) {
        return parseDate(str, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date parseDate1(String str) {
        return parseDate(str, "yyyy-MM-dd");
    }

    /**
     * SDF_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS"
     *
     * @param data
     * @return
     */
    public static String formatSdf(Date data) {
        if (data == null) {
            return null;
        }
        return new SimpleDateFormat(SDF_DATE_FORMAT).format(data);
    }

    /**
     * SDF_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS"
     *
     * @param data
     * @return
     */
    public static String formatSdf(Date data, String format) {
        if (data == null) {
            return null;
        }
        return new SimpleDateFormat(format).format(data);
    }

    public static Date parseDate(String dateStr, SimpleDateFormat format) {
        if (StringUtils.isEmpty(dateStr))
            return null;
        Date date = null;
        try {
            date = format.parse(dateStr);
        } catch (Exception e) {
            logger.error("parseDate data error:" + dateStr + "!", e);
        }
        return date;
    }

    public static Date ConvertToDate(String datestr) {
        SimpleDateFormat simple = null;
        switch (datestr.trim().length()) {
            case 19:// 日期+时间
                simple = new SimpleDateFormat(SDF_DATE_FORMAT);
                break;
            case 10:// 仅日期
                simple = new SimpleDateFormat(YMD_DATE_FORMAT);
                break;
            default:
                break;
        }
        try {
            return simple.parse(datestr.trim());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    /***
     *
     * @Description：获取系统时间n天后的日期
     * @author: <a href="mailto:lijian@joyintech.com">lijian</a> Create on 2013-2-4
     *          下午2:02:35
     * @param days
     * @return
     */
    public static Date getDateAfterNDays(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        // days天后的日期
        calendar.add(Calendar.DATE, days);
        // 将c转换成Date
        Date date = new Date(calendar.getTimeInMillis());
        return date;
    }

    /**
     * 计算天数
     *
     * @param date
     * @param days
     * @return
     */
    public static String getDateAfterNDays(String date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parseDate(date));
        // days天后的日期
        calendar.add(Calendar.DATE, days);
        return formatSdf(calendar.getTime());
    }

    /**
     * 获取末日日月份
     *
     * @return
     */
    public static int getEndDays() {
        GregorianCalendar calendar1 = new GregorianCalendar();
        GregorianCalendar calendar2 = new GregorianCalendar();
        calendar1.setTime(new Date());
        calendar2.setTime(new Date(1099 - 900, 1, 1));
        int n = 0;
        while (!calendar1.after(calendar2)) { // 循环对比，直到相等，n 就是所要的结果
            n++;
            calendar1.add(Calendar.MONTH, 1); // 比较月份，月份+1
        }
        return n - 1;
    }

    public static String getCurrentDateString(String formater) {
        SimpleDateFormat f = new SimpleDateFormat(formater);
        return f.format(getCurrentDateTime());
    }

    public static Date getCurrentDateTime() {
        return new Date();
    }

    public static Date getDateAfterYears(Date olddate, int years) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(olddate.getTime());
        // days天后的日期
        calendar.add(Calendar.YEAR, years);
        calendar.add(Calendar.DATE, -1);
        // 将c转换成Date
        Date date = new Date(calendar.getTimeInMillis());
        return date;
    }

    /***
     *
     * @Description：获取当前日期
     * @author: <a href="mailto:lijian@joyintech.com">lijian</a> Create on 2013-2-22
     *          下午5:01:52
     * @return
     */
    public static String getNowDateStr() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat f = new SimpleDateFormat(YMD_DATE_FORMAT);
        return f.format(c.getTime());
    }

    /***
     *
     * @Description：比较指定的时间与当前系统时间的差是否大于指定的天数
     * @author: <a href="mailto:lijian@joyintech.com">lijian</a> Create on 2013-3-19
     *          上午10:58:05
     * @param date
     * @param days
     * @return
     */
    public static boolean compareTime(String date_str, int days) {
        if ("".equals(date_str) || null == date_str) {
            return false;
        }
        SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long date_seconds = 0;
        try {
            Date date = st.parse(date_str);
            date_seconds = date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (System.currentTimeMillis() - date_seconds > days * 24 * 60 * 60 * 1000) {
            return true;
        }
        return false;
    }

    /**
     * 获取当前到期日截止还有几个月
     *
     * @param endDate
     * @return
     */
    public static int getEndMonth(Date endDate) {
        GregorianCalendar calendar1 = new GregorianCalendar();
        GregorianCalendar calendar2 = new GregorianCalendar();
        calendar1.setTime(new Date());
        calendar2.setTime(endDate);
        int n = 0;
        while (!calendar1.after(calendar2)) { // 循环对比，直到相等，n 就是所要的结果
            n++;
            calendar1.add(Calendar.MONTH, 1); // 比较月份，月份+1
        }
        return n - 1;
    }

    /**
     * 获取当前到期日截止还有几个月
     *
     * @param endDate
     * @return
     */
    public static int getEndMonth(Date beginDate, Date endDate) {
        GregorianCalendar calendar1 = new GregorianCalendar();
        GregorianCalendar calendar2 = new GregorianCalendar();
        calendar1.setTime(beginDate);
        calendar2.setTime(endDate);

        int n = 0;
        while (!calendar1.after(calendar2)) { // 循环对比，直到相等，n 就是所要的结果
            n++;
            calendar1.add(Calendar.MONTH, 1); // 比较月份，月份+1
        }
        return n - 1;
    }

    /**
     * 获取参数的下一天
     *
     * @param data
     * @return
     */
    public static Date getNetDay(Date data) {
        GregorianCalendar calendar1 = new GregorianCalendar();
        calendar1.setTime(data);
        calendar1.add(Calendar.DATE, 1);
        return calendar1.getTime();
    }

    /**
     * 计算时间差
     *
     * @param begin_date 开始时间
     * @param end_date   结束时间
     * @return
     */
    public static String countTime(Date begin_date, Date end_date) {
        int hour = 0;
        int minute = 0;
        int second = 0;
        long total_minute = 0;
        StringBuffer sb = new StringBuffer();
        total_minute = (end_date.getTime() - begin_date.getTime()) / (1000 * 60);


        hour = (int) total_minute / 60;
        minute = (int) total_minute % 60;
        second = (int) (end_date.getTime() - begin_date.getTime()) % (1000 * 24 * 60 * 60) % (1000 * 60 * 60) % (1000 * 60) / 1000;
        sb.append(hour).append("小时").append(minute).append("分钟").append(second).append("秒");
        return sb.toString();
    }

    /******************************************************************/

    public static boolean isSameMonth(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(date1);
        calendar2.setTime(date2);
        int month1 = calendar1.get(Calendar.MONTH) + 1;
        int month2 = calendar2.get(Calendar.MONTH) + 1;
        return month1 == month2;
    }

    public static boolean isSameYear(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(date1);
        calendar2.setTime(date2);
        int year1 = calendar1.get(Calendar.YEAR);
        int year2 = calendar2.get(Calendar.YEAR);
        return year1 == year2;
    }

    public static String generateDateString6(Date date) {
        return new SimpleDateFormat("yyMMddHHmmssSSS").format(date);
    }

    public static String generateDateString1(Date date) {
        return new SimpleDateFormat("yyyyMMdd").format(date);
    }

    public static String generateDateString2(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public static String generateDateString3(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static String generateDateString4(Date date) {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(date);
    }

    public static String generateDateString5(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(date);
    }

    public static Date generateStringDate5(String str) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 返回输入字符串时间的当天最大或最小时间
     *
     * @param date      yyyy-MM-dd 长度大于等于10
     * @param zoreorone 0标识返回当天的最小时间 1标识返回最大时间
     * @return
     */
    public static String getMaxOrMinDateString(String date, int zoreorone) {
        if (date.length() < 10 || (zoreorone != 0 && zoreorone != 1)) {
            return date;
        }
        date = date.substring(0, 10);
        if (zoreorone == 0) {
            date = date + " 00:00:00";
        }
        if (zoreorone == 1) {
            date = date + " 23:59:59";
        }
        return date;
    }

    public static Date generateStringDate(String str) {
        if (str.length() == 10) {
            return generateStringDate2(str);
        }
        if (str.length() == 19) {
            return generateStringDate3(str);
        }
        if (str.length() > 19) {
            return generateStringDate5(str);
        } else {
            return null;
        }
    }

    public static Date generateStringDate2(String str) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date generateStringDate3(String str) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    // 将字符串转为时间戳
    public static String getTime(String user_time, String format) {
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date d;
        try {
            d = sdf.parse(user_time);
            long l = d.getTime();
            String str = String.valueOf(l);
            re_time = str.substring(0, 10);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return re_time;
    }

    // 将时间戳转为字符串
    public static String getStrTime(String cc_time, String format) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        // 例如：cc_time=1291778220
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;
    }

    /**
     * 使用预设Format格式化Date成字符串
     */
    public static String format(Date date) {
        return format(date, YMD_DATE_FORMAT);
    }

    /**
     * 使用参数Format格式化Date成字符串
     */
    public static String format(Date date, String pattern) {
        try {
            if (date == null) {
                return null;
            }
            if (StringUtils.isEmpty(pattern)) {
                pattern = YMD_DATE_FORMAT;
            }
            return new SimpleDateFormat(pattern).format(date);
        } catch (Throwable e) {

        }
        return null;
    }

    /**
     * 使用预设格式将字符串转为Date
     */
    public static Date parse(String input) throws ParseException {
        return parse(input, YMD_DATE_FORMAT);
    }

    /**
     * 使用参数Format将字符串转为Date
     */
    public static Date parse(String input, String pattern) {
        try {
            if (StringUtils.isEmpty(pattern)) {
                return null;
            }
            if (StringUtils.isEmpty(pattern)) {
                pattern = YMD_DATE_FORMAT;
            }
            return new SimpleDateFormat(pattern).parse(input);
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * add day with increment, if you want to implement a decrement function, use a
     * negative integer
     * <p>
     * Usage: add(new Date(), 10) to increment 10; add(new Date(), -10) to decrement
     * 10
     */
    public static Date add(Date inputDate, int increment) {
        if (inputDate == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(inputDate);
        cal.add(Calendar.DAY_OF_MONTH, increment);
        return cal.getTime();
    }

    public static String add(Date inputDate, int increment, String targetDateFormat) {
        return format(add(inputDate, increment), targetDateFormat);
    }

    public static Date add(String inputDateStr, int increment) {
        return add(inputDateStr, YMD_DATE_FORMAT, increment);
    }

    public static Date add(String inputDateStr, String inputDataFormat, int increment) {
        Date inputDate = parse(inputDateStr, inputDataFormat);
        if (inputDate == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(inputDate);
        cal.add(Calendar.DAY_OF_MONTH, increment);
        return cal.getTime();
    }

    public static String addWithStringResult(String inputDateStr, int increment) {
        return addWithStringResult(inputDateStr, YMD_DATE_FORMAT, increment);
    }

    public static String addWithStringResult(String inputDateStr, String inputDataFormat, int increment) {
        Date resultDate = add(inputDateStr, inputDataFormat, increment);
        if (resultDate == null) {
            return null;
        }
        return format(resultDate);
    }

    /**
     * add seconds with increment, if you want to implement a decrement function,
     * use a negative integer
     * <p>
     * Usage: addSeconds(new Date(), 10) to increment 10; addSeconds(new Date(),
     * -10) to decrement 10
     */
    public static Date addSeconds(Date inputDate, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(inputDate);
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }

    public static Date addSeconds(int seconds) {
        return addSeconds(new Date(), seconds);
    }

    public static Date addSeconds(Date inputDate, long seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(calendar.getTimeInMillis() + seconds * 1000);
        return calendar.getTime();
    }

    public static Date addSeconds(long seconds) {
        return addSeconds(new Date(), seconds);
    }

    /**
     * add minutes with increment, if you want to implement a decrement function,
     * use a negative integer
     * <p>
     * Usage: addMinutes(new Date(), 10) to increment 10; addSeconds(new Date(),
     * -10) to decrement 10
     */
    public static Date addMinutes(Date inputDate, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(inputDate);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    public static Date addMinutes(int minutes) {
        return addMinutes(new Date(), minutes);
    }

    /**
     * add hours with increment, if you want to implement a decrement function, use
     * a negative integer
     * <p>
     * Usage: addHours(new Date(), 10) to increment 10; addHours(new Date(), -10) to
     * decrement 10
     */
    public static Date addHours(Date inputDate, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(inputDate);
        calendar.add(Calendar.HOUR, hours);
        return calendar.getTime();
    }

    public static Date addHours(int hours) {
        return addHours(new Date(), hours);
    }

    /**
     * timeMills: the number of milliseconds since January 1, 1970, 00:00:00 GMT
     * represented by this date
     */
    public static Date parse(long timeMills) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMills);
        return calendar.getTime();
    }

    /**
     * Substract two dates,return the interval For example:
     * substract(parse("2016-08-03 05:06:07", "yyyy-MM-dd HH:mm:ss"),
     * parse("2016-08-05 05:06:07", "yyyy-MM-dd HH:mm:ss"))==>2
     * substract(parse("2016-08-05 05:06:07", "yyyy-MM-dd HH:mm:ss"),
     * parse("2016-08-03 05:06:07", "yyyy-MM-dd HH:mm:ss"))==>2
     * substract(parse("2016-08-05", "yyyy-MM-dd"), parse("2016-08-03",
     * "yyyy-MM-dd"))==>2
     *
     * @param source
     * @param target
     * @return
     */
    public static long substract(Date source, Date target) {
        long t = STARDARD_FROM_TIMEMILLS;
        long sTimeMills = source.getTime() - t;
        long tTimeMills = target.getTime() - t;
        if (sTimeMills < tTimeMills) {
            long temp = sTimeMills;
            sTimeMills = tTimeMills;
            tTimeMills = temp;
        }
        long start = sTimeMills / TIME_MILLS_ONE_DAY;
        long end = tTimeMills / TIME_MILLS_ONE_DAY;
        return start - end;
    }

    public static int substractSecond(Date start, Date end) {
        long startSeconds = start.getTime();
        long endSeconds = end.getTime();
        long diff = endSeconds - startSeconds;
        BigDecimal diffBd = new BigDecimal(diff);
        BigDecimal result = diffBd.divide(new BigDecimal(1000));
        return result.intValue();
    }

    /**
     * 比较date1是否大于date2
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean gt(Date date1, Date date2) {
        if (date1 != null && date2 != null) {
            if (date1.getTime() > date2.getTime()) {
                return true;
            }
        }
        return false;
    }

    /**
     * * 获取指定日期是星期几 参数为null时表示获取当前日期是星期几
     *
     * @param date
     * @return
     */
    public static String getWeekOfDate(Date date) {
        String[] weekOfDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekOfDays[w];
    }

    /**
     * 判断当前时间是上午还是下午 结果为“0”是上午 结果为“1”是下午
     *
     * @return
     */
    public static int getAMorPM() {
        GregorianCalendar ca = new GregorianCalendar();
        return ca.get(GregorianCalendar.AM_PM);
    }

    /**
     * 根据时间判断是上午还是下午，晚上
     *
     * @param date
     * @return
     */
    public static String getSorXorW(Date date) {
        if (null == date) {
            date = new Date();
        }
        String srt = "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        if (hour >= 6 && hour < 8) {
            srt = "早上";
        } else if (hour >= 8 && hour < 11) {
            srt = "上午";
        } else if (hour >= 11 && hour < 13) {
            srt = "中午";
        } else if (hour >= 13 && hour < 18) {
            srt = "下午";
        } else {
            srt = "晚上";
        }
        return srt;
    }

    /**
     * 获取一周中的第一天
     *
     * @return
     */
    public static Date getWeekMon() {

        Calendar cal = Calendar.getInstance();
        // SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        cal.add(Calendar.DATE, -1);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // 获取本周一的日期
        return cal.getTime();// df.format(cal.getTime())

    }

    /**
     * 获取一周中的最后一天
     *
     * @return
     */
    public static Date getWeekSun() {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // 获取本周一的日期

        // 这种输出的是上个星期周日的日期，因为老外那边把周日当成第一天
        cal.add(Calendar.DATE, -1);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        // 增加一个星期，才是我们中国人理解的本周日的日期
        cal.add(Calendar.WEEK_OF_YEAR, 1);

        return cal.getTime();// df.format(cal.getTime())
    }

    /**
     * 下一天
     *
     * @param date
     * @return
     */
    public static Date getNextDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, +1);// +1今天的时间加一天
        Date time = calendar.getTime();
        return time;
    }

    public static Date getStartTime(Date date) {
        Calendar todayStart = Calendar.getInstance();
        todayStart.setTime(date);
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime();
    }

    public static Date getEndTime(Date date) {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.setTime(date);
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime();
    }

    /*
     * 根据年-月份获取该月的第一天
     */
    public static Date getFistDayByMonth(String yearmonth) {
        Calendar now = Calendar.getInstance();
        String day = now.get(Calendar.DAY_OF_MONTH) + "";

        String time = yearmonth + "-" + day + " 00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date today = new Date();
        try {
            today = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);

        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date fistdate = calendar.getTime();
        String firstday = sdf.format(fistdate.getTime());
        System.out.println(firstday);
        return fistdate;
    }

    /*
     * 根据年月份获取该月的最后一天
     */
    public static Date getLastDayByMonth(String yearmonth) {
        Calendar now = Calendar.getInstance();
        String day = now.get(Calendar.DAY_OF_MONTH) + "";

        String time = yearmonth + "-" + day + " 23:59:59";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date today = new Date();
        try {
            today = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        // 获取前月的最后一天
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        Date lastdate = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String lastday = format.format(lastdate.getTime());
        System.out.println(lastday);
        return lastdate;
    }

    /**
     * 获取某段时间内的所有日期
     *
     * @param dBegin
     * @param dEnd
     * @return
     */
    public static List<Date> findDates(Date dBegin, Date dEnd) {
        List<Date> lDate = new ArrayList<Date>();
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(dEnd);
        // 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            lDate.add(calBegin.getTime());
            calBegin.add(Calendar.DAY_OF_MONTH, 1);

        }
        return lDate;

    }

    // 获取指定月份的天数
    public static int getDaysByYearMonth(int year, int month) {

        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    public static void dayReport(Date month) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(month);// month 为指定月份任意日期
        int year = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH);
        int dayNumOfMonth = getDaysByYearMonth(year, m);
        cal.set(Calendar.DAY_OF_MONTH, 1);// 从一号开始

        for (int i = 0; i < dayNumOfMonth; i++, cal.add(Calendar.DATE, 1)) {
            Date d = cal.getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String df = simpleDateFormat.format(d);
            System.out.println(df);
        }
    }

    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 != null && date2 != null) {
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date2);
            return isSameDay(cal1, cal2);
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 != null && cal2 != null) {
            return cal1.get(0) == cal2.get(0) && cal1.get(1) == cal2.get(1) && cal1.get(6) == cal2.get(6);
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    /**
     * 获取下一个小时
     *
     * @param hour
     * @return
     */
    public static String getNextHour(String queryTime) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_HOUSE_NOSPLIT_FORMAT);
        Calendar currentDate = Calendar.getInstance();
        try {
            currentDate.setTime(sdf.parse(queryTime));
        } catch (ParseException e) {
            currentDate.setTime(new Date());
        }
        currentDate.set(Calendar.HOUR_OF_DAY, currentDate.get(Calendar.HOUR_OF_DAY) + 1);
        return sdf.format(currentDate.getTime());
    }

    /**
     * 获取下一个小时
     *
     * @param hour
     * @return
     */
    public static String getBeforeFourHour(String queryTime) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_HOUSE_NOSPLIT_FORMAT);
        Calendar currentDate = Calendar.getInstance();
        try {
            currentDate.setTime(sdf.parse(queryTime));
        } catch (ParseException e) {
            currentDate.setTime(new Date());
        }
        currentDate.set(Calendar.HOUR_OF_DAY, currentDate.get(Calendar.HOUR_OF_DAY) - 4);
        return sdf.format(currentDate.getTime());
    }

    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(SDF_DATE_FORMAT);
        return sdf.format(new Date());
    }


}
