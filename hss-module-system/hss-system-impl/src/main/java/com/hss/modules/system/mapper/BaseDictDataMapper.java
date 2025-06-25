package com.hss.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.system.entity.BaseDictData;

import java.util.List;

/**
 * @Description: 字典数据
 * @Author: zpc
 * @Date:   2022-11-03
 * @Version: V1.0
 */
public interface BaseDictDataMapper extends BaseMapper<BaseDictData> {

    /**
     * 根据类型查询id和Name
     * @param typeId
     * @return
     */
    List<BaseDictData> listByTypeId(String typeId);

    List<BaseDictData> listByTypeEnName(String enName);

    /**
     * 通过id查询类型下的字典数据
     *
     * @param dictTypeId
     * @return
     */
    List<BaseDictData> getDictDataById(String dictTypeId);

    /**
     * 编辑字典名称与字典排序值
     *
     * @param baseDictData
     * @return
     */
    void editDictTypeAndCode(BaseDictData baseDictData);

    /**
     * 通过id删除字典数据
     *
     * @param id
     * @return
     */
    void deleteDictById(String id);
}
