package com.hss.modules.facility.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.util.LogUtil;
import com.hss.core.common.util.OConvertUtils;
import com.hss.modules.facility.entity.DeviceBI;
import com.hss.modules.facility.entity.DeviceHandover;
import com.hss.modules.facility.service.IDeviceBIService;
import com.hss.modules.facility.service.IDeviceHandoverService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 交接管理
 * @Author: zpc
 * @Date: 2022-12-08
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "交接管理")
@RestController
@RequestMapping("/facility/deviceHandover")
public class DeviceHandoverController extends HssController<DeviceHandover, IDeviceHandoverService> {
    @Autowired
    private IDeviceHandoverService deviceHandoverService;

    @Autowired
    private IDeviceBIService deviceBIService;

    /**
     * 分页列表查询
     *
     * @param deviceHandover
     * @return
     */
    @ApiOperation(value = "交接管理-分页列表查询", notes = "交接管理-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(DeviceHandover deviceHandover,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<DeviceHandover> queryWrapper = new LambdaQueryWrapper<>();
        if (OConvertUtils.isNotEmpty(deviceHandover.getDevId())) {
            queryWrapper.eq(DeviceHandover::getDevId, deviceHandover.getDevId());
        }
        if (OConvertUtils.isNotEmpty(deviceHandover.getDeliverer())) {
            queryWrapper.like(DeviceHandover::getDeliverer, deviceHandover.getDeliverer());
        }
        if (OConvertUtils.isNotEmpty(deviceHandover.getReceiver())) {
            queryWrapper.like(DeviceHandover::getReceiver, deviceHandover.getReceiver());
        }
        //按照时间段查询
        if (OConvertUtils.isNotEmpty(deviceHandover.getBegin_handoverTime())) {
            queryWrapper.ge(DeviceHandover::getHandoverTime, deviceHandover.getBegin_handoverTime());
        }
        if (OConvertUtils.isNotEmpty(deviceHandover.getEnd_handoverTime())) {
            queryWrapper.le(DeviceHandover::getHandoverTime, deviceHandover.getEnd_handoverTime());
        }

        Page<DeviceHandover> page = new Page<>(pageNo,pageSize);
        IPage<DeviceHandover> pageList = deviceHandoverService.page(page, queryWrapper);

        pageList.getRecords().forEach(e -> {
            //设备类别名称
            DeviceBI type = deviceBIService.getById(e.getDevId());
            e.setDevId_disp(type == null ? "": type.getName());
        });
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param deviceHandover
     * @return
     */
    @AutoLog(value = "交接管理-添加")
    @ApiOperation(value = "交接管理-添加", notes = "交接管理-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody DeviceHandover deviceHandover) {
        deviceHandoverService.save(deviceHandover);
        DeviceHandover handover = deviceHandoverService.getById(deviceHandover.getId());
        DeviceBI byId = deviceBIService.getById(handover.getDevId());
        LogUtil.setOperate(byId.getName());
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param deviceHandover
     * @return
     */
    @AutoLog(value = "交接管理-编辑")
    @ApiOperation(value = "交接管理-编辑", notes = "交接管理-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> edit(@RequestBody DeviceHandover deviceHandover) {
        deviceHandoverService.updateById(deviceHandover);
        DeviceHandover handover = deviceHandoverService.getById(deviceHandover.getId());
        DeviceBI byId = deviceBIService.getById(handover.getDevId());
        LogUtil.setOperate(byId.getName());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "交接管理-删除")
    @ApiOperation(value = "交接管理-删除", notes = "交接管理-删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        DeviceHandover handover = deviceHandoverService.getById(id);
        DeviceBI byId = deviceBIService.getById(handover.getDevId());
        LogUtil.setOperate(byId.getName());
        deviceHandoverService.removeById(id);

        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "交接管理-批量删除")
    @ApiOperation(value = "交接管理-批量删除", notes = "交接管理-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.deviceHandoverService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "交接管理-通过id查询")
    @ApiOperation(value = "交接管理-通过id查询", notes = "交接管理-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        DeviceHandover deviceHandover = deviceHandoverService.getById(id);
        return Result.OK(deviceHandover);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param deviceHandover
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceHandover deviceHandover) {
        return super.exportXls(request, deviceHandover, DeviceHandover.class, "交接管理");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, DeviceHandover.class);
    }

}
