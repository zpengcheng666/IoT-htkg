package com.hss.modules.scada.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.hss.config.ScadaConfigProperties;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.core.common.util.LogUtil;
import com.hss.modules.scada.entity.ConDianWei;
import com.hss.modules.scada.entity.ConSheBei;
import com.hss.modules.scada.entity.ConWangGuan;
import com.hss.modules.scada.entity.GsChangJing;
import com.hss.modules.scada.model.*;
import com.hss.modules.scada.service.IConDianWeiService;
import com.hss.modules.scada.service.IConWangGuanService;
import com.hss.modules.scada.service.IGsChangJingService;
import com.hss.modules.system.model.OptionModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @description: 场景列表
* @author zpc
* @date 2024/3/19 13:56
* @version 1.0
*/
@Slf4j
@Api(tags = "场景表")
@RestController
@RequestMapping("/api/scada")
@CrossOrigin
public class GsSceneController extends HssController<GsChangJing, IGsChangJingService> {
    @Autowired
    private IGsChangJingService gsChangJingService;

    @Autowired
    private IConWangGuanService conWangGuanService;

    @Autowired
    private IConDianWeiService conDianWeiService;

    @Autowired
    private ScadaConfigProperties scadaConfigProperties;

    /**
     * 分页列表查询
     *
     * @param gsChangJing
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "场景表-分页列表查询", notes = "场景表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GsChangJing gsChangJing,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){
        LambdaQueryWrapper<GsChangJing> queryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotEmpty(gsChangJing.getName())){
            queryWrapper.like(GsChangJing::getName,gsChangJing.getName());
        }
        if(StringUtils.isNotEmpty(gsChangJing.getSubSystem())){
            queryWrapper.eq(GsChangJing::getSubSystem,gsChangJing.getSubSystem());
        }
        Page<GsChangJing> page = new Page<>(pageNo, pageSize);
        IPage<GsChangJing> pageList = gsChangJingService.page(page, queryWrapper);
        // 清空json文件
        pageList.getRecords().forEach(e -> {
            e.setDatajson("");
            e.setSubSystemName(this.scadaConfigProperties.getSubSystemName(e.getSubSystem()));
            e.setModuleName(this.scadaConfigProperties.getModuleName(e.getSubSystem(), e.getModuleId()));
        });

        return Result.OK(pageList);
    }
    @ApiOperation(value = "场景菜单", notes = "场景菜单")
    @GetMapping(value = "/listMenu")
    public Result<List<GsChangJing>> listMenu(@ApiParam("子系统") @RequestParam(name = "sumSystem",required = false) String sumSystem) {
        List<GsChangJing> list = gsChangJingService.listMenu(sumSystem);
        return Result.OK(list);
    }

    @ApiOperation(value = "场景表-列表查询", notes = "场景表-列表查询")
    @GetMapping(value = "/listByModel")
    public Result<?> queryListByModel(String subSystem,
                                   String moduleId) {
        LambdaQueryWrapper<GsChangJing> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GsChangJing::getSubSystem, subSystem);
        queryWrapper.eq(GsChangJing::getModuleId, moduleId)
                .eq(GsChangJing::getIsPublished, "1");
        List<GsChangJing> pageList = gsChangJingService.list(queryWrapper);

        // 清空json文件
        List<Map<String, String>> ids = pageList.stream().map(e -> {
            Map<String, String> tmp = new HashMap<>(16);
            tmp.put("stageId", e.getId());
            tmp.put("stageName", e.getName());
            return tmp;
        }).collect(Collectors.toList());
        return Result.OK(ids);
    }

    @ApiOperation(value = "场景列表-查询所有发布的场景", notes = "场景列表-查询所有发布的场景")
    @GetMapping(value = "/listModels")
    public Result<?> listModels() {
        LambdaQueryWrapper<GsChangJing> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GsChangJing::getIsPublished, "1");

        List<GsChangJing> pageList = gsChangJingService.list(queryWrapper);
        // 清空json文件
        List<GsChangJingWrapper> ids = pageList.stream().map(e -> {
            GsChangJingWrapper item = new GsChangJingWrapper();
            item.setStageId(e.getId());
            item.setStageName(e.getName());
            item.setSubSystem(e.getSubSystem());
            item.setSubModel(e.getModuleId());
            return item;
        }).collect(Collectors.toList());

        return Result.OK(ids);
    }

    /**
     * 分页列表查询
     *
     * @param gsChangJing
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "场景表-分页列表查询", notes = "场景表-分页列表查询")
    @PostMapping(value = "/list2")
    public Result<?> queryPageList2(GsChangJing gsChangJing,
                                    @RequestParam(name = "stageName", required = false) String stageName,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        QueryWrapper<GsChangJing> queryWrapper = QueryGenerator.initQueryWrapper(gsChangJing, req.getParameterMap());
        if(StringUtils.isNotEmpty(stageName)) {
             queryWrapper.like("name",stageName);
        }
        Page<GsChangJing> page = new Page<GsChangJing>(pageNo, pageSize);
        IPage<GsChangJing> pageList = gsChangJingService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param gsChangJing
     * @return
     */
    @AutoLog(value = "添加场景")
    @ApiOperation(value = "场景表-添加", notes = "场景表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GsChangJing gsChangJing) {
        gsChangJingService.save(gsChangJing);
        LogUtil.setOperate(gsChangJing.getName());
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param gsChangJing
     * @return
     */
    @AutoLog(value = "编辑场景")
    @ApiOperation(value = "场景表-编辑", notes = "场景表-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> edit(@RequestBody GsChangJing gsChangJing) {
        gsChangJingService.updateById(gsChangJing);
        LogUtil.setOperate(gsChangJing.getName());
        return Result.OK("编辑成功!");
    }

    @AutoLog(value = "发布场景")
    @ApiOperation(value = "场景表-发布", notes = "场景表-发布（1：发布；0：取消发布）")
    @RequestMapping(value = "/publish", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> publish(@RequestBody ScenePublishModel model) {
        gsChangJingService.publish(model);
        GsChangJing byId = gsChangJingService.getById(model.getId());
        LogUtil.setOperate(byId.getName() + ":" + model.getStatus());
        return Result.OK("操作成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除场景")
    @ApiOperation(value = "场景表-通过id删除", notes = "场景表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        GsChangJing byId = gsChangJingService.getById(id);
        gsChangJingService.delById(id);
        LogUtil.setOperate(byId.getName());
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "批量删除场景")
    @ApiOperation(value = "场景表-批量删除", notes = "场景表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.gsChangJingService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "场景表-通过id查询", notes = "场景表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        GsChangJing gsChangJing = gsChangJingService.getById(id);
        return Result.OK(gsChangJing);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param gsChangJing
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GsChangJing gsChangJing) {
        return super.exportXls(request, gsChangJing, GsChangJing.class, "场景表");
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
        return super.importExcel(request, response, GsChangJing.class);
    }

    @AutoLog(value = "保存场景数据")
    @ApiOperation(value = "场景-创建/更新场景数据", notes = "场景-创建/更新场景数据")
    @PostMapping("/saveStageData")
    public Result<Map<String, String>> saveStageData(GsChangJingSaveStageData data) {
        Map<String, String> data1 = gsChangJingService.saveStageData(data);
        LogUtil.setOperate(data.getName());
        return Result.ok(data1);
    }

    @PostMapping("/editStageData")
    public String editStageData(String id) {
        GsChangJing entity = gsChangJingService.getById(id);
        return entity.getDatajson();
    }

    @ApiOperation(value = "场景-获取场景数据", notes = "场景-获取场景数据")
    @PostMapping("/viewStageData")
    public Result<Map<String, String>> viewStageData(@RequestParam(value = "stageId", required = false) String stageId) {
        Map<String, String> data = new HashMap<>(16);
        GsChangJing entity = gsChangJingService.getById(stageId);
        if (entity == null) {
            data.put("stageJson", "");
            return Result.OK(data);
        }
        data.put("stageJson", entity.getDatajson());
        return Result.OK(data);
    }

    @GetMapping("/getMyMoudleData")
    public Map<String, JSONPObject> getMyMoudleData(String id) {
        return new HashMap<>(16);
    }

    @DeleteMapping("/deleteMyMoudleData")
    public Map<String, JSONPObject> deleteMyMoudleData(String id) {
        return new HashMap<>(16);
    }

    @ApiOperation(value = "场景-获取网关列表", notes = "场景-获取网关列表")
    @GetMapping("/getWg")
    public Result<List<ConWangGuan>> getWg(ConWangGuan conWangGuan,
                                           HttpServletRequest req) {
        QueryWrapper<ConWangGuan> queryWrapper = QueryGenerator.initQueryWrapper(conWangGuan, req.getParameterMap()).orderByAsc("name");
        List<ConWangGuan> list = conWangGuanService.list(queryWrapper);
        return Result.OK(list);
    }

    /**
     * 这个接口查询的是从网关中导入的设备列表，所以呢，应该从
     * CON_DIANWEI 表中获取，因为点位关联的设备，是不能修改的
     * 而且也没有其他属性，只保存了设备ID、设备名称，所以就都放到了 点位表中，作为冗余字段来处理了。
     * @param wgId
     * @param sbName
     * @return
     */
    @ApiOperation(value = "场景-获取设备列表", notes = "场景-获取设备列表")
    @GetMapping("/getSb")
    public Result<List<ConSheBei>> getSb(String wgId, String sbName) {

        QueryWrapper<ConDianWei> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(" DISTINCT DEVICE_ID, DEVICE_NAME")
                .lambda()
                .eq(StringUtils.isNotEmpty(wgId), ConDianWei::getWgid, wgId)
                .like(StringUtils.isNotEmpty(sbName), ConDianWei::getName, sbName)
                .orderByAsc(ConDianWei::getDeviceName);

        List<ConDianWei> list = this.conDianWeiService.list(queryWrapper);
        List<ConSheBei> retList = list.stream().map( e -> {
            ConSheBei sheBei = new ConSheBei();
            sheBei.setId(e.getDeviceId());
            sheBei.setName(e.getDeviceName());
            return sheBei;
        }).collect(Collectors.toList());

       return Result.OK(retList);
    }

    @ApiOperation(value = "场景-获取点位列表", notes = "场景-获取点位列表")
    @GetMapping("/getCgq")
    public Result<List<ConDianWei>> getCgq(String wgId, String deviceId) {
        LambdaQueryWrapper<ConDianWei> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(wgId)){
            queryWrapper.eq(ConDianWei::getWgid, wgId);
        }
        if (StringUtils.isNotEmpty(deviceId)){
            queryWrapper.eq(ConDianWei::getDeviceId, deviceId);
        }
        queryWrapper.orderByAsc(ConDianWei::getName);
        List<ConDianWei> list = conDianWeiService.list(queryWrapper);
        return Result.OK(list);
    }

    @ApiOperation(value = "获取子系统和模块配置信息", notes = "获取子系统和模块配置信息")
    @GetMapping(value = "/config/subsystems")
    public Result<?> querySubSystems() {

        //this.scadaConfigProperties.getSubSystem();

        //return Result.OK(this.subSystems.getSubSystems());

        return Result.OK(this.scadaConfigProperties.getSubSystem());
    }

    @ApiOperation(value = "场景Options", notes = "场景Options")
    @GetMapping(value = "/options")
    public Result<?> listOptions() {
        LambdaQueryWrapper<GsChangJing> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(GsChangJing::getId, GsChangJing::getName);
        List<GsChangJing> pageList = gsChangJingService.list(queryWrapper);

        List<OptionModel> optionModels =
                pageList.stream().map(e -> {
                    OptionModel option = new OptionModel();
                    option.setId(e.getId());
                    option.setName(e.getName());
                    return option;
                }).collect(Collectors.toList());

        return Result.OK(optionModels);
    }

    @ApiOperation(value = "根据子系统id获取有存储策略的设备类型", notes = "根据子系统id获取有报警策略的设备类型")
    @GetMapping(value = "/listStoreDeviceTypeBySubSystem")
    public Result<List<ConSheBeiDoorOptions>> listStoreDeviceTypeBySubSystem(
            @ApiParam(value = "子系统id") @RequestParam(value = "subSystemId", required = false) String subSystemId){
        List<ConSheBeiDoorOptions> list = gsChangJingService.listStoreDeviceTypeBySubSystem(subSystemId);
        return Result.OK(list);

    }
    @ApiOperation(value = "根据子系统id获取有报警策略的设备类型", notes = "根据子系统id获取有报警策略的设备类型")
    @GetMapping(value = "/listAlarmDeviceTypeBySubSystem")
    public Result<List<ConSheBeiDoorOptions>> listAlarmDeviceTypeBySubSystem(
            @ApiParam(value = "子系统id") @RequestParam(value = "subSystemId", required = false) String subSystemId){
        List<ConSheBeiDoorOptions> list = gsChangJingService.listAlarmDeviceTypeBySubSystem(subSystemId);
        return Result.OK(list);

    }
    @ApiOperation(value = "获取场景下的数据表格", notes = "获取场景下的数据表格")
    @GetMapping(value = "/listDataTableBySceneId")
    public Result<List<DataTableVO>> listDataTableBySceneId(
            @ApiParam(value = "场景id", required = true) @RequestParam(value = "sceneId") String sceneId
    ){
        List<DataTableVO> list = gsChangJingService.listDataTableBySceneId(sceneId);
        return Result.OK(list);
    }
    @ApiOperation(value = "获取环境场景下的设备信息", notes = "获取场景下的数据表格")
    @GetMapping(value = "/listEnvDeviceBySceneId")
    public Result<List<EnvDeviceTableVO>> listEnvDeviceBySceneId(
            @ApiParam(value = "场景id", required = true) @RequestParam(value = "sceneId") String sceneId){
        List<EnvDeviceTableVO> list = gsChangJingService.listEnvDeviceBySceneId(sceneId);
        return Result.OK(list);
    }

    @ApiOperation(value = "获取场景下的设备列表", notes = "获取场景下的设备列表")
    @GetMapping(value = "/listDevice")
    public Result<List<DeviceSortVO>> listSortDevice(
            @ApiParam(value = "场景id", required = true) @RequestParam(value = "sceneId") String sceneId){
        List<DeviceSortVO> list = gsChangJingService.listSortDevice(sceneId);
        return Result.OK(list);
    }

    @ApiOperation(value = "保存设备排序", notes = "保存设备排序")
    @PutMapping(value = "/saveDeviceSort")
    public Result<?> saveDeviceSort(@RequestBody List<DeviceSortVO> list){
        gsChangJingService.saveDeviceSort(list);
        return Result.OK("保存成功");
    }

    @ApiOperation(value = "获取场景设备数据", notes = "获取场景设备数据")
    @GetMapping(value = "/listSceneDeviceData")
    public Result<List<DeviceModel>> listSceneDeviceData(@ApiParam("场景id") @RequestParam("sceneId") String sceneId) {
        List<DeviceModel> deviceModels = gsChangJingService.listCurrentDataBySceneId(sceneId);
        return Result.ok(deviceModels);
    }
}
