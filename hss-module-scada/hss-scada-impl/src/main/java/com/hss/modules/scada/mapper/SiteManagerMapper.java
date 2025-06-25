package com.hss.modules.scada.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.modules.scada.entity.SiteManager;
import org.apache.ibatis.annotations.Param;

/**
 * @author zpc
 * @version 1.0
 * @description: 站点管理，根据站点编号查询
 * @date 2024/3/19 16:14
 */
public interface SiteManagerMapper extends BaseMapper<SiteManager> {

    /**
     * 分页查询
     * @param page
     * @param name
     * @return
     */
    Page<SiteManager> pageList(Page<SiteManager> page, @Param("name") String name);

    /**
     * 根据站点编号查询
     * @param siteCode 站点编号
     * @return 站点信息
     */
    SiteManager getByCode(String siteCode);
}
