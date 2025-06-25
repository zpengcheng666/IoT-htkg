package com.hss.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.modules.system.entity.BaseTerminalTemplate;

/**
 * @Description: 终端模板
 * @Author: zpc
 * @Date:   2023-04-04
 * @Version: V1.0
 */
public interface IBaseTerminalTemplateService extends IService<BaseTerminalTemplate> {

    void saveTemplate(BaseTerminalTemplate baseTerminalTemplate);
}
