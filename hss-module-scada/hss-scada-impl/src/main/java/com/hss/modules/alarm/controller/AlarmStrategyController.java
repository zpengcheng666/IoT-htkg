package com.hss.modules.alarm.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.core.common.util.LogUtil;
import com.hss.modules.alarm.entity.AlarmStrategy;
import com.hss.modules.alarm.service.IAlarmStrategyService;
import com.hss.modules.scada.model.StrategyEnable;
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
 * @author zpc
 * @version 1.0
 * @description: 报警策略，使能报警策略
 * @date 2024/3/20 11:43
 */
@Slf4j
@Api(tags = "报警策略")
@RestController
@RequestMapping("/alarm/alarmStrategy")
public class AlarmStrategyController extends HssController<AlarmStrategy, IAlarmStrategyService> {
    @Autowired
    private IAlarmStrategyService alarmStrategyService;

    /**
     * 分页列表查询
     *
     * @param alarmStrategy
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "报警策略-分页列表查询", notes = "报警策略-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(AlarmStrategy alarmStrategy,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<AlarmStrategy> queryWrapper = new LambdaQueryWrapper<>();
        if(StrUtil.isNotEmpty(alarmStrategy.getName())){
            queryWrapper.like(AlarmStrategy::getName, alarmStrategy.getName());
        }
        if (StrUtil.isNotEmpty(alarmStrategy.getDeviceId())){
            queryWrapper.eq(AlarmStrategy::getDeviceId, alarmStrategy.getDeviceId());
        }
        if (StrUtil.isNotEmpty(alarmStrategy.getLevelId())){
            queryWrapper.eq(AlarmStrategy::getLevelId, alarmStrategy.getLevelId());
        }

        Page<AlarmStrategy> page = new Page<>(pageNo, pageSize);
        IPage<AlarmStrategy> pageList = alarmStrategyService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param alarmStrategy
     * @return
     */
    @AutoLog(value = "添加报警策略")
    @ApiOperation(value = "报警策略-添加", notes = "报警策略-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody AlarmStrategy alarmStrategy) {
        alarmStrategyService.add(alarmStrategy);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param alarmStrategy
     * @return
     */
    @AutoLog(value = "编辑报警策略")
    @ApiOperation(value = "报警策略-编辑", notes = "报警策略-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> edit(@RequestBody AlarmStrategy alarmStrategy) {
        alarmStrategyService.updateById(alarmStrategy);
        LogUtil.setOperate(alarmStrategy.getName());
        return Result.OK("编辑成功!");
    }

    @AutoLog(value = "使能报警策略")
    @ApiOperation(value = "联动策略-使能", notes = "联动策略-使能")
    @PutMapping(value = "/enable")
    public Result<?> enable(@RequestBody StrategyEnable strategyEnable) {
        alarmStrategyService.enable(strategyEnable);
        return Result.OK("修改成功!");
    }


    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除报警策略")
    @ApiOperation(value = "报警策略-通过id删除", notes = "报警策略-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        AlarmStrategy byId = alarmStrategyService.getById(id);
        alarmStrategyService.removeById(id);
        LogUtil.setOperate(byId.getName());
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "批量报警策略")
    @ApiOperation(value = "报警策略-批量删除", notes = "报警策略-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.alarmStrategyService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "报警策略-通过id查询", notes = "报警策略-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        AlarmStrategy alarmStrategy = alarmStrategyService.getById(id);
        return Result.OK(alarmStrategy);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param alarmStrategy
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AlarmStrategy alarmStrategy) {
        return super.exportXls(request, alarmStrategy, AlarmStrategy.class, "报警策略");
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
        return super.importExcel(request, response, AlarmStrategy.class);
    }
}
