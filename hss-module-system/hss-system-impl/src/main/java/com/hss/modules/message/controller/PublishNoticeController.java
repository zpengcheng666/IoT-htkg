package com.hss.modules.message.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.util.LogUtil;
import com.hss.modules.message.dto.PublishNoticeMessageDTO;
import com.hss.modules.message.entity.PublishNotice;
import com.hss.modules.message.service.IPublishNoticeService;
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
 * @Description: 通知公告
 * @Author: zpc
 * @Date:   2022-12-08
 * @Version: V1.0
 */
@Slf4j
@Api(tags="通知公告")
@RestController
@RequestMapping("/message/publishNotice")
public class PublishNoticeController extends HssController<PublishNotice, IPublishNoticeService> {

    @Autowired
    private IPublishNoticeService publishNoticeService;


    @ApiOperation(value="通知公告信息表-分页列表查询", notes="通知公告信息表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<PublishNotice>> queryPageList(@RequestParam(name = "terminalIds", required = false) String terminalIds,
                                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){
        List<String> stringList = null;
        if (StringUtils.isBlank(terminalIds)){
            stringList = new ArrayList<>();
        } else {
            stringList = Arrays.asList(terminalIds.split(","));
        }
        Page<PublishNotice> page = new Page<PublishNotice>(pageNo, pageSize);
        return Result.OK(publishNoticeService.queryPage(page, stringList));
    }

    /**
     * 添加
     *
     * @param publishNotice
     * @return
     */
    @AutoLog(value = "通知公告-添加")
    @ApiOperation(value="通知公告-添加", notes="通知公告-添加")
    @PostMapping(value = "/add")
    public Result<IPage<PublishNotice>> add(@RequestBody PublishNotice publishNotice) {
        publishNoticeService.add(publishNotice);
        PublishNotice byId = publishNoticeService.getById(publishNotice.getId());
        LogUtil.setOperate(byId.getTitle());
        return Result.OK("添加成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value="通知公告-通过id查询", notes="通知公告-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
        PublishNotice publishNotice = publishNoticeService.getById(id);
        return Result.OK(publishNotice);
    }

    /**
     * 编辑
     *
     * @param publishNotice
     * @return
     */
    @AutoLog(value = "通知公告-编辑")
    @ApiOperation(value="通知公告-编辑", notes="通知公告-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<?> edit(@RequestBody PublishNotice publishNotice) {
        publishNoticeService.edit(publishNotice);
        PublishNotice byId = publishNoticeService.getById(publishNotice.getId());
        LogUtil.setOperate(byId.getTitle());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "通知公告-通过id删除")
    @ApiOperation(value="通知公告-通过id删除", notes="通知公告-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<IPage<PublishNotice>> delete(@RequestParam(name="id",required=true) String id) {
        PublishNotice byId = publishNoticeService.getById(id);
        LogUtil.setOperate(byId.getTitle());
        publishNoticeService.delete(id);
        return Result.OK("删除成功!");
    }



    @AutoLog(value = "通知公告-发布消息")
    @ApiOperation(value = "通知公告-发布消息", notes = "通知公告-发布消息")
    @PutMapping(value = "/publish")
    public Result<?> publish(@RequestBody PublishNoticeMessageDTO dto) {
        publishNoticeService.publish(dto);
        return Result.OK("发布成功!");
    }

    @ApiOperation(value = "通知公告-查询发布的消息", notes = "通知公告-查询发布的消息")
    @GetMapping(value = "/listByTerminal/{terminalId}")
    public Result<List<PublishNotice>> listByTerminal(@PathVariable("terminalId") String terminalId) {
        List<PublishNotice> list = publishNoticeService.listByTerminal(terminalId);
        return Result.ok(list);
    }
    @AutoLog(value = "通知公告-撤销发布")
    @ApiOperation(value = "通知公告-撤销发布", notes = "通知公告-撤销发布")
    @PutMapping(value = "/revocation/{id}")
    public Result<?> revocation(@PathVariable("id") String id) {
        publishNoticeService.revocation(id);
        return Result.OK("撤销发布成功!");
    }
}
