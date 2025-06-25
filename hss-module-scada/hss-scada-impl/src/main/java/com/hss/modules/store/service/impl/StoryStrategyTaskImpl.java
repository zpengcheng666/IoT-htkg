package com.hss.modules.store.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hss.core.common.exception.HssBootException;
import com.hss.core.task.executor.TaskExecutor;
import com.hss.core.task.impl.CronTask;
import com.hss.core.task.impl.CycleDelayTask;
import com.hss.modules.constant.StoryConstant;
import com.hss.modules.store.constant.StoreConstant;
import com.hss.modules.store.entity.StoreStrategy;
import com.hss.modules.store.service.IStoreStrategyService;
import com.hss.modules.store.service.StoryStrategyTask;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.RedisUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;


/**
 * 联动任务管理
 * @author hd
 */
@Service
@Slf4j
public class StoryStrategyTaskImpl implements StoryStrategyTask, ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private IStoreStrategyService storeStrategyService;
    @Autowired
    private TaskExecutor sleepTaskExecutor;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 周期任务id前缀
     */
    private static final String LINKAGE_STRATEGY_CYCLE_TASK_PRE = "StoryStrategyTask:";

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        initLoadTask();
    }


    @Override
    public void initLoadTask() {
        List<StoreStrategy> cycleTaskList = storeStrategyService.listEnableByType(StoryConstant.TYPE_CYCLE);
        if (!cycleTaskList.isEmpty()){
            for (StoreStrategy strategy : cycleTaskList) {
                addCycleTask(strategy);
            }
        }
        List<StoreStrategy> timingTaskList = storeStrategyService.listEnableByType(StoryConstant.TYPE_TIME);
        if (!timingTaskList.isEmpty()){
            for (StoreStrategy strategy : timingTaskList) {
                addTimingTask(strategy);
            }
        }

    }




    @Override
    public void addTask(StoreStrategy strategy) {
        if (!StoreConstant.IS_ENABLE.equals(strategy.getIsEnable())){
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
    public void updateTask(StoreStrategy strategy) {
        removeTask(strategy);
        addTask(strategy);

    }

    @Override
    public void removeTask(StoreStrategy strategy) {
        if (StoryConstant.TYPE_CYCLE.equals(strategy.getType()) || StoryConstant.TYPE_TIME.equals(strategy.getType())){
            String taskId = getTaskId(strategy);
            sleepTaskExecutor.remove(taskId);
            redisUtil.del(taskId);
        }

    }

    @Override
    public void executeTask(StoreStrategy strategy) {
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
            storeStrategyService.runStory(strategy);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("执行存储策略定时任务失败strategy={}", JSONObject.toJSON(strategy));
        }

    }

    /**
     * 校验任务是否已经在运行
     * @param strategy
     * @param time
     * @return
     */
    private boolean checkRun(StoreStrategy strategy, Long time) {
        if (time == null){
            return true;
        }
        String taskId = getTaskId(strategy);
        Object o = redisUtil.get(taskId);
        if (o != null){
            return true;
        }else {
            long l = time - 1L;
            if (l > 0){
                redisUtil.set(taskId, 1, l);
            }
        }
        return false;
    }

    /**
     * 添加周期任务
     * @param strategy
     */
    private void addCycleTask(StoreStrategy strategy) {
        long cycleTime = getCycleTime(strategy.getUnit(), strategy.getPeriod());
        CycleDelayTask cycleDelayTask = new CycleDelayTask(cycleTime, sleepTaskExecutor);
        cycleDelayTask.setId(getTaskId(strategy));
        cycleDelayTask.setRunnable(() -> executeTask(strategy));
        cycleDelayTask.setWaitTime(getInitWaitTime(cycleTime));
        cycleDelayTask.start();
    }


    /**
     * 获取整数几时时间
     * @param cycleTime
     * @return
     */
    private long getInitWaitTime(long cycleTime){
        long h = 60L * 60L * 1000L;
        if (cycleTime >= h){
            cycleTime = 3600000L;
        }
        if (h % cycleTime == 0L){
            LocalTime now = LocalTime.now();
            long ms = (now.getHour() * 3600L + now.getMinute() * 60L + now.getSecond()) * 1000L;
            long timeOut = ms % cycleTime;
            if (timeOut == 0L){
                return 0L;
            }
            return cycleTime - timeOut;
        }else {
            return 0L;
        }
    }


    @NotNull
    private String getTaskId(StoreStrategy strategy) {
        return LINKAGE_STRATEGY_CYCLE_TASK_PRE + strategy.getId();
    }

    /**
     * 添加定时任务
     * @param strategy
     */
    private void addTimingTask(StoreStrategy strategy) {
        String cron = getCronByTime(strategy.getExpression());
        CronTask cronTask = new CronTask(cron, sleepTaskExecutor);
        cronTask.setId(getTaskId(strategy));
        cronTask.setRunnable(() -> executeTask(strategy));
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
