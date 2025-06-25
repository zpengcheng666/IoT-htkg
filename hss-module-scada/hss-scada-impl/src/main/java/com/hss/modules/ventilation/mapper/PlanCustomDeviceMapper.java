package com.hss.modules.ventilation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.scada.entity.ConSheBei;
import com.hss.modules.ventilation.entity.PlanCustomDevice;

import java.util.List;

/**
 * @Description: 自定义方案设备
 * @Author: zpc
 * @Date:   2023-04-25
 * @Version: V1.0
 */
public interface PlanCustomDeviceMapper extends BaseMapper<PlanCustomDevice> {

    /**
     * 根据方案id查询设备列表
     * @param planId
     * @return
     */
    List<ConSheBei> listDeviceByPlanId(String planId);


    /**
     * 根据方案id删除
     * @param planId
     */
    void delByPlanId(String planId);
}
