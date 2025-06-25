package com.hss.modules.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.modules.message.entity.PublishSatellite;
import com.hss.modules.message.model.PublishSatelliteVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 卫星临空信息
 * @Author: zpc
 * @Date:   2022-12-07
 * @Version: V1.0
 */
public interface PublishSatelliteMapper  extends BaseMapper<PublishSatellite> {


    /**
     * 根据终端id查询发布的信息
     * @param terminalId 终端id
     * @return 信息列表
     */
    List<PublishSatellite> listPublishByTerminalId(@Param("terminalId") String terminalId);

    /**
     * 查询没有过期的数据
     * @return
     */
    List<PublishSatellite> listNoOver();

    /**
     * 分页查询
     * @param page 分页参数
     * @param terminalIds 终端id
     * @return 分页数据
     */
    IPage<PublishSatelliteVO> getPageByTerminal(Page<PublishSatelliteVO> page, @Param("terminalIds") List<String> terminalIds);

    /**
     * 普通分页查询
     * @param page
     * @return
     */
    IPage<PublishSatelliteVO> getPage(Page<PublishSatelliteVO> page);
}
