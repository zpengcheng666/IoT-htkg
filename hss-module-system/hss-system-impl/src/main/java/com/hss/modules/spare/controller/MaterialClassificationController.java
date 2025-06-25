package com.hss.modules.spare.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.util.LogUtil;
import com.hss.modules.spare.entity.MaterialClassification;
import com.hss.modules.spare.model.ItemTypeModel;
import com.hss.modules.spare.model.UpdateItemTypeStatusModel;
import com.hss.modules.spare.service.IMaterialClassificationService;
import com.hss.modules.system.entity.BaseMenu;
import com.hss.modules.system.model.UpdateStatusModel;
import com.hss.modules.system.monitorThing.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @description: 物料分类管理
 * @author wuyihan
 * @date 2024/4/25 14:38
 * @version 1.0
 */
@Slf4j
@Api(tags = "物料分类管理")
@RestController
@RequestMapping("/spare/materialClassification")
public class MaterialClassificationController extends BaseController {

    @Autowired
    private IMaterialClassificationService materialClassificationService;

    /**
     * 查询物料下拉树
     * @return
     */
    @ApiOperation(value="查询物料下拉树", notes="查询物料下拉树")
    @RequestMapping(value = "/queryTreeList", method = RequestMethod.GET)
    public Result<List<ItemTypeModel>> queryTreeList(@RequestParam(name = "ids", required = false) String ids) {
        List<ItemTypeModel> itemTypeModelList = materialClassificationService.queryTreeList(ids);
        return Result.OK(itemTypeModelList);
    }

    /**
     * 添加
     *
     * @param materialClassification
     * @return
     */
    @AutoLog(value = "物料-添加")
    @ApiOperation(value="物料-添加", notes="物料-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody MaterialClassification materialClassification) {
        materialClassificationService.save(materialClassification);
        MaterialClassification byId = materialClassificationService.getById(materialClassification.getId());
        LogUtil.setOperate(byId.getTypeName());
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param materialClassification
     * @return
     */
    @AutoLog(value = "物料-编辑")
    @ApiOperation(value="物料-编辑", notes="物料-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<?> edit(@RequestBody MaterialClassification materialClassification) {
        materialClassificationService.updateById(materialClassification);
        LogUtil.setOperate(materialClassification.getTypeName());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "物料-通过id删除")
    @ApiOperation(value="物料-通过id删除", notes="物料-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name="id",required=true) String id) {
        LogUtil.setOperate(materialClassificationService.getById(id).getTypeName());
        materialClassificationService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "物料-批量删除")
    @ApiOperation(value="物料-批量删除", notes="物料-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        this.materialClassificationService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "物料-通过id查询")
    @ApiOperation(value="物料-通过id查询", notes="物料-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
        MaterialClassification materialClassification = materialClassificationService.getById(id);
        return Result.OK(materialClassification);
    }

    /**
     * @description: 修改状态，0显示1不显示
     * @author wuyihan
     * @date 2024/4/29 10:05
     * @version 1.0
     */
    @ApiOperation(value = "修改状态，0显示1不显示", notes = "修改状态，0显示1不显示")
    @RequestMapping(value = "/editStatus", method = {RequestMethod.POST})
    @RequiresRoles(value = {"admin","administrator"},logical = Logical.OR)
    public Result<?> editStatus(@RequestBody UpdateItemTypeStatusModel updateItemTypeStatusModel) {
        LambdaQueryWrapper<MaterialClassification> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MaterialClassification::getId, updateItemTypeStatusModel.getId());
        MaterialClassification byId = materialClassificationService.getOne(queryWrapper);
        byId.setStatus(updateItemTypeStatusModel.getStatus());
        materialClassificationService.updateById(byId);
        return Result.OK("修改状态成功!");
    }

}
