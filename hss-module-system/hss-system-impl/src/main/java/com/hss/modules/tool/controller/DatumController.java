package com.hss.modules.tool.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.util.LogUtil;
import com.hss.core.common.util.OConvertUtils;
import com.hss.modules.system.entity.BaseDictData;
import com.hss.modules.system.entity.BaseLocation;
import com.hss.modules.system.model.OptionModel;
import com.hss.modules.system.service.IBaseDictDataService;
import com.hss.modules.system.service.IBaseLocationService;
import com.hss.modules.tool.entity.Datum;
import com.hss.modules.tool.entity.DatumClass;
import com.hss.modules.tool.service.IDatumClassService;
import com.hss.modules.tool.service.IDatumService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 资料表
 * @Author: zpc
 * @Date: 2022-12-26
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "资料表")
@RestController
@RequestMapping("/tool/datum")
public class DatumController extends HssController<Datum, IDatumService> {
    @Autowired
    private IDatumService datumService;
    @Autowired
    private IBaseDictDataService baseDictDataService;
    @Autowired
    private IDatumClassService datumClassService;
    @Autowired
    private IBaseLocationService baseLocationService;

    /**
     * 分页列表查询
     *
     * @param datum
     * @return
     */
    @ApiOperation(value = "资料表-分页列表查询", notes = "资料表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(Datum datum,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<Datum> queryWrapper = new LambdaQueryWrapper<>();
        Page<Datum> page = new Page<>(pageNo, pageSize);
        IPage<Datum> pageList = datumService.page(page, queryWrapper);
        pageList.getRecords().forEach(e -> {
            //位置信息
            BaseLocation disp1 = baseLocationService.getById(e.getSite());
            e.setSite_disp(disp1 == null ? "" : disp1.getName());
            //秘密级别
            BaseDictData disp2 = baseDictDataService.getById(e.getSecretLevel());
            e.setSecretLevel_disp(disp2 == null ? "" : disp2.getName());
            //资料类别转码
            DatumClass disp3 = datumClassService.getById(e.getModel());
            e.setModel_disp(disp3 == null ? "" : disp3.getName());
        });
        return Result.OK(pageList);
    }

    /**
     * @description: 资料表不分页
     * @author zpc
     * @date 2022/12/26 14:41
     * @version 1.0
     */
    @AutoLog(value = "资料表不分页")
    @ApiOperation(value = "资料表不分页", notes = "资料表不分页")
    @GetMapping(value = "/listDatum")
    public Result<?> listDatum(Datum datum) {
        LambdaQueryWrapper<Datum> queryWrapper = new LambdaQueryWrapper<>();
        if (OConvertUtils.isNotEmpty(datum.getName())) {
            queryWrapper.like(Datum::getName, datum.getName());//按名称模糊查询
        }

        List<Datum> pageList = datumService.list();
        return Result.OK(pageList);
    }

    /**
     * @description: 资料类别下拉框
     * @author zpc
     * @date 2022/12/28 10:26
     * @version 1.0
     */
    @AutoLog(value = "资料类别下拉框")
    @ApiOperation(value = "资料类别下拉框", notes = "资料类别下拉框")
    @GetMapping(value = "/listDatumOptions")
    public Result<?> listDatumOptions() {
        List<DatumClass> list = datumClassService.list();

        List<OptionModel> collect = list.stream().map(e -> {
            OptionModel model = new OptionModel();
            model.setId(e.getId());
            model.setName(e.getName());

            return model;
        }).collect(Collectors.toList());

        return Result.OK(collect);
    }

    /**
     * 添加
     *
     * @param datum
     * @return
     */
    @AutoLog(value = "资料表-添加")
    @ApiOperation(value = "资料表-添加", notes = "资料表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody Datum datum) {
        datumService.save(datum);
        Datum byId = datumService.getById(datum.getId());
        LogUtil.setOperate(byId.getName());
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param datum
     * @return
     */
    @AutoLog(value = "资料表-编辑")
    @ApiOperation(value = "资料表-编辑", notes = "资料表-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> edit(@RequestBody Datum datum) {
        datumService.updateById(datum);
        Datum byId = datumService.getById(datum.getId());
        LogUtil.setOperate(byId.getName());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "资料表-删除")
    @ApiOperation(value = "资料表-删除", notes = "资料表-删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        Datum byId = datumService.getById(id);
        LogUtil.setOperate(byId.getName());
        datumService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "资料表-批量删除", notes = "资料表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.datumService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "资料表-通过id查询", notes = "资料表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        Datum datum = datumService.getById(id);
        return Result.OK(datum);
    }
}
