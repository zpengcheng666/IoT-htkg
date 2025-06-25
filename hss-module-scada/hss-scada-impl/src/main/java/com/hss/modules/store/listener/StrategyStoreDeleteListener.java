package com.hss.modules.store.listener;

import com.hss.modules.devicetype.entity.DeviceTypeStoreStrategy;
import com.hss.modules.devicetype.event.DeviceTypeStoreStrategyDeleteEvent;
import com.hss.modules.store.entity.StoreStrategy;
import com.hss.modules.store.service.IStoreStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @author hd
 */
@Component
public class StrategyStoreDeleteListener implements ApplicationListener<DeviceTypeStoreStrategyDeleteEvent> {

    @Autowired
    private IStoreStrategyService storeStrategyService;


    @Override
    public void onApplicationEvent(DeviceTypeStoreStrategyDeleteEvent event) {
        DeviceTypeStoreStrategy source = (DeviceTypeStoreStrategy) event.getSource();
        List<StoreStrategy> list = storeStrategyService.listByTypeStrategyId(source.getId());
        if (list.isEmpty()){
            return;
        }
        for (StoreStrategy storeStrategy : list) {
            storeStrategyService.removeById(storeStrategy.getId());
        }

    }

}
