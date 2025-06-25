package com.hss.modules.store.listener;

import com.hss.modules.devicetype.entity.DeviceTypeStoreStrategy;
import com.hss.modules.devicetype.event.DeviceTypeStoreStrategyAddEvent;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.entity.ConSheBei;
import com.hss.modules.scada.service.IConSheBeiService;
import com.hss.modules.store.entity.StoreStrategy;
import com.hss.modules.store.service.IStoreStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author hd
 */
@Component
public class StrategyStoreAddListener implements ApplicationListener<DeviceTypeStoreStrategyAddEvent> {

    @Autowired
    private IStoreStrategyService storeStrategyService;
    @Autowired
    private IConSheBeiService conSheBeiService;
    @Override
    public void onApplicationEvent(DeviceTypeStoreStrategyAddEvent event) {
        DeviceTypeStoreStrategy source = (DeviceTypeStoreStrategy) event.getSource();
        // 1.查询设备
        List<ConSheBei> deviceList = conSheBeiService.listByDeviceTypeId(source.getTypeId());
        if (deviceList.isEmpty()){
            return;
        }
        List<StoreStrategy> list = deviceList.stream().map(conSheBei -> {
            List<ConDeviceAttribute> attributeList = conSheBeiService.listDeviceAttrByDeviceId(conSheBei.getId());

            Map<String, String> attrTypeAttrIdMap = attributeList
                    .stream()
                    .collect(Collectors.toMap(ConDeviceAttribute::getAttrId, ConDeviceAttribute::getId));

            return conSheBeiService.storeStrategyType2strategy(source, attrTypeAttrIdMap, conSheBei.getId());
        }).collect(Collectors.toList());
        storeStrategyService.saveBatch(list);
    }


}
