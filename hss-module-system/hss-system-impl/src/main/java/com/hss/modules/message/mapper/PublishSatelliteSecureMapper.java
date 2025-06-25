package com.hss.modules.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.modules.message.entity.PublishSatelliteSecure;
import com.hss.modules.message.model.PublishSatelliteSecureVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @Description: 安全时段
 * @Author: zpc
 * @Date:   2022-12-07
 * @Version: V1.0
 */
public interface PublishSatelliteSecureMapper extends BaseMapper<PublishSatelliteSecure> {
    IPage<PublishSatelliteSecureVO> getPage(Page<PublishSatelliteSecureVO> page);

    IPage<PublishSatelliteSecureVO> getPageByTerminal(Page<PublishSatelliteSecureVO> page, @Param("terminalIds") List<String> terminalIds);

    List<PublishSatelliteSecure> listNoOver();

    List<PublishSatelliteSecure> listPublishByTerminalId(String terminalId);
}
