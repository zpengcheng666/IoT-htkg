package com.hss.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.system.entity.BaseMessage;
import com.hss.modules.system.mapper.BaseMessageMapper;
import com.hss.modules.system.service.IBaseMessageService;
import org.springframework.stereotype.Service;

/**
 * @Description: 消息管理
 * @Author: zpc
 * @Date:   2022-11-03
 * @Version: V1.0
 */
@Service
public class BaseMessageServiceImpl extends ServiceImpl<BaseMessageMapper, BaseMessage> implements IBaseMessageService {

}
