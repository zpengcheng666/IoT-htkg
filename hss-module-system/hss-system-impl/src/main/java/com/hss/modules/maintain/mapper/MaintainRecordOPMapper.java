package com.hss.modules.maintain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.maintain.entity.MaintainRecordOP;

import java.util.List;

/**
 * @Description: 保养任务-设备关系
 * @Author: zpc
 * @Date:   2022-12-28
 * @Version: V1.0
 */
public interface MaintainRecordOPMapper extends BaseMapper<MaintainRecordOP> {

    /**
     * 根据记录id查询
     * @param recordId
     * @return
     */
    List<MaintainRecordOP> listByRecordId(String recordId);
}
