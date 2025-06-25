package com.hss.modules.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.message.entity.DoWorkTerminal;

import java.util.List;

/**
 * @Description: 手动值班终端中间表
 * @Author: zpc
 * @Date:   2023-12-06
 * @Version: V1.0
 */
public interface DoWorkTerminalMapper extends BaseMapper<DoWorkTerminal> {

    List<String> listTerminalIdsByDoWorkId(String doWorkId);

    void delByMessageId(String messageId);
}
