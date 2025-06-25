package com.hss.modules.spare.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.modules.spare.entity.InventoryCheckEntity;
import com.hss.modules.spare.model.CheckAddDTO;
import com.hss.modules.spare.model.CheckDetail;
import com.hss.modules.spare.model.CheckPageDTO;
import com.hss.modules.spare.service.IInventoryCheckService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
* @ClassDescription: 盘库管理
* @JdkVersion: 1.8
* @Author: hd
* @Created: 2024/4/25 16:00
*/
@Slf4j
@Api(tags="盘库管理")
@RestController
@RequestMapping("/spare/check")
public class CheckController {

    @Autowired
    private IInventoryCheckService checkEntityService;

    @ApiOperation(value = "获取单编号")
    @GetMapping(value = "/getNo")
    public Result<String> getNo() {
        String no = checkEntityService.getNo();
        return Result.ok(no);
    }

    @ApiOperation(value = "添加盘库单")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody CheckAddDTO dto) {
        checkEntityService.add(dto);
        return Result.ok("盘库成功");

    }
    @ApiOperation(value = "查询入库信息")
    @GetMapping(value = "/page")
    public Result<Page<InventoryCheckEntity>> page(CheckPageDTO dto) {
        Page<InventoryCheckEntity> page = checkEntityService.getPage(dto);
        return Result.ok(page);
    }
    @ApiOperation(value = "查询详情")
    @GetMapping(value = "/getById/{id}")
    public Result<CheckDetail> getById(@PathVariable("id") String id) {
        CheckDetail detail = checkEntityService.getDetail(id);
        return Result.ok(detail);
    }







}
