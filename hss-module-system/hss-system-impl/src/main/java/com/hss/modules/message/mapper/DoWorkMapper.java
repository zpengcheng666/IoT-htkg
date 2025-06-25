package com.hss.modules.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.modules.message.dto.DoWorkPageDTO;
import com.hss.modules.message.dto.DoWorkPageVO;
import com.hss.modules.message.entity.DoWork;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @Description: 手动值班
 * @Author: zpc
 * @Date:   2023-12-06
 * @Version: V1.0
 */
public interface DoWorkMapper extends BaseMapper<DoWork> {

    IPage<DoWorkPageVO> pageList(Page<DoWorkPageVO> page,@Param("dto") DoWorkPageDTO dto);

    List<DoWork> listOut(Date from);

    List<DoWork> listShowWorkByTerminalId(@Param("terminalId") String terminalId);

    List<DoWork> lisExcel(@Param("dto") DoWorkPageDTO dto);
}
