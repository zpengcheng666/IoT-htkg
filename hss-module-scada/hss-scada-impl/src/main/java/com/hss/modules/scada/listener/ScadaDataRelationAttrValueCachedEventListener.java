package com.hss.modules.scada.listener;

import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.event.ScadaDataAttrValueCachedEvent;
import com.hss.modules.scada.service.IConDeviceAttributeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

/**
 * 变量关联处理逻辑
 * 检查变量关联并发布更新事件
 * 接收方为变量关联
 * arlam
 * 联动
 * 发布到wegsocket
 * @author hd
 */
@Service
@Slf4j
public class ScadaDataRelationAttrValueCachedEventListener implements ApplicationListener<ScadaDataAttrValueCachedEvent> {
    @Autowired
    private IConDeviceAttributeService deviceAttributeService;

    @Override
    public void onApplicationEvent(ScadaDataAttrValueCachedEvent event) {
        ConDeviceAttribute attr = (ConDeviceAttribute) event.getSource();
        try {
            deviceAttributeService.updateDataByRelationByAttrId(attr);
        } catch (Exception e) {
            log.error("执行变量关联失败attrId={}，e={}", attr.getId(), e);
        }
    }
}
