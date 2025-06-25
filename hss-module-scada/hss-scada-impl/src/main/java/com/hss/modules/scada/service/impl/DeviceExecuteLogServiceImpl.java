package com.hss.modules.scada.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.scada.entity.DeviceExecuteLog;
import com.hss.modules.scada.mapper.DeviceExecuteLogMapper;
import com.hss.modules.scada.service.IDeviceExecuteLogService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @description: 下发设备命令日志
* @author zpc
* @date 2024/3/19 15:19
* @version 1.0
*/
@Service
public class DeviceExecuteLogServiceImpl extends ServiceImpl<DeviceExecuteLogMapper, DeviceExecuteLog> implements IDeviceExecuteLogService {

    @Override
    public List<DeviceExecuteLog> list(DeviceExecuteLog deviceExecuteLog) {
        return baseMapper.listExcel(deviceExecuteLog);
    }
}
