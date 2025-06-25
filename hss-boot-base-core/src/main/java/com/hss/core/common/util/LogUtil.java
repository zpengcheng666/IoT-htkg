package com.hss.core.common.util;


public class LogUtil {

    private static final ThreadLocal<String> LOG_THREAD_LOCAL = new ThreadLocal<>();


    public static String getOperate(){
        return LOG_THREAD_LOCAL.get();
    }

    public static void setOperate(String operate){
        LOG_THREAD_LOCAL.set(operate);
    }

    public static void remove(){
        LOG_THREAD_LOCAL.remove();
    }

}
