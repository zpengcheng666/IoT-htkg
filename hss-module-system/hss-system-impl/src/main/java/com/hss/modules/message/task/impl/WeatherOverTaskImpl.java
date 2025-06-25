package com.hss.modules.message.task.impl;

import com.alibaba.fastjson.JSONObject;
import com.hss.core.task.executor.TaskExecutor;
import com.hss.core.task.impl.SimplenessDelayTask;
import com.hss.modules.message.entity.PublishWeather;
import com.hss.modules.message.service.IPublishWeatherService;
import com.hss.modules.message.task.WeatherOverTask;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.RedisUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * 天气消息过期任务管理
 * @author hd
 */
@Service
@Slf4j
public class WeatherOverTaskImpl implements WeatherOverTask, ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private IPublishWeatherService publishWeatherService;
    @Autowired
    private TaskExecutor sleepTaskExecutor;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 周期任务id前缀
     */
    private static final String TASK_PRE = "weatherOverTask:";

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        initLoadTask();
    }

    @Override
    public void initLoadTask() {
        List<PublishWeather> messageList = publishWeatherService.listNotOver();
        if (!messageList.isEmpty()){
            for (PublishWeather message : messageList) {
                addTask(message);
            }
        }
    }


    @Override
    public void addTask(PublishWeather message) {
        Date weatherTime = message.getWeatherTime();
        LocalDate localDate = weatherTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1L);
        LocalDateTime of = LocalDateTime.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth(), 0, 0, 0);
        long time = Date.from(of.atZone(ZoneId.systemDefault()).toInstant()).getTime();
        long cycle = time - System.currentTimeMillis();
        cycle = Math.max(cycle, 0L);
        SimplenessDelayTask task = new SimplenessDelayTask(cycle, sleepTaskExecutor);
        task.setId(getTaskId(message));
        task.setRunnable(() -> executeTask(message));
        task.start();
    }

    @Override
    public void updateTask(PublishWeather message) {
        removeTask(message);
        addTask(message);

    }

    @Override
    public void removeTask(PublishWeather message) {
        String taskId = getTaskId(message);
        sleepTaskExecutor.remove(taskId);
        redisUtil.del(taskId);

    }

    @Override
    public void executeTask(PublishWeather message) {
        if (checkRun(message)) {
            return;
        }
        try {
            publishWeatherService.overMessage(message);
        } catch (Exception e) {
            log.error("执行天气消息过期定时任务失败message={}", JSONObject.toJSON(message));
        }
    }

    /**
     * 校验任务是否已经在运行
     * @param message
     * @return
     */
    private boolean checkRun(PublishWeather message) {
        String taskId = getTaskId(message);
        Object o = redisUtil.get(taskId);
        if (o != null){
            return true;
        }else {
            redisUtil.set(taskId, 1, 10L * 60L);
        }
        return false;
    }

    @NotNull
    private String getTaskId(PublishWeather strategy) {
        return TASK_PRE + strategy.getId();
    }
}
