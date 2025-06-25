package com.hss.modules.linkage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.linkage.entity.LinkageData;
import com.hss.modules.linkage.mapper.LinkageDataMapper;
import com.hss.modules.linkage.service.ILinkageDataService;
import org.springframework.stereotype.Service;

/**
 * @Description: 联动数据记录
 * @Author: zpc
 * @Date:   2023-01-05
 * @Version: V1.0
 */
@Service
public class LinkageDataServiceImpl extends ServiceImpl<LinkageDataMapper, LinkageData> implements ILinkageDataService {

}
