package com.hss.modules.spare.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.spare.entity.InventoryCheckDetailEntity;
import com.hss.modules.spare.model.CheckDetailItem;

import java.util.List;

/**
* @ClassDescription:
* @JdkVersion: 1.8
* @Author: hd
* @Created: 2024/4/25 16:03
*/
public interface IInventoryCheckDetailService extends IService<InventoryCheckDetailEntity> {

    List<CheckDetailItem> listByOrderId(String id);
}
