package com.hss.modules.scada.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.scada.entity.SiteManager;
import com.hss.modules.scada.model.SetSiteStateDTO;
import com.hss.modules.scada.model.SiteStateUpdateDTO;

/**
 * @Description: 站点管理
 * @Author: zpc
 * @Date:   2024/3/19 13:50
 * @Version: V1.0
 */
public interface ISiteManagerService extends IService<SiteManager> {

    /**
     * 新增
     * @param siteManager
     */
    void add(SiteManager siteManager);

    /**
     * 编辑
     * @param siteManager
     */
    void edit(SiteManager siteManager);

    /**
     * 分页查询
     * @param page
     * @param name
     * @return
     */
    IPage<SiteManager> pageList(Page<SiteManager> page, String name);

    /**
     * 保存网关列表
     * @param siteManager
     */
    void saveGatewayList(SiteManager siteManager);

    /**
     * 站点状态切换
     * @param dto
     */
    void updateState(SiteStateUpdateDTO dto);

    /**
     * 远程设置站点状态
     * @param dto
     */
    void setLocalState(SetSiteStateDTO dto);

    /**
     * 获取本地站点状态
     * @param dto
     * @return
     */
    Integer getLocalState(SetSiteStateDTO dto);

    /**
     * 系统启动获取远程站点装填
     */
    void sysStartInit();

}
