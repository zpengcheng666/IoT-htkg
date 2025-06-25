package com.hss.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.system.entity.BaseTerminalTemplate;
import com.hss.modules.system.mapper.BaseTerminalTemplateMapper;
import com.hss.modules.system.service.IBaseTerminalTemplateService;
import org.springframework.stereotype.Service;


/**
 * @Description: 终端模板
 * @Author: zpc
 * @Date: 2023-04-04
 * @Version: V1.0
 */
@Service
public class BaseTerminalTemplateServiceImpl extends ServiceImpl<BaseTerminalTemplateMapper, BaseTerminalTemplate> implements IBaseTerminalTemplateService {
    @Override
    public void saveTemplate(BaseTerminalTemplate baseTerminalTemplate) {
        this.save(baseTerminalTemplate);
    }
}
