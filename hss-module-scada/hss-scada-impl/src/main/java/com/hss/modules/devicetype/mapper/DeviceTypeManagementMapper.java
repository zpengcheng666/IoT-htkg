package com.hss.modules.devicetype.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.modules.devicetype.entity.DeviceTypeManagement;
import com.hss.modules.devicetype.model.DeviceTypeExcel;
import com.hss.modules.scada.model.DeviceTypeIdNameBO;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * @Description: 设备类型管理
 * @Author: zpc
 * @Date:   2023-01-05
 * @Version: V1.0
 */
public interface DeviceTypeManagementMapper extends BaseMapper<DeviceTypeManagement> {

    /**
     * 分页查询
     * @param page
     * @param name
     * @return
     */
    IPage<DeviceTypeManagement> getPage(Page<DeviceTypeManagement> page, @Param("name") String name);

    /**
     * 根据设备类型查询
     * @param type
     * @return
     */
    DeviceTypeManagement getByType(String type);

    /**
     * 根据ids查询名字
     * @param ids ids
     * @return id和名字列表
     */
    List<DeviceTypeIdNameBO> listNameByIds(Collection<String> ids);

    /**
     * 查询数据供导出excel
     * @return
     */
    List<DeviceTypeExcel> listExcel();

}
