package com.hss.modules.tool.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.system.vo.SelectTreeNode;
import com.hss.core.common.util.LogUtil;
import com.hss.core.common.util.OConvertUtils;
import com.hss.modules.system.entity.BaseDictData;
import com.hss.modules.system.service.IBaseDictDataService;
import com.hss.modules.tool.entity.Datum;
import com.hss.modules.tool.entity.DatumClass;
import com.hss.modules.tool.service.IDatumClassService;
import com.hss.modules.tool.service.IDatumService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @Description: 资料类别
 * @Author: zpc
 * @Date: 2022-12-26
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "资料类别")
@RestController
@RequestMapping("/tool/datumClass")
public class DatumClassController extends HssController<DatumClass, IDatumClassService> {
    @Autowired
    private IDatumClassService datumClassService;
    @Autowired
    private IDatumService datumService;
    @Autowired
    private IBaseDictDataService baseDictDataService;

    /**
     * 分页列表查询
     * @param datumClass
     * @return
     */
    @ApiOperation(value = "资料类别-分页列表查询", notes = "资料类别-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(DatumClass datumClass,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<DatumClass> queryWrapper = new LambdaQueryWrapper<>();
        Page<DatumClass> page = new Page<>(pageNo,pageSize);
        IPage<DatumClass> pageList = datumClassService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

	/**
	* @description: 资料类别-树状图查询
	* @author zpc
	* @date 2022/12/26 10:15
	* @version 1.0
	*/
    @ApiOperation(value = "资料类别-树状", notes = "资料类别-树状")
    @RequestMapping(value = "/datumQueryTreeList", method = RequestMethod.GET)
    public Result<List<SelectTreeNode>> datumQueryTreeList(@RequestParam(name = "ids", required = false) String ids) {
        List<SelectTreeNode> datumList = datumClassService.datumQueryTreeList(ids);
        return Result.OK(datumList);
    }

    /**
    * @description: 通过类别Id查询相关资料信息
    * @author zpc
    * @date 2022/12/26 13:26
    * @version 1.0
    */
    @ApiOperation(value = "通过类别Id查询相关资料信息", notes = "通过类别Id查询相关资料信息")
    @GetMapping(value = "/listById")
    public Result<?> listById(String datumId,String name) {

        LambdaQueryWrapper<Datum> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Datum::getModel, datumId);

        if (StringUtils.isNotEmpty(name)){
            queryWrapper.like(Datum::getName, name);
        }

        List<Datum> list = datumService.list(queryWrapper);

        list.forEach(e ->{
            //位置信息
            String weizhiId = e.getSite();
            if (OConvertUtils.isNotEmpty(weizhiId)) {
                BaseDictData disp1 = baseDictDataService.getById(weizhiId);
                if (OConvertUtils.isNotEmpty(disp1)) {
                    e.setSite_disp(disp1.getName());
                }
            }
            //秘密级别
            String SecretId = e.getSecretLevel();
            if (OConvertUtils.isNotEmpty(SecretId)) {
                BaseDictData disp2 = baseDictDataService.getById(SecretId);
                if (OConvertUtils.isNotEmpty(disp2)) {
                    e.setSecretLevel_disp(disp2.getName());
                }
            }
            //资料类别转码
            String datumId1 = e.getModel();
            if (OConvertUtils.isNotEmpty(datumId1)) {
                DatumClass disp3 = datumClassService.getById(datumId1);
                if (OConvertUtils.isNotEmpty(disp3)) {
                    e.setModel_disp(disp3.getName());
                }
            }
        });

        return Result.OK(list);
    }
    /**
     * 添加
     * @param datumClass
     * @return
     */
    @AutoLog(value = "资料类别-添加")
    @ApiOperation(value = "资料类别-添加", notes = "资料类别-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody DatumClass datumClass) {
        datumClassService.save(datumClass);
        DatumClass byId = datumClassService.getById(datumClass.getId());
        LogUtil.setOperate(byId.getName());
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     * @param datumClass
     * @return
     */
    @AutoLog(value = "资料类别-编辑")
    @ApiOperation(value = "资料类别-编辑", notes = "资料类别-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> edit(@RequestBody DatumClass datumClass) {
        DatumClass tmp = datumClassService.getById(datumClass.getId());
        tmp.setName(datumClass.getName());

        datumClassService.saveOrUpdate(tmp);
        DatumClass byId = datumClassService.getById(datumClass.getId());
        LogUtil.setOperate(byId.getName());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     * @param id
     * @return
     */
    @AutoLog(value = "资料类别-删除")
    @ApiOperation(value = "资料类别-删除", notes = "资料类别-删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        DatumClass byId = datumClassService.getById(id);
        LogUtil.setOperate(byId.getName());
        datumClassService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @ApiOperation(value = "资料类别-批量删除", notes = "资料类别-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.datumClassService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

}
