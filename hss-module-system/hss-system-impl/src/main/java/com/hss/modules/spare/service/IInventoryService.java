package com.hss.modules.spare.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.spare.entity.InventoryEntity;
import com.hss.modules.spare.model.*;

import java.util.List;

/**
* @ClassDescription:
* @JdkVersion: 1.8
* @Author: hd
* @Created: 2024/4/25 16:03
*/
public interface IInventoryService extends IService<InventoryEntity> {

    void adds(List<InventoryBO> bos);
    void add(InventoryBO bo);
    void subs(List<InventoryBO> bos);
    void sub(InventoryBO bo);

    IPage<InventoryShowVO> pageByWarehouse(InventoryShowDTO dto);

    IPage<InventoryShowVO> pageByArea(InventoryShowDTO dto);

    IPage<InventoryShowVO> pageByItem(InventoryShowDTO dto);

    long getNo();


    void updates(List<InventoryBO> bos);
    void update(InventoryBO bo);

    IPage<ItemOptHistory> historyPage(ItemOptDTO dto);

    IPage<PageItemVO> pageItem(PageItemDTO dto);
}
