package com.hss.modules.linkage.listener;

import com.hss.modules.linkage.service.ILinkageStrategyService;
import com.hss.modules.message.event.SatelliteArrivedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 监听卫星临空事件
 * @author hd
 */
@Component
@Slf4j
public class PublishSatelliteListener implements ApplicationListener<SatelliteArrivedEvent> {

    @Autowired
    private ILinkageStrategyService linkageStrategyService;

    @Override
    public void onApplicationEvent(SatelliteArrivedEvent event) {
        try {
            linkageStrategyService.actPublishSatellite();
        } catch (Exception e) {
            log.error("处理卫星临空事件失败", e);
        }


    }
}
