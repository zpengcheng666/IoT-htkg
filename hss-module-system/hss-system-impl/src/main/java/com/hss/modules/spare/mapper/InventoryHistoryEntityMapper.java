package com.hss.modules.spare.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.modules.spare.entity.InventoryHistoryEntity;
import com.hss.modules.spare.model.ItemOptDTO;
import com.hss.modules.spare.model.ItemOptPageVO;
import org.apache.ibatis.annotations.Param;

/**
* @ClassDescription:
* @JdkVersion: 1.8
* @Author: hd
* @Created: 2024/4/25 16:01
*/
public interface InventoryHistoryEntityMapper extends BaseMapper<InventoryHistoryEntity> {

    IPage<ItemOptPageVO> historyPage(Page<ItemOptPageVO> page, @Param("dto") ItemOptDTO dto);
}
