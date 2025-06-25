package com.hss.modules.devicetype.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.devicetype.entity.DeviceTypeAttribute;
import com.hss.modules.devicetype.model.DeviceTypeAttributeSceneListByType;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @Description: 设备类型管理属性管理
 * @Author: zpc
 * @Date:   2023-01-05
 * @Version: V1.0
 */
public interface IDeviceTypeAttributeService extends IService<DeviceTypeAttribute> {

    /**
     * 根据设备类型名称查询
     * @param type
     * @return
     */
    List<DeviceTypeAttribute> listByDeviceType(String type);

    /**
     * 根据typeId删除
     * @param typeId
     */
    void deleteByTypeId(String typeId);


    /**
     * 根据typeid和enName查询
     * @param typeId
     * @param enName
     * @return
     */
    DeviceTypeAttribute getByEnNameAndTypeId(String typeId, String enName);

    List<DeviceTypeAttribute> queryDevClassIdByAttrFilter(String devClassId);

    /**
     * bu组态查询设备属性列表
     * @param type 设备类型
     * @param deviceId 设备id
     * @param sceneId 场景id
     * @return
     */
    DeviceTypeAttributeSceneListByType sceneListByType(String type, String deviceId);


    /**
     * 根据设备类型查询需要关联的点位
     * @param deviceType
     * @return
     */
    List<DeviceTypeAttribute> listRelationByDeviceType(String deviceType);

    /**
     * 根据类型id查询
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
     * 分页查询
     * @param page
     * @param queryWrapper
     * @return
     */
    IPage<DeviceTypeAttribute> pageList(Page<DeviceTypeAttribute> page, QueryWrapper<DeviceTypeAttribute> queryWrapper);

    /**
     * 添加
     * @param deviceTypeAttribute
     */
    void add(DeviceTypeAttribute deviceTypeAttribute);

    /**
     * 编辑
     * @param deviceTypeAttribute
     */
    void edit(DeviceTypeAttribute deviceTypeAttribute);

    /**
     * 查询数据表格显示的属性
     * @param typeId
     * @return
     */
    List<DeviceTypeAttribute> lisDataTableByDeviceId(String typeId);

    /**
     * 获取数据表格显示的属性
     * @param deviceTypeId
     * @return
     */
    List<DeviceTypeAttribute> lisDataListByDeviceId(String deviceTypeId);

    /**
     * 查询数据表格
     * @param deviceTypeIds
     * @return
     */
    List<DeviceTypeAttribute> listByDeviceTypeIdsAndDataTable(Set<String> deviceTypeIds);

    /**
     * 批量长训
     * @param types
     * @return
     */
    List<DeviceTypeAttribute> listByDeviceTypeIds(Collection<String> types);
}
