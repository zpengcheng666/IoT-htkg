package com.hss.modules.spare.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.spare.entity.InventoryMovementDetailEntity;
import com.hss.modules.spare.model.MovementDetailItem;

import java.util.List;

/**
* @ClassDescription:
* @JdkVersion: 1.8
* @Author: hd
* @Created: 2024/4/25 16:01
*/
public interface InventoryMovementDetailMapper extends BaseMapper<InventoryMovementDetailEntity> {

    List<MovementDetailItem> listByOrderId(String orderId);
}
