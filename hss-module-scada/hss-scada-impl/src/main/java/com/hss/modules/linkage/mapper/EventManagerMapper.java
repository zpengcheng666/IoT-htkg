package com.hss.modules.linkage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.modules.linkage.entity.EventManager;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 事件管理
 * @Author: zpc
 * @Date:   2023-01-05
 * @Version: V1.0
 */
public interface EventManagerMapper extends BaseMapper<EventManager> {

    /**
     * 分页查询
     * @param page
     * @param name
     * @return
     */
    IPage<EventManager> list(Page<EventManager> page,@Param("name") String name);
}
