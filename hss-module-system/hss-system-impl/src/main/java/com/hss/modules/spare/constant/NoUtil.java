package com.hss.modules.spare.constant;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @ClassDescription:
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/4/25 16:24
 */
public class NoUtil {

    private static DateTimeFormatter DEF = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public static String getNo(String pre, LocalDate day, long max) {
        return pre + "-" + DEF.format(day) + "-" + getCode(max);
    }
    private static String getCode(long max) {
        String code = String.valueOf(max + 1);
        int length = code.length();
        if (length >= 6) {
            return code;
        }
        StringBuilder codeBuild = new StringBuilder();
        for (int i = 0; i < 6-length; i++) {
            codeBuild.append("0");
        }
        codeBuild.append(code);
        return codeBuild.toString();
    }

}
