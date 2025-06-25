package com.hss.modules.scada.listener;

import com.hss.modules.scada.event.ScadaDeviceAttrPointEvent;
import com.hss.modules.scada.model.GatewayDeviceUpdate;
import com.hss.modules.scada.service.IGatewayAttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author zpc
 * @version 1.0
 * @description: 监听绑定关系变更
 * @date 2024/3/20 9:37
 */
@Component
public class ScadaDeviceAttrPointListener implements ApplicationListener<ScadaDeviceAttrPointEvent> {
    @Autowired
    private IGatewayAttachmentService gatewayAttachmentService;

    @Override
    public void onApplicationEvent(ScadaDeviceAttrPointEvent event) {
        GatewayDeviceUpdate source = (GatewayDeviceUpdate) event.getSource();
        gatewayAttachmentService.deviceUpdate(source);
    }
}
