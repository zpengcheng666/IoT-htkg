package com.hss.modules.preplan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.preplan.entity.ContingencyPlanStage;

import java.util.List;

/**
 * @Description: 应急预案阶段
 * @Author: zpc
 * @Date:   2023-02-27
 * @Version: V1.0
 */
public interface ContingencyPlanStageMapper extends BaseMapper<ContingencyPlanStage> {

    /**
     * 查询阶段表
     * @param plantId
     * @return
     */
    List<ContingencyPlanStage> listByPlantId(String plantId);
}
