package com.hss.modules.spare.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.spare.entity.Carrier;

import java.util.Map;
import java.util.Set;

/**
 * @Description: 供应商表
 * @Author: jeecg-boot
 * @Date:   2024-04-26
 * @Version: V1.0
 */
public interface ICarrierService extends IService<Carrier> {

    Map<String, String> nameMap(Set<String> ids);
}
