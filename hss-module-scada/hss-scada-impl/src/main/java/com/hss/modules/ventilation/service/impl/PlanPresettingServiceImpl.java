package com.hss.modules.ventilation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.ventilation.entity.PlanPresetting;
import com.hss.modules.ventilation.mapper.PlanPresettingMapper;
import com.hss.modules.ventilation.service.IPlanPresettingService;
import com.hss.modules.ventilation.vo.PlanListVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 预设方案
 * @Author: zpc
 * @Date:   2023-04-25
 * @Version: V1.0
 */
@Service
public class PlanPresettingServiceImpl extends ServiceImpl<PlanPresettingMapper, PlanPresetting> implements IPlanPresettingService {

    @Override
    public List<PlanListVO> listByDeviceId(String deviceId) {
        return baseMapper.listByDeviceId(deviceId);
    }
}
