package com.hss.core.task.impl;

import com.hss.core.task.executor.TaskExecutor;
import com.hss.core.task.reload.CronTaskReloadStrategy;

/**
* @description: cron 表达式任务
* @author hd
* @date 2024/3/20 10:02
* @version 1.0
*/
public class CronTask extends BaseTask {

    public CronTask(String cron, TaskExecutor executor) {
        super(executor);
        CronTaskReloadStrategy reloadStrategy = new CronTaskReloadStrategy(cron);
        setReloadStrategy(reloadStrategy);
        long l = System.currentTimeMillis();
        setWaitTime(reloadStrategy.getNextTime(l) - l);
    }
}
