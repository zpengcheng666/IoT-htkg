package com.hss.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.system.entity.BaseDictData;

import java.util.List;
import java.util.Map;

/**
 * @Description: 字典数据
 * @Author: zpc
 * @Date:   2022-11-03
 * @Version: V1.0
 */
public interface IBaseDictDataService extends IService<BaseDictData> {

    /**
     * 根据字典类型id查询map
     * @param typeId
     * @return
     */
    Map<String, String> mapByTypeId(String typeId);

    Map<String, String> mapByTypeEnName(String enName);

    Map<String, String> queryStatisticsWayMap(String statisticsWay);

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
    void deleteById(String id);
}
