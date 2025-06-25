package com.hss.modules.preplan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.preplan.entity.ContingencyRecordTerminal;

import java.util.List;

/**
 * @Description: 特情处置终端关系
 * @Author: zpc
 * @Date:   2023-02-27
 * @Version: V1.0
 */
public interface ContingencyRecordTerminalMapper extends BaseMapper<ContingencyRecordTerminal> {

    /**
     * 根据终端id查询
     * @param terminalId
     * @return
     */
    ContingencyRecordTerminal getByTerminalId(String terminalId);

    /**
     * 根据阶段id查询终端id列表
     * @param stageId
     * @return
     */
    List<String> listTerminalIdByStageId(String stageId);
}
