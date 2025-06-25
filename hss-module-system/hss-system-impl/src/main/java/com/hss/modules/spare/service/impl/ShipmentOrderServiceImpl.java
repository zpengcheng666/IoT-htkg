package com.hss.modules.spare.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.core.common.exception.HssBootException;
import com.hss.modules.spare.constant.NoUtil;
import com.hss.modules.spare.constant.ShipmentType;
import com.hss.modules.spare.entity.ShipmentOrderDetailEntity;
import com.hss.modules.spare.entity.ShipmentOrderEntity;
import com.hss.modules.spare.mapper.ShipmentOrderMapper;
import com.hss.modules.spare.model.*;
import com.hss.modules.spare.service.IAreaService;
import com.hss.modules.spare.service.IInventoryService;
import com.hss.modules.spare.service.IShipmentOrderDetailService;
import com.hss.modules.spare.service.IShipmentOrderService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
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
public class ShipmentOrderServiceImpl extends ServiceImpl<ShipmentOrderMapper, ShipmentOrderEntity> implements IShipmentOrderService {

    @Autowired
    private IInventoryService inventoryService;
    @Autowired
    private IShipmentOrderDetailService shipmentOrderDetailService;
    @Autowired
    private IAreaService areaService;

    /**
     * 入库类型Map
     */
    private static final Map<Integer, String> TYPE_MAP = new HashMap<>();
    // 初始化入库类型
    static {
        TYPE_MAP.put(ShipmentType.BORROW.getType(), ShipmentType.BORROW.getLabel());
        TYPE_MAP.put(ShipmentType.USE.getType(), ShipmentType.USE.getLabel());
    }

    @Override
    public String getNo() {
        LocalDate now = LocalDate.now();
        long max = inventoryService.getNo();
        return NoUtil.getNo("S",now, max);
    }

    private void checkAdd(ShipmentAddDTO dto) {
        if (StringUtils.isBlank(dto.getOrderNo())) {
            throw new HssBootException("单号不能为空");
        }
        if (dto.getOrderType() == null) {
            throw new HssBootException("出库类型不能为空");
        }
        if (CollectionUtils.isEmpty(dto.getItems())) {
            throw new HssBootException("物料信息不能为空");
        }
        for (ReceiptAddItem item : dto.getItems()) {
            if (StringUtils.isBlank(item.getItemId())) {
                throw new HssBootException("物料id不能为空");
            }
            if (item.getQuantity() == null) {
                throw new HssBootException("物料数量不能为空");
            }
            if (item.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                throw new HssBootException("物料数量必须大于0");
            }
            if (StringUtils.isBlank(item.getWarehouseId())) {
                throw new HssBootException("库房不能为空");
            }
            if (StringUtils.isBlank(item.getAreaId())) {
                throw new HssBootException("库区不能为空");
            }
        }

    }

    @Override
    public void add(ShipmentAddDTO dto) {
        checkAdd(dto);
        ShipmentOrderEntity entity = new ShipmentOrderEntity();
        entity.setOrderNo(dto.getOrderNo());
        Integer orderType = dto.getOrderType();
        entity.setOrderType(orderType);
        entity.setUseName(dto.getUseName());
        entity.setShipmentDate(LocalDate.now());
        entity.setRemark(dto.getRemark());
        this.save(entity);
        String receiptId = entity.getId();
        List<ShipmentOrderDetailEntity> details = dto.getItems().stream().map(r -> {
            ShipmentOrderDetailEntity item = new ShipmentOrderDetailEntity();
            item.setShipmentOrderId(receiptId);
            item.setItemId(r.getItemId());
            item.setWarehouseId(r.getWarehouseId());
            item.setAreaId(r.getAreaId());
            item.setQuantity(r.getQuantity());
            return item;
        }).collect(Collectors.toList());

        shipmentOrderDetailService.saveBatch(details);
        List<InventoryBO> bos = dto.getItems().stream().map(i -> {
            InventoryBO bo = new InventoryBO();
            bo.setFormId(receiptId);
            bo.setWarehouseId(i.getWarehouseId());
            bo.setAreaId(i.getAreaId());
            bo.setItemId(i.getItemId());
            bo.setQuantity(i.getQuantity());
            bo.setFormType(orderType);
            return bo;
        }).collect(Collectors.toList());
        inventoryService.subs(bos);


    }

    @Override
    public Page<ShipmentOrderEntity> getPage(ShipmentPageDTO dto) {
        Page<ShipmentOrderEntity> page = baseMapper.page(new Page<>(dto.getPageNo(), dto.getPageSize()), dto);
        for (ShipmentOrderEntity record : page.getRecords()) {
            record.setOrderTypeName(TYPE_MAP.get(record.getOrderType()));
        }
        return page;
    }

    @Override
    public ShipmentDetail getDetail(String id) {
        ShipmentOrderEntity byId = getById(id);
        if (byId == null) {
            return null;
        }
        ShipmentDetail detail = new ShipmentDetail();
        detail.setId(byId.getId());
        detail.setOrderNo(byId.getOrderNo());
        detail.setOrderType(TYPE_MAP.get(byId.getOrderType()));
        detail.setUseName(byId.getUseName());
        detail.setShipmentDate(byId.getShipmentDate());
        detail.setRemark(byId.getRemark());
        List<ReceiptDetailItem> items = shipmentOrderDetailService.listByOrderId(id);
        Set<String> areaIds = items.stream().map(ReceiptDetailItem::getArea).collect(Collectors.toSet());
        Map<String, String> areaMap = areaService.mapAreaIdMap(areaIds);
        for (ReceiptDetailItem item : items) {
            item.setArea(areaMap.get(item.getArea()));
        }
        detail.setItems(items);
        return detail;
    }
}
