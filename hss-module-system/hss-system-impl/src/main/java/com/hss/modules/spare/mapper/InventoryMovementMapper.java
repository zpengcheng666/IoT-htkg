package com.hss.modules.spare.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.modules.spare.entity.InventoryMovementEntity;
import com.hss.modules.spare.model.MovementPageDTO;
import org.apache.ibatis.annotations.Param;

/**
* @ClassDescription:
* @JdkVersion: 1.8
* @Author: hd
* @Created: 2024/4/25 16:01
*/
public interface InventoryMovementMapper extends BaseMapper<InventoryMovementEntity> {

    Page<InventoryMovementEntity> page(Page<Object> objectPage, @Param("dto") MovementPageDTO dto);
}
