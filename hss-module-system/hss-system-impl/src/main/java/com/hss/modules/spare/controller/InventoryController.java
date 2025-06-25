package com.hss.modules.spare.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.modules.spare.entity.InventoryEntity;
import com.hss.modules.spare.model.*;
import com.hss.modules.spare.service.IInventoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
* @ClassDescription: 库存管理
* @JdkVersion: 1.8
* @Author: hd
* @Created: 2024/4/25 16:00
*/
@Slf4j
@Api(tags="库存管理")
@RestController
@RequestMapping("/spare/inventoryEntity")
public class InventoryController extends HssController<InventoryEntity, IInventoryService> {
	@Autowired
	private IInventoryService inventoryEntityService;

	@ApiOperation(value = "根据仓库查询")
	@GetMapping(value = "/pageByWarehouse")
	public Result<IPage<InventoryShowVO>> pageByWarehouse(InventoryShowDTO dto) {
		IPage<InventoryShowVO> page = inventoryEntityService.pageByWarehouse(dto);
		return Result.ok(page);
	}
	@ApiOperation(value = "根据库区查询")
	@GetMapping(value = "/pageByArea")
	public Result<IPage<InventoryShowVO>> pageByArea(InventoryShowDTO dto) {
		IPage<InventoryShowVO> page = inventoryEntityService.pageByArea(dto);
		return Result.ok(page);
	}
	@ApiOperation(value = "根据物料查询")
	@GetMapping(value = "/pageByItem")
	public Result<IPage<InventoryShowVO>> pageByItem(InventoryShowDTO dto) {
		IPage<InventoryShowVO> page = inventoryEntityService.pageByItem(dto);
		return Result.ok(page);
	}
	@ApiOperation(value = "库存操作记录")
	@GetMapping(value = "/opt")
	public Result<IPage<ItemOptHistory>> opt(ItemOptDTO dto) {
		IPage<ItemOptHistory> page = inventoryEntityService.historyPage(dto);
		return Result.ok(page);
	}
	@ApiOperation(value = "查询物料")
	@GetMapping(value = "/pageItem")
	public Result<IPage<PageItemVO>> pageItem(PageItemDTO dto) {
		IPage<PageItemVO> page = inventoryEntityService.pageItem(dto);
		return Result.ok(page);
	}

	


}
