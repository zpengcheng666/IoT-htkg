package com.hss.modules.preplan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.preplan.entity.ContingencyRecordTerminal;

import java.util.List;

/**
 * @Description: 特情处置终端关系
 * @Author: zpc
 * @Date:   2023-02-27
 * @Version: V1.0
 */
public interface IContingencyRecordTerminalService extends IService<ContingencyRecordTerminal> {

    /**
     * 根据terminalId查询
     * @param terminalId
     * @return
     */
    ContingencyRecordTerminal getByTerminalId(String terminalId);

    /**
     * 根据阶段id查询终端id
     * @param stageId 阶段id
     * @return
     */
    List<String> listTerminalIdByStageId(String stageId);
}
