package com.hss.modules.spare.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.modules.spare.entity.InventoryMovementEntity;
import com.hss.modules.spare.model.MovementAddDTO;
import com.hss.modules.spare.model.MovementDetail;
import com.hss.modules.spare.model.MovementPageDTO;
import com.hss.modules.spare.service.IInventoryMovementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
* @ClassDescription: 移库管理
* @JdkVersion: 1.8
* @Author: hd
* @Created: 2024/4/25 16:00
*/
@Slf4j
@Api(tags="移库管理")
@RestController
@RequestMapping("/spare/move")
public class MovementController {

    @Autowired
    private IInventoryMovementService movementEntityService;

    @ApiOperation(value = "获取单编号")
    @GetMapping(value = "/getNo")
    public Result<String> getNo() {
        String no = movementEntityService.getNo();
        return Result.ok(no);
    }

    @ApiOperation(value = "添加移库单")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody MovementAddDTO dto) {
        movementEntityService.add(dto);
        return Result.ok("成功");

    }
    @ApiOperation(value = "查询入库信息")
    @GetMapping(value = "/page")
    public Result<Page<InventoryMovementEntity>> page(MovementPageDTO dto) {
        Page<InventoryMovementEntity> page = movementEntityService.getPage(dto);
        return Result.ok(page);
    }
    @ApiOperation(value = "查询详情")
    @GetMapping(value = "/getById/{id}")
    public Result<MovementDetail> getById(@PathVariable("id") String id) {
        MovementDetail detail = movementEntityService.getDetail(id);
        return Result.ok(detail);
    }







}
