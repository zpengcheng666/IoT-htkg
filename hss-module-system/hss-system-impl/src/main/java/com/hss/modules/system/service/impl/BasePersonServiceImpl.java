package com.hss.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.system.entity.BasePerson;
import com.hss.modules.system.mapper.BasePersonMapper;
import com.hss.modules.system.service.IBasePersonService;
import org.springframework.stereotype.Service;

/**
 * @Description: 人员管理表
 * @Author: zpc
 * @Date:   2022-11-03
 * @Version: V1.0
 */
@Service
public class BasePersonServiceImpl extends ServiceImpl<BasePersonMapper, BasePerson> implements IBasePersonService {

}
