package com.hss.modules.devicetype.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.core.common.exception.HssBootException;
import com.hss.modules.devicetype.entity.DeviceTypeAlarmStrategy;
import com.hss.modules.devicetype.entity.DeviceTypeAttribute;
import com.hss.modules.devicetype.event.DeviceTypeAlarmStrategyAddEvent;
import com.hss.modules.devicetype.event.DeviceTypeAlarmStrategyDeleteEvent;
import com.hss.modules.devicetype.event.DeviceTypeAlarmStrategyEditEvent;
import com.hss.modules.devicetype.mapper.DeviceTypeAlarmStrategyMapper;
import com.hss.modules.devicetype.service.IDeviceTypeAlarmStrategyService;
import com.hss.modules.devicetype.service.IDeviceTypeAttributeService;
import com.hss.modules.scada.model.DeviceTypeStrategyList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 设备类型报警策略
 * @Author: zpc
 * @Date:   2023-01-05
 * @Version: V1.0
 */
@Service
public class DeviceTypeAlarmStrategyServiceImpl extends ServiceImpl<DeviceTypeAlarmStrategyMapper, DeviceTypeAlarmStrategy> implements IDeviceTypeAlarmStrategyService {
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private IDeviceTypeAttributeService deviceTypeAttributeService;


    @Override
    public List<DeviceTypeStrategyList> listCommonByTypeId(String typeId) {
        return baseMapper.listCommonByTypeId(typeId);
    }

    @Override
    public void deleteByTypeId(String typeId) {
        baseMapper.deleteByTypeId(typeId);
    }

    @Override
    public List<DeviceTypeAlarmStrategy> listByTypeId(String typeId) {
        return baseMapper.listByTypeId(typeId);
    }

    @Override
    public boolean isStrategyByTypeId(String typeId) {
        int c = baseMapper.countStrategyByTypeId(typeId);
        return c > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(DeviceTypeAlarmStrategy deviceTypeAlarmStrategy) {
        checkStatusVar(deviceTypeAlarmStrategy);

        save(deviceTypeAlarmStrategy);
        publisher.publishEvent(new DeviceTypeAlarmStrategyAddEvent(deviceTypeAlarmStrategy));
    }

    private void checkStatusVar(DeviceTypeAlarmStrategy deviceTypeAlarmStrategy) {

        String statusVarId = deviceTypeAlarmStrategy.getStatusVarId();
        DeviceTypeAttribute statusAttr = deviceTypeAttributeService.getById(statusVarId);
        if (statusAttr == null){
            throw new HssBootException("状态变量无效");
        }
        if (statusAttr.getIsAssociate() == 1){
            throw new HssBootException("状态变量不能为关联变量");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(DeviceTypeAlarmStrategy deviceTypeAlarmStrategy) {
        checkStatusVar(deviceTypeAlarmStrategy);
        updateById(deviceTypeAlarmStrategy);
        publisher.publishEvent(new DeviceTypeAlarmStrategyEditEvent(deviceTypeAlarmStrategy));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        DeviceTypeAlarmStrategy byId = getById(id);
        removeById(id);
        if (byId != null){
            publisher.publishEvent(new DeviceTypeAlarmStrategyDeleteEvent(byId));
        }
    }
}
