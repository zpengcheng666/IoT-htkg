package com.hss.modules.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.message.entity.DutyGroup;

import java.util.List;

/**
 * @Description: 值班人员
 * @Author: zpc
 * @Date:   2023-04-21
 * @Version: V1.0
 */
public interface IDutyGroupService extends IService<DutyGroup> {
    /**
     * 根据值班信息查看值班小组
     * @param dutyId 值班id
     * @return 值班小组信息
     */
    List<DutyGroup> listByDutyId(String dutyId);

    /**
     * 添加值班小组
     * @param dutyGroup
     */
    void add(DutyGroup dutyGroup);

    /**
     * 百年祭值班小组
     * @param dutyGroup
     */
    void editById(DutyGroup dutyGroup);

}
