package com.hss.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.system.entity.SysBackup;

/**
 * @Description: 系统备份
 * @Author: zpc
 * @Date: 2023-05-15
 * @Version: V1.0
 */
public interface ISysBackupService extends IService<SysBackup> {

    /**
     * 手动执行备份
     *
     * @param sysBackup
     * @return
     */
    boolean backup(SysBackup sysBackup);

}
