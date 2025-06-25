package com.hss.modules.spare.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.spare.entity.ReceiptOrderEntity;
import com.hss.modules.spare.model.ReceiptAddDTO;
import com.hss.modules.spare.model.ReceiptDetail;
import com.hss.modules.spare.model.ReceiptPageDTO;
import com.hss.modules.spare.model.SpareDict;

import java.util.List;

/**
* @ClassDescription:
* @JdkVersion: 1.8
* @Author: hd
* @Created: 2024/4/25 16:04
*/
public interface IReceiptOrderService extends IService<ReceiptOrderEntity> {
    /**
     * 获取入库单编号
     * @return
     */
    String getNo();

    /**
     * 获取入库类型
     * @return
     */
    List<SpareDict> getTypes();


    /**
     * 添加入库单
     * @param dto
     */
    void add(ReceiptAddDTO dto);

    /**
     * 人也查询入库单列表
     * @param dto
     * @return
     */
    Page<ReceiptOrderEntity> getPage(ReceiptPageDTO dto);


    /**
     * 查询入库单详情
     * @param id
     * @return
     */
    ReceiptDetail getDetail(String id);
}
