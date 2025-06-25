package com.hss.core.task.impl;


import com.hss.core.task.executor.TaskExecutor;

/**
 * 普通延时任务
 * @author hd
 */
public class SimplenessDelayTask extends BaseTask {
    public SimplenessDelayTask(long cycle, TaskExecutor executor) {
        super(executor);
        setWaitTime(cycle);
    }
}
