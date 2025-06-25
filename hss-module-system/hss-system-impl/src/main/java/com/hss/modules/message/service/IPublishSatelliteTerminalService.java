package com.hss.modules.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.message.entity.PublishSatelliteTerminal;

import java.util.List;

/**
 * @Description: 卫星消息和终端关系表
 * @Author: zpc
 * @Date:   2022-12-23
 * @Version: V1.0
 */
public interface IPublishSatelliteTerminalService extends IService<PublishSatelliteTerminal> {

    /**
     * 根据messageId删除
     * @param messageId
     */
    void delByMessageId(String messageId);

    /**
     * 根据messageIds批量删除
     * @param ids
     */
    void deleteBatchByMessageIds(List<String> ids);

    /**
     * 根据messageId查询
     * @param messageIds
     * @return
     */
    List<String> listTerminalIdByMessageId(List<String> messageIds);

}
