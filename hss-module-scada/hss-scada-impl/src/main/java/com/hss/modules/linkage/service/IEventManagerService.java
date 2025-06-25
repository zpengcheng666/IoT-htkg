package com.hss.modules.linkage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.linkage.entity.EventAction;
import com.hss.modules.linkage.entity.EventManager;

import java.util.List;

/**
 * @Description: 事件管理
 * @Author: zpc
 * @Date:   2023-01-05
 * @Version: V1.0
 */
public interface IEventManagerService extends IService<EventManager> {

    /**
     * 分页查询
     * @param page
     * @param name
     * @return
     */
    IPage<EventManager> getPage(Page<EventManager> page, String name);

    /**
     * 删除
     * @param id
     */
    void delete(String id);

    /**
     * 根据id获取动作
     * @param eventId
     * @return
     */
    List<EventAction> listActionByEventId(String eventId);

    /**
     * 动作
     * @param eventAction
     * @return
     */
    String action(EventAction eventAction);


    /**
     * 全部事件列表
     * @return
     */
    List<EventManager> listAll();

}
