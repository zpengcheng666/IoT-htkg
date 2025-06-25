package com.hss.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.system.entity.StatisticsSection;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 分布区间统计设置
 * @Author: zpc
 * @Date: 2022-12-05
 * @Version: V1.0
 */
public interface StatisticsSectionMapper extends BaseMapper<StatisticsSection> {

    List<StatisticsSection> queryDevAttrId(@Param("devAttrId") String devAttrId);

    void insertAll(@Param("list") List<StatisticsSection> list);

    /**
     * 根据设备类型id和属性enName查询
     * @param deviceTypeId
     * @param enName
     * @return
     */
    List<StatisticsSection> listByDeviceTypeIdAndEnName(@Param("deviceTypeId") String deviceTypeId, @Param("enName") String enName);
}
