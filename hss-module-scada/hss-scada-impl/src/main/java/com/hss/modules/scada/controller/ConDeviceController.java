package com.hss.modules.scada.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.core.common.util.OConvertUtils;
import com.hss.modules.devicetype.entity.DeviceTypeManagement;
import com.hss.modules.devicetype.service.IDeviceTypeManagementService;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.entity.ConSheBei;
import com.hss.modules.scada.model.*;
import com.hss.modules.scada.service.IConDeviceAttributeService;
import com.hss.modules.scada.service.IConSheBeiService;
import com.hss.modules.scada.service.IDeviceRunRecordService;
import com.hss.modules.store.model.vo.LineStateVO;
import com.hss.modules.store.model.vo.PieStateVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @description: 场景设备，列表查询，增删改查、关联属性、执行动作、变量关联、存储策略、点位关联、设备类型以及在线统计等
* @author zpc
* @date 2024/3/19 14:22
* @version 1.0
*/
@Slf4j
@Api(tags = "场景设备")
@RestController
@RequestMapping("/scada/conSheBei")
@CrossOrigin
public class ConDeviceController extends HssController<ConSheBei, IConSheBeiService> {
    @Autowired
    private IConSheBeiService conSheBeiService;

    @Autowired
    private IConDeviceAttributeService conDeviceAttributeService;

    @Autowired
    private IDeviceRunRecordService deviceRunRecordService;

    @Autowired
    private IDeviceTypeManagementService deviceTypeManagementService;

    /**
     * 根据场景id、设备名称查询相关设备列表
     * @return
     */
    @ApiOperation(value = "设备表-分页列表查询", notes = "设备表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(
            @ApiParam("场景id") @RequestParam(name = "sceneId", required = false) String sceneId,
            @ApiParam("设备类型id") @RequestParam(name = "deviceTypeId", required = false) String deviceTypeId,
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @ApiParam("设备名称") @RequestParam(name = "name", required = false) String name) {
        Page<ConSheBei> page = new Page<>(pageNo, pageSize);
        IPage<ConSheBei> pageList = conSheBeiService.page(page, sceneId, name,deviceTypeId);
        return Result.OK(pageList);
    }

    @ApiOperation(value = "设备列表分页列表查询", notes = "设备列表分页列表查询")
    @GetMapping(value = "/deviceList")
    public Result<IPage<DeviceListVO>> deviceList(DeviceListDTO dto) {
        Page<DeviceListVO> page = new Page<>(dto.getPageNo(), dto.getPageSize());
        //查询设备列表
        IPage<DeviceListVO> pageList = conSheBeiService.deviceList(page, dto);
        return Result.OK(pageList);
    }

    @ApiOperation(value = "设备列表分页列表查询", notes = "设备列表分页列表查询")
    @GetMapping(value = "/deviceList2")
    public Result<IPage<DeviceRunRecordVo>> deviceList2(ConSheBei dto,
                                @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                HttpServletRequest req) {
        Page<ConSheBei> page = new Page<>(pageNo, pageSize);
        QueryWrapper<ConSheBei> queryWrapper = QueryGenerator.initQueryWrapper(dto, req.getParameterMap());
        queryWrapper.orderByDesc("CREATED_TIME");
        IPage<ConSheBei> pageList = conSheBeiService.page(page, queryWrapper);

        IPage<DeviceRunRecordVo> pageList2 = pageList.convert(e -> {
            DeviceRunRecordVo entity = new DeviceRunRecordVo();
            entity.setId(e.getId());
            entity.setName(e.getName());
            entity.setCode(e.getCode());
            entity.setType(e.getType());

            //查询最后一次的启动时间和最后一次的停止时间
            List<Map<String, Object>> times = deviceRunRecordService.lastTime(e.getId());
            times.forEach(x -> {
                Date startTime;
                Date stopTime;
                Integer state = (Integer) x.get("DEVICE_STATE");
                if (1 == state) {
                    startTime = (Date) x.get("RECORD_TIME");
                    entity.setLastStartTime(startTime);
                } else {
                    stopTime = (Date) x.get("RECORD_TIME");
                    entity.setLastStopTime(stopTime);
                }
            });

            //根据DeviceId获取总的运行时长
            Long sumTime = this.deviceRunRecordService.sumTime(e.getId());
            if (sumTime == null) {
                sumTime = 0L;
            }
            Long lastSumTime;
            if (entity.getLastStopTime() != null && entity.getLastStartTime() != null && entity.getLastStopTime().after(entity.getLastStartTime())) {
                lastSumTime = (entity.getLastStopTime().getTime() - entity.getLastStartTime().getTime()) / 1000;
            } else if (entity.getLastStopTime() != null && entity.getLastStartTime() != null && entity.getLastStopTime().before(entity.getLastStartTime())) {
                lastSumTime = (new Date().getTime() - entity.getLastStartTime().getTime()) / 1000;
                sumTime += lastSumTime;
            } else if (entity.getLastStartTime() != null) {
                lastSumTime = (new Date().getTime() - entity.getLastStartTime().getTime()) / 1000;
                sumTime += lastSumTime;
            } else {
                lastSumTime = 0L;
            }
            entity.setLastSumTime(lastSumTime);
            entity.setTotalSumTime(sumTime);
            return entity;
        });
        return Result.OK(pageList2);
    }

