package com.hss.modules.scada.ws.terminal;

import com.hss.modules.system.entity.BaseTerminal;

import java.util.Collection;
import java.util.Set;

/**
 * @ClassDescription: 中心信息服务
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/4/2 9:35
 */
public interface TerminalInfoService {

    /**
     * 是否需要处理
     * @param types 显示类型
     * @return 需要处理
     */
    boolean processHave(Set<String> types);



    /**
     * 获取消息信息
     * @param terminal 终端信息
     * @param types 显示类型
     * @return 消息集合
     */
    Collection<TerminalMsg> list(BaseTerminal terminal, Set<String> types);


}
