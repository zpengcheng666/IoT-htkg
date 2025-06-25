package com.hss.modules.facility.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.core.common.system.vo.SelectTreeNode;
import com.hss.modules.facility.entity.DeviceType;
import com.hss.modules.facility.model.StatQualityConditionModel;

import java.util.List;

/**
 * @Description: 类别管理
 * @Author: zpc
 * @Date:   2022-12-08
 * @Version: V1.0
 */
public interface IDeviceTypeService extends IService<DeviceType> {

    List<SelectTreeNode> queryTreeList(String ids);

    /**
     * 根据设备，按照设备的质量类型进行统计
     * @param classId
     * @return
     */
    List<StatQualityConditionModel> statQualityCondition(String classId);

    List<String> listIdByType(String type);
}
