package com.hss.modules.facility.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.facility.entity.DeviceType;
import com.hss.modules.facility.model.StatQualityConditionModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 类别管理
 * @Author: zpc
 * @Date:   2022-12-08
 * @Version: V1.0
 */
public interface DeviceTypeMapper extends BaseMapper<DeviceType> {

    List<StatQualityConditionModel> statQualityCondition(@Param("classId") String classId);

    /**
     * 递归查询本级和所有子集
     * @param type
     * @return
     */
    List<String> listIdByType(String type);
}
