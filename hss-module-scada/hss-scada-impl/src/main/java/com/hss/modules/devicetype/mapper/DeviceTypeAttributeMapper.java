package com.hss.modules.devicetype.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.devicetype.entity.DeviceTypeAttribute;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * @Description: 设备类型管理属性管理
 * @Author: zpc
 * @Date:   2023-01-05
 * @Version: V1.0
 */
public interface DeviceTypeAttributeMapper extends BaseMapper<DeviceTypeAttribute> {

    /**
     * 根据设备类型查询
     * @param type
     * @return
     */
    List<DeviceTypeAttribute> listByDeviceType(String type);

    /**
     * 根据类型id删除
     * @param typeId
     */
    void deleteByTypeId(String typeId);

    /**
     * 根据typeId和enName查询
     * @param typeId
     * @param enName
     * @return
     */
    DeviceTypeAttribute getByEnNameAndTypeId(@Param("typeId") String typeId, @Param("enName") String enName);

    List<DeviceTypeAttribute> queryFilterAttr(String devClassId);

    /**
     * 根据设备类型查询需要关联的设备点位
     * @param deviceType
     * @return
     */
    List<DeviceTypeAttribute> listRelationByDeviceType(String deviceType);

    /**
     * 查询设备属性
     * @param deviceTypeId
     * @return
     */
    List<DeviceTypeAttribute> listByDeviceTypeId(String deviceTypeId);


    /**
     * 查询动作属性列表
     * @param typeId
     * @return
     */
    List<DeviceTypeAttribute> listActByTypeId(String typeId);

    /**
     * 根据类型id和类型名称删除
     * @param typeId
     * @param category
     * @return
     */
    int countByTypeIdAndEnName(@Param("typeId") String typeId, @Param("category") String category);

    /**
     * 查询数据表格显示的属性
     * @param typeId
     * @return
     */
    List<DeviceTypeAttribute> lisDataTableByDeviceId(String typeId);

    /**
     * 获取数据表格显示的数据
     * @param typeId
     * @return
     */
    List<DeviceTypeAttribute> lisDataListByDeviceId(String typeId);

    /**
     * 查询数据表格
     * @param typeIds
     * @return
     */
    List<DeviceTypeAttribute> listByDeviceTypeIdsAndDataTable(Collection<String> typeIds);

    /**
     * 批量查询
     * @param types
     * @return
     */
    List<DeviceTypeAttribute> listByDeviceTypeIds(Collection<String> types);
}
