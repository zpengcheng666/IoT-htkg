package com.hss.modules.preplan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.preplan.entity.ContingencyRecordWorkitem;

import java.util.List;

/**
 * @Description: 特情处置工作
 * @Author: zpc
 * @Date:   2023-02-27
 * @Version: V1.0
 */
public interface ContingencyRecordWorkitemMapper extends BaseMapper<ContingencyRecordWorkitem> {

    /**
     * 根据阶段id查询
     * @param stageId
     * @return
     */
    List<ContingencyRecordWorkitem> listByStageId(String stageId);

    /**
     * 查询没有完成的数量
     * @param stageId
     * @return
     */
    int countNotCompletedByStageId(String stageId);
}
