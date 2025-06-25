package com.hss.modules.spare.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.core.common.exception.HssBootException;
import com.hss.modules.spare.constant.NoUtil;
import com.hss.modules.spare.constant.SpareConstant;
import com.hss.modules.spare.entity.InventoryCheckDetailEntity;
import com.hss.modules.spare.entity.InventoryCheckEntity;
import com.hss.modules.spare.mapper.InventoryCheckMapper;
import com.hss.modules.spare.model.*;
import com.hss.modules.spare.service.IAreaService;
import com.hss.modules.spare.service.IInventoryCheckDetailService;
import com.hss.modules.spare.service.IInventoryCheckService;
import com.hss.modules.spare.service.IInventoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @ClassDescription:
* @JdkVersion: 1.8
* @Author: hd
* @Created: 2024/4/25 16:02
*/
@Service
public class InventoryCheckServiceImpl extends ServiceImpl<InventoryCheckMapper, InventoryCheckEntity> implements IInventoryCheckService {


    @Autowired
    private IInventoryService inventoryService;
    @Autowired
    private IInventoryCheckDetailService checkDetailService;
    @Autowired
    private IAreaService areaService;
    @Override
    public String getNo() {
        LocalDate now = LocalDate.now();
        long max = inventoryService.getNo();
        return NoUtil.getNo("C",now, max);
    }
    private void checkAdd(CheckAddDTO dto) {
        if (StringUtils.isBlank(dto.getOrderNo())) {
            throw new HssBootException("单号不能为空");
        }
        if (StringUtils.isBlank(dto.getUseName())) {
            throw new HssBootException("负责人不能为空");
        }
        if (CollectionUtils.isEmpty(dto.getItems())) {
            throw new HssBootException("物料信息不能为空");
        }
        for (CheckAddItem item : dto.getItems()) {
            if (StringUtils.isBlank(item.getItemId())) {
                throw new HssBootException("物料id不能为空");
            }
            if (item.getQuantity() == null) {
                throw new HssBootException("物料数量不能为空");
            }
            if (item.getQuantity().compareTo(BigDecimal.ZERO) < 0) {
                throw new HssBootException("物料数量必须大于等于0");
            }
            if (StringUtils.isBlank(item.getWarehouseId())) {
                throw new HssBootException("原始库房不能为空");
            }
            if (StringUtils.isBlank(item.getAreaId())) {
                throw new HssBootException("原始库区不能为空");
            }
        }

    }

    @Override
    public void add(CheckAddDTO dto) {
        checkAdd(dto);
        InventoryCheckEntity entity = new InventoryCheckEntity();
        entity.setCheckNo(dto.getOrderNo());
        entity.setUseName(dto.getUseName());
        entity.setCheckDate(LocalDate.now());
        entity.setRemark(dto.getRemark());
        save(entity);
        String orderId = entity.getId();
        List<InventoryCheckDetailEntity> items = dto.getItems().stream().map(it -> {
            InventoryCheckDetailEntity item = new InventoryCheckDetailEntity();
            item.setCheckId(orderId);
            item.setItemId(it.getItemId());
            item.setWarehouseId(it.getWarehouseId());
            item.setAreaId(it.getAreaId());
            item.setQuantity(it.getQuantity());
            item.setCheckQuantity(it.getCheckQuantity());
            item.setRemark(it.getRemark());
            return item;
        }).collect(Collectors.toList());
        checkDetailService.saveBatch(items);

        List<InventoryBO> bos = items.stream().map(i -> {
            InventoryBO bo = new InventoryBO();
            bo.setFormId(orderId);
            bo.setWarehouseId(i.getWarehouseId());
            bo.setAreaId(i.getAreaId());
            bo.setItemId(i.getItemId());
            bo.setQuantity(i.getCheckQuantity());
            bo.setFormType(SpareConstant.INVENTORY_TYPE_CHECK);
            return bo;
        }).collect(Collectors.toList());
        inventoryService.updates(bos);



    }

    @Override
    public Page<InventoryCheckEntity> getPage(CheckPageDTO dto) {
        return baseMapper.page(new Page<>(dto.getPageNo(), dto.getPageSize()), dto);
    }

    @Override
    public CheckDetail getDetail(String id) {
        InventoryCheckEntity byId = getById(id);
        if (byId == null) {
            return null;
        }
        CheckDetail detail = new CheckDetail();
        detail.setId(byId.getId());
        detail.setOrderNo(byId.getCheckNo());
        detail.setUseName(byId.getUseName());
        detail.setCheckDate(byId.getCheckDate());
        detail.setRemark(byId.getRemark());
        List<CheckDetailItem> items =  checkDetailService.listByOrderId(id);
        Set<String> areaIds = items.stream().map(CheckDetailItem::getArea).collect(Collectors.toSet());
        Map<String, String> areaMap = areaService.mapAreaIdMap(areaIds);
        for (CheckDetailItem item : items) {
            item.setArea(areaMap.get(item.getArea()));
            item.setChangeQuantity(item.getCheckQuantity().subtract(item.getQuantity()));
        }
        detail.setItems(items);
        return detail;
    }

}
