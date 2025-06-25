package com.hss.modules.facility.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.facility.entity.R_DeviceClassAttr;
import com.hss.modules.facility.mapper.R_DeviceClassAttrMapper;
import com.hss.modules.facility.service.IR_DeviceClassAttrService;
import com.hss.modules.system.model.AttrModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @Description: 设备类别与设备属性中间表
 * @Author: zpc
 * @Date:   2022-12-08
 * @Version: V1.0
 */
@Service
public class R_DeviceClassAttrServiceImpl extends ServiceImpl<R_DeviceClassAttrMapper, R_DeviceClassAttr> implements IR_DeviceClassAttrService {

    @Override
    public void saveDeviceClassAttr(AttrModel attrModel) {
        // 1. 根据classid删除掉所有关联的attrid
        LambdaQueryWrapper<R_DeviceClassAttr> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(R_DeviceClassAttr::getClassId, attrModel.getClassId());
        this.remove(deleteWrapper);

        // 2. 重新插入所有的attrids
        if (StringUtils.isEmpty(attrModel.getAttrId())) {
            return;
        }
        String[] attrids = StringUtils.split(attrModel.getAttrId(), ",");

        for (String attrId : attrids) {
            R_DeviceClassAttr entity = new R_DeviceClassAttr();
            entity.setAttrId(attrId);
            entity.setClassId(attrModel.getClassId());
            this.save(entity);
        }
    }
}
