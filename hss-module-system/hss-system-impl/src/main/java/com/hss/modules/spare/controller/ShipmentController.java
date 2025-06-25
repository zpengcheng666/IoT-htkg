package com.hss.modules.spare.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.modules.spare.entity.ShipmentOrderEntity;
import com.hss.modules.spare.model.ShipmentAddDTO;
import com.hss.modules.spare.model.ShipmentDetail;
import com.hss.modules.spare.model.ShipmentPageDTO;
import com.hss.modules.spare.service.IShipmentOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
* @ClassDescription: 出库管理
* @JdkVersion: 1.8
* @Author: hd
* @Created: 2024/4/25 16:00
*/
@Slf4j
@Api(tags="出库管理")
@RestController
@RequestMapping("/spare/shipment")
public class ShipmentController {

    @Autowired
    private IShipmentOrderService shipmentOrderEntityService;

    @ApiOperation(value = "获取单编号")
    @GetMapping(value = "/getNo")
    public Result<String> getNo() {
        String no = shipmentOrderEntityService.getNo();
        return Result.ok(no);
    }

    @ApiOperation(value = "添加出库单")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ShipmentAddDTO dto) {
        shipmentOrderEntityService.add(dto);
        return Result.ok("出库成功");

    }
    @ApiOperation(value = "查询入库信息")
    @GetMapping(value = "/page")
    public Result<Page<ShipmentOrderEntity>> page(ShipmentPageDTO dto) {
        Page<ShipmentOrderEntity> page = shipmentOrderEntityService.getPage(dto);
        return Result.ok(page);
    }
    @ApiOperation(value = "查询详情")
    @GetMapping(value = "/getById/{id}")
    public Result<ShipmentDetail> getById(@PathVariable("id") String id) {
        ShipmentDetail detail = shipmentOrderEntityService.getDetail(id);
        return Result.ok(detail);
    }







}
