package com.hss.modules.preplan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.preplan.entity.ContingencyPlanWorkitem;

import java.util.List;

/**
 * @Description: 应急预案工作
 * @Author: zpc
 * @Date:   2023-02-27
 * @Version: V1.0
 */
public interface IContingencyPlanWorkitemService extends IService<ContingencyPlanWorkitem> {

    /**
     * 根据阶段id查询
     * @param id
     * @return
     */
    List<ContingencyPlanWorkitem> listByStageId(String id);
}