    @ApiOperation(value = "根据设备类型获取设备", notes = "根据设备类型获取设备")
    @GetMapping(value = "/listByTypeId")
    public Result<List<ConSheBei>> listByDeviceTypeId(@ApiParam(value = "类型id", required = true) @RequestParam("typeId") String typeId) {
        //根据设备类型查询
        List<ConSheBei> list = conSheBeiService.listByDeviceTypeId(typeId);
        return Result.OK(list);
    }

    @ApiOperation(value = "设备表-点位关联", notes = "设备表-点位关联")
    @GetMapping(value = "/listPoint")
    public Result<?> listPoint(
            @ApiParam("场景id") @RequestParam(name = "sceneId", required = false) String sceneId,
            @ApiParam("设备类型id") @RequestParam(name = "deviceTypeId", required = false) String deviceTypeId,
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @ApiParam("设备名称") @RequestParam(name = "name", required = false) String name) {
        Page<ConSheBei> page = new Page<>(pageNo, pageSize);
        //点位关联查询
        IPage<ConSheBei> pageList = conSheBeiService.listPoint(page, sceneId, name,deviceTypeId);
        return Result.OK(pageList);
    }

    @ApiOperation(value = "设备表-变量关联", notes = "设备表-变量关联")
    @GetMapping(value = "/listVariable")
    public Result<?> listVariable(
            @ApiParam(value = "场景id", required = true) @RequestParam(name = "sceneId") String sceneId,
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @ApiParam("设备名称") @RequestParam(name = "name", required = false) String name) {
        Page<ConSheBei> page = new Page<>(pageNo, pageSize);
        //变量关联查询
        IPage<ConSheBei> pageList = conSheBeiService.listVariable(page, sceneId, name);
        return Result.OK(pageList);
    }

    @ApiOperation(value = "查询关联的点位", notes = "查询关联的点位")
    @GetMapping("/listDeviceAttrRelation")
    public Result<List<DeviceAttrPointRelationVO>> listDeviceAttrRelation(@ApiParam(value = "设备id", required = true) String deviceId) {
        //查询关联的点位
        List<DeviceAttrPointRelationVO> list = conSheBeiService.listDeviceAttrRelation(deviceId);
        return Result.OK(list);
    }

    @ApiOperation(value = "查询关联的变量", notes = "查询关联的变量")
    @GetMapping("/listDeviceAttrVariable")
    public Result<List<DeviceAttrRelation>> listDeviceAttrVariable(@ApiParam(value = "设备id", required = true) String deviceId) {
        //查询关联的变量
        List<DeviceAttrRelation> list = conSheBeiService.listDeviceAttrVariable(deviceId);
        return Result.OK(list);
    }

    @ApiOperation(value = "获取设备动作列表", notes = "获取设备动作列表")
    @GetMapping("/listActByDeviceId")
    public Result<List<DeviceAttrAct>> listActByDeviceId(@ApiParam(value = "deviceId", required = true) String deviceId) {
        //获取动作列表
        List<DeviceAttrAct> list = conSheBeiService.listActByDeviceId(deviceId);
        return Result.OK(list);
    }

