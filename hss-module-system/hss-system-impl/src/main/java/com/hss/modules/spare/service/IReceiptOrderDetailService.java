package com.hss.modules.spare.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.spare.entity.ReceiptOrderDetailEntity;
import com.hss.modules.spare.model.ReceiptDetailItem;

import java.util.List;

/**
* @ClassDescription: 入库单物料详细信息
* @JdkVersion: 1.8
* @Author: hd
* @Created: 2024/4/25 16:04
*/
public interface IReceiptOrderDetailService extends IService<ReceiptOrderDetailEntity> {


    /**
     * 根据入库单id查询
     * @param orderId 入库单id
     * @return
     */
    List<ReceiptDetailItem> listByOrderId(String orderId);
}
