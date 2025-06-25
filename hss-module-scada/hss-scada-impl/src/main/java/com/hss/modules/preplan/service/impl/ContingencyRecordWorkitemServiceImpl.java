package com.hss.modules.preplan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.preplan.entity.ContingencyRecordWorkitem;
import com.hss.modules.preplan.mapper.ContingencyRecordWorkitemMapper;
import com.hss.modules.preplan.service.IContingencyRecordWorkitemService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 特情处置工作
 * @Author: zpc
 * @Date:   2023-02-27
 * @Version: V1.0
 */
@Service
public class ContingencyRecordWorkitemServiceImpl extends ServiceImpl<ContingencyRecordWorkitemMapper, ContingencyRecordWorkitem> implements IContingencyRecordWorkitemService {

    @Override
    public List<ContingencyRecordWorkitem> listByStageId(String id) {
        //根据阶段id查询
        return baseMapper.listByStageId(id);
    }

    @Override
    public int countNotCompletedByStageId(String stageId) {
        //查询没有完成的数量
        return baseMapper.countNotCompletedByStageId(stageId);
    }
}
