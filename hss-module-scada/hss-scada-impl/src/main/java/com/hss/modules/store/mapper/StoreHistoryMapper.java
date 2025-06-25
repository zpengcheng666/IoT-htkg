package com.hss.modules.store.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.modules.store.entity.StoreHistory;
import com.hss.modules.store.model.*;
import com.hss.modules.store.model.bo.MaxAndMinBO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @Description: 设备运行时历史数据
 * @Author: zpc
 * @Date:   2022-12-01
 * @Version: V1.0
 */
public interface StoreHistoryMapper extends BaseMapper<StoreHistory> {


    /**
     * 查询最大值和最小值
     * @param dto
     * @return
     */
    MaxAndMinBO maxAndMin(@Param("dto") DataHistoryStatSearchModel dto);

    /**
     * 数量统计
     * @param dto
     * @param start
     * @param end
     * @return
     */
    int countByStartAndEnd(@Param("dto") EnvGoodRatioDTO dto, @Param("start") float start, @Param("end") float end);


    /**
     * 查询最后一条数据的值
     * @param variableId
     * @return
     */
    String getValueByVariableId(String variableId);

    List<StoreHistoryStatWrapper> stat(@Param("deviceType") String deviceType,
                                       @Param("deviceId") String deviceId,
                                       @Param("attrName") String attrName,
                                       @Param("startTime") Date startTime,
                                       @Param("endTime") Date endTime,
                                       @Param("subSystem") String subSystem);

    /**
     * 统计历史数据
     * @param dto
     * @return
     */
    List<StoreHistory> historyLineStatistics(@Param("dto") StoreHistoryLineStatisticsDTO dto);

    IPage<StoreHistory> selectHistoryPage(IPage<StoreHistory> page,
                                          @Param("subSystem") String subSystem,
                                          @Param("deviceIds") String[] deviceIds,
                                          @Param("deviceTypes") String[] deviceTypes,
                                          @Param("attrIds") String[] attrIds,
                                          @Param("startTime") Date startTime,
                                          @Param("endTime") Date endTime);

    List<EnvReportDTO> envHourReport(@Param("deviceType") String deviceType,
                                      @Param("deviceId") String deviceId,
                                      @Param("startTime") Date startTime,
                                      @Param("endTime") Date endTime);

    List<EnvReportDTO> envDayReport(@Param("deviceType") String deviceType,
                                      @Param("deviceId") String deviceId,
                                      @Param("startTime") Date startTime,
                                      @Param("endTime") Date endTime);

}
