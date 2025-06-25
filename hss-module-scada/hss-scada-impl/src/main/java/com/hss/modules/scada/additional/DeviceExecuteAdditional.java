package com.hss.modules.scada.additional;

import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.model.DeviceExecuteDTO;

/**
* @description: 执行动作扩展
* @author zpc
* @date 2024/3/19 16:20
* @version 1.0
*/
public interface DeviceExecuteAdditional {

    /**
     * 执行动作附加操作
     * @param dto
     * @param attr
     */
    void additionalOperate(DeviceExecuteDTO dto, ConDeviceAttribute attr);
}
