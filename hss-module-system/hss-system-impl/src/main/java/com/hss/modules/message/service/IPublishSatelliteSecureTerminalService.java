package com.hss.modules.message.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.message.entity.PublishSatelliteSecureTerminal;

import java.util.List;

/**
 * @Description:
 * @Author: zpc
 * @Date:   2022-12-07
 * @Version: V1.0
 */
public interface IPublishSatelliteSecureTerminalService extends IService<PublishSatelliteSecureTerminal> {

    void add(String msgId, List<String> terminalIds);

    void removeByMsgId(String msgId);

    List<String> listTerminalIdByMessageId(List<String> terminalIds);
}
