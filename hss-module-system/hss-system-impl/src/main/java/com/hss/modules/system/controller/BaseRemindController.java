package com.hss.modules.system.controller;

import com.hss.core.common.api.vo.Result;
import com.hss.core.common.aspect.annotation.AutoLog;
import com.hss.core.common.system.base.controller.HssController;
import com.hss.core.common.util.LogUtil;
import com.hss.modules.system.entity.BaseRemind;
import com.hss.modules.system.service.IBaseRemindService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 提醒设置
 * @Author: zpc
 * @Date: 2022-11-21
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "提醒设置")
@RestController
@RequestMapping("/system/baseRemind")
public class BaseRemindController extends HssController<BaseRemind, IBaseRemindService> {
    @Autowired
    private IBaseRemindService baseRemindService;

    /**
     * 分页列表查询
     *
     * @return
     */
    @ApiOperation(value = "提醒设置-分页列表查询", notes = "提醒设置-分页列表查询")
    @GetMapping(value = "/listRemind")
    public Result<?> queryPageList() {
        List<BaseRemind> list = this.baseRemindService.list();
        Map<String, List<BaseRemind>> map = list.stream().collect(Collectors.groupingBy(BaseRemind::getTypeTag));
        List<Map<String, Object>> result = new ArrayList<>();
        map.forEach((key, val) -> {
            Map<String, Object> entry = new HashMap<>();
            entry.put("typeType", key);
            if (val.size() > 0) {
                entry.put("typeTypeName", val.get(0).getTypename());
            }
            entry.put("childs", val);
            result.add(entry);
        });

        return Result.OK(result);
    }

    /**
     * 编辑value
     *
     * @param baseRemind
     * @return
     */
    @AutoLog(value = "提醒设置-编辑value")
    @ApiOperation(value = "提醒设置-编辑value", notes = "提醒设置-编辑value")
    @RequestMapping(value = "/edit", method = {RequestMethod.POST})
    public Result<?> edit(@RequestBody BaseRemind baseRemind) {
        baseRemindService.updateValue(baseRemind);
        BaseRemind byId = baseRemindService.getById(baseRemind.getId());
        LogUtil.setOperate(byId.getTypename());
        return Result.OK("编辑成功!");
    }
}
