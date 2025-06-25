package com.hss.modules.ventilation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.ventilation.entity.PlanPresetting;
import com.hss.modules.ventilation.vo.PlanListVO;

import java.util.List;

/**
 * @Description: 预设方案
 * @Author: zpc
 * @Date:   2023-04-25
 * @Version: V1.0
 */
public interface PlanPresettingMapper extends BaseMapper<PlanPresetting> {

    /**
     * 根据设备id查询方案列表
     * @param deviceId
     * @return
     */
    List<PlanListVO> listByDeviceId(String deviceId);
}
