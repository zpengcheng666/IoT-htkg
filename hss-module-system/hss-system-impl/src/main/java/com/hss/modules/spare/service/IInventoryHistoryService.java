package com.hss.modules.spare.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.spare.entity.InventoryHistoryEntity;
import com.hss.modules.spare.model.ItemOptDTO;
import com.hss.modules.spare.model.ItemOptHistory;

/**
* @ClassDescription:
* @JdkVersion: 1.8
* @Author: hd
* @Created: 2024/4/25 16:03
*/
public interface IInventoryHistoryService extends IService<InventoryHistoryEntity> {

    IPage<ItemOptHistory> historyPage(ItemOptDTO dto);
}
