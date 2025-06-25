package com.hss.modules.devicetype.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.devicetype.entity.DeviceTypeManagementState;
import com.hss.modules.devicetype.mapper.DeviceTypeManagementStateMapper;
import com.hss.modules.devicetype.service.IDeviceTypeManagementStateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 设备类型管理状态管理
 * @Author: zpc
 * @Date:   2023-01-05
 * @Version: V1.0
 */
@Service
public class DeviceTypeManagementStateServiceImpl extends ServiceImpl<DeviceTypeManagementStateMapper, DeviceTypeManagementState> implements IDeviceTypeManagementStateService {

    @Override
    public List<DeviceTypeManagementState> listByTypeId(String typeId) {
        return baseMapper.listByTypeId(typeId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDefault(String id) {
        DeviceTypeManagementState byId = getById(id);
        if (byId == null){
            return;
        }
        baseMapper.setNotDefaultByTypeId(byId.getTypeId());
        DeviceTypeManagementState state = new DeviceTypeManagementState();
        state.setId(id);
        state.setDefaultState(1);
        updateById(state);
    }

    @Override
    public void deleteByTypeId(String typeId) {
        baseMapper.deleteByTypeId(typeId);
    }
}
