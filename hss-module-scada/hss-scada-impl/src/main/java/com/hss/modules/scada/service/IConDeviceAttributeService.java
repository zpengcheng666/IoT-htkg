package com.hss.modules.scada.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.devicetype.entity.DeviceTypeAttribute;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.entity.ConSheBei;
import com.hss.modules.scada.model.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
* @description: 设备属性、变量绑定，根据设备查询相关信息
* @author zpc
* @date 2024/3/19 14:56
* @version 1.0
*/
public interface IConDeviceAttributeService extends IService<ConDeviceAttribute> {
    /**
     * 根据设备id删除
     * @param deviceId 设备id
     */
    void deleteByDeviceId(String deviceId);

    /**
     * 根据deviceId查询查询
     * @param deviceId 设备id
     * @return 属性列表
     */
    List<ConDeviceAttribute> listByDeviceId(String deviceId);

    /**
     * 根据deviceId 和 英文名称查询
     * @param deviceId 设备id
     * @param enName 英文名称
     * @return
     */
    ConDeviceAttribute getByDeviceIdAndAttrEnName(String deviceId, String enName);

    /**
     * 查询绑定和没绑定的数据
     * @param deviceId 设备id
     * @return
     */
    DeviceAttrCountConfigAndBinding countBindingByDeviceId(String deviceId);

    /**
     * 查询变量关联数量
     * @param deviceId 设备id
     * @return
     */
    DeviceAttrCountConfigAndBinding countVariableByTypeId(String deviceId);

    /**
     * 获取变量关联列联表
     * @param deviceId shebeiid
     * @return
     */
    List<DeviceAttrRelation> listDeviceAttrVariable(String deviceId);

    /**
     * 更新变量关联
     * @param sourceAttr
     */
    void updateDataByRelationByAttrId(ConDeviceAttribute sourceAttr);

    /**
     * 删除点位关联
     * @param pointIds
     */
    void delPointRelation(List<String> pointIds);

    /**
     * 删除点位关联的表达式关联
     * @param expressions
     */
    void delPointExpressionRelation(List<String> expressions);

    /**
     * 更新点位绑定
     * @param updateRelationList
     */
    void updateRelation(List<DeviceAttrChangePoint> updateRelationList);

    /**
     * 更新
     * @param updateRelationList
     */
    void updateRelationVar(List<DeviceAttrChangeVar> updateRelationList);

    /**
     * 查询属性绑定没有同步到redis中的数据
     * @return
     */
    List<ConDeviceAttribute> listAttrNotRedis();

    /**
     * 根据设备ids获取有存储策略的属性列表
     * @param devIds
     * @return
     */
    List<ConSheBeiOptions> listStoreAttrByDeviceIds(String devIds);

    /**
     * 根据设备ids获取有存储策略的属性列表
     * @param devIds
     * @return
     */
    List<ConSheBeiOptions> listAlarmAttrByDeviceIds(String devIds);

    /**
     * 根据属性id查询
     * @param typeId
     * @return
     */
    List<ConDeviceAttribute> listByTypeAttrId(String typeId);

    /**
     * 根据点位id查询查询属性ids
     * @param pointId 点位id
     * @return 属性ids
     */
    List<String> listIdByPointId(String pointId);

    /**
     * 根据变量关联查询相关的属性ids
     * @param attrId 属性id
     * @return 相关的属性ids
     */
    List<String> listVarRelationAttrIdByAttrId(String attrId);

    /**
     * 根据点位id查询表达式
     * @param pointId
     * @return
     */
    List<String> listIdByPointIdWithExpression(String pointId);

    /**
     * 根据设备id列表和英文名称查询
     * @param deviceIds
     * @param enName
     * @return
     */
    List<ConDeviceAttribute> listByDeviceIdsAndEnName(Collection<String> deviceIds, String enName);

    /**
     * 根据设备同步顺序ing
     * @param conSheBei 设备
     */
    void syncByDevice(ConSheBei conSheBei);

    /**
     * 根据属性类型添加属性
     * @param source 属性类型
     * @param conSheBei 设备
     */
    void addByDeviceAndType(DeviceTypeAttribute source, ConSheBei conSheBei);

    /**
     * 根据属性类型更新属性
     * @param source 属性类型
     * @param attribute 当前属性
     */
    void upDateByType(DeviceTypeAttribute source, ConDeviceAttribute attribute);

    /**
     * 查询关系
     * @param attrIds
     * @return
     */
    Set<String> listIdByVarExpressionAttrIds(Collection<String> attrIds);

    /**
     * 查询设备id和属性id
     * @param enName
     * @return
     */
    List<DeviceIdAndAttrIdEnNameBO> listDeviceIdAttrIdByEnName(String enName);

    /**
     * 根据英文名称查询id
     * @param enName
     * @return
     */
    List<String> listIdByEnName(String enName);

    /**
    * @description: 根据设备ids和英文名称查询设备属性
    * @author zpc
    * @param deviceIds
    * @param enNames
    * @return
    */
    List<ConDeviceAttribute> listByDeviceIdsAndEnNames(List<String> deviceIds, List<String> enNames);
}
