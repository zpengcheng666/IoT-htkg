package com.hss.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.core.common.exception.HssBootException;
import com.hss.modules.system.entity.BaseParam;
import com.hss.modules.system.mapper.BaseParamMapper;
import com.hss.modules.system.service.IBaseParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * @Description: 参数管理
 * @Author: zpc
 * @Date:   2023-05-15
 * @Version: V1.0
 */
@Service
public class BaseParamServiceImpl extends ServiceImpl<BaseParamMapper, BaseParam> implements IBaseParamService {

    @Autowired
    private IBaseParamService baseParamService;

    private static final String REDIS_KEY = "sys:cache:param";

    @Override
    @CacheEvict(value = REDIS_KEY, key = "#entity.code")
    public boolean save(BaseParam entity) {
        BaseParam param = baseParamService.getByCode(entity.getCode());
        if (param != null) {
            throw new HssBootException("参数编号已经存在");
        }
        return super.save(entity);
    }

    @Override
    public BaseParam getByCode(String code) {
        return baseMapper.getByCode(code);
    }

    @Override
    @Cacheable(value = REDIS_KEY, key = "#code")
    public String getParamByCode(String code) {
        BaseParam param = getByCode(code);
        if (param == null) {
            return null;
        }
        return param.getValue();
    }

    @Override
    public boolean removeById(Serializable id) {
        BaseParam byId = getById(id);
        if (byId == null){
            return true;
        }
        return baseParamService.removeById(byId);
    }

    @Override
    @CacheEvict(value = REDIS_KEY, key = "#entity.code")
    public boolean removeById(BaseParam entity) {
        return super.removeById(entity);
    }

    @Override
    @CacheEvict(value = REDIS_KEY, key = "#entity.code")
    public boolean updateById(BaseParam entity) {
        BaseParam param = baseParamService.getByCode(entity.getCode());
        if (param != null && !param.getId().equals(entity.getId())) {
            throw new HssBootException("参数编号已经存在");
        }
        return super.updateById(entity);
    }

    @Override
    public BaseParam getById(Serializable id) {
        return super.getById(id);
    }


}
