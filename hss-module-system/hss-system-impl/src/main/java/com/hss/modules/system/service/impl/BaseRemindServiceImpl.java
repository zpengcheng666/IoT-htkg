package com.hss.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.system.entity.BaseRemind;
import com.hss.modules.system.mapper.BaseRemindMapper;
import com.hss.modules.system.service.IBaseRemindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 提醒设置
 * @Author: zpc
 * @Date:   2022-11-21
 * @Version: V1.0
 */
@Service
public class BaseRemindServiceImpl extends ServiceImpl<BaseRemindMapper, BaseRemind> implements IBaseRemindService {

    @Autowired
    private  BaseRemindMapper baseRemindMapper;

    @Override
    @Transactional
    public void updateValue(BaseRemind baseRemind) {
        baseRemindMapper.updateValue(baseRemind);
    }
}
