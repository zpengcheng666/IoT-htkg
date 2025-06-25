package com.hss.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.util.LogUtil;
import com.hss.core.common.util.OConvertUtils;
import com.hss.modules.system.entity.BaseDictData;
import com.hss.modules.system.entity.BaseDictType;
import com.hss.modules.system.model.OptionModel;
import com.hss.modules.system.service.IBaseDictDataService;
import com.hss.modules.system.service.IBaseDictTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 字典数据
 * @Author: zpc、wuyihan
 * @Date: 2024-03-21
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "字典数据")
@RestController
@RequestMapping("/system/baseDictData")
public class BaseDictDataController extends HssController<BaseDictData, IBaseDictDataService> {
    @Autowired
    private IBaseDictDataService baseDictDataService;

    @Autowired
    private IBaseDictTypeService baseDictTypeService;

    /**
     * 添加
     *
     * @param baseDictData
     * @return
     */
    @AutoLog(value = "字典数据-添加")
    @ApiOperation(value = "字典数据-添加", notes = "字典数据-添加")
    @PostMapping(value = "/addDictData")
    public Result<?> addDictData(@RequestBody BaseDictData baseDictData) {
        baseDictData.setDeleted(0);
        baseDictData.setIsEnable(0);
        baseDictData.setEditable(0);
        baseDictData.setCreatetime(new Date());
        baseDictDataService.save(baseDictData);
        BaseDictData byId = baseDictDataService.getById(baseDictData.getId());
        LogUtil.setOperate(byId.getName());
        return Result.OK("添加成功！");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "字典数据-删除")
    @ApiOperation(value = "字典数据-删除", notes = "字典数据-删除")
    @DeleteMapping(value = "/deleteDictData")
    public Result<?> deleteDictData(@RequestParam(name = "id", required = true) String id) {
        BaseDictData byId = baseDictDataService.getById(id);
        LogUtil.setOperate(byId.getName());
        baseDictDataService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 通过id查询类型下的字典数据
     *
     * @param dictTypeId
     * @return
     */
    @ApiOperation(value = "字典数据-通过id查询类型下的字典数据", notes = "字典数据-通过id查询类型下的字典数据")
    @GetMapping(value = "/selectByIdDictData")
    public Result<?> selectById(@RequestParam(name = "dictTypeId", required = true) String dictTypeId) {
        List<BaseDictData> baseDictData = baseDictDataService.getDictDataById(dictTypeId);
        return Result.OK(baseDictData);
    }

    /**
     * 添加字典数据
     *
     * @param baseDictData
     * @return
     */
    @AutoLog(value = "字典数据-添加字典数据")
    @ApiOperation(value = "字典数据-添加字典数据", notes = "字典数据-添加字典数据")
    @PostMapping(value = "/addDict")
    public Result<?> addDict(@RequestBody BaseDictData baseDictData) {
        baseDictData.setDeleted(0);
        baseDictData.setIsEnable(0);
        baseDictData.setEditable(0);
        baseDictData.setCreatetime(new Date());
        baseDictDataService.save(baseDictData);
        BaseDictData byId = baseDictDataService.getById(baseDictData.getId());
        LogUtil.setOperate(byId.getName());
        return Result.OK("添加成功！");
    }

    /**
     * 编辑字典名称与字典排序值
     *
     * @param baseDictData
     * @return
     */
    @AutoLog(value = "编辑字典名称与字典排序值")
    @ApiOperation(value = "编辑字典名称与字典排序值", notes = "编辑字典名称与字典排序值")
    @RequestMapping(value = "/editDictNameAndCode", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> editDictTypeAndCode(@RequestBody BaseDictData baseDictData) {
        baseDictDataService.editDictTypeAndCode(baseDictData);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除字典数据
     *
     * @param id
     * @return
     */
    @AutoLog(value = "字典类型-通过id删除字典数据")
    @ApiOperation(value = "字典类型-通过id删除字典数据", notes = "字典类型-通过id删除字典数据")
    @DeleteMapping(value = "/removeDictData")
    public Result<?> removeDictData(@RequestParam(name = "id", required = true) String id) {
        baseDictDataService.deleteById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 获取通用Options
     *
     * @param type
     * @return
     */
    @ApiOperation(value = "获取通用Options", notes = "获取通用Options-> 1.secret-level:涉密等级；2.on-duty-type：值班类别 ；3.skill-level；技能等级4.nationality-list：国别；" +
            "5.satellite-type-list：卫星类型；6.emergency-type：突发事件类型；7.officer-military-rank：军官军衔；8.sergeancy-military-rank；" +
            "9.civilian-military-rank：文职军衔；10.alarm-level-list：报警级别；11.alarm-type-list：报警类型；12.credentials-type：证件类型；" +
            "13.user-major：专业；14.maintain-item-class：保养项目类别；15.quality-condition：质量状况；16.location-class：管理区；17.soldier-military-rank：士兵军衔" +
            "18.positionid:职称，19.address：地址 20.location-category :位置类型 21.message-type：消息类型 22.openType：开门类型" +
            "23.cardResult：刷卡结果 24.result-approval 审批结果 25.post-duty：岗位 26.postion-duty：职位 27.time-period：时间段" +
            "28.time-cyclical：周期")
    @GetMapping(value = "/queryOptions")
    public Result<?> queryOptions(@RequestParam(name = "type") String type) {
        LambdaQueryWrapper<BaseDictType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BaseDictType::getEnName, type);
        BaseDictType dictType = baseDictTypeService.getOne(queryWrapper);
        if (dictType == null) {
            return Result.OK(new ArrayList<OptionModel>());
        }
        LambdaQueryWrapper<BaseDictData> dataQueryWrapper = new LambdaQueryWrapper<>();
        dataQueryWrapper.eq(BaseDictData::getDictTypeId, dictType.getId()).orderByAsc(BaseDictData::getOrderNum);
        List<BaseDictData> dataList = baseDictDataService.list(dataQueryWrapper);
        List<OptionModel> options = dataList.stream().map(e -> {
            OptionModel tmp = new OptionModel();
            tmp.setId(e.getId());
            tmp.setCode(e.getCode());
            tmp.setName(e.getName());
            tmp.setEnName(e.getEnName());
            return tmp;
        }).collect(Collectors.toList());
        return Result.OK(options);
    }
}
