package com.hss.modules.message.job;

import com.hss.modules.message.service.IDoWorkService;
import com.hss.modules.message.service.IPublishSatelliteSecureService;
import com.hss.modules.message.service.IPublishSatelliteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
* @description: 卫星离开
* @author zpc
* @date 2024/3/21 9:20
* @version 1.0
*/
@Component
public class DoWorkJob {

    @Autowired
    private IDoWorkService doWorkService;
    @Autowired
    private IPublishSatelliteService publishSatelliteService;
    @Autowired
    private IPublishSatelliteSecureService publishSatelliteSecureService;

    @Scheduled(cron = "0/2 * * * * ? ")
    public void check() {
        doWorkService.checkTimeOut();
        publishSatelliteService.checkState();
//        publishSatelliteSecureService.checkState();
    }
}
