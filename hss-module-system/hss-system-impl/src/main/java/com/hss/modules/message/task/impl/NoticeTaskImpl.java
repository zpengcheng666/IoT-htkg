package com.hss.modules.message.task.impl;

import com.alibaba.fastjson.JSONObject;
import com.hss.core.task.executor.TaskExecutor;
import com.hss.core.task.impl.SimplenessDelayTask;
import com.hss.modules.message.entity.PublishNotice;
import com.hss.modules.message.service.IPublishNoticeService;
import com.hss.modules.message.task.NoticeTask;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.RedisUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


/**
 * 天气消息过期任务管理
 * @author hd
 */
@Service
@Slf4j
public class NoticeTaskImpl implements NoticeTask, ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private IPublishNoticeService publishNoticeService;
    @Autowired
    private TaskExecutor sleepTaskExecutor;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 周期任务id前缀
     */
    private static final String OVER_TASK_PRE = "noticeOverTask:";
    private static final String IN_TASK_PRE = "noticeInTask:";

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        initLoadTask();
    }

    @Override
    public void initLoadTask() {
        List<PublishNotice> messageList = publishNoticeService.listNotOverList();
        if (!messageList.isEmpty()){
            for (PublishNotice message : messageList) {
                addTask(message);
            }
        }
    }

    @Override
    public void addTask(PublishNotice message) {
        addOverTask(message);
        addInTask(message);
    }

    /**
     * 添加过期任务
     * @param message
     */
    private void addOverTask(PublishNotice message) {
        if (message.getState().equals(3)){
            return;
        }
        Date time = message.getOverdueTime();
        if (time == null){
            return;
        }
        long cycle = time.getTime() - System.currentTimeMillis();
        cycle = Math.max(cycle, 0L);
        SimplenessDelayTask task = new SimplenessDelayTask(cycle, sleepTaskExecutor);
        task.setId(getOverTaskId(message));
        task.setRunnable(() -> executeOverTask((message)));
        task.start();
    }
    /**
     * 添加进入任务
     * @param message
     */
    private void addInTask(PublishNotice message) {
        if (!message.getState().equals(1)) {
            return;
        }
        Date time = message.getEffectiveTime();
        if (time == null){
            return;
        }
        long cycle = time.getTime() - System.currentTimeMillis();
        cycle = Math.max(cycle, 0L);
        SimplenessDelayTask task = new SimplenessDelayTask(cycle, sleepTaskExecutor);
        task.setId(getInTaskId(message));
        task.setRunnable(() -> executeInTask((message)));
        task.start();
    }

    @Override
    public void updateTask(PublishNotice message) {
        removeTask(message);
        addTask(message);

    }

    @Override
    public void removeTask(PublishNotice message) {
        String overTaskId = getOverTaskId(message);
        sleepTaskExecutor.remove(overTaskId);
        redisUtil.del(overTaskId);

        String inTaskId = getInTaskId(message);
        sleepTaskExecutor.remove(inTaskId);
        redisUtil.del(inTaskId);

    }

    /**
     * 过期
     * @param message
     */
    private void executeOverTask(PublishNotice message) {
        if (checkOverRun(message)) {
            return;
        }
        try {
            publishNoticeService.overMessage(message);
        } catch (Exception e) {
            log.error("执行通知公告消息过期定时任务失败message={}", JSONObject.toJSON(message));
        }

    }
    /**
     * 过期
     * @param message
     */
    private void executeInTask(PublishNotice message) {
        if (checkInRun(message)) {
            return;
        }
        try {
            publishNoticeService.inMessage(message);
        } catch (Exception e) {
            log.error("执行通知公告生效定时任务失败message={}", JSONObject.toJSON(message));
        }

    }


    /**
     * 校验任务是否已经在运行
     * @param message
     * @return
     */
    private boolean checkOverRun(PublishNotice message) {
        String taskId = getOverTaskId(message);
        Object o = redisUtil.get(taskId);
        if (o != null){
            return true;
        }else {
            redisUtil.set(taskId, 1, 10L * 60L);
        }
        return false;
    }
    /**
     * 校验任务是否已经在运行
     * @param message
     * @return
     */
    private boolean checkInRun(PublishNotice message) {
        String taskId = getInTaskId(message);
        Object o = redisUtil.get(taskId);
        if (o != null){
            return true;
        }else {
            redisUtil.set(taskId, 1, 10L * 60L);
        }
        return false;
    }



    @NotNull
    private String getOverTaskId(PublishNotice strategy) {
        return OVER_TASK_PRE + strategy.getId();
    }
    @NotNull
    private String getInTaskId(PublishNotice strategy) {
        return IN_TASK_PRE + strategy.getId();
    }



}
