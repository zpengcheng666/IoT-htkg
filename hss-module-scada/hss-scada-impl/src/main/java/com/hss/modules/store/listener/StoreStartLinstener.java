package com.hss.modules.store.listener;

import com.hss.modules.store.constant.StoreConstant;
import com.hss.modules.store.service.impl.StoreDataServiceImpl;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class StoreStartLinstener  implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private RedisUtil redisUtil;
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Map<Object, Object> cache = redisUtil.hmget(StoreConstant.REDIS_KEY_CURRENT_VALUE);
        if (cache != null){
            for (Map.Entry<Object, Object> objectObjectEntry : cache.entrySet()) {
                StoreDataServiceImpl.VALUE_CACHE.put(((String) objectObjectEntry.getKey()), ((String) objectObjectEntry.getValue()));
            }
        }
    }
}
