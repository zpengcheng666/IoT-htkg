package com.hss.modules.spare.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.spare.entity.InventoryCheckDetailEntity;
import com.hss.modules.spare.mapper.InventoryCheckDetailMapper;
import com.hss.modules.spare.model.CheckDetailItem;
import com.hss.modules.spare.service.IInventoryCheckDetailService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @ClassDescription:
* @JdkVersion: 1.8
* @Author: hd
* @Created: 2024/4/25 16:02
*/
@Service
public class InventoryCheckDetailServiceImpl extends ServiceImpl<InventoryCheckDetailMapper, InventoryCheckDetailEntity> implements IInventoryCheckDetailService {

    @Override
    public List<CheckDetailItem> listByOrderId(String orderId) {
        return baseMapper.listByOrderId(orderId);
    }
}
