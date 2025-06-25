package com.hss.modules.tool.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.tool.entity.Tool;
import com.hss.modules.tool.mapper.ToolMapper;
import com.hss.modules.tool.service.IToolService;
import org.springframework.stereotype.Service;

/**
 * @Description: 工具管理
 * @Author: zpc
 * @Date:   2022-12-23
 * @Version: V1.0
 */
@Service
public class ToolServiceImpl extends ServiceImpl<ToolMapper, Tool> implements IToolService {

}
