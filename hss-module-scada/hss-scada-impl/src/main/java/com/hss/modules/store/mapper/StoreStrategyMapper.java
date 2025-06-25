package com.hss.modules.store.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.modules.store.entity.StoreStrategy;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 设备运行时数据存储策略
 * @Author: zpc
 * @Date:   2022-12-01
 * @Version: V1.0
 */
public interface StoreStrategyMapper extends BaseMapper<StoreStrategy> {

    /**
     * 分页查询
     * @param page
     * @param name
     * @param deviceId
     * @return
     */
    IPage<StoreStrategy> getPage(Page<StoreStrategy> page, @Param("name") String name, @Param("deviceId") String deviceId);

    /**
     * 根据设备id删除
     * @param deviceId
     */
    void deleteByDeviceId(String deviceId);


    /**
     * 根据设备id查询
     * @param deviceId
     * @return
     */
    List<StoreStrategy> listByDeviceId(String deviceId);

    /**
     * 根据策略类型id查询
     * @param typeId
     * @return
     */
    List<StoreStrategy> listByTypeStrategyId(String typeId);

    /**
     * 根据类型查询使能的任务
     * @param type
     * @return
     */
    List<StoreStrategy> listEnableByType(String type);
}