    @ApiOperation(value = "获取数据表格显示的属性", notes = "获取数据表格显示的属性")
    @GetMapping("/lisDataTableByDeviceId")
    public Result<List<DeviceAttrDataTable>> lisDataTableByDeviceId(@ApiParam(value = "deviceId", required = true) String deviceId) {
        //获取数据表格显示的属性
        List<DeviceAttrDataTable> list = conSheBeiService.lisDataTableByDeviceId(deviceId);
        return Result.OK(list);
    }

    @ApiOperation(value = "获取数据列表显示的属性", notes = "获取数据列表显示的属性")
    @GetMapping("/lisDataListByDeviceId")
    public Result<List<DeviceAttrDataTable>> lisDataListByDeviceId(@ApiParam(value = "deviceId", required = true) String deviceId) {
        //获取数据列表属性信息
        List<DeviceAttrDataTable> list = conSheBeiService.lisDataListByDeviceId(deviceId);
        return Result.OK(list);
    }

    @ApiOperation(value = "获取设备要显示的趋势属性", notes = "获取设备要显示的趋势属性")
    @GetMapping("/listDeviceTendencyAttr")
    public Result<List<ConDeviceAttribute>> listDeviceTendencyAttr(@ApiParam(value = "deviceId", required = true) String deviceId) {
        //获取设备需要展示的趋势曲线
        List<ConDeviceAttribute> list = conSheBeiService.listDeviceTendencyAttr(deviceId);
        return Result.OK(list);
    }

    @ApiOperation(value = "设备中门的下拉框", notes = "设备中门的下拉框")
    @GetMapping(value = "/conSheBeiDoorOptiongs")
    public Result<List<ConSheBeiOptions>> conSheBeiDoorOptiongs() {
        //查询门设备
        List<ConSheBeiOptions> list = conSheBeiService.listDoorOption();
        return Result.OK(list);
    }

    @ApiOperation(value = "设备中门安检门的下拉框", notes = "设备中门安检门的下拉框")
    @GetMapping(value = "/checkDoor")
    public Result<List<ConSheBeiOptions>> checkDoor(ConSheBeiOptions optionModel) {
        //查询安检门设备
        List<ConSheBeiOptions> list = conSheBeiService.listCheckDoorOption();
        return Result.OK(list);
    }

