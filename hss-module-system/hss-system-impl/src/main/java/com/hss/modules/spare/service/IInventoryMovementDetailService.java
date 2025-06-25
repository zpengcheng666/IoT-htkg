package com.hss.modules.spare.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.spare.entity.InventoryMovementDetailEntity;
import com.hss.modules.spare.model.MovementDetailItem;

import java.util.List;

/**
* @ClassDescription:
* @JdkVersion: 1.8
* @Author: hd
* @Created: 2024/4/25 16:04
*/
public interface IInventoryMovementDetailService extends IService<InventoryMovementDetailEntity> {

    /**
     * 根据移库单id查询详情
     * @param orderId 移库单id
     * @return
     */
    List<MovementDetailItem> listByOrderId(String orderId);
}
