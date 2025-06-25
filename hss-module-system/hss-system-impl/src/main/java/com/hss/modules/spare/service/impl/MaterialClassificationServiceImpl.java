package com.hss.modules.spare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.spare.entity.MaterialClassification;
import com.hss.modules.spare.mapper.MaterialClassificationMapper;
import com.hss.modules.spare.model.ItemTypeModel;
import com.hss.modules.spare.service.IMaterialClassificationService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @Description: 物料分类管理接口实现类
 * @Author: wuyihan
 * @Date:   2024/4/25 14:38
 * @Version: V1.0
 */
@Service
public class MaterialClassificationServiceImpl extends ServiceImpl<MaterialClassificationMapper, MaterialClassification> implements IMaterialClassificationService {
    @Override
    public List<ItemTypeModel> queryTreeList(String ids) {
        LambdaQueryWrapper<MaterialClassification> query = new LambdaQueryWrapper<>();
        query.orderByAsc(MaterialClassification::getOrderNum);
        List<MaterialClassification> list= this.list(query);
        List<ItemTypeModel> treeList = new ArrayList<>();
        // 添加第一级的单元
        for (int i = 0; i < list.size(); i++) {
            MaterialClassification tmp = list.get(i);
            if (StringUtils.isEmpty(tmp.getParentId()) || "0".equals(tmp.getParentId())){
                treeList.add(this.createTreeNode(tmp, list));
            }
        }
        return  treeList;
    }

    @Override
    public Map<String, String> getNameMap(Set<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new HashMap<>();
        }
        return listByIds(ids).stream().collect(Collectors.toMap(MaterialClassification::getId, MaterialClassification::getTypeName));
    }

    private ItemTypeModel createTreeNode(MaterialClassification materialClassification, List<MaterialClassification> list){
        ItemTypeModel model = new ItemTypeModel();
        model.setId(materialClassification.getId());
        model.setTypeName(materialClassification.getTypeName());
        model.setParentId(materialClassification.getParentId());
        model.setStatus(materialClassification.getStatus());
        model.setOrderNum(materialClassification.getOrderNum());
        for (int i = 0; i < list.size(); i++) {
            MaterialClassification tmp = list.get(i);
            if (StringUtils.equals(tmp.getParentId(), materialClassification.getId())){
                model.addChild(createTreeNode(tmp, list));
            }
        }
        return model;
    }
}
