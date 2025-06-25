package com.hss.modules.spare.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.spare.entity.InventoryMovementEntity;
import com.hss.modules.spare.model.MovementAddDTO;
import com.hss.modules.spare.model.MovementDetail;
import com.hss.modules.spare.model.MovementPageDTO;

/**
* @ClassDescription:
* @JdkVersion: 1.8
* @Author: hd
* @Created: 2024/4/25 16:04
*/
public interface IInventoryMovementService extends IService<InventoryMovementEntity> {

    void add(MovementAddDTO dto);

    String getNo();

    Page<InventoryMovementEntity> getPage(MovementPageDTO dto);

    MovementDetail getDetail(String id);
}
