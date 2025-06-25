package com.hss.core.task.boos;

import java.util.function.LongFunction;

/**
 * @author 调度线程执行程序
 */
public interface BoosThreadRunnable extends Runnable {

    /**
     * 设置执行器
     * @param consumer
     */
    void setConsumer(LongFunction<Long> consumer);

    /**
     * 更新下一次等待时间
     * @param waitTime
     */
    void updateNextTime(long waitTime);
}
