package com.hss.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.system.entity.BaseDictType;

/**
 * @Description: 字典类型
 * @Author: zpc
 * @Date:   2022-11-03
 * @Version: V1.0
 */
public interface BaseDictTypeMapper extends BaseMapper<BaseDictType> {

    /**
     * 编辑字典类型与字典码值
     *
     * @param baseDictType
     * @return
     */
    void updateNameAndEnName(BaseDictType baseDictType);

    /**
     * 通过id删除字典类型
     *
     * @param id
     * @return
     */
    void deleteDictById(String id);
}
