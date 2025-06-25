package com.hss.modules.scada.service.impl;

import com.hss.modules.scada.constant.ScadaConstant;
import com.hss.modules.scada.service.DeviceAttrVarExpressionRationService;
import com.hss.modules.scada.service.IConDeviceAttributeService;
import com.hss.modules.util.ExpressionUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.util.RedisUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author zpc
 * @version 1.0
 * @description: 查询关联的变量的属性Ids，根据表达式删除属性
 * @date 2024/3/19 15:15
 */
@Service
public class DeviceAttrVarExpressionRationServiceImpl implements DeviceAttrVarExpressionRationService {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ExpressionUtil expressionUtil;
    @Autowired
    private IConDeviceAttributeService deviceAttributeService;

    @Override
    public Collection<String> getVarRelationAttrIdsByAttrId(String attrId) {
        String key = getKey(attrId);
        Object o1 = redisUtil.get(key);
        if (o1 != null) {
            return (List<String>) o1;
        }
        List<String> attrIds = deviceAttributeService.listVarRelationAttrIdByAttrId(attrId);
        redisUtil.set(key, attrIds, 86400L);
        return attrIds;
    }

    @Override
    public void delByExpression(String expression) {
        if (StringUtils.isBlank(expression)) {
            return;
        }
        Set<String> attrIds = expressionUtil.listValueId(expression);
        if (CollectionUtils.isEmpty(attrIds)) {
            return;
        }
        for (String attrId : attrIds) {
            redisUtil.del(getKey(attrId));
        }
    }

    @NotNull
    private String getKey(String attrId) {
        return ScadaConstant.REDIS_KEY_REGISTER + attrId;
    }
}
