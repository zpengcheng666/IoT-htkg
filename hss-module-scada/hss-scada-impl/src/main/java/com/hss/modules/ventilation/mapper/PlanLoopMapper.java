package com.hss.modules.ventilation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.ventilation.entity.PlanLoop;
import com.hss.modules.ventilation.vo.PlanListVO;

import java.util.List;

/**
 * @Description: 全自动方案
 * @Author: zpc
 * @Date:   2023-04-25
 * @Version: V1.0
 */
public interface PlanLoopMapper extends BaseMapper<PlanLoop> {


    /**
     * 查询方案
     * @param deviceId
     * @return
     */
    List<PlanListVO> listByDeviceId(String deviceId);
}
