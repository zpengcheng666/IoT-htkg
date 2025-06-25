package com.hss.modules.facility.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.core.common.system.vo.SelectTreeNode;
import com.hss.modules.facility.entity.DeviceType;
import com.hss.modules.facility.mapper.DeviceTypeMapper;
import com.hss.modules.facility.model.StatQualityConditionModel;
import com.hss.modules.facility.service.IDeviceTypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description: 类别管理
 * @Author: zpc
 * @Date:   2022-12-08
 * @Version: V1.0
 */
@Service
public class DeviceTypeServiceImpl extends ServiceImpl<DeviceTypeMapper, DeviceType> implements IDeviceTypeService {

    /**
     * queryTreeList 根据设备类别id查询,前端回显调用
     */
    @Override
    public List<SelectTreeNode> queryTreeList(String ids) {
        //  获取所有设备类别，并封装成SelectTreeNode
        List<SelectTreeNode> treeNodesList= this.list().stream()
                .map(e -> SelectTreeNode.from(e.getId(), e.getName(), e.getPId(), e.getName()))
                .collect(Collectors.toList());

        //  获取父节点，0表示父节点
        List<SelectTreeNode> collect = treeNodesList.stream()
                //  父节点pid IS NULL OR pid equals to 0 OR IS EMPTY
                .filter(e -> Objects.isNull(e.getPid()) || StringUtils.isBlank(e.getPid()) || Objects.equals(e.getPid(), "0"))
                .map(e ->{
                    //  递归调用，生成子节点
                    e.setChildren(getchildrens(e, treeNodesList));
                    return e;
                }).collect(Collectors.toList());
        return collect;
    }

    //递归查询子节点
    private List<SelectTreeNode> getchildrens(SelectTreeNode root,List<SelectTreeNode> treeNodes){
        //  root为每次最新的传递过来的根节点
        List<SelectTreeNode> collect = treeNodes.stream()
                //  根据传递过来的根节点 ,拿到他的id，来查询出他的子节点id 这里有个特点 e.id = 子节点的父节点id
                .filter(e -> Objects.equals(e.getPid(), root.getId()))
                .map(e -> {
                    //  递归调用，生成子节点
                    e.setChildren(getchildrens(e, treeNodes));
                    return e;
                }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<StatQualityConditionModel> statQualityCondition(String classId) {
        return this.getBaseMapper().statQualityCondition(classId);
    }

    @Override
    public List<String> listIdByType(String type) {
        return baseMapper.listIdByType(type);
    }
}
