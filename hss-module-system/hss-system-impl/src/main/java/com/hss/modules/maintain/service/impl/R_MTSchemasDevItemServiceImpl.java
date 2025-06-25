package com.hss.modules.maintain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.maintain.entity.MaintainSchemas;
import com.hss.modules.maintain.entity.R_MTSchemasDevItem;
import com.hss.modules.maintain.mapper.R_MTSchemasDevItemMapper;
import com.hss.modules.maintain.service.IMaintainSchemasService;
import com.hss.modules.maintain.service.IR_MTSchemasDevItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 保养任务-设备关系表
 * @Author: zpc
 * @Date: 2022-12-28
 * @Version: V1.0
 */
@Service
public class R_MTSchemasDevItemServiceImpl extends ServiceImpl<R_MTSchemasDevItemMapper, R_MTSchemasDevItem> implements IR_MTSchemasDevItemService {
    @Autowired
    private IMaintainSchemasService maintainSchemasService;

    @Override
    public void saveSchemasDevice(MaintainSchemas maintainSchemas) {
        //1.先保存保养方案列表
        this.maintainSchemasService.save(maintainSchemas);

        //2.保养方案Id
        String schemasId = maintainSchemas.getId();

        //3.保养类别id
        String classIds = maintainSchemas.getItemClassId();
        //4.设备id
        List<String> deviceClassIds = maintainSchemas.getDeviceClassIds();
        for (String devIds : deviceClassIds) {
            //5. 保养方案-设备类别-保养类别关系表保存
            R_MTSchemasDevItem entity = new R_MTSchemasDevItem();
            entity.setSchemasId(schemasId);
            entity.setItemClass(classIds);
            entity.setDeviceClassId(devIds);

            this.save(entity);
        }
    }

    @Override
    public void updateSchemasDevice(MaintainSchemas schemas) {
        //1.修改方案列表
        this.maintainSchemasService.updateById(schemas);

        //2. 根据方案id，删除所关联的设备
        LambdaQueryWrapper<R_MTSchemasDevItem> delWrapper = new LambdaQueryWrapper<>();
        delWrapper.eq(R_MTSchemasDevItem::getSchemasId, schemas.getId());
        this.remove(delWrapper);

        //方案id
        String schemasIdid = schemas.getId();
        //保养类别id
        String classIds = schemas.getItemClassId();
        //设备id
        List<String> stringList = schemas.getDeviceClassIds();

        for (String devId : stringList) {
            //3. 保养方案-设备类别-保养类别关系表保存
            R_MTSchemasDevItem entity = new R_MTSchemasDevItem();
            entity.setSchemasId(schemasIdid);
            entity.setItemClass(classIds);
            entity.setDeviceClassId(devId);
            this.save(entity);
        }
    }
}
