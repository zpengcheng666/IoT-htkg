package com.hss.modules.store.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.store.entity.StoreHistory;
import com.hss.modules.store.model.EnvGoodRatioDTO;
import com.hss.modules.store.model.EnvReportResult;
import com.hss.modules.store.model.StoreHistoryLineStatisticsDTO;
import com.hss.modules.store.model.StoreHistoryStatWrapper;
import com.hss.modules.store.model.vo.LineStateVO;
import com.hss.modules.store.model.vo.PieStateVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @Description: 设备运行时历史数据
 * @Author: zpc
 * @Date:   2022-12-01
 * @Version: V1.0
 */
public interface IStoreHistoryService extends IService<StoreHistory> {

    /**
     * 分布统计
     * @param dto
     * @return
     */
    List<PieStateVO>  distributeState(EnvGoodRatioDTO dto);

    /**
     * 根据变量id存储
     * @param attr
     * @param value
     */
    void saveByVariableId(ConDeviceAttribute attr, String value);

    /**
     * 设备运行时历史数据——统计分析
     * @param deviceType
     * @param deviceId
     * @param attrName
     * @param startTime
     * @param endTime
     * @param subSystem
     */
    List<StoreHistoryStatWrapper> stat(String deviceType, String deviceId,
                                              String attrName,Date startTime, Date endTime,
                                              String subSystem);

    /**
     * 历史曲线统计
     * @param dto
     * @return
     */
    LineStateVO historyLineStatistics(StoreHistoryLineStatisticsDTO dto);

    IPage<StoreHistory> selectHistoryPage(IPage<StoreHistory> page,
                                          String subSystem,
                                          String[] deviceIds,
                                          String[] deviceTypes,
                                          String[] attrIds,
                                          Date startTime,
                                          Date endTime);

    /**
     * 环境运行数据报表
     * @param deviceType
     * @param deviceId
     * @param reportType
     * @param date
     * @return
     */
    List<EnvReportResult> reportEnv(String deviceType, String deviceId, String reportType, Date date);
}
