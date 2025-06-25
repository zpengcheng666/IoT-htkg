package com.hss.modules.ventilation.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.modules.scada.entity.ConSheBei;
import com.hss.modules.scada.service.IConSheBeiService;
import com.hss.modules.ventilation.entity.PlanCustom;
import com.hss.modules.ventilation.service.IPlanCustomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 自定义方案
 * @Author: zpc
 * @Date: 2023-04-25
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "自定义方案")
@RestController
@RequestMapping("/ventilation/planCustom")
public class PlanCustomController extends HssController<PlanCustom, IPlanCustomService> {
    @Autowired
    private IPlanCustomService planCustomService;

    @Autowired
    private IConSheBeiService conSheBeiService;

    /**
     * 分页列表查询
     *
     * @param planCustom
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "自定义方案-分页列表查询", notes = "自定义方案-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(PlanCustom planCustom,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<PlanCustom> queryWrapper = QueryGenerator.initQueryWrapper(planCustom, req.getParameterMap());
        Page<PlanCustom> page = new Page<PlanCustom>(pageNo, pageSize);
        IPage<PlanCustom> pageList = planCustomService.getPage(page, queryWrapper);
        pageList.getRecords().forEach(e -> {
            ConSheBei byId = conSheBeiService.getById(e.getDdcId());
            e.setControlSystemName(byId == null ? "" : byId.getName());
        });
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param planCustom
     * @return
     */
    @AutoLog(value = "添加自定义方案")
    @ApiOperation(value = "自定义方案-添加", notes = "自定义方案-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody PlanCustom planCustom) {
        planCustomService.add(planCustom);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param planCustom
     * @return
     */
    @AutoLog(value = "编辑自定义方案")
    @ApiOperation(value = "自定义方案-编辑", notes = "自定义方案-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> edit(@RequestBody PlanCustom planCustom) {
        planCustomService.edit(planCustom);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除自定义方案")
    @ApiOperation(value = "自定义方案-通过id删除", notes = "自定义方案-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        planCustomService.delete(id);
        return Result.OK("删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "自定义方案-通过id查询", notes = "自定义方案-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        PlanCustom planCustom = planCustomService.queryById(id);
        return Result.OK(planCustom);
    }
}
