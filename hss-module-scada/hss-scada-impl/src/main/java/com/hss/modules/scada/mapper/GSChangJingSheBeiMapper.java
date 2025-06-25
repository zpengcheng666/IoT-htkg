package com.hss.modules.scada.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.scada.entity.GSChangJingSheBei;
import com.hss.modules.scada.model.DeviceSortVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zpc
 * @version 1.0
 * @description: 场景、设备关联关系表，根据场景id查询设备、根据场景id删除场景设备、根据设备id查询场景id、根据设备id删除场景设备、
 * 查询场景关联的设备、获取排序列表
 * @date 2024/3/19 16:12
 */
public interface GSChangJingSheBeiMapper extends BaseMapper<GSChangJingSheBei> {

    /**
     * 根据场景id查询设备
     *
     * @param sceneId
     * @return
     */
    List<String> listDeviceIdsBySceneId(String sceneId);

    /**
     * 根据场景id删除场景设备
     *
     * @param sceneId
     */
    void deleteBySceneId(String sceneId);

    /**
     * 根据设备id查询场景id
     *
     * @param deviceId
     * @return
     */
    String getSceneIdByDeviceId(String deviceId);

    /**
     * 根据设备id删除场景设备
     *
     * @param deviceId
     */
    void deleteByDeviceId(String deviceId);

    /**
     * 查询场景关联的设备
     *
     * @param deviceId
     * @return
     */
    List<String> listSceneIdByDeviceId(String deviceId);

    /**
     * 获取排序列表
     *
     * @param sceneId
     * @return
     */
    List<DeviceSortVO> listSortDevice(@Param("sceneId") String sceneId);
}
