package com.hss.core.task;

import com.hss.core.task.reload.TaskReloadStrategy;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

/**
 * 任务
 * @author hd
 */
public interface Task {

    /**
     * 获取任务名称
     * @return
     */
    String getName();

    /**
     * 获取任务状态
     * @return
     */
    TaskStateEnum getState();

    /**
     * 运行任务
     */
    void run();

    /**
     * 销毁前回调
     */
    void destroyFun();

    /**
     * 执行失败回调
     * @param e
     */
    void errFun(Throwable e);

    /**
     * 获取任务启动时间
     * @return
     */
    @Nullable
    Date getRunTime();

    /**
     * 启动任务
     */
    void start();

    /**
     * 删除任务
     */
    void destroy();

    /**
     * 设置执行器
     * @param runnable
     */
    void setRunnable(Runnable runnable);

    /**
     * 设置重新加载策略
     * @param reloadStrategy
     */
    void setReloadStrategy(TaskReloadStrategy reloadStrategy);
}
