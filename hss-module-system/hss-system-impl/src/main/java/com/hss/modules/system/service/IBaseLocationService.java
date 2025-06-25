package com.hss.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.core.common.system.vo.SelectTreeNode;
import com.hss.modules.system.entity.BaseLocation;

import java.util.List;

/**
 * @Description: 存储位置信息
 * @Author: zpc
 * @Date:   2022-11-03
 * @Version: V1.0
 */
public interface IBaseLocationService extends IService<BaseLocation> {
    /**
     * 查询所有存储位置信息,并分节点进行显示
     * @return
     */
    List<SelectTreeNode> queryTreeList(String ids);

}
