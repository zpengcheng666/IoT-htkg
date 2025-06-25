package com.hss.modules.spare.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.spare.entity.MaterialClassification;
import com.hss.modules.spare.model.ItemTypeModel;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description: 物料分类管理接口
 * @author wuyihan
 * @date 2024/4/25 14:38
 * @version 1.0
 */
public interface IMaterialClassificationService extends IService<MaterialClassification> {

    /**
     * 查询所有物料类型
     * @return
     */
    List<ItemTypeModel> queryTreeList(String ids);


    Map<String, String> getNameMap(Set<String> ids);
}
