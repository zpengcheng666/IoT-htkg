package com.hss.modules.scada.listener;

import com.hss.modules.devicetype.entity.DeviceTypeAttribute;
import com.hss.modules.devicetype.event.DeviceTypeAttrAddEvent;
import com.hss.modules.scada.entity.ConSheBei;
import com.hss.modules.scada.service.IConDeviceAttributeService;
import com.hss.modules.scada.service.IConSheBeiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
* @description: 设备类型管理添加属性监听发布
* @author zpc
* @date 2024/3/20 9:30
* @version 1.0
*/
@Component
public class DeviceTypeAttrAddListener implements ApplicationListener<DeviceTypeAttrAddEvent> {

    @Autowired
    private IConDeviceAttributeService conDeviceAttributeService;
    @Autowired
    private IConSheBeiService conSheBeiService;
    @Override
    public void onApplicationEvent(DeviceTypeAttrAddEvent event) {
        DeviceTypeAttribute source = (DeviceTypeAttribute) event.getSource();

        List<ConSheBei> list = conSheBeiService.listByDeviceTypeId(source.getTypeId());
        if (list.isEmpty()){
            return;
        }
        for (ConSheBei conSheBei : list) {
            conDeviceAttributeService.addByDeviceAndType(source, conSheBei);
        }
    }
}
