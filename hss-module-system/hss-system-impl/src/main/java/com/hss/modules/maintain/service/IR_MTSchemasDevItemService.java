package com.hss.modules.maintain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.maintain.entity.MaintainSchemas;
import com.hss.modules.maintain.entity.R_MTSchemasDevItem;

/**
 * @Description: 保养任务-设备关系表
 * @Author: zpc
 * @Date:   2022-12-28
 * @Version: V1.0
 */
public interface IR_MTSchemasDevItemService extends IService<R_MTSchemasDevItem> {

    void saveSchemasDevice(MaintainSchemas maintainSchemas);

    void updateSchemasDevice(MaintainSchemas schemas);
}
