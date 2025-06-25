package com.hss.modules.system.controller;

import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.modules.system.entity.StatisticsSection;
import com.hss.modules.system.model.StatisticsSectionModel;
import com.hss.modules.system.service.IStatisticsSectionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 分布区间统计设置
 * @Author: zpc
 * @Date: 2022-12-05
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "分布区间统计设置")
@RestController
@RequestMapping("/system/statisticsSection")
public class StatisticsSectionController extends HssController<StatisticsSection, IStatisticsSectionService> {
    @Autowired
    private IStatisticsSectionService statisticsSectionService;

    /**
     * @description: 分布区间统计设置-列表查询
     * @author zpc
     * @date 2023/1/9 20:29
     * @version 1.0
     */
    @ApiOperation(value = "分布区间统计设置-列表查询", notes = "分布区间统计设置-列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(StatisticsSection statisticsSection, String devAttrId) {
        //1.根据属性id来查询统计值，如果属性id为空，查询所有
        List<StatisticsSection> list = statisticsSectionService.queryStatistics(devAttrId);

        //2.将查询后的分布区间统计list，按照属性英文名称分组
        Map<String, List<StatisticsSection>> map = list.stream().collect(Collectors.groupingBy(StatisticsSection::getAttrEnName));

        List<Map<String, Object>> result = new ArrayList<>();
        map.forEach((key, val) -> {
            Map<String, Object> entry = new HashMap<>();
            entry.put("attrEnName", key);
            if (val.size() > 0) {
                entry.put("attrName", val.get(0).getAttrName());
            }
            List<String> collect = val.stream().map(StatisticsSection::getSectionDisplay).collect(Collectors.toList());
            entry.put("intervalValueList",collect);

            List<String> idList = val.stream().map(StatisticsSection::getId).collect(Collectors.toList());
            entry.put("idList",idList);
            result.add(entry);
        });

        return Result.OK(result);
    }

    /**
     * 添加
     *
     * @param model
     * @return
     */
    @AutoLog(value = "分布区间统计设置-新增")
    @ApiOperation(value = "分布区间统计设置-新增", notes = "分布区间统计设置-新增")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody StatisticsSectionModel model) {
        statisticsSectionService.addAll(model);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     * @param model
     * @return
     */
    @AutoLog(value = "分布区间统计设置-修改")
    @ApiOperation(value = "分布区间统计设置-修改", notes = "分布区间统计设置-修改")
    @RequestMapping(value = "/edit", method = {RequestMethod.POST})
    public Result<?> edit(@RequestBody StatisticsSectionModel model) {
        statisticsSectionService.deleteAndInsert(model);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     * @param id
     * @return
     */
    @AutoLog(value = "分布区间统计设置-删除")
    @ApiOperation(value = "分布区间统计设置-删除", notes = "分布区间统计设置-删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        statisticsSectionService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @ApiOperation(value = "分布区间统计设置-批量删除", notes = "分布区间统计设置-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.statisticsSectionService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }
}
