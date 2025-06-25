package com.hss.modules.scada.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.scada.entity.ConWangGuan;

import java.util.List;

/**
* @description: 网关
* @author zpc
* @date 2024/3/19 13:50
* @version 1.0
*/
public interface ConWangGuanMapper extends BaseMapper<ConWangGuan> {

    /**
     * 根据站点id查询
     * @param siteId 站点id
     * @return 网关列表
     */
    List<ConWangGuan> listBySiteId(String siteId);
}
