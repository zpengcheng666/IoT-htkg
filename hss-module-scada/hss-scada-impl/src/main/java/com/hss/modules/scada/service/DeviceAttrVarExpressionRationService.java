package com.hss.modules.scada.service;
import java.util.Collection;

/**
* @description: 设备属性和点位关联关系，查询关联的变量的attrIds，根据表达式删除
* @author zpc
* @date 2024/3/19 14:54
* @version 1.0
*/
public interface DeviceAttrVarExpressionRationService {

    /**
     * 查询关联的变量的attrIds
     * @param attrId
     * @return
     */
    Collection<String> getVarRelationAttrIdsByAttrId(String attrId);

    /**
     * 根据表达式删除
     * @param expression
     */
    void delByExpression(String expression);
}
