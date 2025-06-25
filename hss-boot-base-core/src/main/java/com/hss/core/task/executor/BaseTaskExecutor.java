package com.hss.core.task.executor;

import com.hss.core.task.boos.BoosThreadRunnable;
import com.hss.core.task.container.TaskContainer;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 基础任务执行器
 *
 * @author hd
 */
public class BaseTaskExecutor implements TaskExecutor {
    /**
     * 工作线程
     */
    private final Executor workThreadPool;

    /**
     * 任务容器
     */
    private final TaskContainer taskContainer;

    /**
     * 任务调度器
     */
    private final BoosThreadRunnable boosThreadRunnable;


    public BaseTaskExecutor(BoosThreadRunnable boosThreadRunnable, TaskContainer taskContainer) {
        this.boosThreadRunnable = boosThreadRunnable;
        this.boosThreadRunnable.setConsumer(this::execute);

        this.taskContainer = taskContainer;

        workThreadPool = new ThreadPoolExecutor(
                5,
                100,
                300,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1000),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );


        /**
         * 任务线程
         */
        Thread boosThread = new Thread(boosThreadRunnable);
        boosThread.setName("taskExecutor");
        boosThread.start();
    }


    /**
     * 1.执行任务
     * 并删除或重新装在
     */
    private long execute(long currentTime) {
        Collection<TaskRunExecutor> list = taskContainer.listByTime(currentTime);
        if (CollectionUtils.isNotEmpty(list)) {
            for (TaskRunExecutor taskRunExecutor : list) {
                remove(taskRunExecutor.getId());
                workThreadPool.execute(() -> taskRunExecutor.execute(currentTime));
            }
        }
        return getNextTime();
    }
    /**
     * 直接执行任务
     * 并删除或重新装在
     */
    private long execute(long currentTime, TaskRunExecutor task) {
        workThreadPool.execute(() -> task.execute(currentTime));
        return getNextTime();
    }

    /**
     * 获取下一次执行时间
     */
    private long  getNextTime() {
        return taskContainer.getNextExecuteTime();
    }


    /**
     * 添加任务
     * 当任务执行时间小于等于当前时间的时候直接执行
     * 否则添加到等待中
     * 如果下一次的执行时间大于任务执行时间，更新等待任务时间
     *
     * @param task 任务
     */
    @Override
    public void addTask(TaskRunExecutor task) {
        long now = System.currentTimeMillis();
        long executorTime = task.getExecutorTime();
        if (executorTime <= now) {
            execute(now, task);
            return;
        }
        long nextTime = getNextTime();
        taskContainer.add(executorTime, task);
        if (nextTime > executorTime){
            boosThreadRunnable.updateNextTime(executorTime);
        }
    }

    /**
     * 删除任务
     *
     * @param id
     */
    @Override
    public void remove(String id) {
        taskContainer.remove(id);
    }
}
