package com.hss.modules.devicetype.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.devicetype.entity.DeviceTypeStoreStrategy;
import com.hss.modules.devicetype.event.DeviceTypeStoreStrategyAddEvent;
import com.hss.modules.devicetype.event.DeviceTypeStoreStrategyDeleteEvent;
import com.hss.modules.devicetype.event.DeviceTypeStoreStrategyEditEvent;
import com.hss.modules.devicetype.mapper.DeviceTypeStoreStrategyMapper;
import com.hss.modules.devicetype.service.IDeviceTypeStoreStrategyService;
import com.hss.modules.scada.model.DeviceTypeStrategyList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 设备类型存储策略
 * @Author: zpc
 * @Date:   2023-01-05
 * @Version: V1.0
 */
@Service
public class DeviceTypeStoreStrategyServiceImpl extends ServiceImpl<DeviceTypeStoreStrategyMapper, DeviceTypeStoreStrategy> implements IDeviceTypeStoreStrategyService {

    @Autowired
    private ApplicationEventPublisher publisher;


    @Override
    public List<DeviceTypeStrategyList> listCommonByTypeId(String typeId) {
        return baseMapper.listCommonByTypeId(typeId);
    }

    @Override
    public void deleteByTypeId(String typeId) {
        baseMapper.deleteByTypeId(typeId);
    }

    @Override
    public List<DeviceTypeStoreStrategy> listByTypeId(String typeId) {
        return baseMapper.listByTypeId(typeId);
    }

    @Override
    public boolean isStrategyByTypeId(String typeId) {
        int c = baseMapper.countStrategyByTypeId(typeId);
        return c > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(DeviceTypeStoreStrategy deviceTypeStoreStrategy) {
        boolean save = save(deviceTypeStoreStrategy);
        publisher.publishEvent(new DeviceTypeStoreStrategyAddEvent(deviceTypeStoreStrategy));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(DeviceTypeStoreStrategy deviceTypeStoreStrategy) {
        boolean b = updateById(deviceTypeStoreStrategy);
        publisher.publishEvent(new DeviceTypeStoreStrategyEditEvent(deviceTypeStoreStrategy));

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        DeviceTypeStoreStrategy byId = getById(id);
        removeById(id);
        if (byId != null){
            publisher.publishEvent(new DeviceTypeStoreStrategyDeleteEvent(byId));
        }



    }
}
