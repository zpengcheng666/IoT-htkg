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
import com.hss.modules.facility.entity.DeviceBI;
import com.hss.modules.facility.entity.DeviceType;
import com.hss.modules.facility.model.DeviceRunLogDTO;
import com.hss.modules.facility.model.DeviceRunLogVO;
import com.hss.modules.facility.model.StatQualityConditionModel;
import com.hss.modules.facility.service.*;
import com.hss.modules.maintain.entity.MaintainRecord;
import com.hss.modules.maintain.entity.R_MTDeviceRecordItem;
import com.hss.modules.maintain.service.IMaintainRecordService;
import com.hss.modules.maintain.service.IR_MTDeviceRecordItemService;
import com.hss.modules.system.entity.BaseDictData;
import com.hss.modules.system.entity.BaseLocation;
import com.hss.modules.system.service.IBaseDictDataService;
import com.hss.modules.system.service.IBaseLocationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 设施设备
 * @Author: zpc
 * @Date: 2022-12-08
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "设施设备")
@RestController
@RequestMapping("/facility/deviceBI")
public class DeviceBIController extends HssController<DeviceBI, IDeviceBIService> {
    @Autowired
    private IDeviceBIService deviceBIService;

    @Autowired
    private IBaseDictDataService baseDictDataService;

    @Autowired
    private IBaseLocationService baseLocationService;

    @Autowired
    private IDeviceTypeService deviceTypeService;

    @Autowired
    private IDeviceRunLogService deviceRunLogService;

    @Autowired
    private IR_MTDeviceRecordItemService r_MTDeviceRecordItemService;

    @Autowired
    private IMaintainRecordService maintainRecordService;

    @Autowired
    private IDeviceAttrService deviceAttrService;

    /**
     * @description: 设施设备-分页
     * @author zpc
     * @date 2022/12/9 19:22
     * @version 1.0
     */
    @ApiOperation(value = "设施设备-分页列表查询", notes = "设施设备-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(DeviceBI deviceBI,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<DeviceBI> queryWrapper = getDeviceQueryWrapper(deviceBI);

        Page<DeviceBI> page = new Page<>(pageNo, pageSize);
        IPage<DeviceBI> pageList = deviceBIService.page(page, queryWrapper);

        pageList.getRecords().forEach(e -> {
            //质量状况
            BaseDictData disp2 = baseDictDataService.getById(e.getQuality());
            e.setQuality_disp(disp2 == null ? "" : disp2.getName());

            //设备类别名称
            DeviceType disp3 = deviceTypeService.getById(e.getClassId());
            e.setClassId_disp(disp3 == null ? "" : disp3.getName());
        });
        return Result.OK(pageList);
    }

    @ApiOperation(value = "查询设备相关属性", notes = "查询设备相关属性")
    @GetMapping(value = "/queryDeviceAttr")
    public Result<?> queryDeviceAttr(@RequestParam(name = "deviceId") String deviceId) {
        List<DeviceAttr> list = deviceAttrService.queryAttrsAndValsByDeviceId(deviceId);
        return Result.OK(list);
    }

    @NotNull
    private static LambdaQueryWrapper<DeviceBI> getDeviceQueryWrapper(DeviceBI deviceBI) {
        LambdaQueryWrapper<DeviceBI> queryWrapper = new LambdaQueryWrapper<>();
        if (OConvertUtils.isNotEmpty(deviceBI.getName())) {
            queryWrapper.like(DeviceBI::getName, deviceBI.getName());
        }
        if (OConvertUtils.isNotEmpty(deviceBI.getZgCode())) {
            queryWrapper.like(DeviceBI::getZgCode, deviceBI.getZgCode());
        }
        if (OConvertUtils.isNotEmpty(deviceBI.getDevModel())) {
            queryWrapper.like(DeviceBI::getDevModel, deviceBI.getDevModel());
        }
        if (OConvertUtils.isNotEmpty(deviceBI.getSite())) {
            queryWrapper.like(DeviceBI::getSite, deviceBI.getSite());
        }
        if (OConvertUtils.isNotEmpty(deviceBI.getClassId())) {
            queryWrapper.eq(DeviceBI::getClassId, deviceBI.getClassId());
        }
        return queryWrapper;
    }

    /**
     * @description: 根据设备id查询动用使用记录、运行记录列表
     * @author zpc
     * @date 2023/2/7 11:20
     * @version 1.0
     */
    @ApiOperation(value = "根据设备id查询动用使用记录、运行记录列表", notes = "根据设备id查询动用使用记录、运行记录列表")
    @GetMapping(value = "/runLogList")
    public Result<IPage<DeviceRunLogVO>> runLogList(DeviceRunLogDTO dto) {
        List<String> typeIds = null;
        if (StringUtils.isNotBlank(dto.getType())) {
            typeIds = deviceTypeService.listIdByType(dto.getType());
        }
        return Result.OK(deviceRunLogService.logPage(dto, typeIds));
    }

