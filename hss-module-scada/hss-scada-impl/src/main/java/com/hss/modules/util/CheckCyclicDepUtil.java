package com.hss.modules.util;

import com.hss.core.common.exception.HssBootException;
import com.hss.modules.alarm.service.IAlarmStrategyService;
import com.hss.modules.linkage.service.ILinkageStrategyService;
import com.hss.modules.scada.service.IConDeviceAttributeService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 校验循环依赖
 * @author hd
 */
@Component
public class CheckCyclicDepUtil {

    @Autowired
    private IAlarmStrategyService alarmStrategyService;
    @Autowired
    private ILinkageStrategyService linkageStrategyService;
    @Autowired
    private IConDeviceAttributeService conDeviceAttributeService;


    /**
     * 校验
     * @param inputAttrIds 输入
     * @param outputAttrIds 输出
     * @return
     */
    public boolean check(Set<String> inputAttrIds, Set<String> outputAttrIds) {
        // 初次校验
        for (String outputAttrId : outputAttrIds) {
            if (inputAttrIds.contains(outputAttrId)){
                return true;
            }
        }
        int i = 0;
        while (true) {
            i ++;
            Set<String> collectionNode = listAll(outputAttrIds);
            if (collectionNode.isEmpty()){
                return false;
            }
            for (String outAttrId : collectionNode) {
                if (inputAttrIds.contains(outAttrId)){
                    return true;
                }
            }
            outputAttrIds = collectionNode;
            if (i > 100){
                System.out.println(i);
                throw new HssBootException("事件嵌套过深或循环依赖,请先检查嵌套问题");
            }


        }
    }

    private Set<String> listAll(Set<String> attrIds){
        Set<String> list = new HashSet<>();
        Set<String> listPointVar = listPointVar(attrIds);
        if (CollectionUtils.isNotEmpty(listPointVar)){
            list.addAll(listPointVar);
        }
        Set<String> listLinage = listLinage(attrIds);
        if (CollectionUtils.isNotEmpty(listLinage)){
            list.addAll(listLinage);
        }
        Set<String> listAlarm = listAlarm(attrIds);
        if (CollectionUtils.isNotEmpty(listAlarm)){
            list.addAll(listAlarm);
        }
        return list;
    }

    private Set<String> listPointVar(Collection<String> attrIds){
        return conDeviceAttributeService.listIdByVarExpressionAttrIds(attrIds);
    }
    private Set<String> listLinage(Collection<String> attrIds){
        return linkageStrategyService.listAttrIdByExpressionAttrIds(attrIds);
    }
    private Set<String> listAlarm(Collection<String> attrIds){
        return alarmStrategyService.listAttrIdsByAttrIds(attrIds);
    }


}
