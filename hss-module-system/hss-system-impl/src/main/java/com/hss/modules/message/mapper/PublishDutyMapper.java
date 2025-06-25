package com.hss.modules.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.modules.message.entity.PublishDuty;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @Description: 值班安排
 * @Author: zpc
 * @Date:   2022-12-08
 * @Version: V1.0
 */
public interface PublishDutyMapper extends BaseMapper<PublishDuty> {
    /**
     * 消息发布
     * @param now
     * @param messageId
     * @return
     */
    int publish(@Param("now") Date now, @Param("id") String messageId);

    /**
     * 查看发布到终端的消息
     * @param terminalId 终端id
     * @return
     */
    List<PublishDuty> listByTerminal(String terminalId);

    /**
     * 分页查询
     * @param page
     * @param terminalIds
     * @return
     */
    IPage<PublishDuty> queryPage(Page<PublishDuty> page, @Param("terminalIds") List<String> terminalIds);
}
