package com.hss.modules.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.message.entity.PublishDutyTerminal;

import java.util.List;

/**
 * @Description: 值班消息和终端关系表
 * @Author: zpc
 * @Date:   2022-12-23
 * @Version: V1.0
 */
public interface IPublishDutyTerminalService extends IService<PublishDutyTerminal> {

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

    /**
     * 根据终端id查询
     * @param terminalId 终端id
     * @return 值班安排ids
     */
    List<String> listDutyIdsByTerminalId(String terminalId);
}
