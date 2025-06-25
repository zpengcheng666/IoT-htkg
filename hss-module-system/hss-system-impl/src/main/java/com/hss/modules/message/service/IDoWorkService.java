package com.hss.modules.message.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.message.dto.DoWorkPageDTO;
import com.hss.modules.message.dto.DoWorkPageVO;
import com.hss.modules.message.dto.PublishMessageDTO;
import com.hss.modules.message.entity.DoWork;
import com.hss.modules.message.model.DoWorkTerminalInfoVO;

import java.util.List;

/**
 * @Description: 手动值班
 * @Author: zpc
 * @Date:   2023-12-06
 * @Version: V1.0
 */
public interface IDoWorkService extends IService<DoWork> {

    /**
     * 分页查询
     * @param dto 参数
     * @return 分页数据
     */
    IPage<DoWorkPageVO> pageList(DoWorkPageDTO dto);

    /**
     * 发布
     * @param dto 参数
     */
    void publish(PublishMessageDTO dto);

    /**
     * 查询发布
     * @param id 消息id
     */
    void revocation(String id);


    /**
     * 检查超时
     */
    void checkTimeOut();

    /**
     * 列表查询
     * @param terminalId 终端id
     * @return 消息列表
     */
    List<DoWorkTerminalInfoVO> listShowWorkByTerminalId(String terminalId);

    /**
     * excel查询
     * @param dto 参数
     * @return 消息列表
     */
    List<DoWork> listExcel(DoWorkPageDTO dto);

    /**
     * 新增
     * @param doWork
     */
    void add(DoWork doWork);

    /**
     * 编辑
     * @param doWork
     */
    void edit(DoWork doWork);

    /**
     * 批量添加
     * @param list
     */
    void addList(List<DoWork> list);
}
