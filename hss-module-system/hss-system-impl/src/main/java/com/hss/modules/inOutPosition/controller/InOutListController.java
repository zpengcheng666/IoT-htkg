package com.hss.modules.inOutPosition.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.util.LogUtil;
import com.hss.core.common.util.OConvertUtils;
import com.hss.modules.inOutPosition.entity.InOutExternal;
import com.hss.modules.inOutPosition.entity.InOutInternal;
import com.hss.modules.inOutPosition.entity.InOutList;
import com.hss.modules.inOutPosition.service.IInOutExternalService;
import com.hss.modules.inOutPosition.service.IInOutInternalService;
import com.hss.modules.inOutPosition.service.IInOutListService;
import com.hss.modules.system.entity.BaseUser;
import com.hss.modules.system.model.OptionModel;
import com.hss.modules.system.service.IBaseUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description: 进出阵地列表
 * @Author: zpc
 * @Date: 2022-12-13
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "进出阵地列表")
@RestController
@RequestMapping("/inOutPosition/inOutList")
public class InOutListController extends HssController<InOutList, IInOutListService> {
    @Autowired
    private IInOutListService inOutListService;
    @Autowired
    private IInOutExternalService inOutExternalService;
    @Autowired
    private IInOutInternalService inOutInternalService;
    @Autowired
    private IBaseUserService baseUserService;
    @Autowired
    private ApplicationContext applicationContext;


    /**
     * 分页列表查询
     *
     * @param inOutList
     * @return
     */
    @ApiOperation(value = "进出阵地列表-分页列表查询", notes = "进出阵地列表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(InOutList inOutList,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<InOutList> queryWrapper = new LambdaQueryWrapper<>();
        if (OConvertUtils.isNotEmpty(inOutList.getStatus())) {
            queryWrapper.eq(InOutList::getStatus, inOutList.getStatus());
        }
        if (OConvertUtils.isNotEmpty(inOutList.getDepartment())) {
            queryWrapper.like(InOutList::getDepartment, inOutList.getDepartment());
        }
        if (OConvertUtils.isNotEmpty(inOutList.getLeaderId())) {
            queryWrapper.eq(InOutList::getLeaderId, inOutList.getLeaderId());
        }

        //外部人员list
        List<InOutExternal> outExternals = inOutExternalService.list();
        Map<String, List<InOutExternal>> exteMap = outExternals.stream()
                .collect(Collectors.groupingBy(InOutExternal::getListId));

        //内部人员list
        List<InOutInternal> inOutExternals = inOutInternalService.list();
        Map<String, List<InOutInternal>> inMap = inOutExternals.stream()
                .collect(Collectors.groupingBy(InOutInternal::getListId));

        Page<InOutList> page = new Page<>(pageNo,pageSize);
        IPage<InOutList> pageList = inOutListService.page(page, queryWrapper);

        pageList.getRecords().forEach(e -> {
            e.setInsiderList(inMap.get(e.getId()));
            e.setOutsiderList(exteMap.get(e.getId()));

            BaseUser byId = baseUserService.getById(e.getLeaderId());
            e.setLeaderId_disp(byId == null ? "" :byId.getName());
        });
        return Result.OK(pageList);
    }

    /**
     * @description: 内部人员姓名，下拉框
     * @author zpc
     * @date 2022/12/13 10:02
     * @version 1.0
     */
    @ApiOperation(value = "内部人员姓名，下拉框", notes = "内部人员姓名，下拉框")
    @GetMapping(value = "/listInternalOptions")
    public Result<?> listInternalOptions(OptionModel optionModel) {
        List<BaseUser> list = baseUserService.list();
        List<BaseUser> collect = list.stream().filter(e -> !Objects.equals(e.getUsername(), "admin")).collect(Collectors.toList());
        return Result.OK(collect);
    }

    /**
     * 添加
     *
     * @param inOutList
     * @return
     */
    @AutoLog(value = "进出阵地-添加")
    @ApiOperation(value = "进出阵地-添加", notes = "进出阵地-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody InOutList inOutList) {
        inOutListService.addAll(inOutList);

        InOutList byId = inOutListService.getById(inOutList.getId());
        LogUtil.setOperate(byId.getDepartment()+","+byId.getLeader());
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     * @param inOutList
     * @return
     */
    @AutoLog(value = "进出阵地-编辑")
    @ApiOperation(value = "进出阵地-编辑", notes = "进出阵地-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> edit(@RequestBody InOutList inOutList) {
        inOutListService.editAll(inOutList);

        InOutList byId = inOutListService.getById(inOutList.getId());
        LogUtil.setOperate(byId.getDepartment()+","+byId.getLeader());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "进出阵地列表-删除")
    @ApiOperation(value = "进出阵地列表-删除", notes = "进出阵地列表-删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        InOutList byId = inOutListService.getById(id);
        LogUtil.setOperate(byId.getDepartment()+","+byId.getLeader());
        inOutListService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @ApiOperation(value = "进出阵地列表-批量删除", notes = "进出阵地列表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.inOutListService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "进出阵地列表-通过id查询", notes = "进出阵地列表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        InOutList inOutList = inOutListService.getById(id);
        return Result.OK(inOutList);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param inOutList
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, InOutList inOutList) {
        return super.exportXls(request, inOutList, InOutList.class, "进出阵地列表");
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
        return super.importExcel(request, response, InOutList.class);
    }

}
