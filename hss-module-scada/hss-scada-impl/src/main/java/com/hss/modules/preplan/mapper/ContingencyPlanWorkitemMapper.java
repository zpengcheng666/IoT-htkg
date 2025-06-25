package com.hss.modules.preplan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.preplan.entity.ContingencyPlanWorkitem;

import java.util.List;

/**
 * @Description: 应急预案工作
 * @Author: zpc
 * @Date:   2023-02-27
 * @Version: V1.0
 */
public interface ContingencyPlanWorkitemMapper extends BaseMapper<ContingencyPlanWorkitem> {

    /**
     * 根据stageId查询
     * @param stageId
     * @return
     */
    List<ContingencyPlanWorkitem> listByStageId(String stageId);
}
