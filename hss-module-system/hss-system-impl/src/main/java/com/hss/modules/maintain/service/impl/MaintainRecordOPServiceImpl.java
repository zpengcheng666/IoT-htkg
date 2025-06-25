package com.hss.modules.maintain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.maintain.entity.MaintainRecordOP;
import com.hss.modules.maintain.mapper.MaintainRecordOPMapper;
import com.hss.modules.maintain.service.IMaintainRecordOPService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 保养任务-设备关系
 * @Author: zpc
 * @Date:   2022-12-28
 * @Version: V1.0
 */
@Service
public class MaintainRecordOPServiceImpl extends ServiceImpl<MaintainRecordOPMapper, MaintainRecordOP> implements IMaintainRecordOPService {

    @Override
    public List<MaintainRecordOP> listByRecordId(String recordId) {
        return baseMapper.listByRecordId(recordId);
    }
}
