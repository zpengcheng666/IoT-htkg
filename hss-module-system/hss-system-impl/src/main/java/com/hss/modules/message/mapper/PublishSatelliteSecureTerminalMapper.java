package com.hss.modules.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.message.entity.PublishSatelliteSecureTerminal;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @Description: 安全时段
 * @Author: zpc
 * @Date:   2022-12-07
 * @Version: V1.0
 */
public interface PublishSatelliteSecureTerminalMapper extends BaseMapper<PublishSatelliteSecureTerminal> {

    void removeByMsgId(String msgId);

    List<String> listTerminalIdByMessageId(@Param("msgIds") List<String> msgIds);
}
