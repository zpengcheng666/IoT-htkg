package com.hss.modules.constant;

/**
 * 常量
 * @author 26060
 */
public interface StoryConstant {

    /**
     * 存储类型-固定时间
     */
    String TYPE_TIME = "timing";

    /**
     * 村春类型 周期
     */
    String TYPE_CYCLE = "cycle";

    /**
     * 卫星临空事件
     */
    String TYPE_SATELLITE = "satellite";

    /**
     * 报警确认事件
     */
    String TYPE_ALARM_CONFIRM = "alarmAck";


    /**
     * 单位 小时
     */
    String UNIT_H = "hour";

    /**
     * 单位 分钟
     */
    String UNIT_M = "minute";

    /**
     * 单位 秒
     */
    String UNIT_S = "second";


    /**
     * reids key
     */
    String REDIS_KEY_STORY_JOB = "storyJob";



    /**
     * reids key 联动任务
     */
    String REDIS_KEY_LINKAGE_JOB = "linkageJob";

}
