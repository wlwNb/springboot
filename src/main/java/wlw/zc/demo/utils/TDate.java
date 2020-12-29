package wlw.zc.demo.utils;

/**
 * Created by wangmin on 18/7/25.
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class TDate extends Date {

    private static final long serialVersionUID = 1L;
    private static Date minDate;
    private static Date maxDate;
    public static final TimeZone TIME_ZONE_UTC = TimeZone.getTimeZone("UTC");
    public static final TimeZone TIME_ZONE_CHN = TimeZone.getTimeZone("GMT+08:00");

    static {

        try {

            SimpleDateFormat dateFormat = getFormat();
            minDate = dateFormat.parse("1970-01-01 08:00:01");
            maxDate = dateFormat.parse("2099-12-31 23:59:59");
        } catch (ParseException e) {
            // TODO Auto-generated catch block
        }

    }

    public static SimpleDateFormat getFormat() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TIME_ZONE_CHN);
        return df;
    }

    private final static ThreadLocal<SimpleDateFormat> utcDf = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            df.setTimeZone(TIME_ZONE_UTC);
            return df;
        }
    };
    private final static ThreadLocal<SimpleDateFormat> shortDf = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            df.setTimeZone(TIME_ZONE_CHN);
            return df;
        }
    };

    public static SimpleDateFormat getShortFormat() {
        return shortDf.get();
    }


    public static SimpleDateFormat getUtcFormat() {
        return utcDf.get();
    }

    public static Date getMin() {

        return new Date(minDate.getTime());
    }

    public static Date getMax() {

        return new Date(maxDate.getTime());
    }

    public static TDate assign(Date date) {

        TDate ed = new TDate();
        ed.setTime(date.getTime());
        return ed;
    }

    public static TDate assign(String date) throws Exception {

        Date dt = getFormat().parse(date);
        TDate ed = new TDate();
        ed.setTime(dt.getTime());
        return ed;
    }

    public TDate AddDays(double d) {

        Double di = d * 24 * 60 * 60 * 1000;
        this.setTime(this.getTime() + di.longValue());
        return this;
    }

    @Override
    public String toString() {

        return toString(this);
    }

//    public String toShortString() {
//
//        return toShortString(this);
//    }

    public static String toShortString(Date dt) {

        return getShortFormat().format(dt);
    }

    public static String toString(Date dt) {
        if (dt == null) {
            return "";
        }
        return getFormat().format(dt);
    }

    public static Date bottom(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date top(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DATE, 1);
        return cal.getTime();
    }


//    public static String toShortString(Date dt) {
//
//        return dtShortFormat.format(dt);
//    }

    //与当前时间的间隔单位秒
    public static long getInterval(Date dt) {

        TDate now = new TDate();
        long betweens = (long) ((now.getTime() - dt.getTime()) / 1000);
        return betweens;
    }


    public static String format(Date time, String format) {
        SimpleDateFormat DateFormat = new SimpleDateFormat(format);
        return DateFormat.format(time);
    }

    public static Date format(String str, String format) {
        try {
            SimpleDateFormat DateFormat = new SimpleDateFormat(format);
            return new Date(DateFormat.parse(str).getTime());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Date addYear(Date time, int aYears) {
        GregorianCalendar cal = getCalendar(time);
        cal.add(GregorianCalendar.YEAR, aYears);
        return new Date(cal.getTimeInMillis());
    }

    public static Date addMonth(Date time, int aMonth, String aa) {
        GregorianCalendar cal = getCalendar(time);
        cal.add(GregorianCalendar.MONTH, aMonth);
        return new Date(cal.getTimeInMillis());
    }

    public static Date addDay(Date time, int aDays) {
        GregorianCalendar cal = getCalendar(time);
        cal.add(GregorianCalendar.DAY_OF_MONTH, aDays);
        return new Date(cal.getTimeInMillis());
    }

    public static Date addHour(Date time, int aHours) {
        GregorianCalendar cal = getCalendar(time);
        cal.add(GregorianCalendar.HOUR, aHours);
        return new Date(cal.getTimeInMillis());
    }

    public static Date addMinutes(Date time, int aMinutes) {
        GregorianCalendar cal = getCalendar(time);
        cal.add(GregorianCalendar.MINUTE, aMinutes);
        return new Date(cal.getTime().getTime());
    }

    public static Date addSecond(Date time, int aSeconds) {
        GregorianCalendar cal = getCalendar(time);
        cal.add(GregorianCalendar.SECOND, aSeconds);
        return new Date(cal.getTime().getTime());
    }

    private static GregorianCalendar getCalendar(Date time) {
        GregorianCalendar aCalendar = new GregorianCalendar();
        aCalendar.setTimeInMillis(time.getTime());
        return aCalendar;
    }

    public static long diffYear(Date time1, Date time2) {
        GregorianCalendar aCalendar1 = getCalendar(time1);
        GregorianCalendar aCalendar2 = getCalendar(time2);
        return aCalendar1.get(GregorianCalendar.YEAR) - aCalendar2.get(GregorianCalendar.YEAR);
    }

    public static long diffMonth(Date time1, Date time2) {
        GregorianCalendar aCalendar1 = getCalendar(time1);
        GregorianCalendar aCalendar2 = getCalendar(time2);
        return (aCalendar1.get(GregorianCalendar.YEAR) - aCalendar2.get(GregorianCalendar.YEAR)) * 12 +
                (aCalendar1.get(GregorianCalendar.MONTH) - aCalendar2.get(GregorianCalendar.MONTH));
    }

    public static long diffDay(Date time1, Date time2) {
        return (time1.getTime() - time2.getTime()) / 24 / 60 / 60 / 1000;
    }

    public static long diffHour(Date time1, Date time2) {
        return (time1.getTime() - time2.getTime()) / 60 / 60 / 1000;
    }

    public static long diffMinute(Date time1, Date time2) {
        return (time1.getTime() - time2.getTime()) / 60 / 1000;
    }


    public static boolean greater(Date time1, Date time2) {
        return time1.compareTo(time2) > 0 ? true : false;
    }

    public static boolean greaterEqual(Date time1, Date time2) {
        return time1.compareTo(time2) >= 0 ? true : false;
    }

    public static boolean middle(Date aValidDate, Date aIOpenSVDate, Date aExpiredate) {
        return aValidDate.compareTo(aIOpenSVDate) < 0 && aExpiredate.compareTo(aIOpenSVDate) > 0 ? true : false;
    }
}
