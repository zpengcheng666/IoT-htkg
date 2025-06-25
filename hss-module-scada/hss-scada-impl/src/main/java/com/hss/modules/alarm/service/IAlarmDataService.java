package com.hss.modules.alarm.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.alarm.entity.AlarmData;
import com.hss.modules.alarm.model.AlarmAckDTO;
import com.hss.modules.alarm.model.AlarmBatchAckDTO;

import java.util.List;

/**
* @description: 报警数据、报警确认、批量确认，根据报警级别获取报警数据
* @author zpc
* @date 2024/3/20 11:37
* @version 1.0
*/
public interface IAlarmDataService extends IService<AlarmData> {

    /**
     * 确认报警
     * @param dto
     */
    void ack(AlarmAckDTO dto);

    /**
     * 批量确认报警
     * @param dto
     */
    void batchAck(AlarmBatchAckDTO dto);

    /**
     * 根据报警级别获取报警数据
     * @param alarmLevel
     * @return
     */
    List<AlarmData> getAlarmData(String alarmLevel);

    /**
     * 报警数据分页列表
     * @param page
     * @param queryWrapper
     * @return
     */
    IPage<AlarmData> queryPageList(Page<AlarmData> page, QueryWrapper<AlarmData> queryWrapper);

    /**
     * 查询报警数量
     */
    int getCount();
}
