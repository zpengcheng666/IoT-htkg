package com.hss.modules.ventilation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.scada.entity.ConSheBei;
import com.hss.modules.ventilation.entity.PlanCustomDevice;

import java.util.List;

/**
 * @Description: 自定义方案设备
 * @Author: zpc
 * @Date:   2023-04-25
 * @Version: V1.0
 */
public interface IPlanCustomDeviceService extends IService<PlanCustomDevice> {

    /**
     * 根据方案id查询设备列表
     * @param planId
     * @return
     */
    List<ConSheBei> listDeviceByPlanId(String planId);


    /**
     * 根据方案id删除
     * @param id
     */
    void delByPlanId(String id);
}
