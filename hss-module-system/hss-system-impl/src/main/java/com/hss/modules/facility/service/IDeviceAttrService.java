package com.hss.modules.facility.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.facility.entity.DeviceAttr;

import java.util.List;

/**
 * @Description: 属性字典
 * @Author: zpc
 * @Date:   2022-12-08
 * @Version: V1.0
 */
public interface IDeviceAttrService extends IService<DeviceAttr> {

    public List<DeviceAttr> queryAttrListByClassId(String classId);

    List<DeviceAttr> queryAttrsAndValsByDeviceId(String deviceId);

}
