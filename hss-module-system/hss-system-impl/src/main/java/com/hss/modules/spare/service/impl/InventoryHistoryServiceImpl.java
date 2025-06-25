package com.hss.modules.spare.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.spare.constant.ReceiptType;
import com.hss.modules.spare.constant.ShipmentType;
import com.hss.modules.spare.constant.SpareConstant;
import com.hss.modules.spare.entity.InventoryHistoryEntity;
import com.hss.modules.spare.mapper.InventoryHistoryEntityMapper;
import com.hss.modules.spare.model.ItemOptDTO;
import com.hss.modules.spare.model.ItemOptHistory;
import com.hss.modules.spare.model.ItemOptPageVO;
import com.hss.modules.spare.service.IAreaService;
import com.hss.modules.spare.service.IInventoryHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
public class InventoryHistoryServiceImpl extends ServiceImpl<InventoryHistoryEntityMapper, InventoryHistoryEntity> implements IInventoryHistoryService {

    private static final Map<Integer, String> TYPE_MAP = new HashMap<>();
    static {
        TYPE_MAP.put(ReceiptType.PURCHASE.getType(), ReceiptType.PURCHASE.getLabel());
        TYPE_MAP.put(ReceiptType.BORROW.getType(), ReceiptType.BORROW.getLabel());
        TYPE_MAP.put(ReceiptType.USE.getType(), ReceiptType.USE.getLabel());
        TYPE_MAP.put(ShipmentType.USE.getType(), ShipmentType.USE.getLabel());
        TYPE_MAP.put(ShipmentType.BORROW.getType(), ShipmentType.BORROW.getLabel());
        TYPE_MAP.put(SpareConstant.INVENTORY_TYPE_MOVE, "移库");
        TYPE_MAP.put(SpareConstant.INVENTORY_TYPE_CHECK, "盘库");
    }
    @Autowired
    private IAreaService areaService;

    @Override
    public IPage<ItemOptHistory> historyPage( ItemOptDTO dto) {
        IPage<ItemOptPageVO> page = baseMapper.historyPage(new Page<>(dto.getPageNo(), dto.getPageSize()), dto);
        Set<String> areaIds = page.getRecords().stream().map(ItemOptPageVO::getAreaId).collect(Collectors.toSet());
        Map<String, String> areaMap = areaService.mapAreaIdMap(areaIds);
        return page.convert(vo -> {
            ItemOptHistory itemOptHistory = new ItemOptHistory();
            itemOptHistory.setItemNo(vo.getItemNo());
            itemOptHistory.setItemName(vo.getItemName());
            itemOptHistory.setQuantity(vo.getQuantity());
            itemOptHistory.setWarehouse(areaMap.get(vo.getAreaId()));
            itemOptHistory.setOptType(TYPE_MAP.get(vo.getFormType()));
            itemOptHistory.setOptTime(vo.getCreateTime());
            return itemOptHistory;

        });
    }
}
