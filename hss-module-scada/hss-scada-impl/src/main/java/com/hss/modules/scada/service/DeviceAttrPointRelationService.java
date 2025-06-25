package com.hss.modules.scada.service;
import java.util.Collection;


/**
* @description: 设备属性和点位关联关系
* @author zpc
* @date 2024/3/19 14:54
* @version 1.0
*/
public interface DeviceAttrPointRelationService {

    /**
     * 查询属性id
     * @param pointId 点位id
     * @return 属性id集合
     */
    Collection<String> getAttrIdByPointId(String pointId);

    /**
     * 根据点位id删除
     * @param pointId
     */
    void del(String pointId);
}
