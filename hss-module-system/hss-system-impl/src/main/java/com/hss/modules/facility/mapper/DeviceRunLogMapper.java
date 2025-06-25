package com.hss.modules.facility.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.modules.facility.entity.DeviceRunLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description:
 * @Author: zpc
 * @Date:   2022-12-08
 * @Version: V1.0
 */
public interface DeviceRunLogMapper extends BaseMapper<DeviceRunLog> {

    DeviceRunLog getLastByDeviceId(String deviceId);

    IPage<DeviceRunLog> page(Page<DeviceRunLog> deviceRunLogPage,
                             @Param("deviceName") String deviceName,
                             @Param("typeIds") List<String> typeIds,
                             @Param("devId") String devId

    );
}
