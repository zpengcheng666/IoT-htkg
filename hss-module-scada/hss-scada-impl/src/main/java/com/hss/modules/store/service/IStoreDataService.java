package com.hss.modules.store.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.store.entity.StoreData;

/**
 * @Description: 设备运行时数据
 * @Author: zpc
 * @Date:   2022-12-01
 * @Version: V1.0
 */
public interface IStoreDataService extends IService<StoreData> {


    /**
     * 数据更新
     * @param variableId
     * @param value
     */
    void updateByVariableId(String  variableId, String value);


    /**
     * 获取数据
     * @param variableId
     * @return
     */
    String getByVariableId(String  variableId);

}
