package com.hss.modules.spare.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.core.common.exception.HssBootException;
import com.hss.modules.spare.entity.InventoryEntity;
import com.hss.modules.spare.entity.InventoryHistoryEntity;
import com.hss.modules.spare.mapper.InventoryMapper;
import com.hss.modules.spare.model.*;
import com.hss.modules.spare.service.IAreaService;
import com.hss.modules.spare.service.IInventoryHistoryService;
import com.hss.modules.spare.service.IInventoryService;
import com.hss.modules.spare.service.IMaterialClassificationService;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
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
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, InventoryEntity> implements IInventoryService {

    @Autowired
    private IInventoryHistoryService inventoryHistoryEntityService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IAreaService areaService;
    @Autowired
    private IMaterialClassificationService itemTypeService;

    @Override
    public long getNo() {
        return redisUtil.incr("BP_ORDER", 1L);
    }

    @Override
    public void updates(List<InventoryBO> bos) {
        for (InventoryBO bo : bos) {
            update(bo);
        }

    }

    @Override
    public void update(InventoryBO bo) {
        InventoryEntity entity = baseMapper.get(bo.getAreaId(), bo.getItemId());
        if (entity == null) {
            throw new HssBootException("库存不存在");
        }
        BigDecimal change = bo.getQuantity().subtract(entity.getQuantity());
        int update = baseMapper.update(entity.getId(), bo.getQuantity(), entity.getVersion());
        if (update != 1) {
            throw new HssBootException("数据已改变请重试");
        }
        InventoryHistoryEntity historyEntity = new InventoryHistoryEntity();
        historyEntity.setFormId(bo.getFormId());
        historyEntity.setFormType(bo.getFormType());
        historyEntity.setWarehouseId(bo.getWarehouseId());
        historyEntity.setAreaId(bo.getAreaId());
        historyEntity.setItemId(bo.getItemId());
        historyEntity.setQuantity(change);
        inventoryHistoryEntityService.save(historyEntity);
    }

    @Override
    public IPage<ItemOptHistory> historyPage(ItemOptDTO dto) {
        return inventoryHistoryEntityService.historyPage(dto);
    }

    @Override
    public IPage<PageItemVO> pageItem(PageItemDTO dto) {
        IPage<PageItemVO> page = baseMapper.pageItem(new Page<>(dto.getPageNo(), dto.getPageSize()), dto);
        Set<String> areaIds = page.getRecords().stream().map(PageItemVO::getAreaId).collect(Collectors.toSet());
        Map<String, String> areaNameMap = areaService.mapAreaIdMap(areaIds);
        for (PageItemVO itemVO : page.getRecords()) {
            itemVO.setWarehouseName(areaNameMap.get(itemVO.getAreaId()));
        }
        return page;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void adds(List<InventoryBO> bos) {
        for (InventoryBO bo : bos) {
            add(bo);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(InventoryBO bo) {
        InventoryEntity entity = baseMapper.get(bo.getAreaId(), bo.getItemId());
        BigDecimal quantity = null;
        if (entity == null) {
            entity = new InventoryEntity();
            entity.setItemId(bo.getItemId());
            entity.setQuantity(bo.getQuantity());
            entity.setWarehouseId(bo.getWarehouseId());
            entity.setAreaId(bo.getAreaId());
            save(entity);
        } else {
            quantity  = entity.getQuantity().add(bo.getQuantity());
            int update = baseMapper.update(entity.getId(), quantity, entity.getVersion());
            if (update != 1) {
                throw new HssBootException("数据已改变请重试");
            }
        }
        InventoryHistoryEntity historyEntity = new InventoryHistoryEntity();
        historyEntity.setFormId(bo.getFormId());
        historyEntity.setFormType(bo.getFormType());
        historyEntity.setWarehouseId(bo.getWarehouseId());
        historyEntity.setAreaId(bo.getAreaId());
        historyEntity.setItemId(bo.getItemId());
        historyEntity.setQuantity(bo.getQuantity());
        inventoryHistoryEntityService.save(historyEntity);

    }

    @Override
    public void subs(List<InventoryBO> bos) {
        for (InventoryBO bo : bos) {
            sub(bo);
        }

    }

    @Override
    public void sub(InventoryBO bo) {
        InventoryEntity entity = baseMapper.get(bo.getAreaId(), bo.getItemId());
        if (entity == null) {
            throw new HssBootException("库存不存在");
        }
        if (entity.getQuantity().compareTo(bo.getQuantity()) < 0) {
            throw new HssBootException("库存不足");
        }
        BigDecimal up = entity.getQuantity().subtract(bo.getQuantity());
        int update = baseMapper.update(entity.getId(), up, entity.getVersion());
        if (update != 1) {
            throw new HssBootException("数据已改变请重试");
        }
        InventoryHistoryEntity historyEntity = new InventoryHistoryEntity();
        historyEntity.setFormId(bo.getFormId());
        historyEntity.setFormType(bo.getFormType());
        historyEntity.setWarehouseId(bo.getWarehouseId());
        historyEntity.setAreaId(bo.getAreaId());
        historyEntity.setItemId(bo.getItemId());
        historyEntity.setQuantity(BigDecimal.ZERO.subtract(bo.getQuantity()));
        inventoryHistoryEntityService.save(historyEntity);

    }


    @Override
    public IPage<InventoryShowVO> pageByWarehouse(InventoryShowDTO dto) {
        IPage<InventoryShowVO> page = baseMapper.pageByWarehouse(new Page<>(dto.getPageNo(), dto.getPageSize()), dto);
        Set<String> itemTypes = page.getRecords().stream().map(InventoryShowVO::getItemType).collect(Collectors.toSet());
        Map<String, String> typeMap = itemTypeService.getNameMap(itemTypes);
        Map<String, Integer> map1 = new HashMap<>();
        for (InventoryShowVO record : page.getRecords()) {
            String key1 = record.getWarehouseId();
            Integer count1 = map1.computeIfAbsent(key1, k -> 0);
            map1.put(key1, count1 + 1);

        }
        for (InventoryShowVO vo : page.getRecords()) {
            Integer count1 = map1.get(vo.getWarehouseId());
            if (count1 != null) {
                vo.setWarehouseCount(count1);
                map1.put(vo.getWarehouseId(), 0);
            }
            vo.setItemType(typeMap.get(vo.getItemType()));
        }
        return page;
    }

    @Override
    public IPage<InventoryShowVO> pageByArea(InventoryShowDTO dto) {
        IPage<InventoryShowVO> page = baseMapper.pageByArea(new Page<>(dto.getPageNo(), dto.getPageSize()), dto);
        Set<String> itemTypes = page.getRecords().stream().map(InventoryShowVO::getItemType).collect(Collectors.toSet());
        Map<String, String> typeMap = itemTypeService.getNameMap(itemTypes);

        Map<String, Integer> map1 = new HashMap<>();
        Map<String, Integer> map2 = new HashMap<>();
        for (InventoryShowVO record : page.getRecords()) {
            String key1 = record.getWarehouseId();
            Integer count1 = map1.computeIfAbsent(key1, k -> 0);
            map1.put(key1, count1 + 1);

            String key2 = record.getAreaId();
            Integer count2 = map2.computeIfAbsent(key2, k -> 0);
            map2.put(key2, count2 + 1);

        }

        for (InventoryShowVO vo : page.getRecords()) {
            Integer count1 = map1.get(vo.getWarehouseId());
            if (count1 != null) {
                vo.setWarehouseCount(count1);
                map1.put(vo.getWarehouseId(), 0);
            }

            Integer count2 = map2.get(vo.getAreaId());
            if (count2 != null) {
                vo.setAreaCount(count2);
                map2.put(vo.getAreaId(), 0);
            }
            vo.setItemType(typeMap.get(vo.getItemType()));

        }
        return page;
    }

    @Override
    public IPage<InventoryShowVO> pageByItem(InventoryShowDTO dto) {
        IPage<InventoryShowVO> page = baseMapper.pageByItem(new Page<>(dto.getPageNo(), dto.getPageSize()), dto);
        Set<String> itemTypes = page.getRecords().stream().map(InventoryShowVO::getItemType).collect(Collectors.toSet());
        Map<String, String> typeMap = itemTypeService.getNameMap(itemTypes);

        Map<String, Integer> map1 = new HashMap<>();
        for (InventoryShowVO record : page.getRecords()) {
            String key1 = record.getItemId();
            Integer count1 = map1.computeIfAbsent(key1, k -> 0);
            map1.put(key1, count1 + 1);

        }
        for (InventoryShowVO vo : page.getRecords()) {
            Integer count1 = map1.get(vo.getItemId());
            if (count1 != null) {
                vo.setItemCount(count1);
                map1.put(vo.getItemId(), 0);
            }
            vo.setItemType(typeMap.get(vo.getItemType()));
        }
        return page;
    }
}
