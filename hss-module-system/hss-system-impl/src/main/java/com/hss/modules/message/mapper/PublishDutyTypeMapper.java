package com.hss.modules.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.message.entity.PublishDutyType;

import java.util.List;

/**
 * @Description: 值班类型
 * @Author: zpc
 * @Date:   2022-12-08
 * @Version: V1.0
 */
public interface PublishDutyTypeMapper extends BaseMapper<PublishDutyType> {

    /**
     * 根据dutyId查询
     * @param dutyId
     * @return
     */
    List<PublishDutyType> listByDutyId(String dutyId);
}
