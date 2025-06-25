package com.hss.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.system.entity.BaseLog;
import com.hss.modules.system.mapper.BaseLogMapper;
import com.hss.modules.system.service.IBaseLogService;
import org.springframework.stereotype.Service;

/**
 * @Description: 日志表
 * @Author: zpc
 * @Date:   2022-12-05
 * @Version: V1.0
 */
@Service
public class BaseLogServiceImpl extends ServiceImpl<BaseLogMapper, BaseLog> implements IBaseLogService {

}
