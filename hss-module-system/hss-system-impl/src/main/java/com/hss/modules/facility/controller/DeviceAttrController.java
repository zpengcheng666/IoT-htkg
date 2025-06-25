package com.hss.modules.facility.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.util.LogUtil;
import com.hss.core.common.util.OConvertUtils;
import com.hss.modules.facility.entity.DeviceAttr;
import com.hss.modules.facility.service.IDeviceAttrService;
import com.hss.modules.facility.service.IDeviceBIAttrEntityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 属性字典
 * @Author: zpc
 * @Date: 2022-12-08
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "属性字典")
@RestController
@RequestMapping("/facility/deviceAttr")
public class DeviceAttrController extends HssController<DeviceAttr, IDeviceAttrService> {
    @Autowired
    private IDeviceAttrService deviceAttrService;

    @Autowired
    IDeviceBIAttrEntityService deviceBIAttrEntityService;

    /**
     * 分页列表查询
     *
     * @param deviceAttr
     * @return
     */
    @ApiOperation(value = "属性字典-分页列表查询", notes = "属性字典-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(DeviceAttr deviceAttr,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<DeviceAttr> queryWrapper = new LambdaQueryWrapper<>();
        if (OConvertUtils.isNotEmpty(deviceAttr.getName())) {
            queryWrapper.like(DeviceAttr::getName, deviceAttr.getName());
        }
        Page<DeviceAttr> page = new Page<>(pageNo, pageSize);
        IPage<DeviceAttr> pageList = deviceAttrService.page(page, queryWrapper);

        return Result.OK(pageList);
    }

    @ApiOperation(value = "属性字典不分页", notes = "属性字典不分页")
    @GetMapping(value = "/listAttr")
    public Result<?> listAttr() {
        List<DeviceAttr> list = deviceAttrService.list();
        return Result.OK(list);
    }

    @ApiOperation(value = "通过classId查询相关属性", notes = "通过classId查询相关属性")
    @GetMapping(value = "/listByClassId")
    public Result<?> queryPageListByClassId(@RequestParam(value = "classId", required = true) String deviceId) {
        LambdaQueryWrapper<DeviceAttr> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeviceAttr::getDeviceId, deviceId);
        List<DeviceAttr> list = deviceAttrService.list(queryWrapper);
        return Result.OK(list);
    }

    /**
     * 添加设备类型的扩展属性
     *
     * @param deviceAttr 设备属性对象，包含设备类型的扩展属性信息
     * @return 返回操作结果，成功则返回"添加成功！"
     */
    @AutoLog(value = "添加设备类型的扩展属性")
    @ApiOperation(value = "添加设备类型的扩展属性", notes = "添加设备类型的扩展属性")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody DeviceAttr deviceAttr) {
        // 设置属性为未删除状态
        deviceAttr.setDeleted(0);
        // 保存设备属性到数据库
        deviceAttrService.save(deviceAttr);
        // 记录操作信息
        LogUtil.setOperate(deviceAttr.getName());
        return Result.OK("添加成功！");
    }

    /**
     * 编辑属性字典条目
     *
     * @param deviceAttr 要编辑的设备属性对象，包含属性的ID、类型、名称和单位
     * @return 返回编辑操作的结果，成功则返回"编辑成功!"的消息
     */
    @AutoLog(value = "属性字典-编辑")
    @ApiOperation(value = "属性字典-编辑", notes = "属性字典-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> edit(@RequestBody DeviceAttr deviceAttr) {
        // 通过ID获取属性条目，并更新其类型、名称和单位
        DeviceAttr byId = deviceAttrService.getById(deviceAttr.getId());
        byId.setAttrType(deviceAttr.getAttrType());
        byId.setName(deviceAttr.getName());
        byId.setUnit(deviceAttr.getUnit());
        deviceAttrService.updateById(byId);

        // 设置操作日志信息
        LogUtil.setOperate(byId.getAttrType() + "," + deviceAttr.getName() + "," + deviceAttr.getUnit());

        return Result.OK("编辑成功!");
    }


    /**
     * 通过id删除属性字典条目
     *
     * @param id 属性字典的唯一标识符
     * @return 返回一个结果对象，包含删除操作的状态信息
     */
    @AutoLog(value = "属性字典删除")
    @ApiOperation(value = "属性字典删除", notes = "根据ID删除属性字典条目")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        // 通过ID获取属性字典条目
        DeviceAttr byId = deviceAttrService.getById(id);
        // 设置操作日志的详细信息
        LogUtil.setOperate(byId.getName());
        // 根据ID删除属性字典条目
        deviceAttrService.removeById(id);

        return Result.OK("删除成功!");
    }


    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "属性字典-批量删除", notes = "属性字典-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.deviceAttrService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

}
