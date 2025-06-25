package com.hss.modules.preplan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.preplan.entity.ContingencyRecordStage;
import com.hss.modules.preplan.entity.ContingencyRecordWorkitem;
import com.hss.modules.preplan.mapper.ContingencyRecordStageMapper;
import com.hss.modules.preplan.service.IContingencyRecordStageService;
import com.hss.modules.preplan.service.IContingencyRecordWorkitemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 特情处置阶段
 * @Author: zpc
 * @Date:   2023-02-27
 * @Version: V1.0
 */
@Service
public class ContingencyRecordStageServiceImpl extends ServiceImpl<ContingencyRecordStageMapper, ContingencyRecordStage> implements IContingencyRecordStageService {

    @Autowired
    private IContingencyRecordWorkitemService contingencyRecordWorkitemService;
    @Override
    public List<ContingencyRecordStage> listByRecordId(String recordId) {
        //根据记录id查询
        List<ContingencyRecordStage> list = baseMapper.listByRecordId(recordId);
        for (ContingencyRecordStage stage : list) {
            //根据阶段id查询
            List<ContingencyRecordWorkitem> workList = contingencyRecordWorkitemService.listByStageId(stage.getId());
            stage.setWorkList(workList);
        }
        return list;
    }

    @Override
    public int countNotCompletedByRecordId(String planId) {
        //查询没有完成的记录数量
        return baseMapper.countNotCompletedByRecordId(planId);
    }
}
