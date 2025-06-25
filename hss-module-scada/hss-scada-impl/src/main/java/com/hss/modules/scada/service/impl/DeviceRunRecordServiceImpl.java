package com.hss.modules.scada.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.facility.model.DeviceRunLogEvent;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.entity.DeviceRunRecord;
import com.hss.modules.scada.mapper.DeviceRunRecordMapper;
import com.hss.modules.scada.service.IDeviceRunRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @description: 设备运行记录表，查询最后一条记录、查询最后一次的启动时间和最后一次的停止时间、
 * 根据DeviceId获取总的运行时长、添加记录发布事件
* @author zpc
* @date 2024/3/19 15:20
* @version 1.0
*/
@Service
public class DeviceRunRecordServiceImpl extends ServiceImpl<DeviceRunRecordMapper, DeviceRunRecord> implements IDeviceRunRecordService {

    @Autowired
    private ApplicationEventPublisher publisher;
    @Override
    public DeviceRunRecord getLastByDeviceId(String deviceId) {
        return baseMapper.getLastByDeviceId(deviceId);
    }

    @Override
    public List<Map<String, Object>> lastTime(String deviceId) {
        return this.baseMapper.lastTime(deviceId);
    }

    @Override
    public Long sumTime(String deviceId) {
        return this.baseMapper.sumTime(deviceId);
    }

    @Override
    public void add(ConDeviceAttribute attr) {
        boolean isRun = "1".equals(attr.getInitValue());
        Date recordTime = new Date();
        if (isRun) {
            DeviceRunRecord newRecord = new DeviceRunRecord();
            newRecord.setDeviceId(attr.getDeviceId());
            newRecord.setDeviceState(1);
            newRecord.setRecordTime(recordTime);
            newRecord.setSumTime(0);
            save(newRecord);
            publisher.publishEvent(new DeviceRunLogEvent(attr.getDeviceId(), recordTime, null));

        } else {
            DeviceRunRecord record = getLastByDeviceId(attr.getDeviceId());
            if (record == null || record.getDeviceState() == 0) {
                return;
            }
            DeviceRunRecord newRecord = new DeviceRunRecord();
            newRecord.setDeviceId(attr.getDeviceId());
            newRecord.setDeviceState(0);
            newRecord.setRecordTime(recordTime);
            newRecord.setSumTime(((int) ((System.currentTimeMillis() - record.getRecordTime().getTime()) / 1000L)));
            save(newRecord);
            publisher.publishEvent(new DeviceRunLogEvent(attr.getDeviceId(), record.getRecordTime(), recordTime));
        }
    }

}
