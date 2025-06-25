package com.hss.modules.spare.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.spare.entity.InventoryMovementDetailEntity;
import com.hss.modules.spare.mapper.InventoryMovementDetailMapper;
import com.hss.modules.spare.model.MovementDetailItem;
import com.hss.modules.spare.service.IInventoryMovementDetailService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @ClassDescription:
* @JdkVersion: 1.8
* @Author: hd
* @Created: 2024/4/25 16:02
*/
@Service
public class InventoryMovementDetailServiceImpl extends ServiceImpl<InventoryMovementDetailMapper, InventoryMovementDetailEntity> implements IInventoryMovementDetailService {

    @Override
    public List<MovementDetailItem> listByOrderId(String orderId) {
        return baseMapper.listByOrderId(orderId);
    }
}
