package com.hss.modules.system.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* @description: 系统进程监测
* @author zpc
* @date 2024/3/20 15:34
* @version 1.0
*/
@RestController
@Api(tags = "系统进程监测")
@RequestMapping("actuator/metrics")
@Slf4j
public class BaseProcessStatusController {

    @ApiOperation(value="系统进程监测-JVM守护线程数", notes="系统进程监测-JVM守护线程数")
    @GetMapping("/{process.start.time}")
    public Object getProcessInfo() {
        return null;
    }
}
