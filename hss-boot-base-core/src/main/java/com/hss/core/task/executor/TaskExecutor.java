package com.hss.core.task.executor;

/**
 * @author 任务执行器
 */
public interface TaskExecutor {
    /**
     * 添加任务
     * @param task
     */
    void addTask(TaskRunExecutor task);

    /**
     * 删除任务
     * @param id
     */
    void remove(String id);
}
