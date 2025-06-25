package com.hss.modules.ventilation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.ventilation.entity.PlanLoop;
import com.hss.modules.ventilation.vo.PlanListVO;

import java.util.List;

/**
 * @Description: 全自动方案
 * @Author: zpc
 * @Date:   2023-04-25
 * @Version: V1.0
 */
public interface IPlanLoopService extends IService<PlanLoop> {

    /**
     * 根据设备id查询方案
     * @param deviceId
     * @return
     */
    List<PlanListVO> listByDeviceId(String deviceId);

    /**
     * 查询方案
     * @param deviceId
     * @return
     */
    List<PlanListVO> listPlan(String deviceId);
}
