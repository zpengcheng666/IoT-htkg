package com.hss.core.task.reload;

/**
 * 周期延时任务
 * @author 26060
 */
public class CycleTaskReloadStrategy implements TaskReloadStrategy{
    private final long cycle;
    private long upExecutorTime = 0L;

    public CycleTaskReloadStrategy(long cycle) {
        this.cycle = cycle;
    }


    @Override
    public long getNextTime(long currentTime) {
        if (upExecutorTime == 0L){
            upExecutorTime = currentTime;
        }else {
            upExecutorTime = upExecutorTime + cycle;
        }
        return  upExecutorTime + cycle;
    }

    @Override
    public boolean isHaveNext() {
        return true;
    }
}
