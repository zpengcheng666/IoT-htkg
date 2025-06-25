package com.hss.modules.facility.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.facility.entity.DeviceAttr;
import com.hss.modules.facility.mapper.DeviceAttrMapper;
import com.hss.modules.facility.service.IDeviceAttrService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 属性字典
 * @Author: zpc
 * @Date:   2022-12-08
 * @Version: V1.0
 */
@Service
public class DeviceAttrServiceImpl extends ServiceImpl<DeviceAttrMapper, DeviceAttr> implements IDeviceAttrService {

    @Override
    public List<DeviceAttr> queryAttrListByClassId(String classId) {
        return this.baseMapper.queryAttrListByClassId(classId);
    }

    @Override
    public List<DeviceAttr> queryAttrsAndValsByDeviceId(String deviceId) {
        return this.baseMapper.queryAttrsAndValsByDeviceId(deviceId);
    }

}
