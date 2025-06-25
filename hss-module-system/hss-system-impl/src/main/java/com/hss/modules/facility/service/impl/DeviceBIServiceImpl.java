package com.hss.modules.facility.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.facility.entity.DeviceBI;
import com.hss.modules.facility.entity.DeviceBIAttrEntity;
import com.hss.modules.facility.entity.DeviceType;
import com.hss.modules.facility.mapper.DeviceBIMapper;
import com.hss.modules.facility.model.DeviceRunLogBO;
import com.hss.modules.facility.model.DeviceRunLogEvent;
import com.hss.modules.facility.service.IDeviceBIAttrEntityService;
import com.hss.modules.facility.service.IDeviceBIService;
import com.hss.modules.facility.service.IDeviceRunLogService;
import com.hss.modules.facility.service.IDeviceTypeService;
import com.hss.modules.system.entity.BaseLocation;
import com.hss.modules.system.service.IBaseLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 设施设备
 * @Author: zpc
 * @Date:   2022-12-08
 * @Version: V1.0
 */
@Service
public class DeviceBIServiceImpl extends ServiceImpl<DeviceBIMapper, DeviceBI> implements IDeviceBIService {

    @Autowired
    private IDeviceTypeService deviceTypeService;

    @Autowired
    private IBaseLocationService locationService;
    @Autowired
    private IDeviceRunLogService deviceRunLogService;

    @Autowired
    private IDeviceBIAttrEntityService deviceBIAttrEntityService;

    @Override
    public List<DeviceBI> listDetailsByTypeList(List<String> types) {
        LambdaQueryWrapper<DeviceBI> deviceQueryWrapper = new LambdaQueryWrapper<>();
        deviceQueryWrapper.in(DeviceBI::getClassId, types);
        List<DeviceBI> list = this.list(deviceQueryWrapper);
        list.stream().forEach( e -> {
            // 更新设备类型
            DeviceType type = deviceTypeService.getById(e.getClassId());
            e.setClassId_disp(type == null ? "": type.getName());
            // 更新位置信息
            BaseLocation location = locationService.getById(e.getSite());
            e.setSite_disp(location == null? "": location.getName());
        });
        return list;
    }

    @Override
    public void onApplicationEvent(DeviceRunLogEvent event) {
        DeviceRunLogBO source = event.getSource();
        List<String> listIds = baseMapper.listIdsByDeviceId(source.getDeviceId());
        if (listIds.isEmpty()) {
            return;
        }
        for (String id : listIds) {
            deviceRunLogService.add(id, source);
        }
    }

    @Override
    @Transactional
    public DeviceBI addDevice(DeviceBI deviceBI) {
        // 异常处理和事务管理的改进
        try {
            this.save(deviceBI);
            List<DeviceBIAttrEntity> deviceBIAttrEntities = convertDeviceBItoAttrEntities(deviceBI);
            this.deviceBIAttrEntityService.saveBatch(deviceBIAttrEntities);
        } catch (Exception e) {
            log.error("Failed to add device and its attributes", e);
            throw new RuntimeException("Failed to add device and its attributes", e);
        }
        return deviceBI;
    }

    /**
     * 将DeviceBI对象的属性转换为DeviceBIAttrEntity列表
     * @param deviceBI 要转换的DeviceBI对象
     * @return 转换后的DeviceBIAttrEntity列表
     */
    private List<DeviceBIAttrEntity> convertDeviceBItoAttrEntities(DeviceBI deviceBI) {
        if (deviceBI.getDeviceAttrs() == null) {
            return Collections.emptyList(); // 针对null值做处理，避免NullPointerException
        }
        return deviceBI.getDeviceAttrs().stream()
                .map(attr -> {
                    DeviceBIAttrEntity entity = new DeviceBIAttrEntity();
                    entity.setAttrId(attr.getId());
                    entity.setAttrVal(attr.getValue());
                    entity.setDeviceId(deviceBI.getId());
                    return entity;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public boolean editDevice(DeviceBI deviceBI) {
        this.updateById(deviceBI);
        List<DeviceBIAttrEntity> deviceBIAttrEntities = convertDeviceBItoAttrEntities(deviceBI);
        LambdaQueryWrapper<DeviceBIAttrEntity> deleteWrapper = new LambdaQueryWrapper<DeviceBIAttrEntity>()
                .eq(DeviceBIAttrEntity::getDeviceId, deviceBI.getId());
        this.deviceBIAttrEntityService.remove(deleteWrapper);
        this.deviceBIAttrEntityService.saveBatch(deviceBIAttrEntities);
        return true;
    }

    @Transactional
    @Override
    public boolean deleteDevice(String id) {
        this.removeById(id);
        LambdaQueryWrapper<DeviceBIAttrEntity> deleteWrapper = new LambdaQueryWrapper<DeviceBIAttrEntity>()
                .eq(DeviceBIAttrEntity::getDeviceId, id);
        this.deviceBIAttrEntityService.remove(deleteWrapper);
        return true;
    }
}
