package com.hss.modules.scada.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.scada.entity.ConDianWei;

/**
* @description: 同步点位
* @author zpc
* @date 2024/3/19 13:34
* @version 1.0
*/
public interface IConDianWeiService extends IService<ConDianWei> {

    /**
     * 同步指定网关点位数据
     * @param gatewayId
     */
    void syncData(String gatewayId);

    /**
     * 获取点位名称
     * @param variableId 点位id
     * @return 设备名称：点位名称
     */
    String getPointNameByPointId(String variableId);

}
