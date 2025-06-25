package com.hss.modules.linkage.listener;

import com.hss.modules.linkage.service.ILinkageStrategyService;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.event.ScadaDataAttrValueCachedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

/**
 * 缓存更新完成联动
 * @author hd
 */
@Service
@Slf4j
public class ScadaDataLinkageAttrValueCachedEventListener implements ApplicationListener<ScadaDataAttrValueCachedEvent> {
    @Autowired
    private ILinkageStrategyService linkageStrategy;
    @Override
    public void onApplicationEvent(ScadaDataAttrValueCachedEvent event) {
        ConDeviceAttribute attr = (ConDeviceAttribute) event.getSource();
        try {
            linkageStrategy.checkAndRunStrategy(attr);
        } catch (Exception e) {
            log.error("执行联动失败，attrId={}， e={}", attr.getId(), e);
        }
    }
}
