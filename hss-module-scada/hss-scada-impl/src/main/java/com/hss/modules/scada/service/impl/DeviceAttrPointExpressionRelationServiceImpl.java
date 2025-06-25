package com.hss.modules.scada.service.impl;

import com.hss.modules.scada.constant.ScadaConstant;
import com.hss.modules.scada.service.DeviceAttrPointExpressionRelationService;
import com.hss.modules.scada.service.IConDeviceAttributeService;
import com.hss.modules.util.ExpressionUtil;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
* @description: 设备属性和点位关联关系
* @author zpc
* @date 2024/3/19 15:10
* @version 1.0
*/
@Service
public class DeviceAttrPointExpressionRelationServiceImpl implements DeviceAttrPointExpressionRelationService {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IConDeviceAttributeService conDeviceAttributeService;
    @Autowired
    private ExpressionUtil expressionUtil;

    @Override
    public Collection<String> getAttrIdByPointId(String pointId) {
        if (StringUtils.isBlank(pointId)) {
            return null;
        }
        String key = getKey(pointId);
        Object o1 = redisUtil.get(key);
        if (o1 != null) {
            return ((List<String>) o1);
        }
        List<String> attrIds = conDeviceAttributeService.listIdByPointIdWithExpression(pointId);
        redisUtil.set(key, attrIds, 86400L);
        return attrIds;
    }

    @Override
    public void del(String expression) {
        if (StringUtils.isBlank(expression)) {
            return;
        }
        Set<String> ids = expressionUtil.listValueId(expression);
        if (ids.isEmpty()) {
            return;
        }
        for (String id : ids) {
            redisUtil.del(getKey(id));
        }
    }

    private String getKey(String pointId) {
        return ScadaConstant.REDIS_KEY_POINT_EXPRESSION_ATTR + pointId;
    }
}
