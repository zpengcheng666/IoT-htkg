package com.hss.modules.spare.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.modules.spare.entity.ShipmentOrderEntity;
import com.hss.modules.spare.model.ShipmentPageDTO;
import org.apache.ibatis.annotations.Param;

/**
* @ClassDescription:
* @JdkVersion: 1.8
* @Author: hd
* @Created: 2024/4/25 16:02
*/
public interface ShipmentOrderMapper extends BaseMapper<ShipmentOrderEntity> {

    Page<ShipmentOrderEntity> page(Page<Object> objectPage, @Param("dto") ShipmentPageDTO dto);
}
