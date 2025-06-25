package com.hss.modules.alarm.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.alarm.entity.AlarmHistory;
import com.hss.modules.alarm.model.AlarmAckDTO;
import com.hss.modules.alarm.model.AlarmBatchAckDTO;
import com.hss.modules.alarm.model.AlarmHistoryHandlerDTO;
import com.hss.modules.alarm.model.AlarmHistoryStatSearchModel;
import com.hss.modules.store.model.EnvGoodRatioDTO;
import com.hss.modules.store.model.vo.LineStateVO;
import com.hss.modules.store.model.vo.PieStateVO;

import java.util.List;

/**
 * @Description: 报警历史数据
 * @Author: jeecg-boot
 * @Date:   2022-12-01
 * @Version: V1.0
 */
public interface IAlarmHistoryService extends IService<AlarmHistory> {

    /**
     * 报警历史数据-统计分析
     * @param model
     */
    LineStateVO stat(AlarmHistoryStatSearchModel model);

    /**
     * 解除报警
     * @param alarmDataId
     */
    void falseAlarm(String alarmDataId);

    /**
     * 取人报警
     * @param dto
     */
    void ack(AlarmAckDTO dto);


    /**
     * 合格率统计
     * @param dto
     * @return
     */
    List<PieStateVO> goodRatio(EnvGoodRatioDTO dto);

    /**
     * 批量确认报警
     * @param dto
     */
    void batchAck(AlarmBatchAckDTO dto);

    /**
     * 分页查询
     * @param page
     * @param queryWrapper
     * @return
     */
    IPage<AlarmHistory> queryPageList(Page<AlarmHistory> page, LambdaQueryWrapper<AlarmHistory> queryWrapper);

    /**
     * 最后一个月报警数量统计
     * @return
     */
    LineStateVO stateCountLastMonth();

    LineStateVO stateCountAlarm(String subSystems);

    /**
     * 批量处理
     * @param dto
     */
    void handler(AlarmHistoryHandlerDTO dto);
}
