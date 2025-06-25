package com.hss.modules.spare.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.spare.entity.ReceiptOrderDetailEntity;
import com.hss.modules.spare.mapper.ReceiptOrderDetailMapper;
import com.hss.modules.spare.model.ReceiptDetailItem;
import com.hss.modules.spare.service.IReceiptOrderDetailService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @ClassDescription: 入库单物料详细信息
* @JdkVersion: 1.8
* @Author: hd
* @Created: 2024/4/25 16:03
*/
@Service
public class ReceiptOrderDetailServiceImpl extends ServiceImpl<ReceiptOrderDetailMapper, ReceiptOrderDetailEntity> implements IReceiptOrderDetailService {


    @Override
    public List<ReceiptDetailItem> listByOrderId(String orderId) {
        return baseMapper.listByOrderId(orderId);
    }
}
