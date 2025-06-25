package com.hss.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.core.common.system.vo.SelectTreeNode;
import com.hss.modules.system.entity.BaseOrgan;
import com.hss.modules.system.mapper.BaseOrganMapper;
import com.hss.modules.system.service.IBaseOrganService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 组织机构
 * @Author: zpc
 * @Date:   2022-11-03
 * @Version: V1.0
 */
@Service
public class BaseOrganServiceImpl extends ServiceImpl<BaseOrganMapper, BaseOrgan> implements IBaseOrganService {

    /**
     * queryTreeList 根据组织结构id查询,前端回显调用
     */
    @Override
    public List<SelectTreeNode> queryTreeList(String ids) {
        LambdaQueryWrapper<BaseOrgan> query = new LambdaQueryWrapper<>();
        query.orderByAsc(BaseOrgan::getOrderNum);
        List<BaseOrgan> list= this.list(query);
        List<SelectTreeNode> treeList = new ArrayList<>();
        // 添加第一级的单元
        for (int i = 0; i < list.size(); i++) {
            BaseOrgan tmp = list.get(i);
            if (StringUtils.isEmpty(tmp.getPid()) || "0".equals(tmp.getPid())){
                treeList.add(this.createTreeNode(tmp, list));
            }
        }
        return  treeList;
    }

    private SelectTreeNode createTreeNode(BaseOrgan organ, List<BaseOrgan> list){
        SelectTreeNode model = new SelectTreeNode();
        model.setId(organ.getId());
        model.setName(organ.getName());
        model.setTitle(organ.getName());
        model.setDescription(organ.getDescription());
        model.setPid(organ.getPid());
        for (int i = 0; i < list.size(); i++) {
            BaseOrgan tmp = list.get(i);
            if (StringUtils.equals(tmp.getPid(), organ.getId())){
                model.addChild(createTreeNode(tmp, list));
            }
        }
        return model;
    }
}
