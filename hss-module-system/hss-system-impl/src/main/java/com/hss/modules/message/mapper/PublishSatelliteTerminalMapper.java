package com.hss.modules.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.message.entity.PublishSatelliteTerminal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 卫星消息和终端关系表
 * @Author: zpc
 * @Date:   2022-12-23
 * @Version: V1.0
 */
public interface PublishSatelliteTerminalMapper extends BaseMapper<PublishSatelliteTerminal> {

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
    List<String> listTerminalIdByMessageId(@Param("messageIds") List<String> messageIds);

}
