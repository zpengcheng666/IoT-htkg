package com.hss.modules.scada.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.alarm.entity.AlarmStrategy;
import com.hss.modules.devicetype.entity.DeviceTypeAlarmStrategy;
import com.hss.modules.devicetype.entity.DeviceTypeManagement;
import com.hss.modules.devicetype.entity.DeviceTypeStoreStrategy;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.entity.ConDianWei;
import com.hss.modules.scada.entity.ConSheBei;
import com.hss.modules.scada.model.*;
import com.hss.modules.store.entity.StoreStrategy;
import com.hss.modules.store.model.vo.LineStateVO;
import com.hss.modules.store.model.vo.PieStateVO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
* @description: 场景中设备
* @author zpc
* @date 2024/3/19 15:01
* @version 1.0
*/
public interface IConSheBeiService extends IService<ConSheBei> {

    /**
     * 分页查询
     * @param page
     * @param sceneId
     * @param deviceName
     * @return
     */
    IPage<ConSheBei> page(Page<ConSheBei> page, String sceneId, String deviceName,String deviceTypeId);

    /**
     * 查询关联的点位
     * @param deviceId
     * @return
     */
    List<DeviceAttrPointRelationVO> listDeviceAttrRelation(String deviceId);

    /**
     * 保存属性关联关联
     * @param deviceAttrRelationSave
     */
    void saveDeviceAttrRelation(DeviceAttrRelationSave deviceAttrRelationSave);

    /**
     * 根据场景id查询
     * @param stageId
     * @return
     */
    List<ConSheBei> listBySceneId(String stageId);

    /**
     * 根据场景id查询相关属性
     * @param stageId
     * @return
     */
    List<ConDeviceAttribute> listAttrBySceneId(String stageId);

    /**
     * 设备动作执行
     * @param dto
     */
    void execute(DeviceExecuteDTO dto);

    /**
     * 执行动作
     * @param attribute
     * @param valueExpression 值表达式 可以配置1-3000-0
     */
    void executeCommandByValueExpression(ConDeviceAttribute attribute, String valueExpression);

    /**
     * 点位关联查询
     * @param page
     * @param sceneId
     * @param name
     * @return
     */
    IPage<ConSheBei> listPoint(Page<ConSheBei> page, String sceneId, String name,String deviceTypeId);

    /**
     * 变量关联查询
     * @param page
     * @param sceneId
     * @param name
     * @return
     */
    IPage<ConSheBei> listVariable(Page<ConSheBei> page, String sceneId, String name);

    /**
     * 查询关联的变量
     * @param deviceId
     * @return
     */
    List<DeviceAttrRelation> listDeviceAttrVariable(String deviceId);

    /**
     * 获取动作列表
     * @param deviceId
     * @return
     */
    List<DeviceAttrAct> listActByDeviceId(String deviceId);

    /**
     * 查询设备属性列表
     * @param deviceId
     * @return
     */
    List<ConDeviceAttribute> listDeviceAttrByDeviceId(String deviceId);

    /**
     * 查询场景下的所有广播设备
     * @param sceneId
     * @return
     */
    List<ConSheBei> listPublishBySceneId(String sceneId);

    /**
     * 获取摄像机信息
     * @param deviceId
     * @return
     */
    String getCameraByDeviceId(String deviceId);

    /**
     * 根据设备id和属性英文名获取点位信息
     * @param deviceId
     * @param attrEnName
     * @return
     */
    ConDianWei getPointByDeviceIdAndEnName(String deviceId, String attrEnName);

    /**
     * 查看所有的摄像机
     * @return
     */
    List<ConSheBei> ListCamera();

    /**
     * 查询属性中绑定了点位，redis中没有绑定的
     * @return
     */
    List<ConDeviceAttribute> listAttrNotRedis();

    /**
     * 根据设备ids查询设备类型
     * @param deviceIds
     * @return
     */
    List<ConSheBeiDoorOptions> listDeviceTypeByDeviceIds(List<String> deviceIds);

