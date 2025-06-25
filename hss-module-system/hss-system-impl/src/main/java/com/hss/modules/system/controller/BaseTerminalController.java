package com.hss.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.core.common.util.LogUtil;
import com.hss.modules.system.entity.BaseLocation;
import com.hss.modules.system.entity.BaseTermianlInfo;
import com.hss.modules.system.entity.BaseTerminal;
import com.hss.modules.system.service.IBaseLocationService;
import com.hss.modules.system.service.IBaseTermianlInfoService;
import com.hss.modules.system.service.IBaseTerminalService;
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
 * @Description: 终端基本信息表
 * @Author: zpc
 * @Date: 2022-11-22
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "终端基本信息表")
@RestController
@RequestMapping("/system/baseTerminal")
public class BaseTerminalController extends HssController<BaseTerminal, IBaseTerminalService> {
    @Autowired
    private IBaseTerminalService baseTerminalService;

    @Autowired
    private IBaseLocationService baseLocationService;

    @Autowired
    private IBaseTermianlInfoService baseTermianlInfoService;

    /**
     * 分页列表查询
     *
     * @param baseTerminal
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "终端基本信息表-分页列表查询", notes = "终端基本信息表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(BaseTerminal baseTerminal,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<BaseTerminal> queryWrapper = QueryGenerator.initQueryWrapper(baseTerminal, req.getParameterMap());
        Page<BaseTerminal> page = new Page<>(pageNo, pageSize);
        IPage<BaseTerminal> pageList = baseTerminalService.page(page, queryWrapper);
        pageList.getRecords().forEach(e -> {
            BaseLocation type = baseLocationService.getById(e.getLocationId());
            e.setLocationId_disp(type == null ? "": type.getName());
        });
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param baseTerminal
     * @return
     */
    @AutoLog(value = "终端-添加")
    @ApiOperation(value = "终端基本信息表-添加", notes = "终端基本信息表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BaseTerminal baseTerminal) {
        baseTerminalService.saveTerminalInfo(baseTerminal);
        BaseTerminal byId = baseTerminalService.getById(baseTerminal.getId());
        LogUtil.setOperate(byId.getName());

        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param baseTerminal
     * @return
     */
    @AutoLog(value = "终端基本信息表-编辑")
    @ApiOperation(value = "终端基本信息表-编辑", notes = "终端基本信息表-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.POST})
    public Result<?> edit(@RequestBody BaseTerminal baseTerminal) {
        baseTerminalService.updateTerminalInfo(baseTerminal);
        BaseTerminal byId = baseTerminalService.getById(baseTerminal.getId());
        LogUtil.setOperate(byId.getName());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "终端基本信息表-删除")
    @ApiOperation(value = "终端基本信息表-删除", notes = "终端基本信息表-删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        BaseTerminal byId = baseTerminalService.getById(id);
        LogUtil.setOperate(byId.getName());
        //删除关联关系
        LambdaQueryWrapper<BaseTermianlInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BaseTermianlInfo::getTerminalId,id);
        baseTermianlInfoService.remove(queryWrapper);

        baseTerminalService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "终端基本信息表-批量删除", notes = "终端基本信息表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.baseTerminalService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "终端基本信息表-通过id查询", notes = "终端基本信息表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BaseTerminal baseTerminal = baseTerminalService.getById(id);
        return Result.OK(baseTerminal);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param baseTerminal
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BaseTerminal baseTerminal) {
        return super.exportXls(request, baseTerminal, BaseTerminal.class, "终端基本信息表");
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
        return super.importExcel(request, response, BaseTerminal.class);
    }

}
