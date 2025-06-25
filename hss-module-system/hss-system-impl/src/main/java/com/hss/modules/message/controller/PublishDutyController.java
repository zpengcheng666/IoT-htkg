package com.hss.modules.message.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.util.LogUtil;
import com.hss.modules.message.dto.PublishMessageDTO;
import com.hss.modules.message.entity.PublishDuty;
import com.hss.modules.message.service.IPublishDutyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 值班安排
 * @Author: zpc
 * @Date: 2022-12-08
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "值班安排")
@RestController
@RequestMapping("/message/publishDuty")
public class PublishDutyController extends HssController<PublishDuty, IPublishDutyService> {

    @Autowired
    private IPublishDutyService publishDutyService;



    @ApiOperation(value = "值班功能列表-分页列表查询", notes = "值班功能列表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<PublishDuty>> queryPageList(@RequestParam(name = "terminalIds", required = false) String terminalIds,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        List<String> stringList = null;
        if (StringUtils.isBlank(terminalIds)){
            stringList = new ArrayList<>();
        } else {
            stringList = Arrays.asList(terminalIds.split(","));
        }
        Page<PublishDuty> page = new Page<>(pageNo, pageSize);
        return Result.OK(publishDutyService.queryPage(page, stringList));
    }
    @AutoLog(value = "值班安排-添加")
    @ApiOperation(value = "值班安排-添加", notes = "值班安排-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody PublishDuty publishDuty) {
        publishDutyService.add(publishDuty);
        PublishDuty byId = publishDutyService.getById(publishDuty.getId());
        LogUtil.setOperate(byId.getName());
        return Result.OK("添加成功！");
    }
    @ApiOperation(value = "值班功能列表-通过id查询", notes = "值班功能列表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        PublishDuty publishDuty = publishDutyService.queryById(id);
        return Result.OK(publishDuty);
    }
    @AutoLog(value = "值班安排-编辑")
    @ApiOperation(value = "值班安排-编辑", notes = "值班安排-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> edit(@RequestBody PublishDuty publishDuty) {
        publishDutyService.edit(publishDuty);
        PublishDuty byId = publishDutyService.getById(publishDuty.getId());
        LogUtil.setOperate(byId.getName());
        return Result.OK("编辑成功!");
    }
    @AutoLog(value = "值班安排-删除")
    @ApiOperation(value = "值班安排-删除", notes = "值班安排-删除")
    @DeleteMapping(value = "/delete")
    public Result<IPage<PublishDuty>> delete(@RequestParam(name = "id", required = true) String id) {
        PublishDuty byId = publishDutyService.getById(id);
        LogUtil.setOperate(byId.getName());
        publishDutyService.delete(id);
        return Result.OK("删除成功!");
    }

    @AutoLog(value = "值班消息-发布")
    @ApiOperation(value = "值班消息-发布", notes = "值班消息-发布")
    @PutMapping(value = "/publish")
    public Result<?> publish(@RequestBody PublishMessageDTO dto) {
        publishDutyService.publish(dto);
        return Result.OK("发布成功!");
    }

    @ApiOperation(value = "值班功能列表-查询发布的消息", notes = "值班功能列表-查询发布的消息")
    @GetMapping(value = "/listByTerminal/{terminalId}")
    public Result<List<PublishDuty>> listByTerminal(@PathVariable("terminalId") String terminalId) {
        List<PublishDuty> list = publishDutyService.listByTerminal(terminalId);
        return Result.ok(list);
    }

    @AutoLog(value = "值班消息-撤销发布")
    @ApiOperation(value = "值班消息-撤销发布", notes = "值班消息-撤销发布")
    @PutMapping(value = "/revocation/{id}")
    public Result<?> revocation(@PathVariable("id") String id) {
        publishDutyService.revocation(id);
        return Result.OK("撤销发布成功!");
    }
}
