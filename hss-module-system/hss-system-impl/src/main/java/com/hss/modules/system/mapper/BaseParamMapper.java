package com.hss.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.system.entity.BaseParam;

/**
 * @Description: 参数管理
 * @Author: zpc
 * @Date:   2023-05-15
 * @Version: V1.0
 */
public interface BaseParamMapper extends BaseMapper<BaseParam> {

    /**
     * 根据编号查询
     * @param code
     * @return
     */
    BaseParam getByCode(String code);
}
