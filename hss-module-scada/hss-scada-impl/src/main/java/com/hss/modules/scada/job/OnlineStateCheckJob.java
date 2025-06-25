package com.hss.modules.scada.job;

import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.event.ScadaDataMqttReadEvent;
import com.hss.modules.scada.service.IConDeviceAttributeService;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
* @description: 在线状态检查
* @author zpc
* @date 2024/3/20 9:30
* @version 1.0
*/
@Component
public class OnlineStateCheckJob {
    @Autowired
    private IConDeviceAttributeService conDeviceAttributeService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ApplicationEventPublisher publisher;

    private static final long CHECK_TIME = 10 * 60000L;

    @Scheduled(fixedRate = CHECK_TIME, initialDelay = CHECK_TIME)
    public void check() {
        Date nowDate = new Date();
        long checkDate = nowDate.getTime() - CHECK_TIME;
        List<String> ids = conDeviceAttributeService.listIdByEnName("onlineState");
        for (String id : ids) {
            Date date =(Date) redisUtil.get("ONL:" + id);
            if (date == null || date.getTime() < checkDate) {
                ConDeviceAttribute byId = conDeviceAttributeService.getById(id);
                byId.setInitValue("0");
                byId.setUpdatedTime(nowDate);

                Map<String, List<ConDeviceAttribute>> deviceMap = new HashMap<>(4);
                deviceMap.put(byId.getDeviceId(), Collections.singletonList(byId));
                publisher.publishEvent(new ScadaDataMqttReadEvent(deviceMap));
            }
        }
    }
}
