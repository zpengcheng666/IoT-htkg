package com.hss.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.system.entity.StatisticsSection;
import com.hss.modules.system.model.StatisticsSectionModel;

import java.util.List;

/**
 * @Description: 分布区间统计设置
 * @Author: zpc
 * @Date:   2022-12-05
 * @Version: V1.0
 */
public interface IStatisticsSectionService extends IService<StatisticsSection> {

    List<StatisticsSection> queryStatistics(String devAttrId);

    void addAll(StatisticsSectionModel statisticsSection);

    void deleteAndInsert(StatisticsSectionModel model);

    /**
     * 根据设备类型和属性id查询
     * @param deviceTypeId
     * @param attrId
     * @return
     */
    List<StatisticsSection> listByDeviceTypeIdAndEnName(String deviceTypeId, String attrId);
}
