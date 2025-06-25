package com.hss.modules.spare.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.modules.spare.entity.InventoryEntity;
import com.hss.modules.spare.model.InventoryShowDTO;
import com.hss.modules.spare.model.InventoryShowVO;
import com.hss.modules.spare.model.PageItemDTO;
import com.hss.modules.spare.model.PageItemVO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
* @ClassDescription:
* @JdkVersion: 1.8
* @Author: hd
* @Created: 2024/4/25 16:01
*/
public interface InventoryMapper extends BaseMapper<InventoryEntity> {

    InventoryEntity get(@Param("areaId") String areaId, @Param("itemId") String itemId);

    int update(@Param("id") String id, @Param("quantity") BigDecimal quantity, @Param("version") Integer version);


    IPage<InventoryShowVO> pageByWarehouse(Page<InventoryShowVO> page, @Param("dto") InventoryShowDTO dto);

    IPage<InventoryShowVO> pageByArea(Page<Object> page, @Param("dto") InventoryShowDTO dto);

    IPage<InventoryShowVO> pageByItem(Page<Object> objectPage, @Param("dto") InventoryShowDTO dto);

    IPage<PageItemVO> pageItem(Page<Object> objectPage, @Param("dto") PageItemDTO dto);
}
