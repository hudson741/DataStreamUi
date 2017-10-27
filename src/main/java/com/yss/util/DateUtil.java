package com.yss.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;


public class DateUtil {

    public static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final SimpleDateFormat YYYY_MM_DD_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

    public static final SimpleDateFormat YYYY_MM_FORMATTER = new SimpleDateFormat("yyyy-MM");

    public static final SimpleDateFormat MM_DD_FORMATTER = new SimpleDateFormat("MM-dd");

    private static final SimpleDateFormat HHmmss_FORMATTER = new SimpleDateFormat("HH:mm:ss");

    private static String dayNames[] = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };

    public static final String[] WEEK_DAYS = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };

    public static final String FORMATTER_SIMPLE = "HH:mm";

    public static final String[] DATE_PATTERN = {"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "HH:mm:ss","HH:mm","yyyyMMdd HH:mm:ss"};

    /**
     * @param seconds long类型的秒数
     * @return HHmmss类型的字符串(100:30:20)100小时30分20秒
     */
    public static String getHHmmss(long seconds) {
        long hours = seconds / 3600;
        seconds = seconds % 3600;
        long minint = seconds / 60;
        seconds = seconds % 60;
        return String.valueOf(hours) + ":" + String.valueOf(minint) + ":" + seconds;
    }

    public static Date parseYYYYMMDD(String dateStr) {
        try {
            if (StringUtils.isNotBlank(dateStr)) return new SimpleDateFormat("yyyyMMdd").parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatYYYYMMDD(Date date) {
        try {
            return new SimpleDateFormat("yyyyMMdd").format(date);
        } catch (Exception e) {
            e.printStackTrace();;
        }
        return null;
    }

    public static Date parse(String date) {
        try {
            if (StringUtils.isNotBlank(date)) return DateUtils.parseDate(date, DATE_PATTERN);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 得到当前时间之前一定天数对应的日期，时分秒都设为零
     * @param beforeDayNum 之前的天数
     * @return
     */
    public static String getDateOfLastDay(int beforeDayNum){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC+8"));
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        if(beforeDayNum>0){
            calendar.add(Calendar.DATE, -beforeDayNum);
        }
        return DateUtil.format(calendar.getTime(), "yyyy-MM-dd");
    }

    public static String getDateOfLastDayForMMDD(int beforeDayNum){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC+8"));
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        if(beforeDayNum>0){
            calendar.add(Calendar.DATE, -beforeDayNum);
        }
        return DateUtil.format(calendar.getTime(), "MM月dd日");
    }

    public static Date parse(String dateStr, String pattern) {
        try {
            if(dateStr == null|| dateStr.isEmpty()) return null;

            DateFormat formate = new SimpleDateFormat(pattern);
            return formate.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     *
     * @param dateStr
     * @return
     */
    public static Date parseDateTime(String dateStr) {
        try {
            return  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static Date parseDate(String date) {
        return parseDateTime(date);
    }

    public static Date HHmmssParseDate(String date) {
        try {
            return new SimpleDateFormat("HH:mm:ss").parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static Date parseDateYYYY_MM_DD(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static String formatyyyMMddChinese(Date date) {
        return new SimpleDateFormat("yyyy年MM月dd日").format(date);
    }

    /**
     * date1-date2的秒数
     *
     * @param date1
     * @param date2
     * @return
     * @throws ParseException
     */
    public static long timeDistance(Date date1, Date date2) {
        return (date1.getTime() - date2.getTime()) / (1000);
    }

    public static String format() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    /**
     * 直播间水印日期
     * @return
     */
    public static String formatMask() {
        return new SimpleDateFormat("yyyy.MM.dd").format(new Date());
    }

    public static String hHmmssformat(Date date) {
        return new SimpleDateFormat("HH:mm:ss").format(date);
    }

    /**
     * 返回 yyyy-MM-dd HH:mm:ss 格式
     *
     * @param date
     * @return
     */
    public static String format(Date date) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    /**
     * yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String formatYMD(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    /**
     * yyyy-MM
     *
     * @param date
     * @return
     */
    public static String formatYM(Date date) {
        return new SimpleDateFormat("yyyy-MM").format(date);
    }

    /**
     * 返回 06月03日(星期一) PM17:00这样的格式
     *
     * @param date
     * @return
     */
    public static String format_1(Date date) {
        String result = "";
        String weekDays = getWeek(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hou = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        String monthStr = "";
        String dayStr = "";
        String houStr = "";
        String APMStr = "";
        String minStr = "";
        if (month < 10) {
            monthStr = "0" + String.valueOf(month);
        } else {
            monthStr = String.valueOf(month);
        }
        if (day < 10) {
            dayStr = "0" + String.valueOf(day);
        } else {
            dayStr = String.valueOf(day);
        }
        if (hou < 10) {
            houStr = "0" + String.valueOf(hou);
        } else {
            houStr = String.valueOf(hou);
        }
        if (hou < 12) {
            APMStr = "AM";
        } else {
            APMStr = "PM";
        }
        if (min < 10) {
            minStr = "0" + String.valueOf(min);
        } else {
            minStr = String.valueOf(min);
        }
        result = monthStr + "月" + dayStr + "日 " + houStr + ":" + minStr;

        return result;
    }

    /**
     * 返回20120102110000这种格式
     *
     * @param date
     * @return
     */
    public static String format_2(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hou = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        int sec = calendar.get(Calendar.SECOND);
        String monthStr = "";
        String dayStr = "";
        String houStr = "";
        String minStr = "";
        String secStr = "";
        if (month < 10) {
            monthStr = "0" + String.valueOf(month);
        } else {
            monthStr = String.valueOf(month);
        }
        if (day < 10) {
            dayStr = "0" + String.valueOf(day);
        } else {
            dayStr = String.valueOf(day);
        }
        if (hou < 10) {
            houStr = "0" + String.valueOf(hou);
        } else {
            houStr = String.valueOf(hou);
        }
        if (min < 10) {
            minStr = "0" + String.valueOf(min);
        } else {
            minStr = String.valueOf(min);
        }
        if (sec < 10) {
            secStr = "0" + String.valueOf(sec);
        } else {
            secStr = String.valueOf(sec);
        }
        return calendar.get(Calendar.YEAR) + monthStr + dayStr + houStr + minStr + secStr;
    }

    /**
     * 返回星期几
     *
     * @param date
     * @return
     */
    public static String getWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek < 0) {
            dayOfWeek = 0;
        }
        return dayNames[dayOfWeek];
    }

    /**
     * 返回星期几
     *
     * @param date
     * @return
     */
    public static int getWeekIndex(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek < 0) {
            dayOfWeek = 0;
        }
        return dayOfWeek;
    }

    public static String format(String pattern) {
        return new SimpleDateFormat(pattern).format(new Date());
    }

    public static String format(Date date, String pattern) {
        if (date == null) return "";
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * 返回 日期
     *
     * @param
     * @param pattern
     * @param offset 偏移量
     * @param offsetType 偏移类型
     * @return
     */
    public static String format(String pattern, int offset, OffsetType offsetType) {

        Calendar calendar = Calendar.getInstance();// 此时打印它获取的是系统当前时间

        if (OffsetType.DAY.getOffsetType() == offsetType.getOffsetType()) {
            calendar.add(Calendar.DATE, offset); // 得到前一天
        } else if (OffsetType.MONTH.getOffsetType() == offsetType.getOffsetType()) {
            calendar.add(Calendar.MONTH, offset); // 得到前一个月
        } else if (OffsetType.YEAR.getOffsetType() == offsetType.getOffsetType()) {
            calendar.add(Calendar.YEAR, offset); // 得到前一个月
        } else {

        }
        return new SimpleDateFormat(pattern).format(calendar.getTime());

    }


    /**
     * 返回偏移后的日期类型<p>
     * 对日期进行:年、月、日、时、分、秒的加减
     *
     * @param date 指定日期
     * @param offset 偏移量
     * @param offsetType 类型
     * @return
     */
    public static Date format(Date date, int offset, OffsetType offsetType) {
        if(date == null) return null;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        if (OffsetType.DAY.getOffsetType() == offsetType.getOffsetType()) {
            calendar.add(Calendar.DAY_OF_MONTH, offset); // 偏移天

        } else if (OffsetType.MONTH.getOffsetType() == offsetType.getOffsetType()) {
            calendar.add(Calendar.MONTH, offset); // 偏移月

        } else if (OffsetType.YEAR.getOffsetType() == offsetType.getOffsetType()) {
            calendar.add(Calendar.YEAR, offset); // 偏移年

        } else if(OffsetType.MINUTE == offsetType ) {
            calendar.add(Calendar.MINUTE,offset);//偏移分钟

        }else if(OffsetType.HOUR == offsetType ) {
            calendar.add(Calendar.HOUR_OF_DAY,offset);//偏移小时

        }else if(OffsetType.SECOND == offsetType) {
            calendar.add(Calendar.SECOND,offset);//偏移秒
        }else {

        }
        return calendar.getTime();

    }


    /**
     * 字符串转化为日期
     *
     * @param pattern
     * @param date
     * @return
     */
    public static Date String2Date(String pattern, String date) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getSpecifiedDayBefore(String pattern, String date) {
        Date tmpDate = String2Date(pattern, date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(tmpDate);
        calendar.add(Calendar.DATE, -1);
        return new SimpleDateFormat(pattern).format(calendar.getTime());
    }

    public static String getSpecifiedDayAfter(String pattern, String date) {
        Date tmpDate = String2Date(pattern, date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(tmpDate);
        calendar.add(Calendar.DATE, 1);
        return new SimpleDateFormat(pattern).format(calendar.getTime());
    }

    public static Date getSpecifiedDayBefore(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    public static Date getSpecifiedDayAfter(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }

    /**
     * 日期转换为周
     *
     * @param date
     * @param pattern
     * @return
     */
    public static int getWeek(String date, String pattern) {

        SimpleDateFormat sdf = null;
        if (pattern == null || "".equals(pattern.trim())) {
            sdf = new SimpleDateFormat("yyyy-MM-dd");
        } else {
            sdf = new SimpleDateFormat(pattern);
        }
        Calendar cl = Calendar.getInstance();
        if (date != null && !"".equals(date.trim())) {
            try {
                cl.setTime(sdf.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
                return -1;
            }
        }

        return cl.get(Calendar.WEEK_OF_YEAR);

    }

    public static enum OffsetType {
        SECOND(3),MINUTE(2),HOUR(1),DAY(0), MONTH(-1), YEAR(-2);

        private final int OffsetType;

        private OffsetType(int OffsetType) {
            this.OffsetType = OffsetType;
        }

        public int getOffsetType() {
            return OffsetType;
        }
    }

    public boolean hasDisplay(Date date) {
        return date != null;
    }



    /**
     * 获取昨天的开始时间
     * @param
     * @return
     */
    public static Calendar getYestodayBegin(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE, -1);
        return calendar;
    }

    public static Calendar getYestodayBegin1(int n){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE, -n);
        return calendar;
    }

    /**
     * 获取今天的结束时间
     *
     * @return
     */
    public static Calendar getTodayEnd() {
        return getDayEnd(new Date());
    }

    public static Calendar getTomorrowEnd() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 1);
        return getDayEnd(calendar.getTime());
    }

    /**
     * 获取今天的开始时间
     *
     * @return
     */
    public static Calendar getTodayBegin() {

        return getDayBegin(new Date());
    }

    public static Calendar getTomorrowBegin(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 1);
        return getDayBegin(calendar.getTime());
    }

    /**
     * 获取一天的开始时间
     *
     * @param date
     * @return
     */
    public static Calendar getDayBegin(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    /**
     * 获取一天的结束时间
     *
     * @param date
     * @return
     */
    public static Calendar getDayEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar;
    }

    /**
     * 获取自然月开始日期
     *
     * @param date
     * @return
     */
    public static Date getMonthBegin(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return getDayBegin(calendar.getTime()).getTime();

    }

    /**
     * 获取自然月的结束日期
     *
     * @param date
     * @return
     */
    public static Date getMonthEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return getDayEnd(calendar.getTime()).getTime();
    }

    /**
     * 获取当前日期对应的n天的日期.
     *
     * 2014-04-01 n=-1 return 2014-03-31 ,n=1 return 2014-04-02
     *
     * @param n>0 当前日期后n天，n<0获取当前日期前n天日期 n
     * @return
     */
    public static Date getCurrDayOffsetDate(int n) {
        Calendar c = new GregorianCalendar();
        c.setTime(new Date());
        c.add(Calendar.DATE, n);
        return c.getTime();
    }

    /**
     * 计算两个日期之间相差的年数
     *
     * @param smdate 较小的时间
     * @param bdate 较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int getBettweenYears(Date smdate, Date bdate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        int time1 = cal.getWeekYear();
        cal.setTime(bdate);
        int time2 = cal.getWeekYear();
        return time2 - time1;
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate 较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int getBettweenDays(Date smdate, Date bdate) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 计算两个日期之间相差的小时数
     *
     * @param smdate 较小的时间
     * @param bdate 较大的时间
     * @return 相差小时数
     * @throws ParseException
     */
    public static int getBettweenHours(Date smdate, Date bdate) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_hours = (time2 - time1) / (1000 * 3600 );
        return Integer.parseInt(String.valueOf(between_hours));
    }

    public static long getBettweenMinutes(Date smdate, Date bdate) throws ParseException {
        long time1 = bdate.getTime();
        long time2 = smdate.getTime();
        long between_minutes = (time1 - time2) / (1000 * 60 );
        return between_minutes;
    }

    public static String getTimestamp2String(Timestamp timestamp, String pattern) {
        return DateUtil.format(new Date(timestamp.getTime()), pattern);
    }

    /**
     * 时间优先 最近在前
     *
     * @param date1
     * @param date2
     * @return -1: date1>date2, 0: date1==date2, 1: date1<date2
     */
    public static int compare(Date date1, Date date2) {
        Long d1 = date1 == null ? 0 : date1.getTime();
        Long d2 = date2 == null ? 0 : date2.getTime();
        return ((d1 & d2) == 0 ? 1 : -1) * d1.compareTo(d2);
    }

    /**
     * 计算现在到指定未来时间的天数
     *
     * @param futureDate 未来的时间
     * @return
     */
    public static int calcDayCountOfNowToFuture(Date futureDate) {
        Date nowDate = new Date();
        long millsOfNow = nowDate.getTime();
        long millsOfFuture = futureDate.getTime();
        if (millsOfNow < millsOfFuture) {
            return (int) Math.round((millsOfFuture - millsOfNow) / 1000.0 / 60.0 / 60.0 / 24.0);
        }
        return 0;
    }

    /**
     * 获取当前日期上周日的日期
     *
     * @return 上周日的日期
     */
    public static Date getLastSunday() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, c.get(Calendar.DAY_OF_WEEK) != 1 ? -(c.get(Calendar.DAY_OF_WEEK) - 1) : -7);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    /**
     * 累加毫秒
     *
     * @param date
     * @param millis
     * @return
     */
    public static long plusMillis(Date date, long millis) {
        return date.getTime() + millis;
    }


    public static  int getYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    /**
     * 当前毫秒
     *
     * @return
     */
    public static long currentMillis() {
        return System.currentTimeMillis();
    }


    /**
     * 获取今天已过的分钟数
     * @return
     */
    public static int getTodayCurrentMinutes() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int minutes = hour * 60 + minute;
        return minutes;
    }

    /**
     * a integer to xx:xx
     * @param time
     * @return
     */
    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                //second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    /**
     * 根据用户生日计算年龄
     */
    public static int getAgeByBirthday(Date birthday) {
        Calendar cal = Calendar.getInstance();

        if (cal.before(birthday)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(birthday);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                // monthNow==monthBirth
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                // monthNow>monthBirth
                age--;
            }
        }
        return age;
    }

    public static String[] getTimeIndex(){
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DATE);
        int hour = Calendar.getInstance().get(Calendar.HOUR);
        int mini = Calendar.getInstance().get(Calendar.MINUTE);
        int se   = Calendar.getInstance().get(Calendar.SECOND)/2;
        String now = year+"_"+month+"_"+day+"_"+hour+"_"+mini+"_"+se;
        String last = null;
        if(se == 0){
            last = year+"_"+month+"_"+day+"_"+hour+"_"+(mini-1)+"_"+29;
        }else{
            last = year+"_"+month+"_"+day+"_"+hour+"_"+mini+"_"+(se-1);
        }

        return new String[]{now,last};

    }

    public static int getIndex(int minite,int length) {

        int total = 60/length;

        int[] o = new int[total];
        for(int i=0;i<total;i++){
            o[i] = length*i;
        }

        for(int i=0;i<o.length;i++) {
            if( minite >=o[i] && minite<o[i+1]){
                return i+1;
            }else if(minite<o[0]){
                return 1;
            }else if(minite>=o[o.length-1]){
                return o.length;
            }
        }
        return 1;
    }

    public static String getTimeIndex(int index){
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DATE);
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        if(index ==2) {
            return year + "_" + month + "_" + day + "_" + hour;
        }else if(index == 1){
            return year + "_" + month + "_" + day;
        }

        return "";
    }


    public static String getTimeMinuteGroupIndex(int length) {
        String timeIndexHour = getTimeIndex(2);
        int minite = 46;
        int total = 60/length;

        int[] o = new int[total];
        for(int i=0;i<total;i++){
            o[i] = length*i;
        }
        int minuteIndex = 1;
        for(int i=0;i<o.length;i++) {
            if( minite >=o[i] && i+1<=o.length-1 && minite<o[i+1]){
                minuteIndex =  i+1;
            }else if(minite<o[0]){
                minuteIndex =  1;
            }else if(minite>=o[o.length-1]){
                minuteIndex =  o.length;
            }
        }

        return timeIndexHour+"_"+minuteIndex;

    }



}
