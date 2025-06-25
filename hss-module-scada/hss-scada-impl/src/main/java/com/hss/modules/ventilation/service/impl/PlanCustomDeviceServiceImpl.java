package com.hss.modules.ventilation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.scada.entity.ConSheBei;
import com.hss.modules.ventilation.entity.PlanCustomDevice;
import com.hss.modules.ventilation.mapper.PlanCustomDeviceMapper;
import com.hss.modules.ventilation.service.IPlanCustomDeviceService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 自定义方案设备
 * @Author: zpc
 * @Date:   2023-04-25
 * @Version: V1.0
 */
@Service
public class PlanCustomDeviceServiceImpl extends ServiceImpl<PlanCustomDeviceMapper, PlanCustomDevice> implements IPlanCustomDeviceService {

    @Override
    public List<ConSheBei> listDeviceByPlanId(String planId) {
        return baseMapper.listDeviceByPlanId(planId);
    }

    @Override
    public void delByPlanId(String planId) {
        baseMapper.delByPlanId(planId);
    }
}
