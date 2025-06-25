package com.hss.modules.spare.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.spare.entity.ShipmentOrderEntity;
import com.hss.modules.spare.model.ShipmentAddDTO;
import com.hss.modules.spare.model.ShipmentDetail;
import com.hss.modules.spare.model.ShipmentPageDTO;

/**
* @ClassDescription:
* @JdkVersion: 1.8
* @Author: hd
* @Created: 2024/4/25 16:04
*/
public interface IShipmentOrderService extends IService<ShipmentOrderEntity> {

    /**
     * 获取出库编号
     * @return 出库编号
     */
    String getNo();

    void add(ShipmentAddDTO dto);

    Page<ShipmentOrderEntity> getPage(ShipmentPageDTO dto);

    ShipmentDetail getDetail(String id);
}
