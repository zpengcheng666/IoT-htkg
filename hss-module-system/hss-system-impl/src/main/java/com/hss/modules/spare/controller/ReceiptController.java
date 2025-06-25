package com.hss.modules.spare.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.modules.spare.entity.ReceiptOrderEntity;
import com.hss.modules.spare.model.ReceiptAddDTO;
import com.hss.modules.spare.model.ReceiptDetail;
import com.hss.modules.spare.model.ReceiptPageDTO;
import com.hss.modules.spare.model.SpareDict;
import com.hss.modules.spare.service.IReceiptOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
* @ClassDescription: 入库管理
* @JdkVersion: 1.8
* @Author: hd
* @Created: 2024/4/25 16:00
*/
@Slf4j
@Api(tags="入库管理")
@RestController
@RequestMapping("/spare/receipt")
public class ReceiptController {

    @Autowired
    private IReceiptOrderService receiptOrderEntityService;

    @ApiOperation(value = "获取入库单编号")
    @GetMapping(value = "/getNo")
    public Result<String> getNo() {
        String no = receiptOrderEntityService.getNo();
        return Result.ok(no);
    }

    @ApiOperation(value = "获取入库类型")
    @GetMapping(value = "/getTypes")
    public Result<List<SpareDict>> getTypes() {
        List<SpareDict> list = receiptOrderEntityService.getTypes();
        return Result.ok(list);
    }

    @ApiOperation(value = "添加入库单")
    @PostMapping(value = "/add")
    public Result<?> queryPageLis(@RequestBody ReceiptAddDTO dto) {
        receiptOrderEntityService.add(dto);
        return Result.ok("入库成功");

    }
    @ApiOperation(value = "查询入库信息")
    @GetMapping(value = "/page")
    public Result<Page<ReceiptOrderEntity>> page(ReceiptPageDTO dto) {
        Page<ReceiptOrderEntity> page = receiptOrderEntityService.getPage(dto);
        return Result.ok(page);
    }
    @ApiOperation(value = "查询详情")
    @GetMapping(value = "/getById/{id}")
    public Result<ReceiptDetail> getById(@PathVariable("id") String id) {
        ReceiptDetail detail = receiptOrderEntityService.getDetail(id);
        return Result.ok(detail);
    }







}
