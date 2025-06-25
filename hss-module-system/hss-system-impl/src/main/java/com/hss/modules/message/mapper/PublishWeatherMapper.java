package com.hss.modules.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.modules.message.entity.PublishWeather;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: zpc
 * @Date:   2022-12-06
 * @Version: V1.0
 */
public interface PublishWeatherMapper extends BaseMapper<PublishWeather> {

    /**
     * 查看发布到终端的消息
     * @param terminalId 终端id
     * @return
     */
    List<PublishWeather> getByTerminal(@Param("terminalId") String terminalId);

    /**
     * 查询日期内有多少条记录
     * @param weatherTime
     * @return
     */
    int countByDay(Date weatherTime);

    /**
     * 根据日期和状态查询
     * @param day
     * @param state
     * @return
     */
    String getByDateAndState(@Param("day") Date day, @Param("state")int state);

    /**
     * 分页查询
     * @param page
     * @param terminalIds
     * @return
     */
    IPage<PublishWeather> queryPage(Page<PublishWeather> page, @Param("terminalIds") List<String> terminalIds);

    /**
    * @description: 查询总数
    * @author zpc
    * @date 2023/2/15 15:46
    * @version 1.0
    */
    Long queryCount(@Param("terminalIds") List<String> terminalIds);

    /**
     * 查询没有过期的消息
     * @return
     */
    List<PublishWeather> listNotOver();
}
