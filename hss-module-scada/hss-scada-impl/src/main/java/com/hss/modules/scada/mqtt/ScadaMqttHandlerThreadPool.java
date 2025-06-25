package com.hss.modules.scada.mqtt;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;
/**
* @description: 手动创建线程池（暂时没使用，在别处重新定义创建）
* @author zpc
* @date 2024/3/20 10:06
* @version 1.0
*/
//@Configuration
public class ScadaMqttHandlerThreadPool {
    @Bean
    public ThreadPoolTaskExecutor scadaThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程数目
        executor.setCorePoolSize(16);
        //指定最大线程数
        executor.setMaxPoolSize(64);
        //队列中最大的数目
        executor.setQueueCapacity(64);
        //线程名称前缀
        executor.setThreadNamePrefix("scadaThreadPool_");
        //设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //线程空闲后的最大存活时间
        executor.setKeepAliveSeconds(60);
        //加载
        executor.initialize();
        return executor;
    }
}
