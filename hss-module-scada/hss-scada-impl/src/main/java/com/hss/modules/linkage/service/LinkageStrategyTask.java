package com.hss.modules.linkage.service;

import com.hss.modules.linkage.entity.LinkageStrategy;

/**
 * 联动定时任务
 * @author hd
 */
public interface LinkageStrategyTask {

    /**
     * 系统启动的时候加载job
     */
    void initLoadTask();

    /**
     * 添加任务
     * @param strategy
     */
    void addTask(LinkageStrategy strategy);

    /**
     * 更新任务
     * @param strategy
     */
    void updateTask(LinkageStrategy strategy);

    /**
     * 删除任务
     * @param strategy
     */
    void removeTask(LinkageStrategy strategy);


    /**
     * 执行任务
     * @param strategy
     */
    void executeTask(LinkageStrategy strategy);
}
