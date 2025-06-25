package com.hss.modules.alarm.hander;

import com.hss.modules.alarm.model.AlarmProcessData;

/**
* @description: Alarm处理器
* @author zpc
* @date 2024/3/20 15:00
* @version 1.0
*/
public interface AlarmProcess {

    /**
     * 处理报警
     * @param data
     * @return 是否需要下一步处理
     */
    boolean process(AlarmProcessData data);

    /**
     * 第1步
     * 取出缓存
     * 设置缓存状态
     */
    int CACHE = 10;

    /**
     * 第2步
     * 校验是否是报警
     * 设置报警状态
     */
    int CHECK = 20;

    /**
     * 第3步
     * 校验报警时机
     * 分为 新的报警，解除报警。原来就存在的报警
     */
    int CHECK_STATE = 30;

    /**
     * 第4步
     * 发布状态变量变更
     */
    int PUBLISH_STATUS_UPDATE = 40;

    /**
     * 第5步
     * 更新缓存
     */
    int CACHE_UPDATE = 50;

    /**
     * 第6步
     * 报警存储
     */
    int SAVE = 60;

    /**
     * 第7步
     * 报警信息到平台
     */
    int PUBLISH = 100;


}
