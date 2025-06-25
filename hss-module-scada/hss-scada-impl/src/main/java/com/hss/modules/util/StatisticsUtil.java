package com.hss.modules.util;

import cn.hutool.core.date.CalendarUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StatisticsUtil {
    private StatisticsUtil(){

    }

    /* 按照年统计 */
    public static final String STAT_METHOD_YEAR = "year";

    /* 按照季度统计 */
    public static final String STAT_METHOD_QUARTER = "quarter";

    /* 按照月统计 */
    public static final String STAT_METHOD_MONTH = "month";

    /* 按照天统计 */
    public static final String STAT_METHOD_DAY = "day";

    /* 默认统计5年 */
    public static Integer STAT_DEFAULT_YEAR = 5 - 1;

    /* 默认统计8个季度 */
    public static Integer STAT_DEFAULT_QUARTER = 8 - 1;

    /* 默认统计12个月 */
    public static Integer STAT_DEFAULT_MONTH = 12 - 1;

    /* 默认统计一个月 */
    public static Integer STAT_DEFAULT_DAY = 30 - 1;

    public static List<String> generateXAxis(Date startTime, Date endTime, String statisticsMethod){
        Assert.notEmpty(statisticsMethod, "statisticsMethod must not be null");
        endTime = (endTime == null) ? new Date() : endTime;
        startTime = (startTime == null) ? generateStartTime(endTime, statisticsMethod) : startTime;

        long startDate = startTime.getTime();
        long endDate = endTime.getTime();
        if (StringUtils.equalsIgnoreCase(STAT_METHOD_YEAR, statisticsMethod)){
            return years(startDate, endDate);
        } else if (StringUtils.equalsIgnoreCase(STAT_METHOD_QUARTER, statisticsMethod)){
            return yearAndQuarter(startDate, endDate);
        } else if (StringUtils.equalsIgnoreCase(STAT_METHOD_MONTH, statisticsMethod)){
            return yearAndMonths(startDate, endDate);
        } else if (StringUtils.equalsIgnoreCase(STAT_METHOD_DAY, statisticsMethod)){
            return yearAndMonthAndDays(startDate, endDate);
        } else {
            throw new IllegalArgumentException("statisticsMethod 必须为year、quarter、month和day， 其他类型不支持");
        }
    }

    public static Date generateStartTime(Date endTime, String statisticsMethod) {
        Calendar start = CalendarUtil.calendar();
        if (StringUtils.equalsIgnoreCase(STAT_METHOD_YEAR, statisticsMethod)){
            start.add(Calendar.YEAR, -STAT_DEFAULT_YEAR);
            return CalendarUtil.beginOfYear(start).getTime();
        } else if (StringUtils.equalsIgnoreCase(STAT_METHOD_QUARTER, statisticsMethod)){
            start.add(Calendar.MONTH, -(STAT_DEFAULT_QUARTER * 3));
            return CalendarUtil.beginOfQuarter(start).getTime();
        } else if (StringUtils.equalsIgnoreCase(STAT_METHOD_MONTH, statisticsMethod)){
            start.add(Calendar.MONTH, -STAT_DEFAULT_MONTH);
            return CalendarUtil.beginOfMonth(start).getTime();
        } else if (StringUtils.equalsIgnoreCase(STAT_METHOD_DAY, statisticsMethod)){
            start.add(Calendar.MONTH, -1);
            return CalendarUtil.beginOfDay(start).getTime();
        } else {
            throw new IllegalArgumentException("statisticsMethod 必须为year、quarter、month和day， 其他类型不支持");
        }
    }

    public static List<String> years(long startDate, long endDate){
        List<String> results = new ArrayList<>();
        final Calendar cal = CalendarUtil.calendar(startDate);
        while (startDate <= endDate) {
            // 如果开始时间超出结束时间，让结束时间为开始时间，处理完后结束循环
            results.add(String.valueOf(cal.get(Calendar.YEAR)));
            cal.add(Calendar.YEAR, 1);
            startDate = cal.getTimeInMillis();
        }
        return results;
    }

    public static List<String> yearAndQuarter(long startDate, long endDate){
        List<String> results = new ArrayList<>();
        final Calendar cal = CalendarUtil.calendar(startDate);
        while (startDate <= endDate) {
            // 如果开始时间超出结束时间，让结束时间为开始时间，处理完后结束循环
            results.add(StrUtil.builder().append(cal.get(Calendar.YEAR)).append("-").append(cal.get(Calendar.MONTH) / 3 + 1).toString());
            cal.add(Calendar.MONTH, 3);
            startDate = cal.getTimeInMillis();
        }
        return results;
    }

    public static List<String> yearAndMonths(long startDate, long endDate){
        List<String> results = new ArrayList<>();
        final Calendar cal = CalendarUtil.calendar(startDate);
        while (startDate <= endDate) {
            // 如果开始时间超出结束时间，让结束时间为开始时间，处理完后结束循环
            results.add(StrUtil.builder().append(cal.get(Calendar.YEAR)).append("-").append(StringUtils.leftPad(String.valueOf(cal.get(Calendar.MONTH) + 1), 2, "0")).toString());
            cal.add(Calendar.MONTH, 1);
            startDate = cal.getTimeInMillis();
        }
        return results;
    }

    public static List<String> yearAndMonthAndDays(long startDate, long endDate){
        List<String> results = new ArrayList<>();
        final Calendar cal = CalendarUtil.calendar(startDate);
        while (startDate <= endDate) {
            // 如果开始时间超出结束时间，让结束时间为开始时间，处理完后结束循环
            results.add(StrUtil.builder().append(cal.get(Calendar.YEAR)).append("-")
                    .append(StringUtils.leftPad(String.valueOf(cal.get(Calendar.MONTH) + 1), 2, "0"))
                    .append("-")
                    .append(StringUtils.leftPad(String.valueOf(cal.get(Calendar.DATE)), 2, "0")).toString());

            cal.add(Calendar.DATE, 1);
            startDate = cal.getTimeInMillis();
        }
        return results;
    }

//    public static void main(String[] args) {
//        System.out.println(DateUtil.format(generateStartTime(new Date(), STAT_METHOD_YEAR), "yyyy-MM-dd HH:mm:ss"));
//        System.out.println(DateUtil.format(generateStartTime(new Date(), STAT_METHOD_QUARTER), "yyyy-MM-dd HH:mm:ss"));
//        System.out.println(DateUtil.format(generateStartTime(new Date(), STAT_METHOD_MONTH), "yyyy-MM-dd HH:mm:ss"));
//        System.out.println(DateUtil.format(generateStartTime(new Date(), STAT_METHOD_DAY), "yyyy-MM-dd HH:mm:ss"));
//        System.out.println(DateUtil.format(CalendarUtil.beginOfQuarter(Calendar.getInstance()).getTime(), "yyyy-MM-dd"));
//        System.out.println(generateXAxis(null, null, STAT_METHOD_YEAR));
//        System.out.println(generateXAxis(null, null, STAT_METHOD_QUARTER));
//        System.out.println(generateXAxis(null, null, STAT_METHOD_MONTH));
//        System.out.println(generateXAxis(null, null, STAT_METHOD_DAY));
//    }
}
