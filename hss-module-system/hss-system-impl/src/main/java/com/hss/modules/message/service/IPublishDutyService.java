package com.hss.modules.message.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.message.dto.PublishMessageDTO;
import com.hss.modules.message.entity.PublishDuty;

import java.util.List;

/**
 * @Description: 值班安排
 * @Author: zpc
 * @Date:   2022-12-08
 * @Version: V1.0
 */
public interface IPublishDutyService extends IService<PublishDuty> {
    /**
     * 发布消息
     * @param dto
     */
    void publish(PublishMessageDTO dto);

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
    List<PublishDuty> listByTerminal(String terminalId);

    /**
     * 插销发布
     * @param id
     */
    void revocation(String id);


    /**
     * 新增
     * @param publishDuty
     */
    void add(PublishDuty publishDuty);

    /**
     * 查询
     * @param id
     * @return
     */
    PublishDuty queryById(String id);

    /**
     * 分页查询
     * @param page
     * @param terminalIds
     * @return
     */
    IPage<PublishDuty> queryPage(Page<PublishDuty> page, List<String> terminalIds);

    /**
     * 编辑
     * @param publishDuty
     */
    void edit(PublishDuty publishDuty);
}
