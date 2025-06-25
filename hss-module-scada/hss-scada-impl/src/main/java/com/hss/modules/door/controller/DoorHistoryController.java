package com.hss.modules.door.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.util.LogUtil;
import com.hss.modules.door.dto.DoorHistoryAddDTO;
import com.hss.modules.door.dto.OpenDoorDTO;
import com.hss.modules.door.entity.DoorHistory;
import com.hss.modules.door.service.IDoorHistoryService;
import com.hss.modules.door.vo.GetLastVO;
import com.hss.modules.door.vo.OpenDoorVO;
import com.hss.modules.scada.entity.ConSheBei;
import com.hss.modules.scada.service.IConSheBeiService;
import com.hss.modules.system.entity.BaseDictData;
import com.hss.modules.system.service.IBaseDictDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.Date;

/**
 * @Description: -门禁
 * @Author: zpc、chushubin、handong、wuyihan
 * @Date: 2022-12-05
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "进出记录-门禁")
@RestController
@RequestMapping("/door/doorHistory")
public class DoorHistoryController extends HssController<DoorHistory, IDoorHistoryService> {
    @Autowired
    private IDoorHistoryService doorHistoryService;

    @Autowired
    private IBaseDictDataService baseDictDataService;

    @Autowired
    private IConSheBeiService conSheBeiService;

    @ApiOperation(value = "根据id查询", notes = "根据id查询")
    @GetMapping(value = "/{id}")
    public Result<DoorHistory> getById(@PathVariable("id") String id) {
        return Result.OK(doorHistoryService.getById(id));
    }

    @ApiOperation(value = "进出记录-门禁-分页列表查询", notes = "进出记录-门禁-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<OpenDoorVO>> queryPageList(OpenDoorDTO dto) {
        IPage<OpenDoorVO> resultPage = doorHistoryService.pageList(dto);
        return Result.OK(resultPage);
    }

    /**
     * 门禁进出记录导出
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/doorInout/exportXls")
    public ModelAndView exportXls(OpenDoorDTO dto) {
        dto.setPageNo(1);
        dto.setPageSize(-1);
        IPage<OpenDoorVO> resultPage = doorHistoryService.pageList(dto);
        return super.exportXls(resultPage.getRecords(), OpenDoorVO.class, "进出记录");
    }


    @AutoLog(value = "添加进出门记录")
    @ApiOperation(value = "进出记录-门禁-添加", notes = "进出记录-门禁-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody DoorHistoryAddDTO dto) {
        doorHistoryService.add(dto);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑进出门记录
     *
     * @param doorHistory
     * @return
     */
    @AutoLog(value = "编辑进出门记录")
    @ApiOperation(value = "进出记录-门禁-编辑", notes = "进出记录-门禁-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.POST})
    public Result<?> edit(@RequestBody DoorHistory doorHistory) {
        //写入更改时间
        doorHistory.setUpdatedTime(new Date());
        doorHistoryService.updateById(doorHistory);
        LogUtil.setOperate(doorHistory.getDoorName());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除进出门记录
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除进出门记录")
    @ApiOperation(value = "进出记录-门禁-通过id删除", notes = "进出记录-门禁-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        DoorHistory byId = doorHistoryService.getById(id);
        doorHistoryService.removeById(id);
        LogUtil.setOperate(byId.getDoorName());
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除进出门记录
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "批量删除进出门记录")
    @ApiOperation(value = "进出记录-门禁-批量删除", notes = "进出记录-门禁-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.doorHistoryService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }


    @Override
    protected void hanleDataDetail(DoorHistory e) {
        //开门类型
        BaseDictData type = baseDictDataService.getById(e.getOpenType());
        e.setOpenType_disp(type == null ? "" : type.getName());
        //门名称
        ConSheBei doorId = conSheBeiService.getById(e.getDoorId());
        e.setDoorName(doorId == null ? "" : doorId.getName());
    }

    @ApiOperation(value = "查询最近一次通行记录", notes = "查询最近一次通行记录")
    @GetMapping(value = "/getLast")
    public Result<GetLastVO> getLast(@ApiParam(value = "deviceId", required = true) @RequestParam("deviceId") String deviceId) {
        GetLastVO vo = doorHistoryService.getLast(deviceId);
        return Result.OK(vo);
    }


}
