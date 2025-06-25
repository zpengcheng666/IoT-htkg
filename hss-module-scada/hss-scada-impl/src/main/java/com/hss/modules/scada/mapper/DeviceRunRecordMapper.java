package com.hss.modules.scada.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.scada.entity.DeviceRunRecord;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

/**
* @description: 设备运行记录表、根据设备id查询设备运行记录、查询最后一次的启动时间和最后一次的停止时间、根据DeviceId获取总的运行时长
* @author zpc
* @date 2024/3/19 16:08
* @version 1.0
*/
public interface DeviceRunRecordMapper extends BaseMapper<DeviceRunRecord> {

    /**
     * 根据设备id查询设备运行记录
     * @param deviceId
     * @return
     */
    DeviceRunRecord getLastByDeviceId(String deviceId);

    /**
     * 查询最后一次的启动时间和最后一次的停止时间
     * @param deviceId
     * @return
     */
    @MapKey("DEVICE_STATE")
    List<Map<String, Object>> lastTime(String deviceId);

    /**
     * 根据DeviceId获取总的运行时长
     * @param deviceId
     * @return
     */
    Long sumTime(String deviceId);
}
