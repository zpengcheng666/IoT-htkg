package com.hss.modules.scada.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.scada.entity.GsChangJing;
import com.hss.modules.scada.model.*;

import java.util.List;
import java.util.Map;

/**
 * @author zpc
 * @version 1.0
 * @description: 场景管理增删改查，保存场景、获取场景但钱所有数据，发布、根据子系统id获取有存储策略的设备类型、
 * 查询场景名称和子系统，根据子系统id获取有报警策略的设备类型、场景排序、获取场景下的数据表格、获取环境场景下的设备信息
 * @date 2024/3/19 14:04
 */
public interface IGsChangJingService extends IService<GsChangJing> {
    /**
     * 场景保存
     * @param data
     * @return
     */
    Map<String, String> saveStageData(GsChangJingSaveStageData data);

    /**
     * 获取场景当前所有数据
     * @param stageId
     * @return
     */
    List<DeviceModel> listCurrentDataBySceneId(String stageId);

    /**
     * 发布
     * @param model
     */
    void publish(ScenePublishModel model);

    /**
     * 删除场景
     * @param id
     */
    void delById(String id);

    /**
     * 根据子系统id获取有存储策略的设备类型
     * @param subSystemId
     * @return
     */
    List<ConSheBeiDoorOptions> listStoreDeviceTypeBySubSystem(String subSystemId);

    /**
     * 根据子系统id获取有报警策略的设备类型
     * @param subSystemId
     * @return
     */
    List<ConSheBeiDoorOptions> listAlarmDeviceTypeBySubSystem(String subSystemId);

    /**
     * 查询场景名称和子系统
     * @param deviceId
     * @return
     */
    List<GsChangJing> listNameAndSystemByDeviceId(String deviceId);

    /**
     * 获取场景下的数据表格
     * @param sceneId 场景id
     * @return
     */
    List<DataTableVO> listDataTableBySceneId(String sceneId);

    /**
     * 获取环境场景下的设备信息
     * @param sceneId
     * @return
     */
    List<EnvDeviceTableVO> listEnvDeviceBySceneId(String sceneId);

    /**
     * 场景排序
     * @param sceneId
     * @return
     */
    List<DeviceSortVO> listSortDevice(String sceneId);

    /**
     * 保存场景设备排序
     * @param list
     */
    void saveDeviceSort(List<DeviceSortVO> list);

    /**
     * 场景菜单
     * @param sumSystem
     * @return
     */
    List<GsChangJing> listMenu(String sumSystem);
}
