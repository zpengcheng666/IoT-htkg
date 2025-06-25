package com.hss.modules.message.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.message.dto.PublishMessageDTO;
import com.hss.modules.message.entity.PublishWeather;

import java.util.List;

/**
 * @Description: 气象信息
 * @Author: zpc
 * @Date:   2022-12-06
 * @Version: V1.0
 */
public interface IPublishWeatherService extends IService<PublishWeather> {

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
     * 查看发布到终端的信息
     * @param terminalId
     * @return
     */
    List<PublishWeather> getByTerminal(String terminalId);

    /**
     * 新增
     * @param publishWeather
     */
    void add(PublishWeather publishWeather);

    /**
     * 插销发布
     * @param id
     */
    void revocation(String id);

    /**
     * 编辑
     * @param publishWeather
     */
    void edit(PublishWeather publishWeather);


    /**
     * 分页查询
     * @param page
     * @param terminalIds
     * @return
     */
    IPage<PublishWeather> queryPage(Page<PublishWeather> page, List<String> terminalIds);


    /**
     * 查询没有过期的天气数据
     * @return
     */
    List<PublishWeather> listNotOver();

    /**
     * 消息过期
     * @param message
     */
    void overMessage(PublishWeather message);

}
