package com.hss.modules.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.message.entity.DoWorkTerminal;

import java.util.List;

/**
 * @Description: 手动值班终端中间表
 * @Author: zpc
 * @Date:   2023-12-06
 * @Version: V1.0
 */
public interface IDoWorkTerminalService extends IService<DoWorkTerminal> {

    /**
     * 根据id查询终端id
     * @param doWorkId 查询终端信息
     * @return 终端id列表
     */
    List<String> listTerminalIdsByDoWorkId(String doWorkId);

    /**
     * 根据信息删除
     * @param messageId 信息id
     */
    void delByMessageId(String messageId);
}
