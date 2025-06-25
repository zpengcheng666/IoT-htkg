package com.hss.modules.alarm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.alarm.entity.AlarmHistory;
import com.hss.modules.alarm.model.AlarmHistoryHandlerDTO;
import com.hss.modules.alarm.model.AlarmHistoryStatSearchModel;
import com.hss.modules.alarm.model.AlarmStatisticsBO;
import com.hss.modules.alarm.model.StateCountLastMonthBO;
import com.hss.modules.store.model.EnvGoodRatioDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @Description: 报警历史数据
 * @Author: jeecg-boot
 * @Date: 2022-12-01
 * @Version: V1.0
 */
public interface AlarmHistoryMapper extends BaseMapper<AlarmHistory> {

    public List<AlarmHistory> statisticsAlarmHistories(
            @Param("statisticsMethod") String statisticsMethod,
            @Param("statisticsResult") String statisticsResult,
            @Param("statisticsWay") String statisticsWay,
            @Param("subsystem") String subsystem,
            @Param("deviceType") List<String> deviceType,
            @Param("deviceId") List<String> deviceId,
            @Param("attrEnName") List<String> attrEnName,
            @Param("startTime") Date startTime,
            @Param("endTime") Date endTime);

    /**
     * 根据alarmDataId查询
     * @param alarmDataId
     * @return
     */
    AlarmHistory getByAlarmDataId(String alarmDataId);


    /**
     * 报警统计
     * @param model
     * @return
     */
    List<AlarmStatisticsBO> statistics(@Param("model") AlarmHistoryStatSearchModel model);

    /**
     * 获取报警时常
     * @param dto
     * @return
     */
    Long getBadSumTime(@Param("dto") EnvGoodRatioDTO dto);

    /**
     * 最后一个月报警数量统计
     * @param upMonthDate
     * @return
     */
    List<StateCountLastMonthBO> stateCountLastMonth(Date upMonthDate);

    List<StateCountLastMonthBO> stateCountAlarm(@Param("upMonthDate")Date upMonthDate,@Param("subSystems")String subSystems);

    void handler(@Param("dto") AlarmHistoryHandlerDTO dto, @Param("date") Date date);

    List<AlarmHistory> listByAlarmDataIds(@Param("alarmDataIds") List<String> ids);
}
