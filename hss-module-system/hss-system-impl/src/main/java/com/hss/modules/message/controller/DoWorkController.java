package com.hss.modules.message.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.util.LogUtil;
import com.hss.modules.message.dto.DoWorkPageDTO;
import com.hss.modules.message.dto.DoWorkPageVO;
import com.hss.modules.message.dto.PublishMessageDTO;
import com.hss.modules.message.entity.DoWork;
import com.hss.modules.message.service.IDoWorkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @Description: 手动值班
 * @Author: zpc
 * @Date: 2023-12-06
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "手动值班")
@RestController
@RequestMapping("/message/doWork")
public class DoWorkController extends HssController<DoWork, IDoWorkService> {
    @Autowired
    private IDoWorkService doWorkService;

    @ApiOperation(value = "手动值班-分页列表查询", notes = "手动值班-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<DoWorkPageVO>> queryPageList(DoWorkPageDTO dto) {
        IPage<DoWorkPageVO> pageList = doWorkService.pageList(dto);
        return Result.OK(pageList);
    }

    @AutoLog(value = "手动值班-添加")
    @ApiOperation(value = "手动值班-添加", notes = "手动值班-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody DoWork doWork) {
        doWorkService.add(doWork);
        LogUtil.setOperate(doWork.getWorkName());
        return Result.OK("添加成功！");
    }

    @AutoLog(value = "手动值班-编辑")
    @ApiOperation(value = "手动值班-编辑", notes = "手动值班-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> edit(@RequestBody DoWork doWork) {
        doWorkService.edit(doWork);
        LogUtil.setOperate(doWork.getWorkName());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "手动值班-通过id删除")
    @ApiOperation(value = "手动值班-通过id删除", notes = "手动值班-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        DoWork byId = doWorkService.getById(id);
        if (byId != null) {
            doWorkService.removeById(id);
            LogUtil.setOperate(byId.getWorkName());
        }

        return Result.OK("删除成功!");
    }

    @ApiOperation(value = "手动值班-通过id查询", notes = "手动值班-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        DoWork doWork = doWorkService.getById(id);
        return Result.OK(doWork);
    }

    /**
     * 导出excel
     *
     * @param dto
     */
    @RequestMapping(value = "/exportXls")
    @ApiOperation(value = "手动值班-导出", notes = "手动值班-导出")
    public ModelAndView exportXls(DoWorkPageDTO dto) {
        dto.setPageNo(0);
        dto.setPageSize(-1);
        List<DoWork> list = doWorkService.listExcel(dto);
        return super.exportXls(list, DoWork.class, "手动值班");
    }

    /**
     * 通过excel导入数据
     *
     * @param list
     * @return
     */
    @ApiOperation(value = "手动值班-导入", notes = "手动值班-导入")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(@RequestBody List<DoWork> list) {
        if (list.isEmpty()) {
            return Result.error("导入表格为空");
        }
        doWorkService.addList(list);
        return Result.ok("导入成功");
    }

    @AutoLog(value = "手动值班-发布消息")
    @ApiOperation(value = "手动值班-发布消息", notes = "手动值班-发布消息")
    @PutMapping(value = "/publish")
    public Result<?> publish(@RequestBody PublishMessageDTO dto) {
        doWorkService.publish(dto);
        return Result.OK("发布成功!");
    }

    @AutoLog(value = "手动值班-撤销发布")
    @ApiOperation(value = "手动值班-撤销发布", notes = "手动值班-撤销发布")
    @PutMapping(value = "/revocation/{id}")
    public Result<?> revocation(@PathVariable("id") String id) {
        doWorkService.revocation(id);
        return Result.OK("撤销发布成功!");
    }

}
