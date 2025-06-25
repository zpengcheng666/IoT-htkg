package com.hss.modules.scada.constant;

/**
 * @author zpc
 * @version 1.0
 * @description: 常用的常数标志
 * @date 2024/3/19 16:18
 */
public interface ScadaConstant {

    /**
     * 环境标志
     */
    String HUANJING = "huanjing";

    /**
     * 设备属性英文名称，呼叫控制属性
     */
    String CALL = "callCtrl";

    /**
     * 功能码
     */
    String CODE = "0";

    /**
     * 没有找到数据
     */
    String NOT_FOUND = "2";

    /**
     * 标志
     */
    String KEY = "code";

    /**
     * 报警
     */
    String ALARM = "alarm";

    /**
     * 值班
     */
    String DUTY = "duty";

    /**
     * 门禁
     */
    String DOOR = "door";

    /**
     * 刷卡
     */
    String CARD = "card";

    /**
     * 通知公告
     */
    String NOTICE = "notice";

    /**
     * 通知公告
     */
    String WEATHER = "weather";

    /**
     * 卫星
     */
    String SATELLITE = "satellite";

    String SATELLITENOT = "satelliteNot";

    /**
     * 应急处置
     */
    String YJCZ = "yjcz";

    /**
     * 应急处置
     */
    String AJM = "ajm";

    /**
     * 进入人数
     */
    String JINRENSHU = "jinrenshu";

    /**
     * 出门人数
     */
    String CHURNSHU = "churnshu";

    /**
     * 变量关联
     */
    String REDIS_KEY_REGISTER = "attrVar::";

    /**
     * 点位关联
     */
    String REDIS_KEY_POINT_ATTR = "pointAttr::";
    String REDIS_KEY_POINT_EXPRESSION_ATTR = "pointExpressionAttr::";

    /**
     * 设备缓存
     */
    String REDIS_KEY_DEVICE_CACHE = "sDevice";

    /**
     * 设备属性关联
     */
    String REDIS_KEY_DEVICE_ATTR = "deviceAttr::";

    /**
     * 属性缓存
     */
    String REDIS_KEY_ATTR = "attr";

    /**
     * 设备数据redis key
     */
    String REDIS_KEY_ATTR_DATA = "ad:";

    /**
     * 点位关联
     */
    String RELATION_TYPE_POINT = "point";

    /**
     * 变量关联
     */
    String RELATION_TYPE_VAR = "var";

    Integer IS_ONE = 1;

    String ENV_PARAM = "scada_env_table";

    String ONLINE_STATE = "onlineState";

    String REDIS_KEY_DEVICE_SCENE = "DS";

}
