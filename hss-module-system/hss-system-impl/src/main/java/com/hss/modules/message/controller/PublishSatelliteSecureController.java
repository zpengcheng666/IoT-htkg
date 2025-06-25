package com.hss.modules.message.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.modules.message.dto.PublishMessageDTO;
import com.hss.modules.message.entity.PublishSatellite;
import com.hss.modules.message.entity.PublishSatelliteSecure;
import com.hss.modules.message.model.PublishSatelliteSecureVO;
import com.hss.modules.message.service.IPublishSatelliteSecureService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

/**
 * @Description: 安全时段信息
 * @Author: zpc
 * @Date: 2022-12-07
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "安全时段信息")
@RestController
@RequestMapping("/message/publishSatelliteSecure")
public class PublishSatelliteSecureController extends HssController<PublishSatelliteSecure, IPublishSatelliteSecureService> {

    @Autowired
    private IPublishSatelliteSecureService publishSatelliteSecureService;

    @ApiOperation(value = "安全时段信息-分页列表查询", notes = "安全时段信息-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<PublishSatelliteSecureVO>> queryPageList(@RequestParam(name = "terminalIds", required = false) String terminalIds,
                                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        IPage<PublishSatelliteSecureVO> page = publishSatelliteSecureService.getPage(new Page<>(pageNo, pageSize), getTerminalIds(terminalIds));
        return Result.OK(page);
    }

    /**
     * 解析str参数
     * @param terminalIds
     * @return
     */
    private List<String> getTerminalIds(String terminalIds) {
        if (StringUtils.isBlank(terminalIds)) {
            return null;
        }
        return  Arrays.asList(terminalIds.split(","));
    }

    @ApiOperation(value = "安全时段信息-添加", notes = "安全时段信息-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody PublishSatelliteSecure publishSatellite) {
        publishSatelliteSecureService.add(publishSatellite);
        return Result.OK("添加成功！");
    }

    @ApiOperation(value = "安全时段信息-通过id查询", notes = "安全时段信息-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        PublishSatelliteSecure publishSatellite = publishSatelliteSecureService.getById(id);
        return Result.OK(publishSatellite);
    }

    @ApiOperation(value = "安全时段信息-编辑", notes = "安全时段信息-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> edit(@RequestBody PublishSatelliteSecure publishSatellite) {
        publishSatelliteSecureService.edit(publishSatellite);
        return Result.OK("编辑成功!");
    }

    @ApiOperation(value = "安全时段信息-通过id删除", notes = "安全时段信息-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<IPage<PublishSatellite>> delete(@RequestParam(name = "id", required = true) String id) {
        publishSatelliteSecureService.delete(id);
        return Result.OK("删除成功!");
    }

    @ApiOperation(value = "安全时段信息-批量删除", notes = "安全时段信息-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<IPage<PublishSatellite>> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.publishSatelliteSecureService.deleteBatch(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }


    @ApiOperation(value = "安全时段信息-批量添加/保存", notes = "安全时段信息-批量添加/保存")
    @RequestMapping(value = "/saveBatch", method = RequestMethod.POST)
    public Result<?> saveBatch( @RequestBody List<PublishSatelliteSecure> list) {
        this.publishSatelliteSecureService.saveBatch(list);
        return Result.OK("导入数据成功");
    }

    @RequestMapping(value = "/exportXls")
    @ApiOperation(value = "安全时段信息-批量导出", notes = "安全时段信息-批量导出")
    public ModelAndView exportXls(@RequestParam(name = "terminalIds", required = false) String terminalIds) {
        IPage<PublishSatelliteSecureVO> page = publishSatelliteSecureService.getPage(new Page<>(1, -1), getTerminalIds(terminalIds));
        return super.exportXls(page.getRecords(), PublishSatelliteSecureVO.class, "卫星临空信息表");
    }


    @ApiOperation(value = "安全时段信息-发布消息", notes = "安全时段信息-发布消息")
    @PutMapping(value = "/publish")
    public Result<?> publish(@RequestBody PublishMessageDTO dto) {
        publishSatelliteSecureService.publish(dto);
        return Result.OK("发布成功!");
    }

    @ApiOperation(value = "安全时段信息-撤销发布", notes = "安全时段信息-撤销发布")
    @PutMapping(value = "/revocation/{id}")
    public Result<?> revocation(@PathVariable("id") String id) {
        publishSatelliteSecureService.revocation(id);
        return Result.OK("撤销发布成功!");
    }
}
