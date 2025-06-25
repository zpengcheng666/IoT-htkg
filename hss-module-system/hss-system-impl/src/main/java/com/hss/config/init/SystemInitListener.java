package com.hss.config.init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * @desc: 启动程序，初始化路由配置
 * @author: flyme
 */
@Slf4j
@Component
public class SystemInitListener implements ApplicationListener<ApplicationReadyEvent>, Ordered {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {

        log.info(" 服务已启动，初始化路由配置 ###################");
        String context = "AnnotationConfigServletWebServerApplicationContext";
        if (applicationReadyEvent.getApplicationContext().getDisplayName().indexOf(context) > -1) {
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
