package com.hss.core.task.impl;

import com.hss.core.task.Task;
import com.hss.core.task.TaskStateEnum;
import com.hss.core.task.executor.TaskExecutor;
import com.hss.core.task.executor.TaskRunExecutor;
import com.hss.core.task.reload.TaskReloadStrategy;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.UUID;

/**
* @description: BaseTask任务
* @author zpc
* @date 2024/3/20 10:01
* @version 1.0
*/
@Slf4j
public class BaseTask implements Task, TaskRunExecutor {

    private String id = UUID.randomUUID().toString();

    /**
     * 任务名称
     */
    private String name;

    /**
     * 任务状态
     */
    private TaskStateEnum state = TaskStateEnum.INIT;

    /**
     * 任务执行的代码
     */
    private Runnable runnable;

    /**
     * 重新装在策略
     */
    private TaskReloadStrategy reloadStrategy;

    /**
     * 开始运行的时间
     */
    private Date runTime;

    /**
     * 任务执行器
     */
    private TaskExecutor taskExecutor;

    /**
     * 执行时间
     */
    private long nextTime;

    public BaseTask(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void setReloadStrategy(TaskReloadStrategy reloadStrategy) {
        this.reloadStrategy = reloadStrategy;
    }

    public void setTaskExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public void setWaitTime(long waitTime) {
        this.nextTime = System.currentTimeMillis() + waitTime;
    }

    @Override
    public synchronized TaskStateEnum getState() {
        return state;
    }

    protected synchronized void setState(TaskStateEnum state) {
        this.state = state;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Date getRunTime() {
        return runTime;
    }

    @Override
    public void start() {
        setState(TaskStateEnum.WAIT);
        taskExecutor.addTask(this);
    }

    @Override
    public void destroy() {
        destroyExecute();
        setState(TaskStateEnum.DESTROY);
        taskExecutor.remove(this.getId());
    }

    @Override
    public void run() {
        if (runnable != null) {
            runnable.run();
        }
    }

    @Override
    public void execute(long currentTime) {
        runExecute();
        boolean b = reloadStrategyExecute(currentTime);
        if (b) {
            taskExecutor.addTask(this);
        }
    }

    /**
     * 执行销毁方法
     */
    private void destroyExecute() {
        try {
            destroyFun();
        } catch (Throwable e) {
            log.error("执行销毁程序失败", e);
        }
    }

    /**
     * 执行软方法
     */
    private void runExecute() {
        if (getState() == TaskStateEnum.RUN) {
            return;
        }
        setState(TaskStateEnum.RUN);
        runTime = new Date();
        try {
            run();
        } catch (Throwable e) {
            errFunExecute(e);
        }
        runTime = null;
    }

    /**
     * 执行重装载策略
     */
    private boolean reloadStrategyExecute(long currentTime) {
        if (reloadStrategy == null) {
            return false;
        }
        if (!reloadStrategy.isHaveNext()) {
            return false;
        }
        try {
            this.nextTime = reloadStrategy.getNextTime(currentTime);
            setState(TaskStateEnum.WAIT);
            return true;
        } catch (Throwable e) {
            log.error("获取重新加载时间失败", e);
        }
        return false;
    }

    /**
     * 执行错误回调
     *
     * @param e
     */
    private void errFunExecute(Throwable e) {
        try {
            errFun(e);
        } catch (Throwable ex) {
            log.error("执行错误处理方法异常", ex);
        }
    }


    @Override
    public long getExecutorTime() {
        return nextTime;
    }

    @Override
    public void destroyFun() {

    }


    @Override
    public void errFun(Throwable e) {

    }
}
