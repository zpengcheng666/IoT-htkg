package com.hss.modules.ventilation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.core.common.exception.HssBootException;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.model.DeviceAttrData;
import com.hss.modules.scada.service.DeviceDataService;
import com.hss.modules.scada.service.IConDeviceAttributeService;
import com.hss.modules.ventilation.entity.PlanLoop;
import com.hss.modules.ventilation.mapper.PlanLoopMapper;
import com.hss.modules.ventilation.service.IPlanLoopService;
import com.hss.modules.ventilation.service.IPlanPresettingService;
import com.hss.modules.ventilation.vo.PlanListVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @Description: 全自动方案
 * @Author: zpc
 * @Date:   2023-04-25
 * @Version: V1.0
 */
@Service
public class PlanLoopServiceImpl extends ServiceImpl<PlanLoopMapper, PlanLoop> implements IPlanLoopService {

    @Autowired
    private DeviceDataService deviceDataService;
    @Autowired
    private IConDeviceAttributeService conDeviceAttributeService;
    @Autowired
    private IPlanPresettingService planPresettingService;

    @Override
    public List<PlanListVO> listPlan(String deviceId) {
        ConDeviceAttribute workingMode = conDeviceAttributeService.getByDeviceIdAndAttrEnName(deviceId, "workingMode");
        if (workingMode == null || (workingMode.getIsAssociate() == 1 && StringUtils.isEmpty(workingMode.getVariableId()))){
            throw new HssBootException("无法读取设备模式");
        }


        DeviceAttrData data = deviceDataService.getAttrValueByAttrId(workingMode.getId());
        //2023-11-10修改判断
        if (".00".equals(data.getValue()) ||"0".equals(data.getValue()) || "0.00".equals(data.getValue())){
            return  Collections.emptyList();
        }
        if ("1.00".equals(data.getValue()) || "1".equals(data.getValue())){
            return planPresettingService.listByDeviceId(deviceId);
        }
        if ("2.00".equals(data.getValue()) || "2".equals(data.getValue())){
            return listByDeviceId(deviceId);
        }
        return  Collections.emptyList();
    }

    @Override
    public List<PlanListVO> listByDeviceId(String deviceId) {
        return baseMapper.listByDeviceId(deviceId);
    }
}