    @ApiOperation(value = "根据设备id查询相关属性", notes = "根据设备id查询相关属性")
    @GetMapping(value = "/devIdsQueryAttrs")
    public Result<?> devIdsQueryAttrs(String devIds) {
        //1.通过设备类型id获取设备
        LambdaQueryWrapper<ConDeviceAttribute> queryWrapper = new LambdaQueryWrapper<>();
        if (OConvertUtils.isNotEmpty(devIds)) {
            String[] split = devIds.split(",");
            List<String> stringList = Arrays.asList(split);
            queryWrapper.in(ConDeviceAttribute::getDeviceId, stringList);
        }
        List<ConDeviceAttribute> list = conDeviceAttributeService.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            return new Result<>();
        }
        List<ConSheBeiOptions> collect = list.stream().map(k -> {
            ConSheBeiOptions temp = new ConSheBeiOptions();
            //设备id
            temp.setId(k.getId());
            //设备名称
            temp.setName(k.getName());
            return temp;
        }).collect(Collectors.toList());
        return Result.OK(collect);
    }

    /**
     * 添加设备
     *
     * @param conSheBei
     * @return
     */
    @AutoLog(value = "添加设备")
    @ApiOperation(value = "设备表-添加", notes = "设备表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ConSheBei conSheBei) {
        conSheBeiService.add(conSheBei);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑设备信息
     *
     * @param conSheBei
     * @return
     */
    @AutoLog(value = "编辑设备")
    @ApiOperation(value = "设备表-编辑", notes = "设备表-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> edit(@RequestBody ConSheBei conSheBei) {
        conSheBeiService.edit(conSheBei);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除设备")
    @ApiOperation(value = "设备表-通过id删除", notes = "设备表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        conSheBeiService.delete(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "批量删除设备")
    @ApiOperation(value = "设备表-批量删除", notes = "设备表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.conSheBeiService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "设备表-通过id查询", notes = "设备表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        ConSheBei conSheBei = conSheBeiService.getById(id);
        if (StringUtils.isNotEmpty(conSheBei.getLocationId())) {
            conSheBei.setLocationName(StringUtils.trim(conSheBei.getLocationId()));
        }
        return Result.OK(conSheBei);
    }


    @AutoLog(value = "保存变量关联")
    @ApiOperation(value = "保存变量关联", notes = "保存变量关联")
    @PostMapping("/saveDeviceAttrRelation")
    public Result<?> saveDeviceAttrRelation(@RequestBody DeviceAttrRelationSave deviceAttrRelationSave) {
        //保存属性关联关联
        conSheBeiService.saveDeviceAttrRelation(deviceAttrRelationSave);
        return Result.OK("成功！");
    }

    @AutoLog(value = "保存点位关联")
    @ApiOperation(value = "保存点位关联", notes = "保存点位关联")
    @PostMapping("/saveDeviceAttrPointRelation")
    public Result<?> saveDeviceAttrPointRelation(@RequestBody DeviceAttrPointRelationDTO dto) {
        //保存点位关联
        conSheBeiService.saveDeviceAttrPointRelation(dto);
        return Result.OK("成功！");
    }

    @AutoLog(value = "保存配置信息")
    @ApiOperation(value = "保存配置信息", notes = "保存配置信息")
    @PostMapping("/saveDeviceAttrConfig")
    public Result<?> saveDeviceAttrConfig(@RequestBody DeviceAttrConfigDTO dto) {
        //保存配置信息
        conSheBeiService.saveDeviceAttrConfig(dto);
        return Result.OK("成功！");
    }

    @AutoLog(value = "执行动作")
    @ApiOperation(value = "执行动作", notes = "执行动作")
    @PostMapping("/execute")
    public Result<?> execute(@RequestBody DeviceExecuteDTO dto) {
        //设备动作执行
        conSheBeiService.execute(dto);
        return Result.OK();
    }

    @ApiOperation(value = "查询设备属性列表", notes = "查询设备属性列表")
    @GetMapping("/listDeviceAttrByDeviceId")
    public Result<List<ConDeviceAttribute>> listDeviceAttrByDeviceId(@ApiParam(value = "设备id", required = true) @RequestParam("id") String id) {
        List<ConDeviceAttribute> list = conSheBeiService.listDeviceAttrByDeviceId(id);
        return Result.OK(list);
    }

    @ApiOperation(value = "查询场景下的所有广播设备", notes = "查询场景下的所有广播设备")
    @GetMapping("/listPublishBySceneId")
    public Result<List<ConSheBei>> listPublishBySceneId(@ApiParam(value = "场景id", required = true) @RequestParam("sceneId") String sceneId) {
        List<ConSheBei> list = conSheBeiService.listPublishBySceneId(sceneId);
        return Result.OK(list);
    }

    @ApiOperation(value = "获取摄像机信息", notes = "获取摄像机信息")
    @GetMapping("/getCameraByDeviceId")
    public Result<String> getCameraByDeviceId(@ApiParam(value = "deviceId", required = true) @RequestParam(value = "deviceId") String deviceId) {
        String info = conSheBeiService.getCameraByDeviceId(deviceId);
        return Result.OK(info);
    }


    @ApiOperation(value = "根绝设备ids获取有存储策略的属性", notes = "根绝设备ids获取有存储策略的属性")
    @GetMapping(value = "/listStoreAttrByDeviceIds")
    public Result<List<ConSheBeiOptions>> listStoreAttrByDeviceIds(@ApiParam("设备ids用,分割") @RequestParam("devIds") String devIds) {
        List<ConSheBeiOptions> list = conDeviceAttributeService.listStoreAttrByDeviceIds(devIds);
        return Result.OK(list);
    }

    @ApiOperation(value = "根绝设备ids获取有报警策略的属性", notes = "根绝设备ids获取有报警策略的属性")
    @GetMapping(value = "/listAlarmAttrByDeviceIds")
    public Result<List<ConSheBeiOptions>> listAlarmAttrByDeviceIds(@ApiParam("设备ids用,分割") @RequestParam("devIds") String devIds) {
        List<ConSheBeiOptions> list = conDeviceAttributeService.listAlarmAttrByDeviceIds(devIds);
        return Result.OK(list);
    }

    @Deprecated
    @ApiOperation(value = "搜索设备属性中绑定了点位redis没有绑定的点位", notes = "搜索设备属性中绑定了点位redis没有绑定的点位")
    @GetMapping("/listAttrNotRedis")
    public Result<List<ConDeviceAttribute>> listAttrNotRedis() {
        List<ConDeviceAttribute> list = conSheBeiService.listAttrNotRedis();
        return Result.OK(list);
    }


    @ApiOperation(value = "根据类型类型查找", notes = "根据类型类型查找")
    @GetMapping(value = "/listByType")
    public Result<List<ConSheBei>> listByTypeCode(@ApiParam(value = "typeCode", required = true) @RequestParam("typeCode") String typeCode) {
        List<ConSheBei> list = conSheBeiService.listByTypeCode(typeCode);
        return Result.OK(list);
    }

    @ApiOperation(value = "获取控制系统下的风机列表", notes = "获取控制系统下的风机列表")
    @GetMapping(value = "/listFanBySystemDeviceId")
    public Result<List<ConSheBei>> listFanBySystemDeviceId(@ApiParam(value = "systemDeviceId", required = true) @RequestParam("systemDeviceId") String systemDeviceId) {
        //获取控制系统下的风机列表
        List<ConSheBei> list = conSheBeiService.listFanBySystemDeviceId(systemDeviceId);
        return Result.OK(list);
    }

    @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
    @ApiOperation(value = "list所有三色灯光设备", notes = "list所有三色灯光设备")
    @GetMapping(value = "/list3SGOptions")
    public Result<?> list3SGOptions() {
        LambdaQueryWrapper<ConSheBei> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(ConSheBei::getType, "三色");
        List<ConSheBei> list = conSheBeiService.list(queryWrapper);
        List<ConSheBeiOptions> listDoor = list.stream().map(e -> {
            ConSheBeiOptions model = new ConSheBeiOptions();
            model.setId(e.getId());
            model.setName(e.getName());
            return model;
        }).collect(Collectors.toList());
        return Result.OK(listDoor);
    }

    @ApiOperation(value = "设备类型统计", notes = "设备类型统计")
    @GetMapping(value = "/statByDeviceType")
    public Result<LineStateVO> statByDeviceType() {
        LineStateVO stat = conSheBeiService.statByDeviceType();
        return Result.OK(stat);
    }

    @ApiOperation(value = "设备在线统计", notes = "设备在线统计")
    @GetMapping(value = "/statByDeviceState")
    public Result<List<PieStateVO>> statByDeviceState() {
        List<PieStateVO> stat = conSheBeiService.statByDeviceState();
        return Result.OK(stat);
    }

    @ApiOperation(value = "设备属性详情", notes = "设备属性详情")
    @GetMapping(value = "/getAttrById")
    public Result<ConDeviceAttribute> getAttrById(@ApiParam(value = "id", required = true) @RequestParam("id") String id) {
        ConDeviceAttribute attr = conDeviceAttributeService.getById(id);
        return Result.OK(attr);
    }

    @ApiOperation(value = "所有摄像机设备", notes = "所有摄像机设备")
    @GetMapping(value = "/listAllCamera")
    public Result<List<ConSheBei>> listAllCamera() {
        List<ConSheBei> list = conSheBeiService.listAllCamera();
        return Result.OK(list);
    }

    @ApiOperation(value = "所有广播设备", notes = "所有摄像机设备")
    @GetMapping(value = "/listAllPublish")
    public Result<List<ConSheBei>> listAllPublish() {
        List<ConSheBei> list = conSheBeiService.listAllPublish();
        return Result.OK(list);
    }

    @ApiOperation(value = "设备导入", notes = "设备导入")
    @PostMapping(value = "/importExcel")
    public Result<?> saveBatch(@RequestBody List<ConSheBei> list) {
        list = list.stream()
                .filter(e -> StringUtils.isNotEmpty(e.getName()))
                .map(e -> {
                    LambdaQueryWrapper<DeviceTypeManagement> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(DeviceTypeManagement::getName,e.getType());
                    DeviceTypeManagement type = deviceTypeManagementService.getOne(wrapper);
                    if(type == null) {
                        return e;
                    }
                    String name = e.getType();
                    LambdaQueryWrapper<DeviceTypeManagement> nameWrapper = new LambdaQueryWrapper<>();
                    nameWrapper.eq(DeviceTypeManagement::getName,name);
                    DeviceTypeManagement deviceTypeManagement = deviceTypeManagementService.getOne(nameWrapper);
                    e.setDeviceTypeId(deviceTypeManagement.getId());
                    return e;
                }).collect(Collectors.toList());
        conSheBeiService.batchAdd(list);
        return Result.OK("导入数据成功");
    }
}
