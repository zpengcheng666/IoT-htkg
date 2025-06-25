package com.hss.modules.spare.service.impl;

import com.hss.modules.spare.entity.ItemEntity;
import com.hss.modules.spare.mapper.ItemMapper;
import com.hss.modules.spare.service.IItemService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 物料管理
 * @Author: wuyihan
 * @Date:   2024-04-29
 * @Version: V1.0
 */
@Service
public class ItemServiceImpl extends ServiceImpl<ItemMapper, ItemEntity> implements IItemService {

}
