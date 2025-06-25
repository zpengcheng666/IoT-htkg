package com.hss.modules.preplan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.preplan.entity.ContingencyPlan;
import com.hss.modules.system.model.ContingencyModel;

/**
 * @Description: 预案主表
 * @Author: zpc
 * @Date:   2023-02-07
 * @Version: V1.0
 */
public interface IContingencyPlanService extends IService<ContingencyPlan> {
    /**
     * 新增预案
     * @param contingencyModel
     * @return
     */
    void saveStage(ContingencyModel contingencyModel);
}
