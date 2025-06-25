package com.hss.modules.facility.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.facility.entity.DeviceBI;

import java.util.List;

/**
 * @Description: 设施设备
 * @Author: zpc
 * @Date:   2022-12-08
 * @Version: V1.0
 */
public interface DeviceBIMapper extends BaseMapper<DeviceBI> {

    List<String> listIdsByDeviceId(String deviceId);
}
