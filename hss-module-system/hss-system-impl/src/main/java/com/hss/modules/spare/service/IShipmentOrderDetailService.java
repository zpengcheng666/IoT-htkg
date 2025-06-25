package com.hss.modules.spare.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.spare.entity.ShipmentOrderDetailEntity;
import com.hss.modules.spare.model.ReceiptDetailItem;

import java.util.List;

/**
* @ClassDescription: 出库单物料详情
* @JdkVersion: 1.8
* @Author: hd
* @Created: 2024/4/25 16:04
*/
public interface IShipmentOrderDetailService extends IService<ShipmentOrderDetailEntity> {

    /**
     * 根据出库单查询
     * @param orderId 出库单id
     * @return
     */
    List<ReceiptDetailItem> listByOrderId(String orderId);
}
