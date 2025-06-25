package com.hss.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.system.entity.BaseDictData;
import com.hss.modules.system.mapper.BaseDictDataMapper;
import com.hss.modules.system.service.IBaseDictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 字典数据
 * @Author: zpc
 * @Date:   2022-11-03
 * @Version: V1.0
 */
@Service
public class BaseDictDataServiceImpl extends ServiceImpl<BaseDictDataMapper, BaseDictData> implements IBaseDictDataService {

    public static final String STAT_WAY_ALARM_LEVEL = "alarmLevel";

    public static final String STAT_WAY_ALARM_LEVEL_DICT_ENNAME = "alarm-level-list";

    public static final String STAT_WAY_ALARM_TYPE = "alarmType";

    public static final String STAT_WAY_ALARM_TYPE_DICT_ENNAME = "alarm-type-list";

    @Autowired
    private BaseDictDataMapper baseDictDataMapper;

    @Override
    public Map<String, String> mapByTypeId(String typeId) {
        List<BaseDictData> list = baseMapper.listByTypeId(typeId);

        return list.stream().collect(Collectors.toMap(BaseDictData::getId,BaseDictData::getName));
    }

    @Override
    public Map<String, String> mapByTypeEnName(String enName) {
        List<BaseDictData> list = baseMapper.listByTypeEnName(enName);
        return list.stream().collect(Collectors.toMap(BaseDictData::getId,BaseDictData::getName));
    }

    @Override
    public Map<String, String> queryStatisticsWayMap(String statisticsWay) {
        if (STAT_WAY_ALARM_LEVEL.equals(statisticsWay)){
            return this.mapByTypeEnName(STAT_WAY_ALARM_LEVEL_DICT_ENNAME);
        } else {
            return this.mapByTypeEnName(STAT_WAY_ALARM_TYPE_DICT_ENNAME);
        }
    }

    @Override
    public List<BaseDictData> getDictDataById(String dictTypeId) {
        List<BaseDictData> list = baseDictDataMapper.getDictDataById(dictTypeId);
        return list;
    }

    @Override
    public void editDictTypeAndCode(BaseDictData baseDictData) {
        baseDictDataMapper.editDictTypeAndCode(baseDictData);
    }

    @Override
    public void deleteById(String id) {
        baseDictDataMapper.deleteDictById(id);
    }
}
