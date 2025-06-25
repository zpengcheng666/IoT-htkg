package com.hss.modules.preplan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.preplan.entity.ContingencyPlan;
import com.hss.modules.preplan.entity.ContingencyPlanStage;
import com.hss.modules.preplan.mapper.ContingencyPlanMapper;
import com.hss.modules.preplan.service.IContingencyPlanService;
import com.hss.modules.preplan.service.IContingencyPlanStageService;
import com.hss.modules.system.model.ContingencyModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Description: 预案主表
 * @Author: zpc
 * @Date:   2023-02-07
 * @Version: V1.0
 */
@Service
public class ContingencyPlanServiceImpl extends ServiceImpl<ContingencyPlanMapper, ContingencyPlan> implements IContingencyPlanService {

    @Autowired
    private IContingencyPlanStageService contingencyPlanStageService;

    @Override
    public void saveStage(ContingencyModel contingencyModel) {
        //1.预案id
        String planId = contingencyModel.getPlanId();
        //2.阶段名称
        String name = contingencyModel.getName();
        ContingencyPlanStage stage = new ContingencyPlanStage();
        stage.setPlanId(planId);
        stage.setName(name);
        stage.setCreatedTime(new Date());
        stage.setDeleted(0);
        contingencyPlanStageService.save(stage);
    }
}
