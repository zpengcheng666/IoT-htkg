package com.hss.modules.preplan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.preplan.entity.ContingencyRecordStage;

import java.util.List;

/**
 * @Description: 特情处置阶段
 * @Author: zpc
 * @Date:   2023-02-27
 * @Version: V1.0
 */
public interface IContingencyRecordStageService extends IService<ContingencyRecordStage> {

    /**
     * 根据记录id查询
     * @param recordId 记录id
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
