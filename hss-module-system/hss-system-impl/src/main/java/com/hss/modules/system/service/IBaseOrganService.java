package com.hss.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.core.common.system.vo.SelectTreeNode;
import com.hss.modules.system.entity.BaseOrgan;

import java.util.List;

/**
 * @Description: 组织机构
 * @Author: zpc
 * @Date:   2022-11-03
 * @Version: V1.0
 */
public interface IBaseOrganService extends IService<BaseOrgan> {
    /**
     * 查询所有组织结构信息,并分节点进行显示
     * @param ids 多个组织id
     * @return
     */
    List<SelectTreeNode> queryTreeList(String ids);

}
