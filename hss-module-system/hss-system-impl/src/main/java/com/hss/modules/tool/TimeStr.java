package com.hss.modules.tool;

import org.apache.commons.lang3.time.DateUtils;

/**
 * @ClassDescription:
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/2/27 10:19
 */
public class TimeStr {

    private static final int M = 60;
    private static final int H = M * 60;


    public static  String getTimeStr(int time) {

        long hours = time / H;

        long minutes = (time % H) / M;

        long seconds = (time % M);

        String result = hours + "小时 " + minutes + "分 " + seconds + "秒";

//        if (time < M) {
//            return time + "秒";
//        }
//        if (time < H) {
//            return time / M + "分";
//        }
//        return time / H + "小时";
        return result;
    }
}
