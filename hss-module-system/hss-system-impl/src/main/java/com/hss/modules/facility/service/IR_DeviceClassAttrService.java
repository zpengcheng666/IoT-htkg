package com.hss.modules.facility.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.facility.entity.R_DeviceClassAttr;
import com.hss.modules.system.model.AttrModel;

/**
 * @Description: 设备类别与设备属性中间表
 * @Author: zpc
 * @Date:   2022-12-08
 * @Version: V1.0
 */
public interface IR_DeviceClassAttrService extends IService<R_DeviceClassAttr> {

    void saveDeviceClassAttr(AttrModel attrModel);
}
