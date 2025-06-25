package com.hss.modules.preplan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.preplan.entity.ContingencyRecord;
import com.hss.modules.preplan.pojo.ProcessWorkDTO;
import com.hss.modules.system.model.ContingencyRecordAlarmTerminal;

/**
 * @Description: 特情处置记录
 * @Author: zpc
 * @Date:   2023-02-13
 * @Version: V1.0
 */
public interface IContingencyRecordService extends IService<ContingencyRecord> {

    /**
     * 保存报警添加应急预案
     * @param contingencyRecordAlarmTerminal
     */
    void saveAlarmTerminal(ContingencyRecordAlarmTerminal contingencyRecordAlarmTerminal);

    /**
     * 查看终端id是否有源
     * @param terminalId
     * @return
     */
    boolean isHaveYuAn(String terminalId);

    /**
     * 根据终端id查询
     * @param terminalId
     * @return
     */
    ContingencyRecord getByTerminalId(String terminalId);

    /**
     * 处理预案
     * @param dto
     */
    void processWork(ProcessWorkDTO dto);
}
