package com.hss.modules.scada.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.scada.entity.DeviceExecuteLog;

import java.util.List;

/**
* @description: 下发设备命令日志
* @author zpc
* @date 2024/3/19 15:03
* @version 1.0
*/
public interface IDeviceExecuteLogService extends IService<DeviceExecuteLog> {

    /**
     * 查询excel
     * @param deviceExecuteLog
     * @return
     */
    List<DeviceExecuteLog> list(DeviceExecuteLog deviceExecuteLog);
}
