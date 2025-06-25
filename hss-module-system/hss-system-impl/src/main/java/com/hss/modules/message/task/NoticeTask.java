package com.hss.modules.message.task;

import com.hss.modules.message.entity.PublishNotice;

/**
 * 消息任务
 * @author hd
 */
public interface NoticeTask {

    /**
     * 系统启动的时候加载job
     */
    void initLoadTask();

    /**
     * 添加任务
     * @param message
     */
    void addTask(PublishNotice message);


    /**
     * 更新任务
     * @param message
     */
    void updateTask(PublishNotice message);

    /**
     * 删除任务
     * @param message
     */
    void removeTask(PublishNotice message);
}
