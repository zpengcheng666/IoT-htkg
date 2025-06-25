package com.hss.modules.system.controller;

import com.hss.core.common.api.vo.Result;
import com.hss.modules.system.monitorThing.AjaxResult;
import com.hss.modules.system.monitorThing.BaseController;
import com.hss.modules.system.monitorThing.Server;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
* @description: 主机状态监测
* @author zpc
* @date 2024/3/20 15:34
* @version 1.0
*/
@RestController
@Api(tags = "主机状态监测")
@RequestMapping("/monitor/server")
@Slf4j
public class BaseComputerStatusController extends BaseController {
    private String prefix = "monitor/server";

    @ApiOperation(value="主机状态监测", notes="主机状态监测")
    @GetMapping()
    public AjaxResult getInfo() throws Exception {
        Server server = new Server();
        server.copyTo();
        server.setDate(new Date());
        return AjaxResult.success(server);
    }

    @GetMapping(value = "/status")
    public Result<?> status() {
        return Result.OK();
    }
}
