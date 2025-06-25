package com.hss.modules.tool.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.core.common.system.vo.SelectTreeNode;
import com.hss.modules.tool.entity.DatumClass;
import com.hss.modules.tool.mapper.DatumClassMapper;
import com.hss.modules.tool.service.IDatumClassService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 资料类别表
 * @Author: zpc
 * @Date:   2022-12-26
 * @Version: V1.0
 */
@Service
public class DatumClassServiceImpl extends ServiceImpl<DatumClassMapper, DatumClass> implements IDatumClassService {

    /**
     * queryTreeList 根据设备类别id查询,前端回显调用
     */
    @Override
    public List<SelectTreeNode> datumQueryTreeList(String ids) {
        LambdaQueryWrapper<DatumClass> query = new LambdaQueryWrapper<>();
        List<DatumClass> list= this.list(query);
        List<SelectTreeNode> _list = new ArrayList<>();
        // 添加第一级的单元
        for (int i = 0; i < list.size(); i++) {
            DatumClass tmp = list.get(i);
            if (StringUtils.isEmpty(tmp.getPid())){
                _list.add(this.createTreeNode(tmp, list));
            }
        }
        return  _list;
    }

    private SelectTreeNode createTreeNode(DatumClass datumType, List<DatumClass> list){
        SelectTreeNode model = new SelectTreeNode();
        model.setId(datumType.getId());
        model.setTitle(datumType.getName());
        model.setPid(datumType.getPid());
        for (int i = 0; i < list.size(); i++) {
            DatumClass tmp = list.get(i);
            if (StringUtils.equals(tmp.getPid(), datumType.getId())){
                model.addChild(createTreeNode(tmp, list));
            }
        }
        return model;
    }

}
