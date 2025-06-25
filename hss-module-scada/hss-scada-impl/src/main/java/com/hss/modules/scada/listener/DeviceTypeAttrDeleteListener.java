package com.hss.modules.scada.listener;

import com.hss.modules.devicetype.entity.DeviceTypeAttribute;
import com.hss.modules.devicetype.event.DeviceTypeAttrDeleteEvent;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.service.IConDeviceAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;


/**
* @description: 设备类型属性删除监听、发布
* @author zpc
* @date 2024/3/20 9:32
* @version 1.0
*/
@Component
public class DeviceTypeAttrDeleteListener implements ApplicationListener<DeviceTypeAttrDeleteEvent> {

    @Autowired
    private IConDeviceAttributeService conDeviceAttributeService;

    @Override
    public void onApplicationEvent(DeviceTypeAttrDeleteEvent event) {
        DeviceTypeAttribute source = (DeviceTypeAttribute) event.getSource();
        List<ConDeviceAttribute> attrList = conDeviceAttributeService.listByTypeAttrId(source.getId());
        if (attrList.isEmpty()){
            return;
        }
        for (ConDeviceAttribute attribute : attrList) {
            conDeviceAttributeService.removeById(attribute.getId());
        }
    }
}
