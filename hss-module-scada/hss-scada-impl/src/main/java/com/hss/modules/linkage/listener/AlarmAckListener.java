package com.hss.modules.linkage.listener;

import com.hss.modules.alarm.event.AlarmAckActLinkageEvent;
import com.hss.modules.linkage.entity.LinkageStrategy;
import com.hss.modules.linkage.service.ILinkageStrategyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 监听报警确认
 * @author hd
 */
@Component
@Slf4j
public class AlarmAckListener implements ApplicationListener<AlarmAckActLinkageEvent> {

    @Autowired
    private ILinkageStrategyService linkageStrategyService;


    @Override
    public void onApplicationEvent(AlarmAckActLinkageEvent event) {
        String linkageId = (String) event.getSource();
        if (StringUtils.isNotEmpty(linkageId)){
            try {
                LinkageStrategy byId = linkageStrategyService.getById(linkageId);
                if (byId != null && "1".equals(byId.getIsEnable())){
                    linkageStrategyService.runAction(byId);
                }
            } catch (Exception e) {
                log.error("报警确认执行联动失败", e);
            }
        }

    }
}
