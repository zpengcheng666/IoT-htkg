package com.hss.modules.preplan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.preplan.entity.ContingencyPlanStage;
import com.hss.modules.system.model.ContingencyWorkModel;

import java.util.List;

/**
 * @Description: 应急预案阶段
 * @Author: zpc
 * @Date:   2023-02-27
 * @Version: V1.0
 */
public interface IContingencyPlanStageService extends IService<ContingencyPlanStage> {

    /**
     * 应急预案工作-添加
     * @param model
     * @return
     */
    void saveWork(ContingencyWorkModel model);

    /**
     * 阶段和工作
     * @param contingencyId 预案id
     * @return 阶段和工作
     */
    List<ContingencyPlanStage> listStageAndWorksByContingencyId(String contingencyId);
}
