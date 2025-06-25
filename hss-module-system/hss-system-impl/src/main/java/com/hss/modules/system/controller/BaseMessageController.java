package com.hss.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.util.LogUtil;
import com.hss.core.common.util.LoginUserUtils;
import com.hss.core.common.util.OConvertUtils;
import com.hss.modules.system.entity.BaseDictData;
import com.hss.modules.system.entity.BaseMessage;
import com.hss.modules.system.service.IBaseDictDataService;
import com.hss.modules.system.service.IBaseMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;

/**
 * @Description: 消息管理
 * @Author: zpc
 * @Date: 2022-11-03
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "消息管理")
@RestController
@RequestMapping("/system/baseMessage")
public class BaseMessageController extends HssController<BaseMessage, IBaseMessageService> {
    @Autowired
    private IBaseMessageService baseMessageService;

    @Autowired
    private IBaseDictDataService baseDictDataService;

    /**
     * 分页列表查询
     *
     * @param baseMessage
     * @return
     */
    @ApiOperation(value = "消息管理-分页列表查询", notes = "消息管理-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(BaseMessage baseMessage,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<BaseMessage> queryWrapper = new LambdaQueryWrapper<>();
        if (OConvertUtils.isNotEmpty(baseMessage.getDetail())) {
            queryWrapper.like(BaseMessage::getDetail, baseMessage.getDetail());
        }

        Page<BaseMessage> page = new Page<>(pageNo,pageSize);
        IPage<BaseMessage> pageList = baseMessageService.page(page, queryWrapper);
        pageList.getRecords().forEach(e -> {
            //消息类别
            BaseDictData type = baseDictDataService.getById(e.getType());
            e.setType_disp(type == null ? "": type.getName());

        });

        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param baseMessage
     * @return
     */
    @AutoLog(value = "消息管理-添加")
    @ApiOperation(value = "消息管理-添加", notes = "消息管理-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BaseMessage baseMessage) {
        baseMessage.setCreateTime(new Date());
        baseMessage.setStatus(0);
        baseMessage.setUserId(LoginUserUtils.getUser().getId());
        baseMessageService.save(baseMessage);
        BaseMessage byId = baseMessageService.getById(baseMessage.getId());
        LogUtil.setOperate(byId.getType());
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param baseMessage
     * @return
     */
    @AutoLog(value = "消息管理-编辑")
    @ApiOperation(value = "消息管理-编辑", notes = "消息管理-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> edit(@RequestBody BaseMessage baseMessage) {
        baseMessageService.updateById(baseMessage);
        BaseMessage byId = baseMessageService.getById(baseMessage.getId());
        LogUtil.setOperate(byId.getType());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "消息管理-删除")
    @ApiOperation(value = "消息管理-删除", notes = "消息管理-删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        BaseMessage byId = baseMessageService.getById(id);
        LogUtil.setOperate(byId.getType());
        baseMessageService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "消息管理-批量删除", notes = "消息管理-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.baseMessageService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "消息管理-通过id查询", notes = "消息管理-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BaseMessage baseMessage = baseMessageService.getById(id);
        return Result.OK(baseMessage);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param baseMessage
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BaseMessage baseMessage) {
        return super.exportXls(request, baseMessage, BaseMessage.class, "消息管理");
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
        return super.importExcel(request, response, BaseMessage.class);
    }

}
