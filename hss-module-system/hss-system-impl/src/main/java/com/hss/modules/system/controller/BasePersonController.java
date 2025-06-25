package com.hss.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.core.common.util.LogUtil;
import com.hss.modules.system.entity.BaseDictData;
import com.hss.modules.system.entity.BasePerson;
import com.hss.modules.system.service.IBaseDictDataService;
import com.hss.modules.system.service.IBasePersonService;
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

/**
 * @Description: 人员管理表
 * @Author: zpc
 * @Date: 2022-11-03
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "人员管理表")
@RestController
@RequestMapping("/system/basePerson")
public class BasePersonController extends HssController<BasePerson, IBasePersonService> {
    @Autowired
    private IBasePersonService basePersonService;
    @Autowired
    private IBaseDictDataService baseDictDataService;

    /**
     * 分页列表查询
     *
     * @param basePerson
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "人员管理表-分页列表查询", notes = "人员管理表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(BasePerson basePerson,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<BasePerson> queryWrapper = QueryGenerator.initQueryWrapper(basePerson, req.getParameterMap());
        Page<BasePerson> page = new Page<>(pageNo, pageSize);
        IPage<BasePerson> pageList = basePersonService.page(page, queryWrapper);
        pageList.getRecords().forEach(e -> {
            //军衔
            BaseDictData dictData1 = baseDictDataService.getById(e.getMilitary());
            e.setMilitary_disp(dictData1 == null ? "" : dictData1.getName());

            //职称
            BaseDictData positionDisp = baseDictDataService.getById(e.getPosition());
            e.setPosition_disp(positionDisp == null ? "" : positionDisp.getName());
        });
        return Result.OK(pageList);
    }

    @ApiOperation(value = "人员管理表-分页列表查询", notes = "人员管理表-分页列表查询")
    @GetMapping(value = "/listPerson")
    public Result<?> listPerson(BasePerson basePerson) {
        List<BasePerson> pageList = basePersonService.list();
        pageList.forEach(e -> {
            //军衔
            BaseDictData dictData1 = baseDictDataService.getById(e.getMilitary());
            e.setMilitary_disp(dictData1 == null ? "" : dictData1.getName());

            //职称
            BaseDictData positionDisp = baseDictDataService.getById(e.getPosition());
            e.setPosition_disp(positionDisp == null ? "" : positionDisp.getName());
        });
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param basePerson
     * @return
     */
    @AutoLog(value = "人员管理表-添加")
    @ApiOperation(value = "人员管理表-添加", notes = "人员管理表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BasePerson basePerson) {
        basePersonService.save(basePerson);
        BasePerson byId = basePersonService.getById(basePerson.getId());
        LogUtil.setOperate(byId.getName());
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param basePerson
     * @return
     */
    @AutoLog(value = "人员管理表-编辑")
    @ApiOperation(value = "人员管理表-编辑", notes = "人员管理表-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> edit(@RequestBody BasePerson basePerson) {
        basePersonService.updateById(basePerson);
        BasePerson byId = basePersonService.getById(basePerson.getId());
        LogUtil.setOperate(byId.getName());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "人员管理表-删除")
    @ApiOperation(value = "人员管理表-删除", notes = "人员管理表-删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        BasePerson byId = basePersonService.getById(id);
        LogUtil.setOperate(byId.getName());
        basePersonService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "人员管理表-批量删除", notes = "人员管理表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.basePersonService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "人员管理表-通过id查询", notes = "人员管理表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BasePerson basePerson = basePersonService.getById(id);
        return Result.OK(basePerson);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param basePerson
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BasePerson basePerson) {
        return super.exportXls(request, basePerson, BasePerson.class, "人员管理表");
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
        return super.importExcel(request, response, BasePerson.class);
    }

}
