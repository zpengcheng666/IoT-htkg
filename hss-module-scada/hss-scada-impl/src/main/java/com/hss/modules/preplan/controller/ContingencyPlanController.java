package com.hss.modules.preplan.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.util.LogUtil;
import com.hss.core.common.util.OConvertUtils;
import com.hss.modules.preplan.entity.ContingencyPlan;
import com.hss.modules.preplan.service.IContingencyPlanService;
import com.hss.modules.system.entity.BaseDictData;
import com.hss.modules.system.model.ContingencyModel;
import com.hss.modules.system.model.OptionModel;
import com.hss.modules.system.service.IBaseDictDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 预案主表
 * @Author: zpc、wuyihan
 * @Date: 2024-03-21
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "预案主表")
@RestController
@RequestMapping("/system/contingencyPlan")
public class ContingencyPlanController extends HssController<ContingencyPlan, IContingencyPlanService> {
    @Autowired
    private IContingencyPlanService contingencyPlanService;

    @Autowired
    private IBaseDictDataService baseDictDataService;

    /**
     * 分页列表查询
     *
     * @param contingencyPlan
     * @return
     */
    @ApiOperation(value = "预案主表-分页列表查询", notes = "预案主表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ContingencyPlan contingencyPlan,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<ContingencyPlan> queryWrapper = new LambdaQueryWrapper<>();
        if (OConvertUtils.isNotEmpty(contingencyPlan.getContingencyClass())) {
            //突发事件类型
            queryWrapper.eq(ContingencyPlan::getContingencyClass, contingencyPlan.getContingencyClass());
        }
        if (OConvertUtils.isNotEmpty(contingencyPlan.getName())) {
            //预案名称
            queryWrapper.like(ContingencyPlan::getName, contingencyPlan.getName());
        }
        Page<ContingencyPlan> page = new Page<>(pageNo, pageSize);
        IPage<ContingencyPlan> pageList = contingencyPlanService.page(page, queryWrapper);
        pageList.getRecords().forEach(e -> {
            BaseDictData byId = baseDictDataService.getById(e.getContingencyClass());
            e.setContingencyClass_disp(byId == null ? "" : byId.getName());
        });
        return Result.OK(pageList);
    }

    @ApiOperation(value = "突发事件类型下拉框", notes = "突发事件类型下拉框")
    @GetMapping(value = "/listContingencyPlanOptions")
    public Result<?> listContingencyPlanOptions() {
        LambdaQueryWrapper<BaseDictData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BaseDictData::getDictTypeId, "A3046CCDABAA42EC9C23D950A8427B99");
        List<BaseDictData> dataList = baseDictDataService.list(queryWrapper);
        List<OptionModel> collect = dataList.stream().map(e -> {
            OptionModel model = new OptionModel();
            model.setId(e.getId());
            model.setName(e.getName());
            return model;
        }).collect(Collectors.toList());
        return Result.OK(collect);
    }

    @AutoLog(value = "预案管理-图片保存")
    @ApiOperation(value = "预案管理-图片保存", notes = "预案管理-图片保存")
    @PostMapping(value = "/saveImgContingency")
    public Result<?> saveImgContingency(@RequestBody ContingencyPlan contingencyPlan) {
        ContingencyPlan contingency = contingencyPlanService.getById(contingencyPlan.getId());
        contingency.setImgUrls(contingencyPlan.getImgUrls());
        contingencyPlanService.updateById(contingency);
        return Result.OK(contingencyPlan).success("保存成功");
    }


    @AutoLog(value = "预案管理添加")
    @ApiOperation(value = "预案主表-添加", notes = "预案主表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ContingencyPlan contingencyPlan) {
        contingencyPlanService.save(contingencyPlan);
        return Result.OK("添加成功！");
    }


    @ApiOperation(value = "预案添加阶段", notes = "预案添加阶段")
    @PostMapping(value = "/addStage")
    public Result<?> addStage(@RequestBody ContingencyModel contingencyModel) {
        contingencyPlanService.saveStage(contingencyModel);
        ContingencyPlan byId = contingencyPlanService.getById(contingencyModel.getPlanId());
        LogUtil.setOperate(byId.getName());
        return Result.OK("添加成功！");
    }

    @AutoLog(value = "预案管理-编辑")
    @ApiOperation(value = "预案主表-编辑", notes = "预案主表-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> edit(@RequestBody ContingencyPlan contingencyPlan) {
        contingencyPlanService.updateById(contingencyPlan);
        ContingencyPlan byId = contingencyPlanService.getById(contingencyPlan.getId());
        LogUtil.setOperate(byId.getName());
        return Result.OK("编辑成功!");
    }


    @AutoLog(value = "预案管理-删除")
    @ApiOperation(value = "预案主表-删除", notes = "预案主表-删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        ContingencyPlan byId = contingencyPlanService.getById(id);
        LogUtil.setOperate(byId.getName());
        contingencyPlanService.removeById(id);
        return Result.OK("删除成功!");
    }

    @ApiOperation(value = "预案主表-批量删除", notes = "预案主表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.contingencyPlanService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    @ApiOperation(value = "预案主表-通过id查询", notes = "预案主表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        ContingencyPlan contingencyPlan = contingencyPlanService.getById(id);
        return Result.OK(contingencyPlan);
    }



}
