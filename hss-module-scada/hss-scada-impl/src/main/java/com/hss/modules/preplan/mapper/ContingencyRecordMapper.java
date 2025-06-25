package com.hss.modules.preplan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.preplan.entity.ContingencyRecord;


/**
 * @Description: 特情处置记录
 * @Author: zpc
 * @Date:   2023-02-13
 * @Version: V1.0
 */
public interface ContingencyRecordMapper extends BaseMapper<ContingencyRecord> {

    /**
     * 根据终端id查询没有完成的预案属性
     * @param terminalId
     * @return
     */
    int countNotCompletedByTerminalId(String terminalId);
}
