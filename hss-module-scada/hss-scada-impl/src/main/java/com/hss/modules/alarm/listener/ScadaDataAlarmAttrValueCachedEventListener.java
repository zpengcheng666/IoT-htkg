package com.hss.modules.alarm.listener;

import com.hss.modules.alarm.service.IAlarmStrategyService;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.event.ScadaDataAttrValueCachedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

/**
 * 缓存更新完成报警
 * @author hd
 */
@Service
@Slf4j
public class ScadaDataAlarmAttrValueCachedEventListener implements ApplicationListener<ScadaDataAttrValueCachedEvent> {
    @Autowired
    private IAlarmStrategyService alarmStrategyService;
    @Override
    public void onApplicationEvent(ScadaDataAttrValueCachedEvent event) {
        ConDeviceAttribute attr = (ConDeviceAttribute) event.getSource();
        try {
            alarmStrategyService.checkAndRunStrategy(attr.getId());
        } catch (Exception e) {
            log.error("执行报警策略失败attrId={}，e={}", attr.getId(), e);
        }
    }
}
