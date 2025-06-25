package com.hss.modules.spare.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.spare.entity.ShipmentOrderDetailEntity;
import com.hss.modules.spare.mapper.ShipmentOrderDetailMapper;
import com.hss.modules.spare.model.ReceiptDetailItem;
import com.hss.modules.spare.service.IShipmentOrderDetailService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @ClassDescription:
* @JdkVersion: 1.8
* @Author: hd
* @Created: 2024/4/25 16:03
*/
@Service
public class ShipmentOrderDetailServiceImpl extends ServiceImpl<ShipmentOrderDetailMapper, ShipmentOrderDetailEntity> implements IShipmentOrderDetailService {

    @Override
    public List<ReceiptDetailItem> listByOrderId(String orderId) {
        return baseMapper.listByOrderId(orderId);
    }
}
