package com.hss.modules.message.util;

import com.hss.core.common.exception.HssBootException;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @ClassDescription:
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/5/17 10:58
 */
public final class TimeUtil {

    /**
     * 获取时间间隔
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 时间间隔字符串
     */
    public static String getDurationStr(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            throw new HssBootException("开始时间和结束时间不能为空");
        }
        if (startTime.isAfter(endTime)) {
            throw new HssBootException("开始时间必须小于结束时间");
        }
        Duration duration = Duration.between(startTime, endTime);
        long se = duration.getSeconds();
        if (se <= 60L) {
            return se + "秒";
        } else if (se <= 3600L){
            long s = se % 60L;
           return se/60L + (s < 10 ?  ":0" : ":") + s;
        } else {
            long h = se / 3600L;
            long ms = se % 3600L;
            long m = ms / 60L;
            long s = ms % 60L;
            return h + (m < 10 ? ":0" : ":" ) + m + (s < 10 ?  ":0" : ":") + s;
        }
    }
}
