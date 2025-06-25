package com.hss.modules.tool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.core.common.system.vo.SelectTreeNode;
import com.hss.modules.tool.entity.DatumClass;

import java.util.List;

/**
 * @Description: 资料类别表
 * @Author: zpc
 * @Date:   2022-12-26
 * @Version: V1.0
 */
public interface IDatumClassService extends IService<DatumClass> {
    List<SelectTreeNode> datumQueryTreeList(String ids);
}
