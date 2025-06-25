package com.hss.modules.message.task;

import com.hss.modules.message.entity.PublishWeather;

/**
 * 天气过期任务
 * @author hd
 */
public interface WeatherOverTask {

    /**
     * 系统启动的时候加载job
     */
    void initLoadTask();

    /**
     * 添加任务
     * @param message
     */
    void addTask(PublishWeather message);

    /**
     * 更新任务
     * @param message
     */
    void updateTask(PublishWeather message);

    /**
     * 删除任务
     * @param message
     */
    void removeTask(PublishWeather message);

    /**
     * 执行任务
     * @param message
     */
    void executeTask(PublishWeather message);
}
