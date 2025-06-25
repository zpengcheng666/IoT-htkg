package com.hss.modules.preplan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.preplan.entity.ContingencyRecordTerminal;
import com.hss.modules.preplan.mapper.ContingencyRecordTerminalMapper;
import com.hss.modules.preplan.service.IContingencyRecordTerminalService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 特情处置终端关系
 * @Author: zpc
 * @Date:   2023-02-27
 * @Version: V1.0
 */
@Service
public class ContingencyRecordTerminalServiceImpl extends ServiceImpl<ContingencyRecordTerminalMapper, ContingencyRecordTerminal> implements IContingencyRecordTerminalService {

    @Override
    public ContingencyRecordTerminal getByTerminalId(String terminalId) {
        //根据终端id查询
        return baseMapper.getByTerminalId(terminalId);
    }

    @Override
    public List<String> listTerminalIdByStageId(String stageId) {
        //根据阶段id查询终端id列表
        return baseMapper.listTerminalIdByStageId(stageId);
    }
}
