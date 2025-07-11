package com.hss.modules.spare.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.spare.entity.ShipmentOrderDetailEntity;
import com.hss.modules.spare.model.ReceiptDetailItem;

import java.util.List;

/**
* @ClassDescription:
* @JdkVersion: 1.8
* @Author: hd
* @Created: 2024/4/25 16:02
*/
public interface ShipmentOrderDetailMapper extends BaseMapper<ShipmentOrderDetailEntity> {

    List<ReceiptDetailItem> listByOrderId(String orderId);
}
