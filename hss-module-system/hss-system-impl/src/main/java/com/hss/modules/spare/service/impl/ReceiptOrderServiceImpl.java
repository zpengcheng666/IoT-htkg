package com.hss.modules.spare.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.core.common.exception.HssBootException;
import com.hss.modules.spare.constant.NoUtil;
import com.hss.modules.spare.constant.ReceiptType;
import com.hss.modules.spare.entity.Carrier;
import com.hss.modules.spare.entity.ReceiptOrderDetailEntity;
import com.hss.modules.spare.entity.ReceiptOrderEntity;
import com.hss.modules.spare.mapper.ReceiptOrderMapper;
import com.hss.modules.spare.model.*;
import com.hss.modules.spare.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
* @ClassDescription:
* @JdkVersion: 1.8
* @Author: hd
* @Created: 2024/4/25 16:03
*/
@Service
public class ReceiptOrderServiceImpl extends ServiceImpl<ReceiptOrderMapper, ReceiptOrderEntity> implements IReceiptOrderService {

    @Autowired
    private IReceiptOrderDetailService orderDetailService;
    @Autowired
    private IInventoryService inventoryService;
    @Autowired
    private ICarrierService carrierService;
    @Autowired
    private IAreaService areaService;

    /**
     * 入库类型Map
     */
    private static final Map<Integer, String> TYPE_MAP = new HashMap<>();
    // 初始化入库类型
    static {
        TYPE_MAP.put(ReceiptType.PURCHASE.getType(), ReceiptType.PURCHASE.getLabel());
        TYPE_MAP.put(ReceiptType.BORROW.getType(), ReceiptType.BORROW.getLabel());
        TYPE_MAP.put(ReceiptType.USE.getType(), ReceiptType.USE.getLabel());
    }

    @Override
    public String getNo() {
        LocalDate now = LocalDate.now();
        long max = inventoryService.getNo();
        return NoUtil.getNo("R",now, max);
    }

    @Override
    public List<SpareDict> getTypes() {
        List<SpareDict> list = new ArrayList<>(3);
        list.add(new SpareDict(ReceiptType.PURCHASE.getType(), ReceiptType.PURCHASE.getLabel()));
        list.add(new SpareDict(ReceiptType.BORROW.getType(), ReceiptType.BORROW.getLabel()));
        list.add(new SpareDict(ReceiptType.USE.getType(), ReceiptType.USE.getLabel()));
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(ReceiptAddDTO dto) {
        checkAdd(dto);
        ReceiptOrderEntity receipt = new ReceiptOrderEntity();
        receipt.setOrderNo(dto.getOrderNo());
        Integer orderType = dto.getOrderType();
        receipt.setOrderType(orderType);
        receipt.setSupplierId(dto.getSupplierId());
        receipt.setUseName(dto.getUseName());
        receipt.setReceiptDate(LocalDate.now());
        receipt.setRemark(dto.getRemark());
        this.save(receipt);
        String receiptId = receipt.getId();
        List<ReceiptOrderDetailEntity> details = dto.getItems().stream().map(r -> {
            ReceiptOrderDetailEntity item = new ReceiptOrderDetailEntity();
            item.setReceiptOrderId(receiptId);
            item.setItemId(r.getItemId());
            item.setWarehouseId(r.getWarehouseId());
            item.setAreaId(r.getAreaId());
            item.setQuantity(r.getQuantity());
            return item;
        }).collect(Collectors.toList());

        orderDetailService.saveBatch(details);
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
        inventoryService.adds(bos);


    }

    @Override
    public Page<ReceiptOrderEntity> getPage(ReceiptPageDTO dto) {
        Page<ReceiptOrderEntity> page = baseMapper.page(new Page<>(dto.getPageNo(), dto.getPageSize()), dto);
        Set<String> supplierIds = page.getRecords().stream().map(ReceiptOrderEntity::getSupplierId).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
        Map<String,String> supplierNameMap = carrierService.nameMap(supplierIds);
        for (ReceiptOrderEntity record : page.getRecords()) {
            String supplierId = record.getSupplierId();
            if (StringUtils.isNotBlank(supplierId)) {
                String name = supplierNameMap.get(supplierId);
                if (StringUtils.isNotBlank(name)) {
                    record.setSupplierId(name);
                }
            }
            record.setOrderTypeName(TYPE_MAP.get(record.getOrderType()));

        }

        return page;
    }

    @Override
    public ReceiptDetail getDetail(String id) {
        ReceiptOrderEntity byId = getById(id);
        if (byId == null) {
            return null;
        }
        ReceiptDetail detail = new ReceiptDetail();
        detail.setId(byId.getId());
        detail.setOrderNo(byId.getOrderNo());
        detail.setOrderType(TYPE_MAP.get(byId.getOrderType()));
        if (StringUtils.isNotBlank(byId.getSupplierId())) {
            Carrier supplier = carrierService.getById(byId.getSupplierId());
            if (supplier != null) {
                detail.setSupplier(supplier.getCarrierName());
            }
        }
        detail.setUseName(byId.getUseName());
        detail.setReceiptDate(byId.getReceiptDate());
        detail.setRemark(byId.getRemark());
        List<ReceiptDetailItem> items = orderDetailService.listByOrderId(id);
        Set<String> areaIds = items.stream().map(ReceiptDetailItem::getArea).collect(Collectors.toSet());
        Map<String, String> areaMap = areaService.mapAreaIdMap(areaIds);
        for (ReceiptDetailItem item : items) {
            item.setArea(areaMap.get(item.getArea()));
        }
        detail.setItems(items);

        return detail;
    }

    private void checkAdd(ReceiptAddDTO dto) {
        if (StringUtils.isBlank(dto.getOrderNo())) {
            throw new HssBootException("单号不能为空");
        }
        if (dto.getOrderType() == null) {
            throw new HssBootException("入库类型不能为空");
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
}
