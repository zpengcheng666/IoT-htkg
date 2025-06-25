package com.hss.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.core.common.api.vo.Result;
import com.hss.modules.system.entity.SysRecovery;

/**
 * @Description: 系统备份
 * @Author: zpc
 * @Date:   2023-05-15
 * @Version: V1.0
 */
public interface ISysRecoveryService extends IService<SysRecovery> {

    Result<?> recovery(SysRecovery sysRecovery);
}
