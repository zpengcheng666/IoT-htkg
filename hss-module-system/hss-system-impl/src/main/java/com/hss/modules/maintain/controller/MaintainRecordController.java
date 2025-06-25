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
import com.hss.modules.maintain.entity.*;
import com.hss.modules.maintain.model.MaintainOperateWrapper;
import com.hss.modules.maintain.model.MaintainSubmitDTO;
import com.hss.modules.maintain.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 保养任务表
 * @Author: zpc
 * @Date: 2022-12-15
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "保养任务表")
@RestController
@RequestMapping("/maintain/maintainRecord")
public class MaintainRecordController extends HssController<MaintainRecord, IMaintainRecordService> {
    @Autowired
    private IMaintainRecordService maintainRecordService;
    @Autowired
    private IMaintainSchemasService maintainSchemasService;

    @Autowired
    private IR_MTSchemasDevItemService  schemasDevItemService;

    @Autowired
    private IDeviceBIService deviceBIService;

    @Autowired
    private IDeviceTypeService deviceTypeService;

    @Autowired
    private IR_MTDeviceRecordItemService r_MTDeviceRecordItemService;

    @Autowired
    private IMaintainOperateService maintainOperateService;

    /**
     * 分页列表查询
     *
     * @param maintainRecord
     * @return
     */
    @ApiOperation(value = "保养任务表-分页列表查询", notes = "保养任务表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(MaintainRecord maintainRecord,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

        LambdaQueryWrapper<MaintainRecord> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(MaintainRecord::getItemClass, maintainRecord.getItemClass());
        if (OConvertUtils.isNotEmpty(maintainRecord.getStatus())) {
            queryWrapper.eq(MaintainRecord::getStatus, maintainRecord.getStatus());
        }
        if (OConvertUtils.isNotEmpty(maintainRecord.getPartLeaderId())) {
            queryWrapper.eq(MaintainRecord::getPrincipalId, maintainRecord.getPrincipalId());
        }
        if (OConvertUtils.isNotEmpty(maintainRecord.getStartTime_begin())) {
            queryWrapper.ge(MaintainRecord::getStartTime, maintainRecord.getStartTime_begin());
        }
        if (OConvertUtils.isNotEmpty(maintainRecord.getStartTime_end())) {
            queryWrapper.le(MaintainRecord::getStartTime, maintainRecord.getStartTime_end());
        }

        Page<MaintainRecord> page = new Page<MaintainRecord>(pageNo, pageSize);
        IPage<MaintainRecord> pageList = maintainRecordService.page(page, queryWrapper);

        //保养任务与设备id关系表
        List<R_MTDeviceRecordItem> items = r_MTDeviceRecordItemService.list();
        Map<String, List<R_MTDeviceRecordItem>> map = items.stream().collect(Collectors.groupingBy(R_MTDeviceRecordItem::getRecordId));

        pageList.getRecords().forEach(e -> {
            List<R_MTDeviceRecordItem> deviceRecordItems = map.get(e.getId());
            if (deviceRecordItems != null && !deviceRecordItems.isEmpty()){
                e.setDeviceList(deviceRecordItems.stream().map(R_MTDeviceRecordItem::getDeviceId)
                        .collect(Collectors.toList()));
            }
        });
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param record
     * @return
     */
    @AutoLog(value = "保养任务-添加")
    @ApiOperation(value = "保养任务-添加", notes = "保养任务-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody MaintainRecord record) {
        maintainRecordService.addRecordDevice(record);

        MaintainRecord byId = maintainRecordService.getById(record.getId());
        MaintainSchemas maintainSchemas = maintainSchemasService.getById(byId.getSchemaId());
        LogUtil.setOperate(maintainSchemas.getSchemasName());
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param maintainRecord
     * @return
     */
    @AutoLog(value = "保养任务-编辑")
    @ApiOperation(value = "保养任务表-编辑", notes = "保养任务表-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> edit(@RequestBody MaintainRecord maintainRecord) {
        maintainRecordService.updateRecordDevice(maintainRecord);

        MaintainRecord byId = maintainRecordService.getById(maintainRecord.getId());
        MaintainSchemas maintainSchemas = maintainSchemasService.getById(byId.getSchemaId());
        LogUtil.setOperate(maintainSchemas.getSchemasName());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "保养任务-删除")
    @ApiOperation(value = "保养任务表-通过id删除", notes = "保养任务表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        MaintainRecord byId = maintainRecordService.getById(id);
        LogUtil.setOperate(byId.getSchemaName());
        maintainRecordService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "保养任务表-批量删除", notes = "保养任务表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.maintainRecordService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "保养任务表-通过id查询", notes = "保养任务表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<MaintainRecord> queryById(@RequestParam(name = "id", required = true) String id) {
        MaintainRecord entity = maintainRecordService.getById(id);

        LambdaQueryWrapper<R_MTDeviceRecordItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(R_MTDeviceRecordItem::getRecordId, id);
        List<R_MTDeviceRecordItem> itemList = r_MTDeviceRecordItemService.list(queryWrapper);
        if (!CollectionUtils.isEmpty(itemList)){

            List<MaintainOperateWrapper> operates = new ArrayList<>();

            List<String> deviceIds = itemList.stream().map(R_MTDeviceRecordItem::getDeviceId)
                    .collect(Collectors.toList());

            LambdaQueryWrapper<DeviceBI> deviceQueryWrapper = new LambdaQueryWrapper<>();
            deviceQueryWrapper.in(DeviceBI::getId, deviceIds);
            List<DeviceBI> devices = this.deviceBIService.list(deviceQueryWrapper);

            // 设置 deviceType 和 保养要求列表
            devices.forEach(e -> {

                DeviceType deviceType = this.deviceTypeService.getById(e.getClassId());

                // 根据设备类型/保养类别 -》 查询保养要求
                LambdaQueryWrapper<MaintainOperate> mtOperateQueryWrapper = new LambdaQueryWrapper<>();
                mtOperateQueryWrapper.eq(MaintainOperate::getDeviceClassId, e.getClassId())
                        .eq(MaintainOperate::getItemClass, entity.getItemClass());

                List<MaintainOperate> list = this.maintainOperateService.list(mtOperateQueryWrapper);
                if (list != null){
                    list.forEach(item -> {
                        MaintainOperateWrapper wrapper = new MaintainOperateWrapper(item);
                        wrapper.setDeviceId(e.getId());
                        wrapper.setDeviceName(e.getName());
                        wrapper.setDeviceClassName(deviceType.getName());
                        operates.add(wrapper);
                    });
                }
            });

            entity.setOperates(operates);
        }

        return Result.OK(entity);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param maintainRecord
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, MaintainRecord maintainRecord) {
        return super.exportXls(request, maintainRecord, MaintainRecord.class, "保养任务表");
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
        return super.importExcel(request, response, MaintainRecord.class);
    }

    @ApiOperation(value = "根据保养类别和保养方案，获取设备列表", notes = "根据保养类别和保养方案，获取设备列表")
    @GetMapping(value = "/listDevicesByMTClassAndMTSchema")
    public Result<?> listDevicesByMTClassAndMTSchema(String mtClassId, String mtSchemaId) {

        // 1. 根据mtClassId + mtSchemaId 获取设备类型列表
        LambdaQueryWrapper<R_MTSchemasDevItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(R_MTSchemasDevItem::getItemClass, mtClassId);
        queryWrapper.eq(R_MTSchemasDevItem::getSchemasId, mtSchemaId);
        List<String> deviceTypeList = this.schemasDevItemService.list(queryWrapper).stream().map(e -> e.getDeviceClassId()).collect(Collectors.toList());
        if (deviceTypeList.isEmpty()){
            return Result.OK(new ArrayList<>());
        }

        // 2. 根据设备类型列表、获取设备列表
        LambdaQueryWrapper<DeviceBI> deviceQueryWrapper = new LambdaQueryWrapper<>();
        deviceQueryWrapper.in(DeviceBI::getClassId, deviceTypeList);

        List<DeviceBI> deviceList = deviceBIService.listDetailsByTypeList(deviceTypeList);

        return Result.OK(deviceList);
    }

    @ApiOperation(value = "保养任务表-修改状态", notes = "保养任务表-修改状态")
    @PostMapping(value = "/editStatus")
    public Result<?> editStatus(@RequestBody MaintainRecord model) {
        MaintainRecord entity = this.maintainRecordService.getById(model.getId());
        entity.setStatus(model.getStatus());
        this.maintainRecordService.saveOrUpdate(entity);
        return Result.OK("修改状态成功！");
    }

    @AutoLog(value = "签发保养任务")
    @ApiOperation(value = "签发保养任务", notes = "签发保养任务")
    @PostMapping(value = "/confirm")
    public Result<?> confirm(@RequestParam("id") @ApiParam(value = "id",required = true) String id) {
        this.maintainRecordService.confirm(id);
        return Result.OK("签发成功");
    }

    @AutoLog(value = "执行保养任务")
    @ApiOperation(value = "执行保养任务", notes = "执行保养任务")
    @PostMapping(value = "/act")
    public Result<?> act(@RequestParam("id") @ApiParam(value = "id",required = true) String id) {
        this.maintainRecordService.act(id);
        return Result.OK("执行成功");
    }

    @AutoLog(value = "提交保养任务")
    @ApiOperation(value = "提交保养任务", notes = "提交保养任务")
    @PostMapping(value = "/submit")
    public Result<?> submit(@RequestBody MaintainSubmitDTO dto) {
        this.maintainRecordService.submit(dto);
        return Result.OK("提交成功");
    }

    @AutoLog(value = "审核保养任务")
    @ApiOperation(value = "审核保养任务", notes = "审核保养任务")
    @PostMapping(value = "/complete")
    public Result<?> complete(@RequestParam("id") @ApiParam(value = "id",required = true) String id) {
        this.maintainRecordService.complete(id);
        return Result.OK("审核成功");
    }
}
