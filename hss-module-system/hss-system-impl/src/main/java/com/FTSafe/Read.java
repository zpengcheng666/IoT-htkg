package com.FTSafe;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class Read {

    public static String getDogKey() {
        byte [] dongleInfo = new byte [1024];
        int [] count = new int[1];
        long [] handle = new long[1];
        int nRet = 0;
        Dongle dongle = new Dongle();
        //枚举锁
        nRet = dongle.Dongle_Enum(dongleInfo, count);
        if(nRet != Dongle.DONGLE_SUCCESS) {
            log.error("获取加密狗失败");
            return "";
        }

        //打开第一把锁
        nRet = dongle.Dongle_Open(handle, 0);
        if(nRet != Dongle.DONGLE_SUCCESS)
        {
            log.error("打开加密狗失败");
            return "";
        }
        //读内存区 32个字节
        int i;
        byte []memoryData  = new byte[4];
        nRet = dongle.Dongle_ReadData(handle[0],4096, memoryData,4);
        if(nRet != Dongle.DONGLE_SUCCESS)
        {
            log.error("读取加密狗数据失败");
            return "";
        }
        String s = new String(memoryData);
        //关闭加密锁
        nRet = dongle.Dongle_Close(handle[0]);
        if(nRet != Dongle.DONGLE_SUCCESS)
        {
            log.error("关闭加密狗失败");
        }
        return s;
    }

    public static void main(String[] args) {
        String dogKey = Read.getDogKey();
        System.out.println("========");
        System.out.println(StringUtils.isBlank(dogKey));
        System.out.println(dogKey);

        System.out.println("========");
    }

}