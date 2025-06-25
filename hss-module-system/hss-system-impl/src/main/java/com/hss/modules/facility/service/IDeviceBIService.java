package com.hss.modules.facility.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.facility.entity.DeviceBI;
import com.hss.modules.facility.model.DeviceRunLogEvent;
import org.springframework.context.ApplicationListener;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @Description: 设施设备
 * @Author: zpc
 * @Date:   2022-12-08
 * @Version: V1.0
 */
public interface IDeviceBIService extends IService<DeviceBI> , ApplicationListener<DeviceRunLogEvent> {

    List<DeviceBI> listDetailsByTypeList(List<String> types);

    /**
     * 添加设备
     *  添加基础信息到DF_BI_DEVICE表中
     *  添加扩展属性到DF_BD_DEVICE_ATTR表中
     * @param deviceBI
     * @return
     */
    DeviceBI addDevice(DeviceBI deviceBI);

    /**
     * 编辑设备
     *  编辑基础信息到DF_BI_DEVICE表中
     *  编辑扩展属性到DF_BD_DEVICE_ATTR表中
     * @param deviceBI
     * @return
     */
    boolean editDevice(DeviceBI deviceBI);

    /**
     * 删除设备
     * @param id
     * @return
     */
    boolean deleteDevice(String id);
}
