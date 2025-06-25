package com.hss.modules.message.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.message.entity.PublishDutyType;
import com.hss.modules.message.mapper.PublishDutyTypeMapper;
import com.hss.modules.message.service.IPublishDutyTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 值班类型
 * @Author: zpc
 * @Date:   2022-12-08
 * @Version: V1.0
 */
@Service
public class PublishDutyTypeServiceImpl extends ServiceImpl<PublishDutyTypeMapper, PublishDutyType> implements IPublishDutyTypeService {
    @Override
    public List<PublishDutyType> listByDutyId(String dutyId) {
        return baseMapper.listByDutyId(dutyId);
    }
}
