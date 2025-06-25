package com.hss.modules.maintain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.maintain.entity.MaintainRecord;
import com.hss.modules.maintain.model.MaintainSubmitDTO;

/**
 * @Description: 保养任务表
 * @Author: zpc
 * @Date:   2022-12-15
 * @Version: V1.0
 */
public interface IMaintainRecordService extends IService<MaintainRecord> {

    void addRecordDevice(MaintainRecord record);

    void updateRecordDevice(MaintainRecord maintainRecord);

    /**
     * 签发保养任务
     * @param id
     */
    void confirm(String id);

    /**
     * 执行保养任务
     * @param id
     */
    void act(String id);

    /**
     * 提交保养任务
     * @param dto
     */
    void submit(MaintainSubmitDTO dto);

    /**
     * 审核保养任务
     * @param id
     */
    void complete(String id);
}
