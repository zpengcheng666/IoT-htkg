package com.hss.modules.alarm.constant;

/**
 * 报警常量类
 * @author hd
 */
public interface AlarmConstant {

    /**
     * 固定值报警
     */
    String ALARM_TYPE_VALUE = "specificValue";

    /**
     * 范围报警
     */
    String ALARM_TYPE_RANGE = "interval";

    /**
     * 范围报警分割符
     */
    String ALARM_TYPE_RANGE_SPLIT = "~";

    /**
     * 变化率报警
     * 变化率 =（（最大值 —  最小值）* 100）/ （（最大值产生的时间 —  最小值产生的时间）*（变量最大值 —  变量最小值）* 变化周期）
     */
    String ALARM_TYPE_CHANGE_RATE= "rateOfChange";

    /**
     * redis key 用于计算变化率
     */
    String REDIS_KEY_ALARM_CHANGE_RATE_DATA = "acrd:";

    /**
     * 报警数据
     */
    String REDIS_KEY_ALARM = "alarm:";


    /**
     * 报警存储和发布条件
     */
    String ALARM_SAVE_OR_PUBLISH_CONDITION_ALL = "all";
    /**
     * 进入报警
     */
    String ALARM_SAVE_OR_PUBLISH_CONDITION_TRUE = "true";
    /**
     * 报警解除
     */
    String ALARM_SAVE_OR_PUBLISH_CONDITION_FALSE = "false";
    /**
     * 不做操作
     */
    String ALARM_SAVE_OR_PUBLISH_CONDITION_NONE = "none";

    /**
     * 报警状态-新的报警
     */
    Integer ALARM_STATUS_NEW = 1;
    /**
     * 报警状态-解除报警
     */
    Integer ALARM_STATUS_FALSE = 2;

    /**
     * 报警状态-原来就存在的报警
     */
    Integer ALARM_STATUS_OLD = 0;

    /**
     * 失能
     */
    String NOT_ENABLE = "0";

    /**
     * 使能
     */
    String IS_ENABLE = "1";


    /**
     * 注册
     */
    String REDIS_KEY_REGISTER = "alarmSR:";

    /**
     * 报警策略redisKey
     */
    String REDIS_KEY_ALARM_STRATEGY = "alarmStrategy";
    String REDIS_KEY_ALARM_STRATEGY_UTIL = REDIS_KEY_ALARM_STRATEGY + "::";




}
