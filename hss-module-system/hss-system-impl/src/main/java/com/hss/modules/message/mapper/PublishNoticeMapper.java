package com.hss.modules.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.modules.message.entity.PublishNotice;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @Description: 通知公告
 * @Author: zpc
 * @Date:   2022-12-08
 * @Version: V1.0
 */
public interface PublishNoticeMapper extends BaseMapper<PublishNotice> {

    /**
     * 查看发布到终端的消息
     * @param now 当前时间
     * @param terminalId 终端id
     * @return
     */
    List<PublishNotice> listByTerminal(@Param("now") Date now, @Param("terminalId")String terminalId);

    /**
     * 检查未发布 已经进入的消息
     * @param date
     * @return
     */
    List<String> listIdInByDate(Date date);

    /**
     * 查询过期时间
     * @param date
     * @return
     */
    List<String> listIdsOutByDate(Date date);

    /**
     * 分页查询
     * @param page
     * @param terminalIds
     * @return
     */
    IPage<PublishNotice> queryPage(Page<PublishNotice> page,@Param("terminalIds") List<String> terminalIds);

    /**
     * 查询没有过期的消息
     * @return
     */
    List<PublishNotice> listNotOverList();

}
