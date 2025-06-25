package com.hss.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.system.entity.BaseRemind;

/**
 * @Description: 提醒设置
 * @Author: zpc
 * @Date:   2022-11-21
 * @Version: V1.0
 */
public interface IBaseRemindService extends IService<BaseRemind> {

    void updateValue(BaseRemind baseRemind);
}
