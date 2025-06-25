package com.hss.modules.scada.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.scada.entity.ConDeviceAttribute;
import com.hss.modules.scada.model.ConSheBeiOptions;
import com.hss.modules.scada.model.DeviceAttrCountConfigAndBinding;
import com.hss.modules.scada.model.DeviceAttrRelation;
import com.hss.modules.scada.model.DeviceIdAndAttrIdEnNameBO;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
* @description: 设备属性、变量绑定表
* @author zpc
* @date 2024/3/19 13:41
* @version 1.0
*/
public interface ConDeviceAttributeMapper extends BaseMapper<ConDeviceAttribute> {

    /**
     * 查询点位关联
     * @param deviceId
     * @return
     */
    List<DeviceAttrRelation> listDeviceAttrRelation(String deviceId);

    /**
     * 根据设备id市场农户
     * @param deviceId
     */
    void deleteByDeviceId(String deviceId);

    /**
     * 根据点位id获取设备属性
     * @param variableId
     * @return
     */
    ConDeviceAttribute getByVariableId(String variableId);

    /**
     * 根据设备id获取
     * @param deviceId
     * @return
     */
    List<ConDeviceAttribute> listByDeviceId(String deviceId);



    /**
     * 根据设备id和属性英文名查询
     * @param deviceId
     * @param enName
     * @return
     */
    ConDeviceAttribute getByDeviceIdAndAttrEnName(@Param("deviceId") String deviceId, @Param("enName") String enName);


    /**
     * 查询绑定和没绑定的数据
     * @param deviceId
     * @return
     */
    DeviceAttrCountConfigAndBinding countBindingByTypeId(String deviceId);


    /**
     * 查询变量关联数量
     * @param deviceId
     * @return
     */
    DeviceAttrCountConfigAndBinding countVariableByTypeId(String deviceId);


    /**
     * 获取变量关联列表
     * @param deviceId
     * @return
     */
    List<DeviceAttrRelation> listDeviceAttrVariable(String deviceId);


    /**
     * 更新关联
     * @param conDeviceAttribute
     */
    void updateRelationById(@Param("relation") ConDeviceAttribute conDeviceAttribute);


    /**
     * 根据点位id查询
     * @param variableId
     * @return
     */
    List<ConDeviceAttribute> listByPointId(String variableId);

    /**
     * 查询绑定的数据点
     * @return
     */
    List<ConDeviceAttribute> listPoint();


    /**
     * 查询有存储策略的属性列表 根据设备ids
     * @param deviceIds
     * @return
     */
    List<ConSheBeiOptions> listStoreAttrByDeviceIds(@Param("deviceIds") List<String> deviceIds);

    /**
     * 根据设备类型查询有报警策略的属性
     * @param deviceIds
     * @return
     */
    List<ConSheBeiOptions> listAlarmAttrByDeviceIds(@Param("deviceIds") List<String> deviceIds);

    /**
     * 根据属性类型查询
     * @param typeId
     * @return
     */
    List<ConDeviceAttribute> listByTypeAttrId(String typeId);


    /**
     * 根据设备id查询ids
     * @param deviceId 设备id
     * @return ids
     */
    List<String> listIdsByDeviceId(String deviceId);

    /**
     * 根据点位id查询属性ids
     * @param pointId 点位id
     * @return 属性id列表
     */
    List<String> listIdByPointId(String pointId);

    /**
     * 根据属性id查询变量关联的属性id
     * @param attrId 属性id
     * @return 相关的属性id
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
    List<ConDeviceAttribute> listByDeviceIdsAndEnName(@Param("deviceIds") Collection<String> deviceIds, @Param("enName") String enName);

    /**
     * 根据属性id查询关联的属性
     * @param attrId
     * @return
     */
    List<String> listIdByVarExpressionAttrId(String attrId);

    /**
     * 查询设备id和属性id
     * @param enName 英文名
     * @return 设备id和属性id
     */
    List<DeviceIdAndAttrIdEnNameBO> listDeviceIdAttrIdByEnName(String enName);

    /**
     * 根据英文名称查询id
     * @param enName
     * @return
     */
    List<String> listIdByEnName(String enName);

    List<ConDeviceAttribute> listByDeviceIdsAndEnNames(@Param("deviceIds") List<String> deviceIds, @Param("enNames") List<String> enNames);
}
