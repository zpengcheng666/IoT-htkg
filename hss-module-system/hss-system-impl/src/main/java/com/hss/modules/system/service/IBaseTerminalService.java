package com.hss.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.system.entity.BaseTerminal;

import java.util.List;

/**
 * @Description: 终端基本信息表
 * @Author: zpc
 * @Date:   2022-11-22
 * @Version: V1.0
 */
public interface IBaseTerminalService extends IService<BaseTerminal> {

    void saveTerminalInfo(BaseTerminal baseTerminal);

    void updateTerminalInfo(BaseTerminal baseTerminal);
}