    /**
     * @description: 根据设备id查询对应保养任务列表
     * @author zpc
     * @date 2023/2/7 11:24
     * @version 1.0
     */
    @ApiOperation(value = "根据设备id查询对应保养任务列表", notes = "根据设备id查询对应保养任务列表")
    @GetMapping(value = "/maintainRecordList")
    public Result<?> maintainRecordList(String devId,
                                        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        //1.通过设备id从保养任务-设备关系表查询
        LambdaQueryWrapper<R_MTDeviceRecordItem> queryWrapper = new LambdaQueryWrapper<>();
        if (OConvertUtils.isNotEmpty(devId)) {
            queryWrapper.eq(R_MTDeviceRecordItem::getDeviceId, devId);
        }
        List<R_MTDeviceRecordItem> list = r_MTDeviceRecordItemService.list(queryWrapper);
        if (list.size() == 0) {
            return Result.OK();
        }

        //2.查询结果按照任务id分组生成list
        List<String> recordIds = list.stream().map(R_MTDeviceRecordItem::getRecordId).collect(Collectors.toList());

        //3.在保养任务表中按照任务ids查询列表
        LambdaQueryWrapper<MaintainRecord> recordQueryWrapper = new LambdaQueryWrapper<>();
        recordQueryWrapper.in(MaintainRecord::getId, recordIds);
        recordQueryWrapper.eq(MaintainRecord::getDeleted, 0);

        //4.分页返回新的查询结果
        Page<MaintainRecord> page = new Page<>(pageNo,pageSize);
        IPage<MaintainRecord> recordList = maintainRecordService.page(page, recordQueryWrapper);

        return Result.OK(recordList);
    }

    /**
     * @description: 质量统计
     * @author zpc
     * @date 2023/1/5 18:04
     * @version 1.0
     */
    @ApiOperation(value = "质量统计", notes = "质量统计")
    @GetMapping(value = "/statQualityCondition")
    public Result<?> total(@RequestParam(name = "classId", required = true) String classId) {
        List<StatQualityConditionModel> data = this.deviceTypeService.statQualityCondition(classId);
        return Result.OK(data);
    }

    /**
     * 添加
     *
     * @param deviceBI
     * @return
     */
    @AutoLog(value = "设施设备-添加")
    @ApiOperation(value = "设施设备-添加", notes = "设施设备-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody DeviceBI deviceBI) {
        deviceBIService.addDevice(deviceBI);
        DeviceBI byId = deviceBIService.getById(deviceBI.getId());
        LogUtil.setOperate(byId.getName());
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param deviceBI
     * @return
     */
    @AutoLog(value = "设施设备-编辑")
    @ApiOperation(value = "设施设备-编辑", notes = "设施设备-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> edit(@RequestBody DeviceBI deviceBI) {
        deviceBIService.editDevice(deviceBI);
        DeviceBI byId = deviceBIService.getById(deviceBI.getId());
        LogUtil.setOperate(byId.getName());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "设施设备-删除")
    @ApiOperation(value = "设施设备-删除", notes = "设施设备-删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        DeviceBI byId = deviceBIService.getById(id);
        LogUtil.setOperate(byId.getName());
        deviceBIService.deleteDevice(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "设施设备-批量删除", notes = "设施设备-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.deviceBIService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "设施设备-通过id查询", notes = "设施设备-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        DeviceBI deviceBI = deviceBIService.getById(id);
        return Result.OK(deviceBI);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param deviceBI
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceBI deviceBI) {
        LambdaQueryWrapper<DeviceBI> query = getDeviceQueryWrapper(deviceBI);
        return super.exportXls(request,query, deviceBI, DeviceBI.class, "设施设备");
    }

    @Override
    protected void hanleDataDetail(DeviceBI e) {
        //安装位置
        BaseLocation disp1 = baseLocationService.getById(e.getSite());
        e.setSite_disp(disp1 == null ? "" : disp1.getName());

        //设备类别名称
        DeviceType disp3 = deviceTypeService.getById(e.getClassId());
        e.setClassId_disp(disp3 == null ? "" : disp3.getName());
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
        return super.importExcel(request, response, DeviceBI.class);
    }

    @ApiOperation(value = "设备设施-批量添加/保存", notes = "设备设施-批量添加/保存")
    @RequestMapping(value = "/saveBatch", method = RequestMethod.POST)
    public Result<?> saveBatch(@RequestBody List<DeviceBI> list) {
        list = list.stream().filter(e -> StringUtils.isNotEmpty(e.getName()))
                .map(e -> {
                    // 质量状况
                    LambdaQueryWrapper<BaseDictData> dataQuery = new LambdaQueryWrapper<>();
                    dataQuery.eq(BaseDictData::getName, e.getQuality_disp());
                    BaseDictData baseData = baseDictDataService.getOne(dataQuery);
                    if (baseData != null){
                        e.setQuality(baseData.getId());
                    }

                    // 设备类别
                    LambdaQueryWrapper<DeviceType> typeQuery = new LambdaQueryWrapper<>();
                    typeQuery.eq(DeviceType::getName, e.getClassId_disp());
                    DeviceType deviceType = deviceTypeService.getOne(typeQuery);
                    if (deviceType != null){
                        e.setClassId(deviceType.getId());
                    }
                    return e;
                }).collect(Collectors.toList());
        this.deviceBIService.saveBatch(list);
        return Result.OK("导入数据成功");
    }
}
