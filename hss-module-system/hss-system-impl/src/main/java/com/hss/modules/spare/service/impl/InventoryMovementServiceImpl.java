package com.hss.modules.spare.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.core.common.exception.HssBootException;
import com.hss.modules.spare.constant.NoUtil;
import com.hss.modules.spare.constant.SpareConstant;
import com.hss.modules.spare.entity.InventoryMovementDetailEntity;
import com.hss.modules.spare.entity.InventoryMovementEntity;
import com.hss.modules.spare.mapper.InventoryMovementMapper;
import com.hss.modules.spare.model.*;
import com.hss.modules.spare.service.IAreaService;
import com.hss.modules.spare.service.IInventoryMovementDetailService;
import com.hss.modules.spare.service.IInventoryMovementService;
import com.hss.modules.spare.service.IInventoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @ClassDescription:
* @JdkVersion: 1.8
* @Author: hd
* @Created: 2024/4/25 16:03
*/
@Service
public class InventoryMovementServiceImpl extends ServiceImpl<InventoryMovementMapper, InventoryMovementEntity> implements IInventoryMovementService {


    @Autowired
    private IInventoryService inventoryService;
    @Autowired
    private IInventoryMovementDetailService movementDetailService;
    @Autowired
    private IAreaService areaService;

    private void checkAdd(MovementAddDTO dto) {
        if (StringUtils.isBlank(dto.getOrderNo())) {
            throw new HssBootException("单号不能为空");
        }
        if (StringUtils.isBlank(dto.getUseName())) {
            throw new HssBootException("负责人不能为空");
        }
        if (CollectionUtils.isEmpty(dto.getItems())) {
            throw new HssBootException("物料信息不能为空");
        }
        for (MovementAddItem item : dto.getItems()) {
            if (StringUtils.isBlank(item.getItemId())) {
                throw new HssBootException("物料id不能为空");
            }
            if (item.getQuantity() == null) {
                throw new HssBootException("物料数量不能为空");
            }
            if (item.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                throw new HssBootException("物料数量必须大于0");
            }
            if (StringUtils.isBlank(item.getSourceWarehouseId())) {
                throw new HssBootException("原始库房不能为空");
            }
            if (StringUtils.isBlank(item.getSourceAreaId())) {
                throw new HssBootException("原始库区不能为空");
            }
            if (StringUtils.isBlank(item.getTargetWarehouseId())) {
                throw new HssBootException("目标库房不能为空");
            }
            if (StringUtils.isBlank(item.getTargetAreaId())) {
                throw new HssBootException("目标库区不能为空");
            }
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(MovementAddDTO dto) {
        checkAdd(dto);
        InventoryMovementEntity entity = new InventoryMovementEntity();
        entity.setOrderNo(dto.getOrderNo());
        entity.setUseName(dto.getUseName());
        entity.setMovementDate(LocalDate.now());
        entity.setRemark(dto.getRemark());
        save(entity);
        String orderId = entity.getId();
        List<InventoryMovementDetailEntity> items = dto.getItems().stream().map(i -> {
            InventoryMovementDetailEntity item = new InventoryMovementDetailEntity();
            item.setInventoryMovementId(orderId);
            item.setSourceWarehouseId(i.getSourceWarehouseId());
            item.setSourceAreaId(i.getSourceAreaId());
            item.setTargetWarehouseId(i.getTargetWarehouseId());
            item.setTargetAreaId(i.getTargetAreaId());
            item.setItemId(i.getItemId());
            item.setQuantity(i.getQuantity());
            return item;
        }).collect(Collectors.toList());
        movementDetailService.saveBatch(items);

        List<InventoryBO> subBos =items.stream().map(i -> {
            InventoryBO bo = new InventoryBO();
            bo.setFormId(orderId);
            bo.setWarehouseId(i.getSourceWarehouseId());
            bo.setAreaId(i.getSourceAreaId());
            bo.setItemId(i.getItemId());
            bo.setQuantity(i.getQuantity());
            bo.setFormType(SpareConstant.INVENTORY_TYPE_MOVE);
            return bo;
        }).collect(Collectors.toList());
        inventoryService.subs(subBos);

        List<InventoryBO> addBos =items.stream().map(i -> {
            InventoryBO bo = new InventoryBO();
            bo.setFormId(orderId);
            bo.setWarehouseId(i.getTargetWarehouseId());
            bo.setAreaId(i.getTargetAreaId());
            bo.setItemId(i.getItemId());
            bo.setQuantity(i.getQuantity());
            bo.setFormType(SpareConstant.INVENTORY_TYPE_MOVE);
            return bo;
        }).collect(Collectors.toList());
        inventoryService.adds(addBos);
    }

    @Override
    public String getNo() {
        LocalDate now = LocalDate.now();
        long max = inventoryService.getNo();
        return NoUtil.getNo("M",now, max);
    }

    @Override
    public Page<InventoryMovementEntity> getPage(MovementPageDTO dto) {
        return baseMapper.page(new Page<>(dto.getPageNo(), dto.getPageSize()), dto);
    }

    @Override
    public MovementDetail getDetail(String id) {
        InventoryMovementEntity byId = getById(id);
        if (byId == null) {
            return null;
        }
        MovementDetail detail = new MovementDetail();
        detail.setId(byId.getId());
        detail.setOrderNo(byId.getOrderNo());
        detail.setUseName(byId.getUseName());
        detail.setMovementDate(byId.getMovementDate());
        detail.setRemark(byId.getRemark());
        List<MovementDetailItem> items = movementDetailService.listByOrderId(id);
        Set<String> areaIds = items.stream().flatMap(i -> {
            List<String> list = new ArrayList<>(2);
            list.add(i.getSource());
            list.add(i.getTarget());
            return list.stream();
        }).collect(Collectors.toSet());
        Map<String, String> areaMap = areaService.mapAreaIdMap(areaIds);
        for (MovementDetailItem item : items) {
            item.setSource(areaMap.get(item.getSource()));
            item.setTarget(areaMap.get(item.getTarget()));
        }
        detail.setItems(items);

        return detail;
    }

}
