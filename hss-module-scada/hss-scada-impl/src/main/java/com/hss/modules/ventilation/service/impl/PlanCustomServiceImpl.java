package com.hss.modules.ventilation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.core.common.util.LogUtil;
import com.hss.modules.scada.entity.ConSheBei;
import com.hss.modules.ventilation.entity.PlanCustom;
import com.hss.modules.ventilation.entity.PlanCustomDevice;
import com.hss.modules.ventilation.mapper.PlanCustomMapper;
import com.hss.modules.ventilation.service.IPlanCustomDeviceService;
import com.hss.modules.ventilation.service.IPlanCustomService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 自定义方案
 * @Author: zpc
 * @Date:   2023-04-25
 * @Version: V1.0
 */
@Service
public class PlanCustomServiceImpl extends ServiceImpl<PlanCustomMapper, PlanCustom> implements IPlanCustomService {
    @Autowired
    private IPlanCustomDeviceService planCustomDeviceService;


    @Override
    public IPage<PlanCustom> getPage(Page<PlanCustom> page, QueryWrapper<PlanCustom> queryWrapper) {
        Page<PlanCustom> result = page(page, queryWrapper);
        for (PlanCustom record : result.getRecords()) {
            setDevices(record);
        }
        return result;
    }

    private void setDevices(PlanCustom record) {
        List<ConSheBei> deviceList = planCustomDeviceService.listDeviceByPlanId(record.getId());
        List<String> deviceIds = new ArrayList<>(deviceList.size());
        List<String> deviceNames = new ArrayList<>(deviceList.size());
        for (ConSheBei conSheBei : deviceList) {
            deviceIds.add(conSheBei.getId());
            deviceNames.add(conSheBei.getName());
        }
        record.setDeviceIds(deviceIds);
        record.setDeviceNames(deviceNames);
    }

    @Override
    public PlanCustom queryById(String id) {
        PlanCustom byId = getById(id);
        if (byId != null){
            setDevices(byId);
        }
        return byId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        PlanCustom byId = getById(id);
        if (byId == null){
            return;
        }
        removeById(id);
        planCustomDeviceService.delByPlanId(id);
        LogUtil.setOperate(byId.getName());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(PlanCustom planCustom) {
        PlanCustom byId = getById(planCustom.getId());
        if (byId == null){
            return;
        }
        updateById(planCustom);
        planCustomDeviceService.delByPlanId(planCustom.getId());
        if (CollectionUtils.isNotEmpty(planCustom.getDeviceIds())){
            List<PlanCustomDevice> rList = planCustom.getDeviceIds().stream().map(deviceId -> {
                PlanCustomDevice r = new PlanCustomDevice();
                r.setCustomPlanId(planCustom.getId());
                r.setDeviceId(deviceId);
                return r;
            }).collect(Collectors.toList());
            planCustomDeviceService.saveBatch(rList);
        }
        LogUtil.setOperate(planCustom.getName());


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(PlanCustom planCustom) {
        save(planCustom);
        if (CollectionUtils.isNotEmpty(planCustom.getDeviceIds())){
            List<PlanCustomDevice> rList = planCustom.getDeviceIds().stream().map(deviceId -> {
                PlanCustomDevice r = new PlanCustomDevice();
                r.setCustomPlanId(planCustom.getId());
                r.setDeviceId(deviceId);
                return r;
            }).collect(Collectors.toList());
            planCustomDeviceService.saveBatch(rList);
        }
        LogUtil.setOperate(planCustom.getName());


    }
}
