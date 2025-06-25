package com.hss.modules.scada.listener;

import com.hss.modules.scada.service.ISiteManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author zpc
 * @version 1.0
 * @description: 系统启动获取远程站点状态
 * @date 2024/3/20 9:38
 */
@Component
@Slf4j
public class SiteListener implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    private ISiteManagerService siteManagerService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            siteManagerService.sysStartInit();
        } catch (Exception e) {
            log.error("初始化站点状态失败", e);
        }
    }
}
