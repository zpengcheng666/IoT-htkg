package com.hss.core.task.impl;


import com.hss.core.task.executor.TaskExecutor;
import com.hss.core.task.reload.CycleTaskReloadStrategy;

/**
 * 周期延时任务
 * @author hd
 */
public class CycleDelayTask extends BaseTask {

    public CycleDelayTask(long cycle, TaskExecutor executor) {
        super(executor);
        this.setReloadStrategy(new CycleTaskReloadStrategy(cycle));
    }
}
