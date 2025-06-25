package com.hss.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.system.entity.BaseParam;

/**
 * @Description: 参数管理
 * @Author: zpc
 * @Date:   2023-05-15
 * @Version: V1.0
 */
public interface IBaseParamService extends IService<BaseParam> {

    /**
     * 根据code查询
     * @param code code
     * @return
     */
    BaseParam getByCode(String code);

    /**
     * 根据code查询参数值
     * @param code code
     * @return
     */
    String getParamByCode(String code);

}
