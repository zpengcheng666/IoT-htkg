package com.hss.modules.ventilation.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.ventilation.entity.PlanCustom;

/**
 * @Description: 自定义方案
 * @Author: zpc
 * @Date:   2023-04-25
 * @Version: V1.0
 */
public interface IPlanCustomService extends IService<PlanCustom> {

    /**
     * 分页查询
     * @param page
     * @param queryWrapper
     * @return
     */
    IPage<PlanCustom> getPage(Page<PlanCustom> page, QueryWrapper<PlanCustom> queryWrapper);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    PlanCustom queryById(String id);

    /**
     * 根据id查询
     * @param id
     */
    void delete(String id);

    /**
     * 编辑
     * @param planCustom
     */
    void edit(PlanCustom planCustom);

    /**
     * 添加
     * @param planCustom
     */
    void add(PlanCustom planCustom);
}
