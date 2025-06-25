package com.hss.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.core.common.system.vo.SelectTreeNode;
import com.hss.modules.system.entity.BaseLocation;
import com.hss.modules.system.mapper.BaseLocationMapper;
import com.hss.modules.system.service.IBaseLocationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 存储位置信息
 * @Author: zpc
 * @Date:   2022-11-03
 * @Version: V1.0
 */
@Service
public class BaseLocationServiceImpl extends ServiceImpl<BaseLocationMapper, BaseLocation> implements IBaseLocationService {

    @Override
    public List<SelectTreeNode> queryTreeList(String ids) {
        List<BaseLocation> locationList= this.list();
        List<SelectTreeNode> modelList = new ArrayList<>();
        // 添加第一级的单元
        for (int i = 0; i < locationList.size(); i++) {
            BaseLocation tmp = locationList.get(i);
            if (StringUtils.isEmpty(tmp.getPid()) || StringUtils.equals(tmp.getPid(),"0")) {
                modelList.add(createTreeNode(tmp, locationList));
            }
        }
        return  modelList;
    }

    private SelectTreeNode createTreeNode(BaseLocation location, List<BaseLocation> list){
        SelectTreeNode model = new SelectTreeNode();
        model.setId(location.getId());
        model.setName(location.getName());
        model.setTitle(location.getName());
        model.setDescription(location.getDescription());
        model.setPid(location.getPid());
        model.setClassName(location.getClassName());
        model.setClassId(location.getClassId());

        for (int i = 0; i < list.size(); i++) {
            BaseLocation tmp = list.get(i);
            if (StringUtils.equals(tmp.getPid(), location.getId())) {
                model.addChild(createTreeNode(tmp, list));
            }
        }

        return model;
    }
}
