package com.hss.modules.scada.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.scada.entity.ConDianWei;
import com.hss.modules.scada.mapper.ConDianWeiMapper;
import com.hss.modules.scada.model.GateWayVariableBO;
import com.hss.modules.scada.service.IConDianWeiService;
import com.hss.modules.scada.service.IConWangGuanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
* @description: 从网关同步点位
* @author zpc
* @date 2024/3/19 13:35
* @version 1.0
*/
@Service
@Slf4j
public class ConDianWeiServiceImpl extends ServiceImpl<ConDianWeiMapper, ConDianWei> implements IConDianWeiService {
    @Autowired
    private IConWangGuanService wangGuanService;

    @Override
    public void syncData(String gatewayId) {
        List<GateWayVariableBO> gateWayVariableBo = wangGuanService.listPoint(gatewayId);

        if (CollectionUtil.isEmpty(gateWayVariableBo)){
            return;
        }
        List<ConDianWei> list = gateWayVariableBo.stream().flatMap(dev -> dev.getDeviceVar().stream().map(var -> {
            ConDianWei conDianWei = new ConDianWei();
            conDianWei.setWgid(gatewayId);
            conDianWei.setId(var.getId());
            conDianWei.setName(var.getNamev());
            conDianWei.setDataType(var.getDatatype());
            conDianWei.setDeviceId(dev.getDeviceID());
            conDianWei.setDeviceName(dev.getDeviceName());
            conDianWei.setCreatedTime(new Date());
            conDianWei.setUpdatedTime(new Date());
            conDianWei.setNote("");
            return conDianWei;
        })).collect(Collectors.toList());

        // 1.3 删除指定网关的所有设备变量
        LambdaQueryWrapper<ConDianWei> delWrapper = new LambdaQueryWrapper<>();
        delWrapper.eq(ConDianWei::getWgid, gatewayId);
        this.remove(delWrapper);
        this.saveBatch(list);
    }

    @Override
    public String getPointNameByPointId(String variableId) {
        ConDianWei byId = getById(variableId);
        if (byId == null){
            return null;
        }
        return byId.getDeviceName() + ":" + byId.getName();
    }

}
