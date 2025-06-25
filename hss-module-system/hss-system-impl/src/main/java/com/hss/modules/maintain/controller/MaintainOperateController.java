package com.hss.modules.maintain.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.util.LogUtil;
import com.hss.core.common.util.OConvertUtils;
import com.hss.modules.facility.entity.DeviceBI;
import com.hss.modules.facility.entity.DeviceType;
import com.hss.modules.facility.service.IDeviceBIService;
import com.hss.modules.facility.service.IDeviceTypeService;
import com.hss.modules.maintain.entity.MaintainOperate;
import com.hss.modules.maintain.model.MaintainOperateWrapper;
import com.hss.modules.maintain.service.IMaintainOperateService;
import com.hss.modules.system.entity.BaseDictData;
import com.hss.modules.system.service.IBaseDictDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 保养要求表
 * @Author: zpc
 * @Date: 2022-12-27
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "保养要求表")
@RestController
@RequestMapping("/maintain/maintainOperate")
public class MaintainOperateController extends HssController<MaintainOperate, IMaintainOperateService> {
    @Autowired
    private IMaintainOperateService maintainOperateService;

    @Autowired
    private IDeviceBIService deviceBIService;

	@Autowired
	private IDeviceTypeService deviceTypeService;

    @Autowired
    private IBaseDictDataService baseDictDataService;


    /**
     * 分页列表查询
     *
     * @param maintainOperate
     * @return
     */
//    @AutoLog(value = "保养要求表-分页列表查询")
    @ApiOperation(value = "保养要求表-分页列表查询", notes = "保养要求表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(MaintainOperate maintainOperate,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<MaintainOperate> queryWrapper = new LambdaQueryWrapper<>();
        if (OConvertUtils.isNotEmpty(maintainOperate.getItemClass())) {
            queryWrapper.eq(MaintainOperate::getItemClass, maintainOperate.getItemClass());//保养类别
        }
        if (OConvertUtils.isNotEmpty(maintainOperate.getDeviceClassId())) {
            queryWrapper.eq(MaintainOperate::getDeviceClassId, maintainOperate.getDeviceClassId());//设备类别
        }

        Page<MaintainOperate> page = new Page<>(pageNo,pageSize);
        IPage<MaintainOperate> pageList = maintainOperateService.page(page, queryWrapper);

        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param maintainOperate
     * @return
     */
    @AutoLog(value = "保养要求-添加")
    @ApiOperation(value = "保养要求-添加", notes = "保养要求-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody MaintainOperate maintainOperate) {
        maintainOperateService.save(maintainOperate);
        MaintainOperate byId = maintainOperateService.getById(maintainOperate.getId());
        LogUtil.setOperate(byId.getName());
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param maintainOperate
     * @return
     */
    @AutoLog(value = "保养要求-编辑")
    @ApiOperation(value = "保养要求-编辑", notes = "保养要求-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> edit(@RequestBody MaintainOperate maintainOperate) {
        maintainOperateService.updateById(maintainOperate);
        MaintainOperate byId = maintainOperateService.getById(maintainOperate.getId());
        LogUtil.setOperate(byId.getName());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "保养要求-删除")
    @ApiOperation(value = "保养要求-删除", notes = "保养要求-删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        MaintainOperate byId = maintainOperateService.getById(id);
        LogUtil.setOperate(byId.getName());
        maintainOperateService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
//    @AutoLog(value = "保养要求表-批量删除")
    @ApiOperation(value = "保养要求表-批量删除", notes = "保养要求表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.maintainOperateService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
//    @AutoLog(value = "保养要求表-通过id查询")
    @ApiOperation(value = "保养要求表-通过id查询", notes = "保养要求表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        MaintainOperate maintainOperate = maintainOperateService.getById(id);
        return Result.OK(maintainOperate);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param maintainOperate
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, MaintainOperate maintainOperate) {
        return super.exportXls(request, maintainOperate, MaintainOperate.class, "保养要求表");
    }

    @Override
    protected void hanleDataDetail(MaintainOperate e) {
        //保养类别ID
        BaseDictData byId = baseDictDataService.getById(e.getItemClass());
        e.setItemClass_disp(byId == null ? "" : byId.getName());

        //

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
        return super.importExcel(request, response, MaintainOperate.class);
    }

//    @AutoLog(value = "保养要求列表-根据保养类型Id + deviceId查询保养要求列表")
    @ApiOperation(value = "保养要求列表-根据保养类型Id + deviceId查询保养要求列表", notes = "保养要求列表-根据保养类型Id + deviceId查询保养要求列表")
    @GetMapping(value = "/listByItemClassIdAndDeviceId")
    public Result<?> listByDeviceId(@RequestParam(name = "itemClassId", required = true) String itemClassId,
            @RequestParam(name = "deviceId", required = true) String deviceId) {
        // 查询设备类型
        DeviceBI device = this.deviceBIService.getById(deviceId);

		DeviceType deviceType = this.deviceTypeService.getById(device.getClassId());

		// 根据设备类型/保养类别 -》 查询保养要求
        LambdaQueryWrapper<MaintainOperate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MaintainOperate::getDeviceClassId, device.getClassId())
                .eq(MaintainOperate::getItemClass, itemClassId);

        List<MaintainOperate> list = this.maintainOperateService.list(queryWrapper);

        List<MaintainOperateWrapper> results = list.stream().map(item -> {
            MaintainOperateWrapper wrapper = new MaintainOperateWrapper(item);
            wrapper.setDeviceId(device.getId());
            wrapper.setDeviceName(device.getName());
            wrapper.setDeviceClassName(deviceType.getName());
            return wrapper;
        }).collect(Collectors.toList());

        return Result.OK(results);
    }
}
