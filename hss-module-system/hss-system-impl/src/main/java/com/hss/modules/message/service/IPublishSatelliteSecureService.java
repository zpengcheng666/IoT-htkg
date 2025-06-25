package com.hss.modules.message.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.message.dto.PublishMessageDTO;
import com.hss.modules.message.entity.PublishSatelliteSecure;
import com.hss.modules.message.model.PublishSatelliteSecureVO;

import java.util.List;

/**
 * @Description: 卫星临空信息
 * @Author: zpc
 * @Date:   2022-12-07
 * @Version: V1.0
 */
public interface IPublishSatelliteSecureService extends IService<PublishSatelliteSecure> {

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
     * 批量删除
     * @param asList
     */
    void deleteBatch(List<String> asList);


    /**
     * 添加
     * @param entity
     */
    void add(PublishSatelliteSecure entity);

    /**
     * 编辑
     * @param entity
     */
    void edit(PublishSatelliteSecure entity);

    /**
     * 插销发布
     * @param id
     */
    void revocation(String id);





    /**
     * 根据终端id查询
     * @param terminalId 终端id
     * @return 发布的的列表
     */
    List<PublishSatelliteSecure> listPublishByTerminalId(String terminalId);

    void checkState();

    /**
     * 分页查询
     * @param page 分页参数
     * @param terminalIds 终端参数
     * @return 分页数据
     */
    IPage<PublishSatelliteSecureVO> getPage(Page<PublishSatelliteSecureVO> page, List<String> terminalIds);
}
