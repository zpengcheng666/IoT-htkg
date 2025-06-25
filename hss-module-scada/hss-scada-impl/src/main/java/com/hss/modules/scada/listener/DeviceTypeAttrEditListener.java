package com.hss.modules.scada.listener;

import com.hss.modules.devicetype.entity.DeviceTypeAttribute;
import com.hss.modules.devicetype.event.DeviceTypeAttrEditEvent;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.service.IConDeviceAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
* @description: 实行编辑事件
* @author zpc
* @date 2024/3/20 9:33
* @version 1.0
*/
@Component
public class DeviceTypeAttrEditListener implements ApplicationListener<DeviceTypeAttrEditEvent> {

    @Autowired
    private IConDeviceAttributeService conDeviceAttributeService;

    @Override
    public void onApplicationEvent(DeviceTypeAttrEditEvent event) {
        DeviceTypeAttribute source = (DeviceTypeAttribute) event.getSource();
        List<ConDeviceAttribute> attrList = conDeviceAttributeService.listByTypeAttrId(source.getId());
        for (ConDeviceAttribute attribute : attrList) {
            conDeviceAttributeService.upDateByType(source, attribute);
        }
    }
}