    /**
     * 根据子系统查询设备
     * @param subsystem
     * @return
     */
    List<String> listIdBySubsystem(String subsystem);

    /**
     * 根据设备类型查询
     * @param typeId
     * @return
     */
    List<ConSheBei> listByDeviceTypeId(String typeId);

    AlarmStrategy alarmStrategyType2strategy(DeviceTypeAlarmStrategy type, Map<String,String> idMap, String deviceId);

    StoreStrategy storeStrategyType2strategy(DeviceTypeStoreStrategy type, Map<String,String> idMap, String deviceId);

    /**
     * 获取数据表格显示的属性
     * @param deviceId
     * @return
     */
    List<DeviceAttrDataTable> lisDataTableByDeviceId(String deviceId);

    /**
     * 获取数据列表属性信息
     * @param deviceId
     * @return
     */
    List<DeviceAttrDataTable> lisDataListByDeviceId(String deviceId);

    /**
     * 保存点位关联
     * @param dto
     */
    void saveDeviceAttrPointRelation(DeviceAttrPointRelationDTO dto);

    /**
     * 查询设备列表
     * @param page
     * @param dto
     * @return
     */
    IPage<DeviceListVO> deviceList(Page<DeviceListVO> page, DeviceListDTO dto);

    /**
     * 添加设备
     * @param conSheBei
     */
    void add(ConSheBei conSheBei);

    /**
     * 批量添加
     * @param deviceList
     */
    public void batchAdd(List<ConSheBei> deviceList);

    /**
     * 编辑设备
     * @param conSheBei
     */
    void edit(ConSheBei conSheBei);

    /**
     * 删除
     * @param id
     */
    void delete(String id);

    /**
     * 保存配置信息
     * @param dto
     */
    void saveDeviceAttrConfig(DeviceAttrConfigDTO dto);

    /**
     * 设备数据表格
     * @param deviceIds
     * @return
     */
    List<DataTableVO> listDataTableByIds(Collection<String> deviceIds);

    /**
     * 根据设备类型编码查询
     * @param typeCode
     * @return
     */
    List<ConSheBei> listByTypeCode(String typeCode);

    /**
     * 获取控制系统下的风机列表
     * @param systemDeviceId
     * @return
     */
    List<ConSheBei> listFanBySystemDeviceId(String systemDeviceId);

    /**
     * 查询环境表格
     * @param sceneId
     * @return
     */
    List<EnvDeviceTableVO> listEnvDeviceBySceneId(String sceneId);

    /**
     * 设备类型是否存在
     * @param deviceTypeId
     * @return
     */
    boolean existByDeviceTypeId(String deviceTypeId);

    /**
     * 获取设备需要展示的趋势曲线
     * @param deviceId
     * @return
     */
    List<ConDeviceAttribute> listDeviceTendencyAttr(String deviceId);

    /**
     * 同步配置信息
     * @param type
     */
    void syncByType(DeviceTypeManagement type);

    /**
     * 查询门设备
     * @return
     */
    List<ConSheBeiOptions> listDoorOption();

    /**
     * 查询安检门设备
     * @return
     */
    List<ConSheBeiOptions> listCheckDoorOption();

    /**
     * 查询网关id设设备id
     * @param attrEnName 属性英文名称
     * @return 关联关系
     */
    List<GatewayDevice> listGatewayAndDeviceByEnName(String attrEnName);

    /**
     * 设备类型统计
     * @return
     */
    LineStateVO statByDeviceType();

    /**
     * 设备在线统计
     * @return
     */
    List<PieStateVO> statByDeviceState();

    /**
     * 全部摄像机
     * @return
     */
    List<ConSheBei> listAllCamera();

    /**
     * 全部广播设备
     * @return
     */
    List<ConSheBei> listAllPublish();

    LineStateVO stateCountDecices(String subSystems);
}
