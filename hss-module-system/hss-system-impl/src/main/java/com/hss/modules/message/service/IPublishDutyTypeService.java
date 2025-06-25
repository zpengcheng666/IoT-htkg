package com.hss.modules.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.message.entity.PublishDutyType;

import java.util.List;

/**
 * @Description: 值班类型
 * @Author: zpc
 * @Date:   2022-12-08
 * @Version: V1.0
 */
public interface IPublishDutyTypeService extends IService<PublishDutyType> {

    /**
     * 根据dutyID查询
     * @param dutyId
     * @return
     */
    List<PublishDutyType> listByDutyId(String dutyId);


}
