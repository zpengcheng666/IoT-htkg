package com.hss.core.task.executor;

/**
 * @author 26060
 */
public interface TaskRunExecutor  {

    /**
     * 执行任务
     */
    void execute(long currentTime);



    /**
     * 获取任务执行时间
     * @return
     */
    long getExecutorTime();

    /**
     * 获取任务id
     * @return
     */
    String getId();

    /**
     * 设置id
     * @param id
     */
    void setId(String id);
}
