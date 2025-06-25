package com.hss.core.task.executor;

import com.hss.core.task.boos.BoosThreadRunnable;
import com.hss.core.task.boos.WaitBoosThreadRunnable;
import com.hss.core.task.container.ListTaskContainer;
import com.hss.core.task.container.TaskContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



/**
 * @author hd
 */
@Configuration
public class TaskConfig {

    @Bean
    public TaskExecutor sleepTaskExecutor(){
        BoosThreadRunnable boosThreadRunnable = new WaitBoosThreadRunnable();
        TaskContainer taskContainer = new ListTaskContainer();
        return new BaseTaskExecutor(boosThreadRunnable, taskContainer);
    }
}
