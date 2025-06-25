package com.hss.modules.scada.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.scada.entity.GSChangJingSheBei;
import com.hss.modules.scada.model.DeviceSortVO;

import java.util.List;

/**
* @description: 场景、设备关联关系
* @author zpc
* @date 2024/3/19 14:17
* @version 1.0
*/
public interface IGSChangJingSheBeiService extends IService<GSChangJingSheBei> {

    /**
     * 查询设备ids
     * @param id
     * @return
     */
    List<String> listDeviceIdsBySceneId(String id);

    /**
     * 根据场景id删除
     * @param sceneId
     */
    void deleteBySceneId(String sceneId);

    /**
     * 根据设备id获取场景id
     * @param deviceId
     * @return
     */
    String getSceneIdByDeviceId(String deviceId);

    /**
     * 根据设备id删除
     * @param deviceId
     */
    void deleteByDeviceId(String deviceId);


    /**
     * 查询设备关联的场景id
     * @param deviceId
     * @return
     */
    List<String> listSceneIdByDeviceId(String deviceId);

    /**
     * 获取排序列表
     * @param sceneId
     * @return
     */
    List<DeviceSortVO> listSortDevice(String sceneId);

    /**
     * 保存场景设备排序
     * @param list
     */
    void saveDeviceSort(List<DeviceSortVO> list);
}
