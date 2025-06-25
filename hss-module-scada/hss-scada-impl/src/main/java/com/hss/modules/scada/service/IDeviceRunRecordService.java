package com.hss.modules.scada.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.entity.DeviceRunRecord;

import java.util.List;
import java.util.Map;

/**
 * @Description: 设备运行记录表
 * @Author: zpc
 * @Date:   2023-08-01
 * @Version: V1.0
 */
public interface IDeviceRunRecordService extends IService<DeviceRunRecord> {

    /**
     * 查询最后一条记录
     * @param deviceId
     * @return
     */
    DeviceRunRecord getLastByDeviceId(String deviceId);

    /**
     * 查询最后一次的启动时间和最后一次的停止时间
     */
    List<Map<String, Object>> lastTime(String deviceId);

    /**
     * 根据DeviceId获取总的运行时长
     * @param deviceId
     * @return
     */
    Long sumTime(String deviceId);

    /**
     * 添加记录
     * @param attr 属性实时数据
     */
    void add(ConDeviceAttribute attr);
}
