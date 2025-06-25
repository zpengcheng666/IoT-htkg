package com.hss.core.task.reload;

/**
 * 重新装在策略
 * @author hd
 */
public interface TaskReloadStrategy {




    /**
     * 下一次执行时间
     * @param currentTime 当前任务执行时间
     * @return
     */
    long getNextTime(long currentTime);

    boolean isHaveNext();
}
