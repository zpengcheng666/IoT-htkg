package com.hss.core.task.container;

import com.hss.core.task.executor.TaskRunExecutor;

import java.util.Collection;

/**
 * 任务容器接口
 * @author hd
 */
public interface TaskContainer {

    /**
     * 获取待执行任务
     * @param time
     * @return
     */
    Collection<TaskRunExecutor> listByTime(long time);

    /**
     * 添加任务
     * @param task
     */
    void add(long time, TaskRunExecutor task);

    /**
     * 移除任务
     * @param id
     */
    void remove(String id);

    /**
     * 获取最近任务的执行时间
     * @return
     */
    long getNextExecuteTime();

}
