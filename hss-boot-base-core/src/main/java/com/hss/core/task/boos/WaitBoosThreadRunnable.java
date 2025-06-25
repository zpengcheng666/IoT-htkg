package com.hss.core.task.boos;

import lombok.extern.slf4j.Slf4j;

import java.util.function.LongFunction;

/**
 * wait调度线程
 * @author hd
 */
@Slf4j
public class WaitBoosThreadRunnable implements BoosThreadRunnable {

    /**
     * 下一次运行时间
     */
    long nextTime = Long.MAX_VALUE;

    /**
     * 执行任务方法
     */
    LongFunction<Long> consumer;

    public WaitBoosThreadRunnable() {
    }

    @Override
    public void setConsumer(LongFunction<Long> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void run() {
        while (true){
            synchronized (this){
                long timeout = nextTime - System.currentTimeMillis();
                if (timeout > 0){
                    try {
                        wait(timeout);
                    } catch (InterruptedException e) {
                        log.error("打断", e);
                    }
                }

                if (System.currentTimeMillis() >= nextTime){
                    this.nextTime = consumer.apply(System.currentTimeMillis());
                }

            }
        }
    }

    @Override
    public synchronized void updateNextTime(long nextTime) {
        this.nextTime = nextTime;
        notify();

    }
}
