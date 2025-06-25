package com.hss.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.system.entity.BaseDictType;

/**
 * @Description: 字典类型
 * @Author: zpc
 * @Date:   2022-11-03
 * @Version: V1.0
 */
public interface IBaseDictTypeService extends IService<BaseDictType> {

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
    void deleteById(String id);
}
