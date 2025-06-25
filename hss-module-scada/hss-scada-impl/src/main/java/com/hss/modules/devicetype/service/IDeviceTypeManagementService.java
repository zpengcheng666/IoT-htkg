package com.hss.modules.devicetype.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.devicetype.entity.DeviceTypeManagement;
import com.hss.modules.devicetype.model.DeviceTypeExcel;
import com.hss.modules.scada.model.DeviceTypeIdNameBO;
import com.hss.modules.scada.model.DeviceTypeStrategyList;

import java.util.Collection;
import java.util.List;

/**
* @description: 设备类型管理
* @author zpc
* @date 2024/3/20 14:58
* @version 1.0
*/
public interface IDeviceTypeManagementService extends IService<DeviceTypeManagement> {

    /**
     * 查询策略列表
     * @param typeId
     * @return
     */
    List<DeviceTypeStrategyList> listStrategy(String typeId);


    /**
     * 分页查询
     * @param page
     * @param name
     * @return
     */
    IPage<DeviceTypeManagement> getPage(Page<DeviceTypeManagement> page, String name);

    /**
     * 根据id删除设备类型
     * @param id
     */
    void deleteById(String id);

    /**
     * 根据设备类型查询
     * @param type
     * @return
     */
    DeviceTypeManagement getByType(String type);

    /**
     * 导入配置信息
     * @param dirName
     */
    void addByJsonFile(String dirName);


    /**
     * 导入策略
     * @param dirName
     * @param typeId
     */
    void addByJsonFileStrategy(String dirName, String typeId);


    /**
     * 复制设备类型
     * @param deviceTypeManagement
     */
    void copy(DeviceTypeManagement deviceTypeManagement);

    /**
     * 新增设备类型
     * @param deviceTypeManagement
     */
    void add(DeviceTypeManagement deviceTypeManagement);


    /**
     * 编辑设备类型
     * @param deviceTypeManagement
     */
    void edit(DeviceTypeManagement deviceTypeManagement);


    /**
     * 同步属性和策略，主要是为了导库使用
     */
    void syncAttrAndStrategy();

    /**
     * 根据ids查询
     * @param ids
     * @return
     */
    List<DeviceTypeIdNameBO> listNameByIds(Collection<String> ids);

    /**
     * 查询excel
     * @return
     */
    List<DeviceTypeExcel> listExcel();

    /**
     * 导入
     * @param list
     */
    void importExcel(List<DeviceTypeExcel> list);
}
