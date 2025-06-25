package com.hss.modules.inOutPosition.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.inOutPosition.entity.InOutList;

import java.util.Date;
import java.util.List;

/**
 * @Description: 进出阵地列表
 * @Author: zpc
 * @Date:   2022-12-13
 * @Version: V1.0
 */
public interface IInOutListService extends IService<InOutList> {

    void addAll(InOutList inOutList);

    void editAll(InOutList inOutList);

    /**
     * 获取进出信息
     * @param date
     * @return
     */
    List<InOutList> listByPublish(Date date);
}
