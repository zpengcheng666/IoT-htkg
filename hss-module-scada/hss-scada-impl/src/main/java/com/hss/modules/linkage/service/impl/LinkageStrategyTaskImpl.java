package com.hss.modules.linkage.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hss.core.common.exception.HssBootException;
import com.hss.core.task.executor.TaskExecutor;
import com.hss.core.task.impl.CronTask;
import com.hss.core.task.impl.CycleDelayTask;
import com.hss.modules.constant.StoryConstant;
import com.hss.modules.linkage.constant.LinkageConstant;
import com.hss.modules.linkage.entity.LinkageStrategy;
import com.hss.modules.linkage.service.ILinkageStrategyService;
import com.hss.modules.linkage.service.LinkageStrategyTask;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.RedisUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

;

/**
 * 联动任务管理
 * @author hd
 */
@Service
@Slf4j
public class LinkageStrategyTaskImpl implements LinkageStrategyTask, ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private ILinkageStrategyService linkageStrategyService;
    @Autowired
    private TaskExecutor sleepTaskExecutor;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 周期任务id前缀
     */
    private static final String LINKAGE_STRATEGY_CYCLE_TASK_PRE = "linkageStrategyTask:";

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        initLoadTask();
    }


    @Override
    public void initLoadTask() {
        List<LinkageStrategy> cycleTaskList = linkageStrategyService.listEnableCycleTask();
        if (!cycleTaskList.isEmpty()){
            for (LinkageStrategy linkageStrategy : cycleTaskList) {
                addCycleTask(linkageStrategy);
            }
        }
        List<LinkageStrategy> timingTaskList = linkageStrategyService.listEnableTimingTask();
        if (!timingTaskList.isEmpty()){
            for (LinkageStrategy linkageStrategy : timingTaskList) {
                addTimingTask(linkageStrategy);
            }
        }

    }




    @Override
    public void addTask(LinkageStrategy strategy) {
        if (!LinkageConstant.IS_ENABLE.equals(strategy.getIsEnable())){
            return;
        }
        if (StoryConstant.TYPE_CYCLE.equals(strategy.getType())){
            addCycleTask(strategy);
            return;
        }
        if (StoryConstant.TYPE_TIME.equals(strategy.getType())){
            addTimingTask(strategy);
        }

    }

    @Override
    public void updateTask(LinkageStrategy strategy) {
        removeTask(strategy);
        addTask(strategy);

    }

    @Override
    public void removeTask(LinkageStrategy strategy) {
        if (StoryConstant.TYPE_CYCLE.equals(strategy.getType()) || StoryConstant.TYPE_TIME.equals(strategy.getType())){
            String taskId = getTaskId(strategy);
            sleepTaskExecutor.remove(taskId);
            redisUtil.del(taskId);
        }

    }

    @Override
    public void executeTask(LinkageStrategy strategy) {
        Long time = null;
        if (StoryConstant.TYPE_CYCLE.equals(strategy.getType())){
            long cycleTime = getCycleTime(strategy.getUnit(), strategy.getPeriod());
            time = cycleTime / 1000L;

        }
        if (StoryConstant.TYPE_TIME.equals(strategy.getType())){
            time = 24L * 60L * 60L;
        }
        if (checkRun(strategy, time)) {
            return;
        }
        try {
            linkageStrategyService.runAction(strategy);
        } catch (Exception e) {
            log.error("执行联动策略定时任务失败strategy={}", JSONObject.toJSON(strategy));
        }

    }

    /**
     * 校验任务是否已经在运行
     * @param strategy
     * @param time
     * @return
     */
    private boolean checkRun(LinkageStrategy strategy, Long time) {
        if (time == null){
            return true;
        }
        String taskId = getTaskId(strategy);
        Object o = redisUtil.get(taskId);
        if (o != null){
            return true;
        }else {
            long l = time - 1L;
            if (l >0){
                redisUtil.set(taskId, 1, l);
            }

        }
        return false;
    }

    /**
     * 添加周期任务
     * @param linkageStrategy
     */
    private void addCycleTask(LinkageStrategy linkageStrategy) {
        long cycleTime = getCycleTime(linkageStrategy.getUnit(), linkageStrategy.getPeriod());
        CycleDelayTask cycleDelayTask = new CycleDelayTask(cycleTime, sleepTaskExecutor);
        cycleDelayTask.setId(getTaskId(linkageStrategy));
        cycleDelayTask.setRunnable(() -> executeTask(linkageStrategy));
        cycleDelayTask.start();
    }

    @NotNull
    private String getTaskId(LinkageStrategy linkageStrategy) {
        return LINKAGE_STRATEGY_CYCLE_TASK_PRE + linkageStrategy.getId();
    }

    /**
     * 添加定时任务
     * @param linkageStrategy
     */
    private void addTimingTask(LinkageStrategy linkageStrategy) {
        String cron = getCronByTime(linkageStrategy.getExpression());
        CronTask cronTask = new CronTask(cron, sleepTaskExecutor);
        cronTask.setId(getTaskId(linkageStrategy));
        cronTask.setRunnable(() -> executeTask(linkageStrategy));
        cronTask.start();
    }

    /**
     * 获取周期时间
     * @param unit 单位
     * @param periodStr 周期字符串
     * @return 周期ms
     */
    private long getCycleTime(String unit, String periodStr){
        long period = Long.parseLong(periodStr);
        if (StoryConstant.UNIT_H.equals(unit)) {
            return period * 1000L * 60L * 60L;
        }
        if (StoryConstant.UNIT_M.equals(unit)) {
            return period * 1000L * 60L;
        }
        if (StoryConstant.UNIT_S.equals(unit)) {
            return period * 1000L;
        }
        throw new HssBootException("单位错误");
    }

    /**
     * 根据时间获取cron
     * @param time 时间字符串
     * @return cron表达式
     */
    private String getCronByTime(String time){
        LocalTime parse = LocalTime.parse(time);
        int s = parse.getSecond();
        int m = parse.getMinute();
        int h = parse.getHour();
        return s + " " + m + " " + h + " * * ? ";
    }

}
