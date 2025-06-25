package com.hss.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.system.entity.BaseDictType;
import com.hss.modules.system.mapper.BaseDictTypeMapper;
import com.hss.modules.system.service.IBaseDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: 字典类型
 * @Author: zpc
 * @Date:   2022-11-03
 * @Version: V1.0
 */
@Service
public class BaseDictTypeServiceImpl extends ServiceImpl<BaseDictTypeMapper, BaseDictType> implements IBaseDictTypeService {

    @Autowired
    private BaseDictTypeMapper baseDictTypeMapper;

    @Override
    public void updateNameAndEnName(BaseDictType baseDictType) {
        baseDictTypeMapper.updateNameAndEnName(baseDictType);
    }

    @Override
    public void deleteById(String id) {
        baseDictTypeMapper.deleteDictById(id);
    }


}
