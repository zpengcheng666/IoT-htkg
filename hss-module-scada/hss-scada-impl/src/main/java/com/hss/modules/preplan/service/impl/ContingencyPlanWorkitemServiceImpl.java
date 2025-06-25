package com.hss.modules.preplan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.preplan.entity.ContingencyPlanWorkitem;
import com.hss.modules.preplan.mapper.ContingencyPlanWorkitemMapper;
import com.hss.modules.preplan.service.IContingencyPlanWorkitemService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 应急预案工作
 * @Author: zpc
 * @Date:   2023-02-27
 * @Version: V1.0
 */
@Service
public class ContingencyPlanWorkitemServiceImpl extends ServiceImpl<ContingencyPlanWorkitemMapper, ContingencyPlanWorkitem> implements IContingencyPlanWorkitemService {

    @Override
    public List<ContingencyPlanWorkitem> listByStageId(String stageId) {
        //根据stageId查询
        return baseMapper.listByStageId(stageId);
    }
}
