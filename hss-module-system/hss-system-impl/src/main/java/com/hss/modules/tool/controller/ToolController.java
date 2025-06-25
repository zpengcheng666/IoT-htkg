package com.hss.modules.tool.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.util.LogUtil;
import com.hss.core.common.util.OConvertUtils;
import com.hss.modules.system.entity.BaseLocation;
import com.hss.modules.system.service.IBaseDictDataService;
import com.hss.modules.system.service.IBaseLocationService;
import com.hss.modules.tool.entity.Tool;
import com.hss.modules.tool.service.IToolService;
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
* @description: 工具管理
* @author zpc
* @date 2022/12/26 9:58
* @version 1.0
*/
@Slf4j
@Api(tags = "工具管理")
@RestController
@RequestMapping("/tool/tool")
public class ToolController extends HssController<Tool, IToolService> {
    @Autowired
    private IToolService toolService;
    @Autowired
    private IBaseDictDataService baseDictDataService;

    @Autowired
    private IBaseLocationService baseLocationService;

    /**
     * 分页列表查询
     *
     * @param tool
     * @return
     */
    @ApiOperation(value = "工具管理-分页列表查询", notes = "工具管理-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(Tool tool,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<Tool> queryWrapper = new LambdaQueryWrapper<>();
        if (OConvertUtils.isNotEmpty(tool.getName())){
            queryWrapper.like(Tool::getName,tool.getName());//名称模糊查询
        }
        if (OConvertUtils.isNotEmpty(tool.getGoodsModel())){
            queryWrapper.like(Tool::getGoodsModel,tool.getGoodsModel());//工具型号
        }
        if (OConvertUtils.isNotEmpty(tool.getGoodsSort())){
            queryWrapper.eq(Tool::getGoodsSort,tool.getGoodsSort());//配置类型
        }

        Page<Tool> page = new Page<>(pageNo,pageSize);
        IPage<Tool> pageList = toolService.page(page, queryWrapper);

        pageList.getRecords().forEach(e -> {
            //存放位置
            BaseLocation disp1 = baseLocationService.getById(e.getPosition());
            e.setPosition_disp(disp1 == null ? "" : disp1.getName());
        });
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param tool
     * @return
     */
    @AutoLog(value = "工具管理-添加")
    @ApiOperation(value = "工具管理-添加", notes = "工具管理-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody Tool tool) {
        toolService.save(tool);
        Tool byId = toolService.getById(tool.getId());
        LogUtil.setOperate(byId.getName());
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param tool
     * @return
     */
    @AutoLog(value = "工具管理-编辑")
    @ApiOperation(value = "工具管理-编辑", notes = "工具管理-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> edit(@RequestBody Tool tool) {
        toolService.updateById(tool);
        Tool byId = toolService.getById(tool.getId());
        LogUtil.setOperate(byId.getName());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "工具管理-删除")
    @ApiOperation(value = "工具管理-删除", notes = "工具管理-删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        Tool byId = toolService.getById(id);
        LogUtil.setOperate(byId.getName());
        toolService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "工具管理-批量删除", notes = "工具管理-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.toolService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "工具管理-通过id查询", notes = "工具管理-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        Tool tool = toolService.getById(id);
        return Result.OK(tool);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param tool
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Tool tool) {
        return super.exportXls(request, tool, Tool.class, "工具管理");
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
        return super.importExcel(request, response, Tool.class);
    }

}
