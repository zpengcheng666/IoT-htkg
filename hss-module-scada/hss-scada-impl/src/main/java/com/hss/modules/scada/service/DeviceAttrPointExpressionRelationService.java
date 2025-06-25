package com.hss.modules.scada.service;
import java.util.Collection;


/**
* @description: 点位关联表达式绑定
* @author zpc
* @date 2024/3/19 14:54
* @version 1.0
*/
public interface DeviceAttrPointExpressionRelationService {

    /**
     * 根据表达式删除
     * @param point 点位
     * @return 属性id集合
     */
    Collection<String> getAttrIdByPointId(String point);

    /**
     * 根据表达式是删除
     * @param expression
     */
    void del(String expression);
}
