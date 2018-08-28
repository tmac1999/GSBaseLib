package base.app.com.gaosi.gsbaselib.utils;

import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.PatternSyntaxException;

/**
 * Created by pingfu on 2018/2/27.
 */

public class DateUtil {
    //中文日期格式
    public static final String SIMPLE_CHINESE_FORMAT = "yyyy年MM月dd日 HH时mm分ss秒";

    //默认日期格式
    public static final String SIMPLE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * @param data       Date类型的时间
     * @param formatType 格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
     * @return date类型转换为String类型
     */
    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType, Locale.getDefault()).format(data);
    }

    /**
     * @param currentTime 要转换的long类型的时间
     * @param formatType 要转换的string类型的时间格式
     * @return long类型转换为String类型
     * @throws Exception
     */
    public static String longToString(long currentTime, String formatType)
            throws Exception {
        Date date = longToDate(currentTime, formatType); // long类型转成Date类型
        String strTime = dateToString(date, formatType); // date类型转成String
        return strTime;
    }

    /**
     * @param strTime 要转换的string类型的时间，
     * @param formatType 要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日HH时mm分ss秒
     * @return string类型转换为date类型  strTime的时间格式必须要与formatType的时间格式相同
     * @throws Exception
     */
    public static Date stringToDate(String strTime, String formatType)
            throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    /**
     * @param currentTime 要转换的long类型的时间
     * @param formatType  要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
     * @return long转换为Date类型
     * @throws Exception
     */
    public static Date longToDate(long currentTime, String formatType)
            throws Exception {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }

    /**
     * @param strTime    要转换的String类型的时间
     * @param formatType 时间格式
     * @return string类型转换为long类型
     * @throws Exception
     */
    public static long stringToLong(String strTime, String formatType) {
        try {
            Date date = stringToDate(strTime, formatType); // String类型转成date类型
            if (date == null) {
                return 0;
            } else {
                long currentTime = dateToLong(date); // date类型转成long类型
                return currentTime;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * @param date 要转换的date类型的时间
     * @return date类型转换为long类型
     */
    public static long dateToLong(Date date) {
        return date.getTime();
    }


    /**
     * 约课页
     * 初始化时间
     */
    public static HashMap<String, String> getBespokeTime() {
        //dateMap1 = new HashMap<String, String>();
        HashMap<String, String> dateMap = new HashMap<>();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(cal.getTimeInMillis());
        cal.add(Calendar.DAY_OF_YEAR, -1);
        for (int i = 0; i < 14; ++i) {
            cal.add(Calendar.DAY_OF_YEAR, 1);
            String strDa = (String) DateFormat.format("MM月dd日 (EEEE)", cal);
            String strDate = (String) DateFormat.format("yyyy-MM-dd", cal);
            if (i == 0) {
                strDa = "今天";
            } else if (i == 1) {
                // strDa.split(regularExpression, limit);
                String strEEE = "";
                try {
                    strEEE = strDa.split("日 ")[1];
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                } catch (NullPointerException e1) {
                    e1.printStackTrace();
                } catch (PatternSyntaxException e2) {
                    e2.printStackTrace();
                }
                strDa = "明天 " + strEEE;
            }
            dateMap.put(strDa, strDate);
            //dateMap1.put(strDate, strDa);
        }
        return dateMap;
    }

    /**
     * 判断日期是星期几
     *
     * @param pTime 设置的需要判断的时间
     * @return dayForWeek 周*
     */
    public static String getWeek(long pTime) {
        String week = "";
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(pTime);
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return "周日";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            return "周一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
            return "周二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
            return "周三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
            return "周四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            return "周五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            return "周六";
        }
        return week;
    }

    /**
     * 日期字符串格式转化，由格式1转化为格式2
     *
     * @param dateStr     日期字符串
     * @param formatType1 格式1
     * @param formatType2 格式2
     * @return
     */
    public static String translateDateStr(String dateStr, String formatType1, String formatType2) {
        String str = dateStr;
        try {
            Date date = stringToDate(dateStr, formatType1);
            str = dateToString(date, formatType2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return str;
    }

    /**
     * 得到当前日期间隔几天的日期
     *
     * @param date       当前日期
     * @param betweenDay 间隔的天数，正数为当前日期的后几天，负数为当前日期的前几天
     * @return 需要的日期
     */
    public static Date getNextDate(Date date, int betweenDay) {
        long dateLong = dateToLong(date);
        dateLong = dateLong + 24 * 60 * 60 * 1000 * betweenDay;

        return new Date(dateLong);
    }
}
