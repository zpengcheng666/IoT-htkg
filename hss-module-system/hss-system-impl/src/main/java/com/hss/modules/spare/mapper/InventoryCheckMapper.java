package com.hss.modules.spare.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.modules.spare.entity.InventoryCheckEntity;
import com.hss.modules.spare.model.CheckPageDTO;
import org.apache.ibatis.annotations.Param;

/**
* @ClassDescription:
* @JdkVersion: 1.8
* @Author: hd
* @Created: 2024/4/25 16:01
*/
public interface InventoryCheckMapper extends BaseMapper<InventoryCheckEntity> {

    Page<InventoryCheckEntity> page(Page<Object> objectPage, @Param("dto") CheckPageDTO dto);
}
