package com.hss.modules.spare.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.modules.spare.entity.ReceiptOrderEntity;
import com.hss.modules.spare.model.ReceiptPageDTO;
import org.apache.ibatis.annotations.Param;

/**
* @ClassDescription:
* @JdkVersion: 1.8
* @Author: hd
* @Created: 2024/4/25 16:02
*/
public interface ReceiptOrderMapper extends BaseMapper<ReceiptOrderEntity> {


    Page<ReceiptOrderEntity> page(Page<ReceiptOrderEntity> receiptOrderEntityPage, @Param("dto") ReceiptPageDTO dto);
}
