package com.hss.modules.inOutPosition.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.modules.inOutPosition.entity.InOutList;

import java.util.Date;
import java.util.List;

/**
 * @Description: 进出阵地列表
 * @Author: zpc
 * @Date:   2022-12-13
 * @Version: V1.0
 */
public interface InOutListMapper extends BaseMapper<InOutList> {


    /**
     * 查询需要发布到终端的数据
     * @param date
     * @return
     */
    List<InOutList> listByPublish(Date date);
}
