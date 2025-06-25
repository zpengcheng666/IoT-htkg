package com.hss.modules.linkage.constant;

/**
 * @author hd
 */
public interface LinkageConstant {

    /**
     * redisKey+属性id  存储 关联的联动策略id
     */
    String REDIS_KEY_REGISTER = "linkageSR:";
    /**
     * 联动策略缓存
     */
    String REDIS_KEY_LINKAGE_STRATEGY = "linkageStrategy";
    String REDIS_KEY_LINKAGE_STRATEGY_UTIL = "linkageStrategy::";

    /**
     * 使能
     */
    String IS_ENABLE = "1";


    /**
     * 条件联动
     */
    String TYPE_CONDITION = "condition";


    /**
     * 动作类型  变量赋值
     */
    String ACT_TYPE_SET = "set";
    /**
     * 世邦广播
     */
    String ACT_TYPE_PUBLISH_SH = "publishSH";
    /**
     * 来帮广播
     */
    String ACT_TYPE_PUBLISH_LH = "publishLH";
    /**
     * 预置位调用
     */
    String ACT_TYPE_PRE_LOCATION = "perLocation";

}
