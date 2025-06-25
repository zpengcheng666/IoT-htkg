package com.hss.modules.store.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.store.constant.StoreConstant;
import com.hss.modules.store.entity.StoreData;
import com.hss.modules.store.mapper.StoreDataMapper;
import com.hss.modules.store.service.IStoreDataService;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 设备运行时数据
 * @Author: zpc
 * @Date:   2022-12-01
 * @Version: V1.0
 */
@Service
public class StoreDataServiceImpl extends ServiceImpl<StoreDataMapper, StoreData> implements IStoreDataService{

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 设备缓存
     */
    public static final Map<String, String> VALUE_CACHE = new HashMap<>();




    @Override
    public void updateByVariableId(String variableId, String value) {
        VALUE_CACHE.put(variableId, value);
        redisUtil.hset(StoreConstant.REDIS_KEY_CURRENT_VALUE, variableId, value);
    }

    @Override
    public String getByVariableId(String variableId) {
        return VALUE_CACHE.get(variableId);
    }

}
