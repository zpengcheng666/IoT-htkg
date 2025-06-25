package com.hss.modules.preplan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.preplan.entity.ContingencyRecordStage;

import java.util.List;

/**
 * @Description: 特情处置阶段
 * @Author: zpc
 * @Date:   2023-02-27
 * @Version: V1.0
 */
public interface ContingencyRecordStageMapper extends BaseMapper<ContingencyRecordStage> {

    /**
     * 根据记录id查询
     * @param recordId
     * @return
     */
    List<ContingencyRecordStage> listByRecordId(String recordId);

    /**
     * 查询没有完成的记录数量
     * @param planId
     * @return
     */
    int countNotCompletedByRecordId(String planId);
}
