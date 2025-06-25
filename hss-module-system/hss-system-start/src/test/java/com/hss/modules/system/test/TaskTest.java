package com.hss.modules.system.test;

import com.hss.HssSystemApplication;
import com.hss.core.task.executor.TaskExecutor;
import com.hss.core.task.impl.CronTask;
import com.hss.core.task.impl.CycleDelayTask;
import com.hss.core.task.impl.SimplenessDelayTask;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = HssSystemApplication.class)
public class TaskTest {

    @Autowired
    private TaskExecutor sleepTaskExecutor;

//    @Test
    public void test1() throws InterruptedException {
//        CronTask cronTask = new CronTask("10 * * * * ? *", sleepTaskExecutor);
//        cronTask.setRunnable(()->{
//            log.info("cron0");
//        });
//        cronTask.start();
//        CronTask cronTask1 = new CronTask("0 * * * * ? *", sleepTaskExecutor);
//        cronTask1.setRunnable(()->{
//            log.info("cron1");
//        });
//        cronTask1.start();
        for (int i = 59; i >0 ; i--) {
            CronTask cronTask = new CronTask(i+" * * * * ? *", sleepTaskExecutor);
            Integer i1 = new Integer(i);
            cronTask.setRunnable(()->{
                Date runTime = cronTask.getRunTime();
                log.info("cron"+runTime+":"+i1);
            });
            cronTask.start();
        }

        CycleDelayTask cycleDelayTask = new CycleDelayTask(10*1000, sleepTaskExecutor);
        cycleDelayTask.setRunnable(()->{
            log.info("Cycle");
        });
        cycleDelayTask.start();
        for (int i = 0; i < 60; i++) {
            SimplenessDelayTask simplenessDelayTask = new SimplenessDelayTask(i * 1000, sleepTaskExecutor);
            Integer integer = new Integer(i);
            simplenessDelayTask.setRunnable(()->{
                log.info("simpleness:" + integer);
            });
            simplenessDelayTask.start();
        }
        System.out.println(1);
        Thread.sleep(Long.MAX_VALUE);


    }
}
