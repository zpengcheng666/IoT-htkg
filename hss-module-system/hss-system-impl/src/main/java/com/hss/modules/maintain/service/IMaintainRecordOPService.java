package com.hss.modules.maintain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.maintain.entity.MaintainRecordOP;

import java.util.List;

/**
 * @Description: 保养任务-设备关系
 * @Author: zpc
 * @Date:   2022-12-28
 * @Version: V1.0
 */
public interface IMaintainRecordOPService extends IService<MaintainRecordOP> {

    /**
     * 根据记录id擦汗寻
     * @param id
     * @return
     */
    List<MaintainRecordOP> listByRecordId(String id);
}
