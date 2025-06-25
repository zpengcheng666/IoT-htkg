package com.hss.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.modules.system.entity.BaseTerminalTemplate;
import com.hss.modules.system.model.TerminalModel;
import com.hss.modules.system.service.IBaseTerminalTemplateService;
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
 * @Description: 终端模板
 * @Author: zpc
 * @Date: 2023-04-04
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "终端模板")
@RestController
@RequestMapping("/system/baseTerminalTemplate")
public class BaseTerminalTemplateController extends HssController<BaseTerminalTemplate, IBaseTerminalTemplateService> {
    @Autowired
    private IBaseTerminalTemplateService baseTerminalTemplateService;

    /**
     * 分页列表查询
     *
     * @param baseTerminalTemplate
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "终端模板-分页列表查询", notes = "终端模板-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(BaseTerminalTemplate baseTerminalTemplate,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<BaseTerminalTemplate> queryWrapper = QueryGenerator.initQueryWrapper(baseTerminalTemplate, req.getParameterMap());
        Page<BaseTerminalTemplate> page = new Page<BaseTerminalTemplate>(pageNo, pageSize);
        IPage<BaseTerminalTemplate> pageList = baseTerminalTemplateService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * @description: 模板下拉框
     * @author zpc
     * @date 2023/4/7 15:17
     * @version 1.0
     */
    @ApiOperation(value = "模板下拉框", notes = "模板下拉框")
    @GetMapping(value = "/templateOptiongs")
    public Result<?> templateOptiongs(TerminalModel terminalModel) {
        List<BaseTerminalTemplate> list = baseTerminalTemplateService.list();
        List<TerminalModel> listTemplate = list.stream().map(e -> {
            TerminalModel model = new TerminalModel();
            model.setTerminalId(e.getId());
            model.setName(e.getName());
            model.setInfoList(e.getTempList());
            model.setBackgroundColor(e.getColor());
            model.setBackgroundImg(e.getImg());
            model.setAjm(e.getAjm());
            model.setAlarmStatus(e.getAlarmStatus());
            model.setMj(e.getMj());
            model.setYjcz(e.getYjcz());
            model.setCheckDoorId(e.getCheckDoorId());
            model.setDoorId(e.getDoorId());
            model.setAlarmLevel(e.getAlarmLevel());

            return model;
        }).collect(Collectors.toList());
        return Result.OK(listTemplate);
    }

    /**
     * 添加
     *
     * @param baseTerminalTemplate
     * @return
     */
    @ApiOperation(value = "终端模板-添加", notes = "终端模板-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BaseTerminalTemplate baseTerminalTemplate) {
        baseTerminalTemplateService.saveTemplate(baseTerminalTemplate);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param baseTerminalTemplate
     * @return
     */
    @ApiOperation(value = "终端模板-编辑", notes = "终端模板-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> edit(@RequestBody BaseTerminalTemplate baseTerminalTemplate) {
        baseTerminalTemplateService.updateById(baseTerminalTemplate);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "终端模板-通过id删除", notes = "终端模板-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        baseTerminalTemplateService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "终端模板-批量删除", notes = "终端模板-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.baseTerminalTemplateService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "终端模板-通过id查询", notes = "终端模板-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BaseTerminalTemplate baseTerminalTemplate = baseTerminalTemplateService.getById(id);
        return Result.OK(baseTerminalTemplate);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param baseTerminalTemplate
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BaseTerminalTemplate baseTerminalTemplate) {
        return super.exportXls(request, baseTerminalTemplate, BaseTerminalTemplate.class, "终端模板");
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
        return super.importExcel(request, response, BaseTerminalTemplate.class);
    }

}
