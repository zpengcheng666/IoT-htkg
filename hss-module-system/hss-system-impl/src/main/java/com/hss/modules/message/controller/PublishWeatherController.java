package com.hss.modules.message.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.util.LogUtil;
import com.hss.modules.message.dto.PublishMessageDTO;
import com.hss.modules.message.entity.PublishWeather;
import com.hss.modules.message.entity.PublishWeatherTerminal;
import com.hss.modules.message.service.IPublishWeatherService;
import com.hss.modules.message.service.IPublishWeatherTerminalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 气象信息
 * @Author: zpc
 * @Date:   2022-12-06
 * @Version: V1.0
 */
@Slf4j
@Api(tags="气象信息")
@RestController
@RequestMapping("/message/publishWeather")
public class PublishWeatherController extends HssController<PublishWeather, IPublishWeatherService> {
    @Autowired
    private IPublishWeatherService publishWeatherService;

    @Autowired
    private IPublishWeatherTerminalService publishWeatherTerminalService;

    @ApiOperation(value="气象信息表-分页列表查询", notes="气象信息表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<PublishWeather>> queryPageList(@RequestParam(name = "terminalIds", required = false) String terminalIds,
                                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){

        List<String> stringList = null;
        if (StringUtils.isBlank(terminalIds)){
            stringList = new ArrayList<>();
        } else {
            stringList = Arrays.asList(terminalIds.split(","));
        }

        //1.通过前端的终端ids，从PublishWeatherTerminal中查询
        List<String> weatherIdList = new ArrayList<>();

        if (!stringList.isEmpty()){
            QueryWrapper<PublishWeatherTerminal> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("DISTINCT weather_id");
            queryWrapper.in("terminal_id",stringList);
            List<PublishWeatherTerminal> terminalList = publishWeatherTerminalService.list(queryWrapper);
            if (terminalList != null){
                weatherIdList = terminalList.stream().map(PublishWeatherTerminal::getWeatherId).collect(Collectors.toList());
            }
        }
        log.info("weatherIdList = " + weatherIdList);

        // 2. 根据weatherIdList 查询Weathers列表
        Page<PublishWeather> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<PublishWeather> weatherLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!weatherIdList.isEmpty()){
            weatherLambdaQueryWrapper.in(PublishWeather::getId, weatherIdList);
        }

        IPage<PublishWeather> pageList = publishWeatherService.page(page, weatherLambdaQueryWrapper);

        // 3. 需要对每一条记录，填充一下TerminalIds
        if (!pageList.getRecords().isEmpty()){
            weatherIdList = new ArrayList<>();
            weatherIdList = pageList.getRecords().stream().map(PublishWeather::getId).collect(Collectors.toList());

            LambdaQueryWrapper<PublishWeatherTerminal> queryWrapper2 = new LambdaQueryWrapper<>();
            queryWrapper2.in(PublishWeatherTerminal::getWeatherId, weatherIdList);
            List<PublishWeatherTerminal> terminalList = publishWeatherTerminalService.list(queryWrapper2);

            Map<String, List<PublishWeatherTerminal>> map =  terminalList.stream().collect(Collectors.groupingBy(PublishWeatherTerminal::getWeatherId));
            pageList.getRecords().forEach(item -> {
                String key = item.getId();
                List<PublishWeatherTerminal> tmp = map.get(key);
                if (CollectionUtil.isNotEmpty(tmp)){
                    List<String> collect = tmp.stream().map(PublishWeatherTerminal::getTerminalId).collect(Collectors.toList());
                    item.setTerminalIds(collect);
                }
            });
        }

        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param publishWeather
     * @return
     */
    @AutoLog(value = "气象信息-添加")
    @ApiOperation(value="气象信息-添加", notes="气象信息-添加")
    @PostMapping(value = "/add")
    public Result<IPage<PublishWeather>> add(@RequestBody PublishWeather publishWeather) {
        publishWeatherService.add(publishWeather);
        PublishWeather byId = publishWeatherService.getById(publishWeather.getId());
        LogUtil.setOperate(byId == null ? "" : byId.getContent());
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param publishWeather
     * @return
     */
    @AutoLog(value = "气象信息-编辑")
    @ApiOperation(value="气象信息-编辑", notes="气象信息-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<?> edit(@RequestBody PublishWeather publishWeather) {
        publishWeatherService.edit(publishWeather);
        PublishWeather byId = publishWeatherService.getById(publishWeather.getContent());
        LogUtil.setOperate(byId == null ? "": byId.getContent());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @ApiOperation(value="气象信息-通过id删除", notes="气象信息-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name="id") String id) {
        PublishWeather byId = publishWeatherService.getById(id);
        LogUtil.setOperate(byId == null ? "" : byId.getContent());
        publishWeatherService.delete(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @ApiOperation(value="气象信息-批量删除", notes="气象信息-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<IPage<PublishWeather>> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        this.publishWeatherService.deleteBatch(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value="气象信息-通过id查询", notes="气象信息-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
        PublishWeather publishWeather = publishWeatherService.getById(id);
        return Result.OK(publishWeather);
    }

    @AutoLog(value = "气象信息-发布消息")
    @ApiOperation(value = "气象信息-发布消息", notes = "气象信息-发布消息")
    @PutMapping(value = "/publish")
    public Result<?> publish(@RequestBody PublishMessageDTO dto) {
        publishWeatherService.publish(dto);
        return Result.OK("发布成功!");
    }
    @AutoLog(value = "气象信息-撤销发布")
    @ApiOperation(value = "气象信息-撤销发布", notes = "气象信息-撤销发布")
    @PutMapping(value = "/revocation/{id}")
    public Result<?> revocation(@PathVariable("id") String id) {
        publishWeatherService.revocation(id);
        return Result.OK("撤销发布成功!");
    }

    @ApiOperation(value = "气象信息-查询发布的消息", notes = "气象信息-查询发布的消息")
    @GetMapping(value = "/getByTerminal/{terminalId}")
    public Result<?> getByTerminal(@PathVariable("terminalId") String terminalId) {
        List<PublishWeather> byTerminal = publishWeatherService.getByTerminal(terminalId);
        return Result.ok(byTerminal);
    }
}
