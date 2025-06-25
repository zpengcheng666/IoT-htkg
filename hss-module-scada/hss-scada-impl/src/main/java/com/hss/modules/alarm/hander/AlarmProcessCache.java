package com.hss.modules.alarm.hander;

import com.hss.modules.alarm.constant.AlarmConstant;
import com.hss.modules.alarm.entity.AlarmData;
import com.hss.modules.alarm.model.AlarmProcessData;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

/**
 * 获取报警缓存
 * 并设置标识位
 * @author hd
 */
@Service
@Order(AlarmProcess.CACHE)
public class AlarmProcessCache implements AlarmProcess {

    @Autowired
    private RedisUtil redisUtil;
    @Override
    public boolean process(AlarmProcessData data) {
        // 查询报警缓存
        Object o = redisUtil.get(AlarmConstant.REDIS_KEY_ALARM + data.getStrategy().getId());
        if (o == null){
            data.setCached(false);
        }else {
            data.setCached(true);
            data.setAlarmData(((AlarmData) o));
        }
        return true;
    }
}
