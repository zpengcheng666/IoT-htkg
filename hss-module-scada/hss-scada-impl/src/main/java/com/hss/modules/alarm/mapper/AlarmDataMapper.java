package com.hss.modules.alarm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.alarm.entity.AlarmData;

/**
 * @Description: 报警数据
 * @Author: zpc
 * @Date:   2022-12-01
 * @Version: V1.0
 */
public interface AlarmDataMapper extends BaseMapper<AlarmData> {

    /**
     * 查询报警数量
     * @return
     */
    int getCount();
}
