package com.hss.modules.scada.listener;

import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.event.ScadaDataAttrValueUpdateEvent;
import com.hss.modules.scada.service.DeviceDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

/**
 * @author zpc
 * @version 1.0
 * @description: 更新缓存、并发布消息
 * @date 2024/3/20 9:35
 */
@Service
@Slf4j
public class ScadaDataCacheAttrValueUpdateEventListener implements ApplicationListener<ScadaDataAttrValueUpdateEvent> {
    @Autowired
    private DeviceDataService deviceDataService;

    @Override
    public void onApplicationEvent(ScadaDataAttrValueUpdateEvent event) {
        // 1.缓存数据
        try {
            ConDeviceAttribute attr = (ConDeviceAttribute) event.getSource();
            deviceDataService.updateAttrValueByAttr(attr);
        } catch (Exception e) {
            log.error("执行缓存失败", e);
        }
    }
}
