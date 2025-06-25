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
import com.hss.modules.preplan.entity.ContingencyPlanStage;
import com.hss.modules.preplan.entity.ContingencyPlanWorkitem;
import com.hss.modules.preplan.entity.ContingencyRecord;
import com.hss.modules.preplan.pojo.ProcessWorkDTO;
import com.hss.modules.preplan.service.IContingencyPlanService;
import com.hss.modules.preplan.service.IContingencyPlanStageService;
import com.hss.modules.preplan.service.IContingencyPlanWorkitemService;
import com.hss.modules.preplan.service.IContingencyRecordService;
import com.hss.modules.system.model.ContingencyRecordAlarmTerminal;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 特情处置记录
 * @Author: zpc
 * @Date: 2023-02-13
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "特情处置记录")
@RestController
@RequestMapping("/system/contingencyRecord")
public class ContingencyRecordController extends HssController<ContingencyRecord, IContingencyRecordService> {
    @Autowired
    private IContingencyRecordService contingencyRecordService;

    @Autowired
    private IContingencyPlanService contingencyPlanService;

    @Autowired
    private IContingencyPlanStageService contingencyPlanStageService;

    @Autowired
    private IContingencyPlanWorkitemService contingencyPlanWorkitemService;

    @ApiOperation(value = "特情处置记录-分页列表查询", notes = "特情处置记录-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ContingencyRecord contingencyRecord,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<ContingencyRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(ContingencyRecord::getCreatedTime);
        if (OConvertUtils.isNotEmpty(contingencyRecord.getName())) {
            //预案名称模糊查询
            queryWrapper.like(ContingencyRecord::getName, contingencyRecord.getName());
        }
        if (OConvertUtils.isNotEmpty(contingencyRecord.getInitiator())) {
            //启动人模糊查询
            queryWrapper.like(ContingencyRecord::getInitiator, contingencyRecord.getInitiator());
        }
        if (OConvertUtils.isNotEmpty(contingencyRecord.getStartTime())) {
            //大于开始时间
            queryWrapper.ge(ContingencyRecord::getStartTime, contingencyRecord.getStartTime());
        }
        if (OConvertUtils.isNotEmpty(contingencyRecord.getEndTime())) {
            //小于结束时间
            queryWrapper.le(ContingencyRecord::getEndTime, contingencyRecord.getEndTime());
        }
        Page<ContingencyRecord> page = new Page<>(pageNo, pageSize);
        IPage<ContingencyRecord> pageList = contingencyRecordService.page(page, queryWrapper);
        pageList.getRecords().forEach(e -> {
            if (e.getIsCompleted() == 1) {
                e.setIsCompleted_disp("完成");
            }
            if (e.getIsCompleted() == 0) {
                e.setIsCompleted_disp("未完成");
            }
        });
        return Result.OK(pageList);
    }

    @ApiOperation(value = "特情处置记录-详情", notes = "特情处置记录-详情")
    @GetMapping(value = "/details")
    public Result<?> details(@RequestParam(name = "recordId", required = true) String recordId) {
        //1.根据处置记录recordId获取，预案id
        ContingencyRecord record = contingencyRecordService.getById(recordId);
        if (OConvertUtils.isEmpty(record)) {
            return Result.OK(new ArrayList<>());
        }
        //预案id
        String planId = record.getPlanId();
        //启动人
        String initiator = record.getInitiator();
        //启动时间
        Date startTime = record.getStartTime();
        //完成状态
        Integer isCompleted = record.getIsCompleted();
        //2.预案id条件查询
        LambdaQueryWrapper<ContingencyPlan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContingencyPlan::getId, planId);
        List<ContingencyPlan> planList = contingencyPlanService.list(queryWrapper);
        //3.查询阶段项列表
        LambdaQueryWrapper<ContingencyPlanStage> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(ContingencyPlanStage::getPlanId, planId);
        List<ContingencyPlanStage> list1 = contingencyPlanStageService.list(queryWrapper1);
        if (CollectionUtils.isEmpty(list1)) {
            return Result.OK(new ArrayList<>());
        }
        //获取阶段idList
        List<String> stageIdList = list1.stream().map(ContingencyPlanStage::getId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(stageIdList)) {
            return Result.OK(new ArrayList<>());
        }
        //4.查询工作项列表
        LambdaQueryWrapper<ContingencyPlanWorkitem> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.in(ContingencyPlanWorkitem::getStageId, stageIdList);
        List<ContingencyPlanWorkitem> list2 = contingencyPlanWorkitemService.list(queryWrapper2);
        if (CollectionUtils.isEmpty(list2)) {
            return Result.OK(new ArrayList<>());
        }
        //5.将查询结果按照阶段id分组
        //添加提交人和提交时间
        Map<String, List<ContingencyPlanWorkitem>> map = list2.stream().collect(Collectors.groupingBy(ContingencyPlanWorkitem::getStageId));
        list2.forEach(m -> {
            m.setSumbitTime(startTime);
            m.setSumbitPerson(initiator);
            m.setStatus(isCompleted);
        });
        //6.按照阶段id分组重新组成list
        list1.forEach(k -> {
            List<ContingencyPlanWorkitem> workitemList = map.get(k.getId());
            k.setWorkitemList(workitemList);
            //完成状态
            k.setStatus(isCompleted);
        });
        planList.forEach(e -> e.setPlanStageList(list1));
        return Result.OK(planList);
    }
    @ApiOperation(value = "报警添加应急预案", notes = "报警添加应急预案")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ContingencyRecordAlarmTerminal contingencyRecordAlarmTerminal) {
        contingencyRecordService.saveAlarmTerminal(contingencyRecordAlarmTerminal);
        return Result.OK("添加成功！");
    }

    @AutoLog(value = "特情处置-处理")
    @ApiOperation(value = "特情处置记录-处理", notes = "特情处置记录-处理")
    @RequestMapping(value = "/edit", method = {RequestMethod.POST})
    public Result<?> edit(@RequestBody ContingencyRecord contingencyRecord) {
        contingencyRecord.setIsCompleted(contingencyRecord.getIsCompleted());
        contingencyRecord.setEndTime(new Date());
        contingencyRecordService.saveOrUpdate(contingencyRecord);
        ContingencyRecord byId = contingencyRecordService.getById(contingencyRecord.getId());
        LogUtil.setOperate(byId.getName());
        return Result.OK("处理成功!");
    }

    @AutoLog(value = "特情处置-删除")
    @ApiOperation(value = "特情处置记录-删除", notes = "特情处置记录-删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        ContingencyRecord byId = contingencyRecordService.getById(id);
        LogUtil.setOperate(byId.getName());
        contingencyRecordService.removeById(id);
        return Result.OK("删除成功!");
    }

    @ApiOperation(value = "特情处置记录-批量删除", notes = "特情处置记录-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.contingencyRecordService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    @ApiOperation(value = "特情处置记录-通过id查询", notes = "特情处置记录-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        ContingencyRecord contingencyRecord = contingencyRecordService.getById(id);
        return Result.OK(contingencyRecord);
    }

    @ApiOperation(value = "预案处理工作", notes = "预案处理工作")
    @PutMapping(value = "/processWork")
    public Result<?> processWork(@RequestBody ProcessWorkDTO dto) {
         contingencyRecordService.processWork(dto);
        return Result.OK("处理成功");
    }


}
