package com.hss.modules.store.listener;

import com.hss.modules.devicetype.entity.DeviceTypeStoreStrategy;
import com.hss.modules.devicetype.event.DeviceTypeStoreStrategyEditEvent;
import com.hss.modules.scada.entity.ConDeviceAttribute;
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
public class StrategyStoreEditListener implements ApplicationListener<DeviceTypeStoreStrategyEditEvent> {

    @Autowired
    private IStoreStrategyService storeStrategyService;
    @Autowired
    private IConSheBeiService conSheBeiService;

    @Override
    public void onApplicationEvent(DeviceTypeStoreStrategyEditEvent event) {
        DeviceTypeStoreStrategy source = (DeviceTypeStoreStrategy) event.getSource();

        List<StoreStrategy> list = storeStrategyService.listByTypeStrategyId(source.getId());
        if (list.isEmpty()){
            return;
        }
        for (StoreStrategy storeStrategy : list) {
            List<ConDeviceAttribute> attributeList = conSheBeiService.listDeviceAttrByDeviceId(storeStrategy.getDeviceId());

            Map<String, String> attrTypeAttrIdMap = attributeList
                    .stream()
                    .collect(Collectors.toMap(ConDeviceAttribute::getAttrId, ConDeviceAttribute::getId));
            StoreStrategy update = conSheBeiService.storeStrategyType2strategy(source, attrTypeAttrIdMap, storeStrategy.getDeviceId());
            update.setId(storeStrategy.getId());
            storeStrategyService.updateById(update);
        }

    }
}

