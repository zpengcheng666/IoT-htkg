package com.hss.modules.scada.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.scada.entity.DeviceExecuteLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @description: 下发设备命令日志
* @author zpc
* @date 2024/3/19 16:06
* @version 1.0
*/
public interface DeviceExecuteLogMapper extends BaseMapper<DeviceExecuteLog> {

    /**
     * excel查询
     * @param deviceExecuteLog
     * @return
     */
    List<DeviceExecuteLog> listExcel(@Param("entity") DeviceExecuteLog deviceExecuteLog);
}
