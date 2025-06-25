package com.hss.core.task;

/**
 * 任务状态
 * @author hd
 */

public enum TaskStateEnum {
    /**
     * 任务拆功能键完成
     */
    INIT,

    /**
     * 任务运行中
     */
    RUN,
    /**
     * 等待任务执行
     */
    WAIT,

    /**
     * 任务执行完毕
     */
    DESTROY
}
