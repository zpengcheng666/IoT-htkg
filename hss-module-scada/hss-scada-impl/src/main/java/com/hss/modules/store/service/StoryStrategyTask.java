package com.hss.modules.store.service;

import com.hss.modules.store.entity.StoreStrategy;

/**
 * 联动定时任务
 * @author hd
 */
public interface StoryStrategyTask {

    /**
     * 系统启动的时候加载job
     */
    void initLoadTask();

    /**
     * 添加任务
     * @param strategy
     */
    void addTask(StoreStrategy strategy);

    /**
     * 更新任务
     * @param strategy
     */
    void updateTask(StoreStrategy strategy);

    /**
     * 删除任务
     * @param strategy
     */
    void removeTask(StoreStrategy strategy);


    /**
     * 执行任务
     * @param strategy
     */
    void executeTask(StoreStrategy strategy);
}
