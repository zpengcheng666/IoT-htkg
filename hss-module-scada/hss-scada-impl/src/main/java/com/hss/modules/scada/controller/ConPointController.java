package com.hss.modules.scada.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.query.QueryGenerator;
import com.hss.core.common.util.LogUtil;
import com.hss.modules.scada.entity.ConDianWei;
import com.hss.modules.scada.entity.ConWangGuan;
import com.hss.modules.scada.service.IConDianWeiService;
import com.hss.modules.scada.service.IConWangGuanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
* @description: 通信管理-服务管理-网关点位
* @author zpc
* @date 2024/3/19 13:24
* @version 1.0
*/
@Slf4j
@Api(tags = "网关点位")
@RestController
@RequestMapping("/api/scada/ConDianWei")
@CrossOrigin
public class ConPointController extends HssController<ConDianWei, IConDianWeiService> {
    @Autowired
    private IConDianWeiService conDianWeiService;
    @Autowired
    private IConWangGuanService conWangGuanService;

    /**
     * 网关同步后的点位，分页列表查询
     * @param conDianWei
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "同步点位-分页列表查询", notes = "同步点位-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ConDianWei conDianWei,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<ConDianWei> queryWrapper = QueryGenerator.initQueryWrapper(conDianWei, req.getParameterMap());
        Page<ConDianWei> page = new Page<>(pageNo, pageSize);
        IPage<ConDianWei> pageList = conDianWeiService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 从指定网关中同步点位数据
     * @return
     */
    @AutoLog(value = "同步网关点位点位")
    @ApiOperation(value = "从指定网关中同步点位数据", notes = "从指定网关中同步点位数据")
    @GetMapping(value = "/syncdata")
    public Result<?> syncData(@RequestParam("wgid") String wgid,
                              @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        this.conDianWeiService.syncData(wgid);

        LambdaQueryWrapper<ConDianWei> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ConDianWei::getWgid, wgid);
        queryWrapper.orderByDesc(ConDianWei::getCreatedTime);

        Page<ConDianWei> page = new Page<ConDianWei>(pageNo, pageSize);
        IPage<ConDianWei> pageList = conDianWeiService.page(page, queryWrapper);
        ConWangGuan byId = conWangGuanService.getById(wgid);
        LogUtil.setOperate(byId.getName());
        return Result.OK(pageList);
    }

    /**
     * 添加
     * @param conDianWei
     * @return
     */
    @AutoLog(value = "添加点位")
    @ApiOperation(value = "同步点位-添加", notes = "同步点位-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ConDianWei conDianWei) {
        conDianWeiService.save(conDianWei);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     * @param conDianWei
     * @return
     */
    @AutoLog(value = "编辑点位")
    @ApiOperation(value = "同步点位-编辑", notes = "同步点位-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> edit(@RequestBody ConDianWei conDianWei) {
        conDianWeiService.updateById(conDianWei);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除点位")
    @ApiOperation(value = "同步点位-通过id删除", notes = "同步点位-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        conDianWeiService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "批量删除点位")
    @ApiOperation(value = "同步点位-批量删除", notes = "同步点位-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.conDianWeiService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "同步点位-通过id查询", notes = "同步点位-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        ConDianWei conDianWei = conDianWeiService.getById(id);
        return Result.OK(conDianWei);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param conDianWei
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ConDianWei conDianWei) {
        return super.exportXls(request, conDianWei, ConDianWei.class, "同步点位");
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
        return super.importExcel(request, response, ConDianWei.class);
    }

    /**
    * @description: 根据同步网关的点位，获取点位属性信息
    * @author zpc
    * @date 2024/3/19 13:32
    */
    @ApiOperation(value = "点位-获取点位属性信息", notes = "点位-获取点位属性信息")
    @GetMapping("/GetModuleAttrData")
    public Result<?> GetModuleAttrData(String dwid,
                                       @RequestParam(value = "wgid", required = false) String wgid) {
        LambdaQueryWrapper<ConDianWei> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(wgid)) {
            queryWrapper.eq(ConDianWei::getWgid, wgid);
        }
        queryWrapper.eq(ConDianWei::getId, dwid);
        ConDianWei entity = conDianWeiService.getOne(queryWrapper);

        return Result.OK(entity);
    }
}
