package com.hss.modules.maintain.constant;


/**
 * 常量
 * @author hd
 */
public interface MaintainConstant {

    /**
     * 初始状态
     */
    Integer RECORD_STATE_DEFAULT = 0;

    /**
     * 签发状态
     */
    Integer RECORD_STATE_CONFIRM = 1;

    /**
     * 执行状态
     */
    Integer RECORD_STATE_ACT = 2;

    /**
     * 执行完成状态
     */
    Integer RECORD_STATE_ACT_COMPLETE = 3;

    /**
     * 完成
     */
    Integer RECORD_STATE_COMPLETE = 4;


    /**
     * 流程项完成状态
     */
    Integer MAINTAIN_RECORD_OP_COMPLETE= 1;
}
