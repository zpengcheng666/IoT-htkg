package com.hss.modules.preplan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.preplan.entity.ContingencyRecordWorkitem;

import java.util.List;

/**
 * @Description: 特情处置工作
 * @Author: zpc
 * @Date:   2023-02-27
 * @Version: V1.0
 */
public interface IContingencyRecordWorkitemService extends IService<ContingencyRecordWorkitem> {

    /**
     * 根据阶段id查询
     * @param id
     * @return
     */
    List<ContingencyRecordWorkitem> listByStageId(String id);

    /**
     * 查询灭有完成的数量
     * @param stageId
     * @return
     */
    int countNotCompletedByStageId(String stageId);
}
