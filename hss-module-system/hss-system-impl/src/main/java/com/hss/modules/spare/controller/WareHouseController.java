package com.hss.modules.spare.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.util.LogUtil;
import com.hss.modules.spare.entity.Area;
import com.hss.modules.spare.entity.WareHouse;
import com.hss.modules.spare.service.IAreaService;
import com.hss.modules.spare.service.IWareHouseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.hss.core.common.system.base.controller.HssController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author zpc
 * @version 1.0
 * @description: 仓库
 * @date 2024/4/25 14:54
 */
@Slf4j
@Api(tags = "仓库")
@RestController
@RequestMapping("/spare/wareHouse")
public class WareHouseController extends HssController<WareHouse, IWareHouseService> {
    @Autowired
    private IWareHouseService wareHouseService;
    @Autowired
    private IAreaService areaService;

    /**
     * 分页列表查询
     *
     * @param wareHouse
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "仓库-分页列表查询", notes = "仓库-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(WareHouse wareHouse,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<WareHouse> queryWrapper = QueryGenerator.initQueryWrapper(wareHouse, req.getParameterMap());
        Page<WareHouse> page = new Page<>(pageNo, pageSize);
        IPage<WareHouse> pageList = wareHouseService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * @description: 仓库对应库区树状查询
     * @author zpc
     * @date 2024/5/6 15:02
     * @version 1.0
     */
    @ApiOperation(value = "仓库对应库区树状查询", notes = "仓库对应库区树状查询")
    @GetMapping(value = "/treeList")
    public List<WareHouse> treeList() {
        QueryWrapper<WareHouse> qw = new QueryWrapper<>();
        qw.eq("DELETED", 0);
        List<WareHouse> treeList = wareHouseService.list(qw);
        treeList.forEach(e -> {
            QueryWrapper<Area> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("WAREHOUSE_ID", e.getId());
            queryWrapper.eq("DELETED", 0);
            List<Area> areaList = areaService.list(queryWrapper);
            e.setChildren(areaList);
        });
        return treeList;
    }

    /**
     * @description: 仓库数量统计
     * @author zpc
     * @date 2024/4/26 14:46
     * @version 1.0
     */
    @ApiOperation(value = "仓库数量统计", notes = "仓库数量统计")
    @GetMapping(value = "/wareHouseTotal")
    public int wareHouseTotal() {
        LambdaQueryWrapper<WareHouse> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WareHouse::getDeleted, 0);
        return wareHouseService.list(queryWrapper).size();
    }

    /**
     * 添加
     *
     * @param wareHouse
     * @return
     */
    @AutoLog(value = "仓库-添加")
    @ApiOperation(value = "仓库-添加", notes = "仓库-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody WareHouse wareHouse) {
        wareHouseService.save(wareHouse);
        LogUtil.setOperate(wareHouse.getName());
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param wareHouse
     * @return
     */
    @AutoLog(value = "仓库-编辑")
    @ApiOperation(value = "仓库-编辑", notes = "仓库-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> edit(@RequestBody WareHouse wareHouse) {
        wareHouseService.updateById(wareHouse);
        LogUtil.setOperate(wareHouse.getName());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "仓库-通过id删除")
    @ApiOperation(value = "仓库-通过id删除", notes = "仓库-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        LogUtil.setOperate(wareHouseService.getById(id).getName());
        wareHouseService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "仓库-批量删除")
    @ApiOperation(value = "仓库-批量删除", notes = "仓库-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.wareHouseService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "仓库-通过id查询", notes = "仓库-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        WareHouse wareHouse = wareHouseService.getById(id);
        return Result.OK(wareHouse);
    }

}
