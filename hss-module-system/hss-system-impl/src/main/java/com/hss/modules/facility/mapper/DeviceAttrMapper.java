package com.hss.modules.facility.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.facility.entity.DeviceAttr;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 属性字典
 * @Author: zpc
 * @Date:   2022-12-08
 * @Version: V1.0
 */
public interface DeviceAttrMapper extends BaseMapper<DeviceAttr> {
    List<DeviceAttr> queryAttrListByClassId(@Param("classId") String classId);


    List<DeviceAttr> queryAttrsAndValsByDeviceId(@Param("deviceId") String deviceId);
}
