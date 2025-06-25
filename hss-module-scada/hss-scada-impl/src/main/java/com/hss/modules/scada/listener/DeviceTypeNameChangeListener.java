package com.hss.modules.scada.listener;

import com.hss.modules.devicetype.entity.DeviceTypeManagement;
import com.hss.modules.devicetype.event.DeviceTypeNameChangeEvent;
import com.hss.modules.scada.entity.ConSheBei;
import com.hss.modules.scada.service.IConSheBeiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
* @description: 设备类型名称变化
* @author zpc
* @date 2024/3/20 9:34
* @version 1.0
*/
@Component
public class DeviceTypeNameChangeListener implements ApplicationListener<DeviceTypeNameChangeEvent> {

    @Autowired
    private IConSheBeiService conSheBeiService;
    @Override
    public void onApplicationEvent(DeviceTypeNameChangeEvent event) {
        DeviceTypeManagement source = (DeviceTypeManagement) event.getSource();

        List<ConSheBei> list = conSheBeiService.listByDeviceTypeId(source.getId());
        if (list.isEmpty()){
            return;
        }
        List<ConSheBei> updateList = list.stream().map(old -> {
            ConSheBei update = new ConSheBei();
            update.setId(old.getId());
            update.setType(source.getName());
            return update;
        }).collect(Collectors.toList());
        conSheBeiService.updateBatchById(updateList);
    }
}
