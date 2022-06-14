package com.kcwl.framework.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class KcDateUtil {
    public static String DATE_TIME = "yyyy-MM";
    public static String DATETIMEDAY = "yyyyMMdd";
    /** 默认日期格式 */
    public static final String DEF_DATE_FORMAT = "yyyy-MM-dd";
    /** 默认时间格式 */
    public static final String DEF_TIME_FORMAT = "HH:mm:ss";
    /** 默认日期时间格式 */
    public static final String SHOP_DATETIME_FORMAT = "MM月dd日HH时mm分";
    /** 代收默认日期时间格式 */
    public static final String COLLECTION_DATETIME_FORMAT = "yyyy年MM月dd日HH时mm分";
    /** 默认日期时间格式 */
    public static final String DEF_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /** LOGINK默认日期时间格式 */
    public static final String LOGINK_DATETIME_FORMAT = "yyyyMMddHHmmss";
    /** 精确到分钟时间格式 */
    public static final String MINUTE_FORMAT = "yyyy-MM-dd HH:mm";
    /** 去的六位年月日 */
    public static final String SIX_FORMAT = "yyMMdd";

    /** 默认日期匹配格式 */
    public static final String DEF_DATE_PATTERN = "\\d{4}\\-\\d{2}-\\d{2}";
    /** 默认日期时间匹配格式 */
    public static final String DEF_DATETIME_PATTERN = "\\d{4}\\-\\d{2}-\\d{2}\\p{javaWhitespace}\\d{2}:\\d{2}:\\d{2}";

    private static final String DEF_CN_DATE_FORMAT = "yyyy年MM月dd日";

    /** 默认中文日期匹配格式 */
    public static final String DEF_CN_DATE_PATTERN = "\\d{4}\\年\\d{2}月\\d{2}日";

    /** 京东白条默认日期时间格式 */
    public static final String JDD_DATETIME_FORMAT = "yyyyMMddHHmmss";
    /** 东信隐私号默认日期时间格式 */
    public static final String DX_TEL_DATETIME_FORMAT = "yyyyMMddHHmmssSSS";

    public static String DATETIMEMS_FORMAT_FULL = "yyyyMMddHHmmss";

    /** 格式化 凌晨0点 */
    public static final String FIRST_DEF_DATETIME_FORMAT = "yyyy-MM-dd 00:00:00";
    /** 格式化 23:59:59 */
    public static final String END_DEF_DATETIME_FORMAT = "yyyy-MM-dd 23:59:59";

    /**
     * 构造函数(空)
     */
    private KcDateUtil() {}

    /**
     * 获取当前时间Logink默认时间格式yyyyMMddHHmmss
     * @return
     */
    public static String getLoginkNowDateStr() {
        SimpleDateFormat format = new SimpleDateFormat(LOGINK_DATETIME_FORMAT);
        return format.format(new Date());
    }

    /**
     * 获取Logink默认时间格式yyyyMMddHHmmss
     * @return
     */
    public static String getLoginkDateStr(Date date) {
        if(date == null)return null;
        return formatDateToString(date, LOGINK_DATETIME_FORMAT);
    }

    /**
     * 取得系统当前时间戳
     * @return 系统当前时间戳对象
     */
    public static Timestamp getSysTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 将yyyy-MM-dd格式的字符串转换为日期对象
     * @param date yyyy-MM-dd格式字符串
     * @return 转换后的日期对象，无法转换时返回null
     */
    public static Date getDate(String date) {
        if (!matchesPattern(date, DEF_DATE_PATTERN)) return null;
        return parseDate(date, DEF_DATE_FORMAT);
    }

    /**
     * 将yyyy-MM-dd格式的字符串转换为时间戳对象
     * @param date yyyy-MM-dd格式字符串
     * @return 转换后的时间戳对象，无法转换时返回null
     */
    public static Timestamp getTimestamp(String date) {
        if (!matchesPattern(date, DEF_DATE_PATTERN)) return null;
        return new Timestamp(parseDate(date, DEF_DATE_FORMAT).getTime());
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss格式的字符串转换为日期对象
     * @param datetime yyyy-MM-dd HH:mm:ss格式字符串
     * @return 转换后的日期对象，无法转换时返回null
     */
    public static Date getDateTime(String datetime) {
        if (!matchesPattern(datetime, DEF_DATETIME_PATTERN)) return null;
        return parseDate(datetime, DEF_DATETIME_FORMAT);
    }

    public static Date getPatternDate(Date date) {
        if(date == null)return null;
        return parseDate(formatDateTime(date), DEF_DATETIME_FORMAT);
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss格式的字符串转换为时间戳期对象
     * @param datetime yyyy-MM-dd HH:mm:ss格式字符串
     * @return 转换后的时间戳对象，无法转换时返回null
     */
    public static Timestamp getDateTimeStamp(String datetime) {
        if (!matchesPattern(datetime, DEF_DATETIME_PATTERN)) return null;
        return new Timestamp(parseDate(datetime, DEF_DATETIME_FORMAT).getTime());
    }

    /**
     * 将指定格式的字符串对象转换为日期对象
     * @param date 字符串
     * @param pattern 指定的格式
     * @return 转换后的日期，无法转换时返回null
     */
    public static Date getDate(String date, String pattern) {
        return getDate(date, pattern, null);
    }

    /**
     * 将指定格式的字符串对象转换为日期对象
     * @param date 字符串
     * @param pattern 指定的格式
     * @param defVal 默认返回值
     * @return 转换后的日期，无法转换时返回defVal指定值
     */
    public static Date getDate(String date, String pattern, Date defVal) {
        if (date == null || pattern == null) return null;
        Date ret = parseDate(date, pattern);
        return (ret == null) ? defVal : ret;
    }

    /**
     * 根据指定的格式格式将传入字符串转化为日期对象
     * @param date 传入字符串
     * @param format 指定格式
     * @return 格式化后日期对象
     */
    public static Date parseDate(String date, String format) {
        Date d;
        try {
            d = new SimpleDateFormat(format).parse(date);
        } catch (ParseException e) {
            d = null;
        }
        return d;
    }

    /**
     * 根据传入的Unix时间戳，返回时间字符串 <br/>
     *
     * @param utamp 时间戳
     * @param format 格式化类型
     * @return
     */
    public static String getDateStrByStamp(Long utamp, String format) {
        String s = "";
        try {
            s = new SimpleDateFormat(format).format(utamp);
        } catch (Exception e) {
            s = getCurDate();
        }
        return s;
    }

    /**
     * 根据传入的时间字符串，返回指定时间字符串 <br/>
     *
     * @param date 时间戳字符串
     * @param format 格式化类型
     * @return
     */
    public static String getFormatDateByDate(String date, String format) {
        if (date == null || format == null) return null;
        Date formatDate = parseDate(date, format);
        return formatDateToString(formatDate, format);
    }

    /**
     * 返回系统当前时间<br/>
     * 格式：<br/>
     * yyyy-MM-dd HH:mm:ss<br/>
     *
     * @return
     */
    public static String getCurDate() {
        return new SimpleDateFormat(DEF_DATETIME_FORMAT).format(new Date());
    }

    /**
     * 返回系统当前时间<br/>
     * 格式：<br/>
     * yyyy-MM-dd<br/>
     *
     * @return
     */
    public static String getCurDateYMD() {
        return new SimpleDateFormat(DEF_DATE_FORMAT).format(new Date());
    }

    /**
     * 返回系统当前时间<br/>
     * 格式：<br/>
     * MM月dd日HH时mm分<br/>
     *
     * @return
     */
    public static String getShopCurDate() {
        return new SimpleDateFormat(SHOP_DATETIME_FORMAT).format(new Date());
    }
    /**
     * 返回系统当前时间<br/>
     * 格式：<br/>
     * MM月dd日HH时mm分<br/>
     *
     * @return
     */
    public static String getCollectionCurDate() {
        return new SimpleDateFormat(COLLECTION_DATETIME_FORMAT).format(new Date());
    }

    /**
     * 检测输入字符串是否与指定格式匹配
     * @param input 待检测字符串
     * @param pattern 检测格式
     * @return
     * <li>true：匹配</li>
     * <li>false：不匹配</li>
     */
    private static boolean matchesPattern(String input, String pattern) {
        return (input != null) && (input.matches(pattern));
    }

    /**
     * 将日期对象格式化成yyyy-mm-dd类型的字符串
     * @param date 日期对象
     * @return 格式化后的字符串，无法格式化时，返回null
     */
    public static String formatDate(Date date) {
        return formatDateToString(date, DEF_DATE_FORMAT);
    }

    /**
     * 将日期对象格式化成HH:mm:ss类型的字符串
     * @param date 日期对象
     * @return 格式化后的字符串，无法格式化时，返回null
     */
    public static String formatTime(Date date) {
        return formatDateToString(date, DEF_TIME_FORMAT);
    }

    /**
     * 将日期对象格式化成yyyy-MM-dd HH:mm:ss类型的字符串
     * @param date 日期对象
     * @return 格式化后的字符串，无法格式化时，返回null
     */
    public static String formatDateTime(Date date) {
        return formatDateToString(date, DEF_DATETIME_FORMAT);
    }

    /**
     * 将日期对象格式化成yyyy-MM-dd HH:mm类型的字符串
     * @param date 日期对象
     * @return 格式化后的字符串，无法格式化时，返回null
     */
    public static String formateMinuteDate(Date date) {
        return formatDateToString(date, MINUTE_FORMAT);
    }

    /**
     * 将日期对象格式化成指定的格式字符串
     * @param date 日期对象
     * @param format 格式
     * @return 格式化后的字符串，无法格式化时，返回null
     */
    public  static String formatDateToString(Date date, String format) {
        if (date == null) return null;
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * 将日期对象格式化成指定格式的字符串
     * @param date 日期对象
     * @param format 格式
     * @return 格式化后的字符串，无法格式化时，返回null
     */
    public static String formatDate(Date date, String format) {
        if (date == null || format == null) return null;
        String ret;
        try {
            ret = new SimpleDateFormat(format).format(date);
        } catch (RuntimeException e) {
            ret = null;
        }
        return ret;
    }

    /**
     * 获取指定日期月第一天
     * @param
     * @return
     * @throws ParseException
     */
    public static Date getMonthFirstday(String day) throws ParseException {
        SimpleDateFormat simdf = new SimpleDateFormat("yyyyMMdd");
        Date date = simdf.parse(day);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(cal.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    /**
     * 获取指定日期月最后一天
     * @param
     * @return
     * @throws ParseException
     */
    public static Date getMonthLastday(String day) throws ParseException {
        SimpleDateFormat simdf = new SimpleDateFormat("yyyyMMdd");
        Date date = simdf.parse(day);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(cal.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    /**
     * 取得指定日期所在月的最后一天日期对象
     * @param d 指定日期
     * @return 指定日期当月的最后一天日期对象，如指定日期为null时，返回当前月的最后一天日期对象
     */
    public static Date getLastDayObjectInMonth(Date d) {
        Calendar cal = Calendar.getInstance();
        if (d != null) cal.setTime(d);
        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    /**
     * 取得指定日期所在月的最后一天日期值
     * @param d 指定日期
     * @return 当月的最后一天日期值，如指定日期为null时，返回当前月的最后一天日期值
     * @see #getLastDayObjectInMonth(Date)
     */
    public static int getLastDayInMonth(Date d) {
        Date date = getLastDayObjectInMonth(d);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
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
        long between_days=(time2-time1) / (24*60*60*1000);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 当前时间加上制定天数、月、年
     * @param n 加多少
     * @param k 天：Calendar.DATE、月：Calendar.MONTH 等等
     * @return
     */
    public static Date addDate(int n, int k){
        Calendar calendar = Calendar.getInstance();
        return addDate(new Date(), n, k);
    }

    /**
     * 指定时间加上指定天数、月、年
     * @param date 指定时间
     * @param n 加多少
     * @param k 天：Calendar.DATE、月：Calendar.MONTH 等等
     * @return
     */
    public static Date addDate(Date date, int n, int k){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setLenient(true);
        calendar.add(k, n);
        return calendar.getTime();
    }

    /**
     *
     * @param sourceDate
     * @param
     * @return
     * @description
     * 加年份
     */
    public static Date addYear(Date sourceDate,int years){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sourceDate);
        calendar.add(Calendar.YEAR, years);
        return calendar.getTime();
    }

    /**
     *
     * @param sourceDate
     * @param
     * @return
     * @description
     * 加分钟
     */
    public static Date addMinute(Date sourceDate,int minute){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sourceDate);
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }
    public static Date addMonth(Date sourceDate,int month){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sourceDate);
        calendar.add(Calendar.MONTH, month);
        return calendar.getTime();
    }
    public static Date addSecond(Date sourceDate,int second){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sourceDate);
        calendar.add(Calendar.SECOND, second);
        return calendar.getTime();
    }
    /**
     * 指定时间加指定小时
     * @param sourceDate
     * @param hours
     * @return
     * @description
     *
     */
    public static Date addHour(Date sourceDate,int hours){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sourceDate);
        calendar.add(Calendar.HOUR, hours);
        return calendar.getTime();
    }

    /**
     * 当前时间增加或减少 天
     * @param day
     * @return
     */
    public static Date getAddOrMinusDate(Integer day) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH,day);
        return cal.getTime();
    }


    //判断当前时间是否在22点40 至 第二天凌晨
    public static boolean isPayTime(){
        try{
            String format = "HH:mm:ss";
            Date nowTime = new SimpleDateFormat(format).parse(new SimpleDateFormat("HH:mm:ss").format(new Date()));
            Date startTime = new SimpleDateFormat(format).parse("23:55:00");
            Date endTime = new SimpleDateFormat(format).parse("23:59:59");
            Date startTime1 = new SimpleDateFormat(format).parse("00:00:00");
            Date endTime1 = new SimpleDateFormat(format).parse("00:06:00");
            return !isEffectiveDate(nowTime, startTime, endTime)&& !isEffectiveDate(nowTime, startTime1, endTime1);
        }catch (Exception e){
            e.printStackTrace();
            return true;
        }
    }



    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param nowTime 当前时间
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     * @author jqlin
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }


    public static String getTheDayWithFormat(int flag,String format) {
        Calendar ca = Calendar.getInstance();// 得到一个Calendar的实例
        ca.setTime(new Date()); // 设置时间为当前时间
        ca.add(Calendar.DATE, flag);// 日期减1
        Date resultDate = ca.getTime(); // 结果
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(resultDate);
    }

    public static void main(String[] args) throws ParseException {
//		System.out.println(getMonthLastday("20191022"));
//		System.out.println(dateToWeek("2020-02-12 20:02:02"));

        //System.out.println(getFormatDateByDate("2020-02-15 14:12:15", DEF_DATE_FORMAT));
        // System.out.println("月份 == " + DateUtil.getDayOfMonth(new Date(), Calendar.DAY_OF_MONTH));
        System.out.println(getDateByMonthAndDays(0, 1, END_DEF_DATETIME_FORMAT));
        //设置时间格式为yyyy-MM-dd HH:mm:ss
        // SimpleDateFormat sdf = new SimpleDateFormat(DEF_DATETIME_FORMAT);
        // Calendar cal = Calendar.getInstance();
        // //获取到本月起始日
        // int actualMinimum = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        // //获取到本月结束日
        // int actualMaximum = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        // //设置本月起始日的年月日时分秒格式
        // cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), actualMinimum, 00, 00, 00);
        // //打印本月起始日的年月日时分秒格式
        // System.out.println("这个月的第一天是： " + sdf.format(cal.getTime()));
    }

    /**
     * 获取指定日期周一
     * @param dayParam 例如 20191022
     * @return
     * @throws ParseException
     */
    public static Date getMonday(String dayParam) throws ParseException {
        SimpleDateFormat simdf = new SimpleDateFormat("yyyyMMdd");
        Date date = simdf.parse(dayParam);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        return cal.getTime();
    }


    /**
     * 获取指定日期周日
     * @param day 例如 20191022
     * @return
     * @throws ParseException
     */
    public static Date getWeekDay(String day) throws ParseException {
        SimpleDateFormat simdf = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(getMonday(day));
        cal.add(Calendar.DATE, 6);
        return cal.getTime();
    }

    //商户提现时间判断
    public static boolean isShopWithdrawTime() {
        try{
            Calendar cal = Calendar.getInstance();
            if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY||cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
                return false;
            }
            String format = "HH:mm:ss";
            Date nowTime = new SimpleDateFormat(format).parse(new SimpleDateFormat("HH:mm:ss").format(new Date()));
            Date startTime = new SimpleDateFormat(format).parse("17:00:00");
            Date endTime = new SimpleDateFormat(format).parse("23:59:59");
            Date startTime1 = new SimpleDateFormat(format).parse("00:00:00");
            Date endTime1 = new SimpleDateFormat(format).parse("08:00:00");
            return !isEffectiveDate(nowTime, startTime, endTime)&& !isEffectiveDate(nowTime, startTime1, endTime1);
        }catch (Exception e){
            e.printStackTrace();
            return true;
        }
    }

    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }

    // long类型转换为String类型
    // currentTime要转换的long类型的时间
    // formatType要转换的string类型的时间格式
    public static String longToString(long currentTime, String formatType)
            throws ParseException {
        Date date = longToDate(currentTime, formatType); // long类型转成Date类型
        String strTime = dateToString(date, formatType); // date类型转成String
        return strTime;
    }



    // string类型转换为date类型
    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    // long转换为Date类型
    // currentTime要转换的long类型的时间
    // formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    public static Date longToDate(long currentTime, String formatType)
            throws ParseException {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }

    // string类型转换为long类型
    // strTime要转换的String类型的时间
    // formatType时间格式
    // strTime的时间格式和formatType的时间格式必须相同
    public static long stringToLong(String strTime, String formatType)
            throws ParseException {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }

    public static Long dateToLong(Date date) {
        if (date == null) {
            return null;
        }
        return date.getTime();
    }

    public static boolean isToday(Date date){
        if (date == null){
            return false;
        }
        SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd");
        if(fmt.format(date).toString().equals(fmt.format(new Date()).toString())){//格式化为相同格式
            return true;
        }else {
            return false;
        }
    }

    /**
     * 获取日期差
     *
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public static int getBetweenDays(String startDate, String endDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DEF_DATETIME_FORMAT);
        Date fDate = sdf.parse(startDate);
        Date oDate = sdf.parse(endDate);
        long days = (oDate.getTime() - fDate.getTime()) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(days));
    }

    public static String dateToWeek(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];
    }


    /**
     * 获取当前周的第一天
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date getFirstDayOfWeek(Date date) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_WEEK, 1);
        return cal.getTime();
    }

    /**
     * 获取当前周的最后一天
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date getLastDayOfWeek(Date date) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_WEEK, 1);
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) + 6);

        return cal.getTime();
    }

    /**
     * 获取当前月的第一天
     * @param
     * @return
     */
    public static String getCurrentMonthFirstDay() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cale = Calendar.getInstance();
        cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        return format.format(cale.getTime());
    }

    /**
     * 获取当前月的最后一天
     * @param
     * @return
     */
    public static String getCurrentMonthLastDay() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cale = Calendar.getInstance();
        cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DAY_OF_MONTH, 0);
        return format.format(cale.getTime());
    }


    /**
     * 获取当年的第一天
     * @return
     */
    public static Date getCurrYearFirst(){
        Calendar currCal=Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearFirst(currentYear);
    }

    /**
     * 获取当年的最后一天
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
     * 获取当前日期是全年第几天
     * @return Date
     */
    public static int getDayIndexOfYear(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        return calendar.get(Calendar.DAY_OF_YEAR);
    }



    public static int getDaysOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当前时间离凌晨0点还有多少秒
     * @return
     */
    public static Long getSecondsTobeforedawn() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        // 改成这样就好了
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
    }
    /**
     * 获取日期差
     *
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public static int getBetweenDays(String startDate, String endDate, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date fDate = sdf.parse(startDate);
        Date oDate = sdf.parse(endDate);
        long days = (oDate.getTime() - fDate.getTime()) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(days));
    }

    public static String plusDay(String curDate, int days, String format) throws ParseException {
        if (days == 0) {
            return curDate;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date currdate = sdf.parse(curDate);

        Calendar ca = Calendar.getInstance();
        ca.setTime(currdate);
        ca.add(Calendar.DATE, days);

        return sdf.format(ca.getTime());
    }

    /**
     * 时间加减天数
     * @param sourceDate
     * @param days
     * @param format
     * @return
     */
    public static String stepDay(Date sourceDate, int days, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar ca = Calendar.getInstance();
        ca.setTime(sourceDate);
        ca.add(Calendar.DATE, days);
        return sdf.format(ca.getTime());
    }

    /**
     * 在给定的日期加上或减去指定月份后的日期
     *
     * @param sourceDate 原始时间
     * @param month      要调整的月份，向前为负数，向后为正数
     * @return
     */
    public static Date stepMonth(Date sourceDate, int month) {
        Calendar c = Calendar.getInstance();
        c.setTime(sourceDate);
        c.add(Calendar.MONTH, month);
        return c.getTime();
    }

    /**
     * 获取指定日期的当月开始时间
     * @param date
     * @return
     */
    public static Date getTimesMonthmorning(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    /**
     * 获取指定日期的当月截止时间
     * @param date
     * @return
     */
    public static Date getTimesMonthnight(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 24);
        return cal.getTime();
    }

    /**
     * 将yyyy年MM月dd日格式的字符串转换为日期对象
     * @param date yyyy年MM月dd日格式字符串
     * @return 转换后的日期对象，无法转换时返回null
     */
    public static Date getCNDate(String date) {
        if (!matchesPattern(date, DEF_CN_DATE_PATTERN)) return null;
        return parseDate(date, DEF_CN_DATE_FORMAT);
    }

    /**
     * 获取10位的时间 yyyyMMddHHmmss
     * @param date
     * @return
     */
    public static String getDate12Bit(Date date) {
        if(date == null)return null;
        return formatDateToString(date, LOGINK_DATETIME_FORMAT);
    }
    /**
     * 获取2个时间的小时秒差
     * @param startTime
     * @param endTime
     * @param format
     * @return
     */
    public static long getTimeSec(String startTime, String endTime,  String format) {
        // 按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat(format);
        long sec = 0;
        // 获得两个时间的毫秒时间差异
        try {
            long diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
            sec = diff/1000;
        } catch (ParseException e) {}
        return sec;
    }
    /**
     * 将秒转换为年月日时分秒
     *
     * @author GaoHuanjie
     */
    public static String getDayBySec(long second) {
        long days = second / 86400;//转换天数
        second = second % 86400;//剩余秒数
        long hours = second / 3600;//转换小时数
        second = second % 3600;//剩余秒数
        long minutes = second / 60;//转换分钟
        second = second % 60;//剩余秒数
        String day = "";
        if (0 < days){
            day += days + "天";
            if(hours > 0 ){
                day += hours+"小时";
            }else{
                day += "零";
            }
            if(minutes > 0){
                day += minutes+"分";
            }
            return day;
        }else {

            if(hours > 0 ){
                day += hours+"小时";
            }
            day += minutes+"分";
            return day;
        }
    }

    public static String getDateByMonthAndDays(int month, int days, String format) {
        //获取前月的第一天
        Calendar calendar = Calendar.getInstance();//获取当前日期
        calendar.add(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, days);//设置为1号,当前日期既为本月第一天
        //设置时间格式
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        //格式化前3月的时间
        return sdf.format(calendar.getTime());
    }

    public static String getDateBy(int month, int days, String format) {
        //获取前月的第一天
        Calendar calendar = Calendar.getInstance();//获取当前日期
        calendar.add(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, days);//设置为1号,当前日期既为本月第一天
        //设置时间格式
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        //格式化前3月的时间
        return sdf.format(calendar.getTime());
    }

    public static int getDayOfMonth(Date today,Integer type) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);// 此处可换为具体某一时间
        if (type == Calendar.DAY_OF_WEEK) {
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);if (weekDay == 1) {
                weekDay = 7;
            } else {
                weekDay = weekDay - 1;
            }
            return weekDay;
        }
        if (type == Calendar.DAY_OF_MONTH) {
            return calendar.get(Calendar.DAY_OF_MONTH);
        }
        if (type == Calendar.DAY_OF_YEAR) {
            return calendar.get(Calendar.DAY_OF_YEAR);
        }
        return 0;
    }

    //获取某个日期的开始时间
    public static Date getDayStartDate(Date d) {
        Calendar calendar = Calendar.getInstance();
        if(null != d){
            calendar.setTime(d);
        }
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    //获取某个日期的结束时间
    public static Date getDayEndDate(Date d) {
        Calendar calendar = Calendar.getInstance();
        if(null != d) {
            calendar.setTime(d);
        }
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

}
