package com.hss.modules.spare.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.spare.entity.InventoryCheckEntity;
import com.hss.modules.spare.model.CheckAddDTO;
import com.hss.modules.spare.model.CheckDetail;
import com.hss.modules.spare.model.CheckPageDTO;

/**
* @ClassDescription:
* @JdkVersion: 1.8
* @Author: hd
* @Created: 2024/4/25 16:03
*/
public interface IInventoryCheckService extends IService<InventoryCheckEntity> {

    String getNo();

    void add(CheckAddDTO dto);

    Page<InventoryCheckEntity> getPage(CheckPageDTO dto);

    CheckDetail getDetail(String id);
}
