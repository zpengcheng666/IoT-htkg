package com.hss.modules.message.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.message.dto.PublishNoticeMessageDTO;
import com.hss.modules.message.entity.PublishNotice;

import java.util.List;

/**
 * @Description: 通知公告
 * @Author: zpc
 * @Date:   2022-12-08
 * @Version: V1.0
 */
public interface IPublishNoticeService extends IService<PublishNotice> {

    /**
     * 发布消息
     * @param dto
     */
    void publish(PublishNoticeMessageDTO dto);

    /**
     * 新增
     * @param publishNotice
     */
    void add(PublishNotice publishNotice);

    /**
     * 删除消息
     * @param id
     */
    void delete(String id);

    /**
     * 查看发布到终端的信息
     * @param terminalId
     * @return
     */
    List<PublishNotice> listByTerminal(String terminalId);

    /**
     * 插销发布
     * @param id
     */
    void revocation(String id);



    /**
     * 分页查询
     * @param page
     * @param terminalIds
     * @return
     */
    IPage<PublishNotice> queryPage(Page<PublishNotice> page, List<String> terminalIds);

    /**
     * 编辑
     * @param publishNotice
     */
    void edit(PublishNotice publishNotice);

    /**
     * 查询没有过期的消息
     * @return
     */
    List<PublishNotice> listNotOverList();

    /**
     * 过期任务
     * @param message
     */
    void overMessage(PublishNotice message);

    /**
     * 预发布任务
     * @param message
     */
    void inMessage(PublishNotice message);
}
