package com.hss.modules.maintain.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.util.LogUtil;
import com.hss.core.common.util.OConvertUtils;
import com.hss.modules.maintain.entity.MaintainSchemas;
import com.hss.modules.maintain.entity.R_MTSchemasDevItem;
import com.hss.modules.maintain.service.IMaintainSchemasService;
import com.hss.modules.maintain.service.IR_MTSchemasDevItemService;
import com.hss.modules.system.entity.BaseDictData;
import com.hss.modules.system.model.OptionModel;
import com.hss.modules.system.service.IBaseDictDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 保养方案表
 * @Author: zpc
 * @Date: 2022-12-15
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "保养方案表")
@RestController
@RequestMapping("/maintain/maintainSchemas")
public class MaintainSchemasController extends HssController<MaintainSchemas, IMaintainSchemasService> {
    @Autowired
    private IMaintainSchemasService maintainSchemasService;
    @Autowired
    private IBaseDictDataService baseDictDataService;
    @Autowired
    private IR_MTSchemasDevItemService r_MTSchemasDevItemService;

    /**
     * 分页列表查询
     *
     * @param maintainSchemas
     * @return
     */
    @ApiOperation(value = "保养方案表-分页列表查询", notes = "保养方案表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(MaintainSchemas maintainSchemas,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<MaintainSchemas> queryWrapper = new LambdaQueryWrapper<>();
        if (OConvertUtils.isNotEmpty(maintainSchemas.getItemClassId())) {
            queryWrapper.eq(MaintainSchemas::getItemClassId, maintainSchemas.getItemClassId());
        }
        if (OConvertUtils.isNotEmpty(maintainSchemas.getSchemasName())) {
            queryWrapper.like(MaintainSchemas::getSchemasName, maintainSchemas.getSchemasName());
        }

        Page<MaintainSchemas> page = new Page<>(pageNo, pageSize);
        IPage<MaintainSchemas> pageList = maintainSchemasService.page(page, queryWrapper);
        //1.得到关系表list
        List<R_MTSchemasDevItem> rMtSchemasDevItems = r_MTSchemasDevItemService.list();

        //2.按照方案id分组
        Map<String, List<R_MTSchemasDevItem>> map = rMtSchemasDevItems.stream()
                .collect(Collectors.groupingBy(R_MTSchemasDevItem::getSchemasId));

        //方案主表list
        pageList.getRecords().forEach(e -> {
            //3. 通过主表方案id，到关系表R_MTSchemasDevItem中，取值，取出对应多个设备类别id，转为List<String>
            List<String> deviceClassIdList = map
                    .get(e.getId())
                    .stream()
                    .map(R_MTSchemasDevItem::getDeviceClassId)
                    .collect(Collectors.toList());
            e.setDeviceClassIds(deviceClassIdList);

            //保养类别
            String itemId = e.getItemClassId();
            if (OConvertUtils.isNotEmpty(itemId)) {
                BaseDictData disp1 = baseDictDataService.getById(itemId);
                if (OConvertUtils.isNotEmpty(disp1)) {
                    e.setItemClass_disp(disp1.getName());
                }
            }
        });
        return Result.OK(pageList);
    }

    /**
     * @description: 方案名称下拉
     * @author zpc
     * @date 2022/12/21 14:25
     * @version 1.0
     */
    @ApiOperation(value = "方案名称下拉", notes = "方案名称下拉")
    @GetMapping(value = "/schemasOptions")
    public Result<?> schemasOptions(String itemClassId) {
        LambdaQueryWrapper<MaintainSchemas> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MaintainSchemas::getItemClassId, itemClassId);
        List<MaintainSchemas> list = maintainSchemasService.list(queryWrapper);

        List<OptionModel> collect = list.stream().map(e -> {
            OptionModel model = new OptionModel();
            model.setId(e.getId());
            model.setName(e.getSchemasName());
            return model;
        }).collect(Collectors.toList());

        return Result.OK(collect);
    }

    /**
     * 添加
     *
     * @param maintainSchemas
     * @return
     */
    @ApiOperation(value = "保养方案表-添加", notes = "保养方案表-添加")
    @PostMapping(value = "/addSchemasDevice")
    public Result<?> addSchemasDevice(@RequestBody MaintainSchemas maintainSchemas) {
        r_MTSchemasDevItemService.saveSchemasDevice(maintainSchemas);
        MaintainSchemas byId = maintainSchemasService.getById(maintainSchemas.getId());
        LogUtil.setOperate(byId.getSchemasName());
        return Result.OK("添加成功！");
    }

    /**
     * @description: 设备类别返回
     * @author zpc
     * @date 2022/12/30 11:14
     * @version 1.0
     */
    @ApiOperation(value = "根据方案id返回设备类别", notes = "根据方案id返回设备类别")
    @GetMapping(value = "/listSchemasDevClass")
    public Result<?> listSchemasDevClass(String schemasId) {
        LambdaQueryWrapper<R_MTSchemasDevItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(R_MTSchemasDevItem::getSchemasId, schemasId);

        List<R_MTSchemasDevItem> rMtSchemasDevItems = r_MTSchemasDevItemService.list(queryWrapper);
        if (rMtSchemasDevItems == null) {
            return Result.OK();
        }
        List<String> collect = rMtSchemasDevItems.stream().map(R_MTSchemasDevItem::getDeviceClassId).collect(Collectors.toList());
        return Result.OK(collect);
    }

    /**
     * 编辑
     *
     * @param schemas
     * @return
     */
    @AutoLog(value = "保养方案-编辑")
    @ApiOperation(value = "保养方案表-编辑", notes = "保养方案表-编辑")
    @RequestMapping(value = "/editSchemasDevice", method = {RequestMethod.POST})
    public Result<?> editSchemasDevice(@RequestBody MaintainSchemas schemas) {
        r_MTSchemasDevItemService.updateSchemasDevice(schemas);
        MaintainSchemas byId = maintainSchemasService.getById(schemas.getId());
        LogUtil.setOperate(byId.getSchemasName());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "保养方案-删除")
    @ApiOperation(value = "保养方案表-通过id删除", notes = "保养方案表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        MaintainSchemas byId = maintainSchemasService.getById(id);
        LogUtil.setOperate(byId.getSchemasName());
        maintainSchemasService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "保养方案表-批量删除")
    @ApiOperation(value = "保养方案表-批量删除", notes = "保养方案表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.maintainSchemasService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "保养方案表-通过id查询")
    @ApiOperation(value = "保养方案表-通过id查询", notes = "保养方案表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        MaintainSchemas maintainSchemas = maintainSchemasService.getById(id);
        return Result.OK(maintainSchemas);
    }

}
