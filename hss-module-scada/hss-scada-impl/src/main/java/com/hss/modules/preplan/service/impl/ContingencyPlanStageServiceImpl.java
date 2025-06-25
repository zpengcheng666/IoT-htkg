package com.hss.modules.preplan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.preplan.entity.ContingencyPlanStage;
import com.hss.modules.preplan.entity.ContingencyPlanWorkitem;
import com.hss.modules.preplan.mapper.ContingencyPlanStageMapper;
import com.hss.modules.preplan.service.IContingencyPlanStageService;
import com.hss.modules.preplan.service.IContingencyPlanWorkitemService;
import com.hss.modules.system.model.ContingencyWorkModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Description: 应急预案阶段
 * @Author: zpc
 * @Date:   2023-02-27
 * @Version: V1.0
 */
@Service
public class ContingencyPlanStageServiceImpl extends ServiceImpl<ContingencyPlanStageMapper, ContingencyPlanStage> implements IContingencyPlanStageService {

    @Autowired
    private IContingencyPlanWorkitemService contingencyPlanWorkitemService;

    @Override
    public void saveWork(ContingencyWorkModel model) {
        //1.阶段id
        String stageId = model.getStageId();
        //2.工作项名称
        String name = model.getName();
        //3.工作项内容
        String content = model.getContent();
        ContingencyPlanWorkitem workitem = new ContingencyPlanWorkitem();
        workitem.setContent(content);
        workitem.setStageId(stageId);
        workitem.setName(name);
        workitem.setCreatedTime(new Date());
        workitem.setDeleted(0);
        contingencyPlanWorkitemService.save(workitem);
    }

    @Override
    public List<ContingencyPlanStage> listStageAndWorksByContingencyId(String contingencyId) {
        List<ContingencyPlanStage> list = baseMapper.listByPlantId(contingencyId);
        for (ContingencyPlanStage contingencyPlanStage : list) {
            contingencyPlanStage.setWorkitemList(contingencyPlanWorkitemService.listByStageId(contingencyPlanStage.getId()));
        }

        return list;
    }
}
