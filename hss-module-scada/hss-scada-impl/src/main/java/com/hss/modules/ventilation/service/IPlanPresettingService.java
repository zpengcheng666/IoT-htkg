package com.hss.modules.ventilation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.ventilation.entity.PlanPresetting;
import com.hss.modules.ventilation.vo.PlanListVO;

import java.util.List;

/**
 * @Description: 预设方案
 * @Author: zpc
 * @Date:   2023-04-25
 * @Version: V1.0
 */
public interface IPlanPresettingService extends IService<PlanPresetting> {

    /**
     * 根据设备id查询方案
     * @param deviceId
     * @return
     */
    List<PlanListVO> listByDeviceId(String deviceId);
}
