package com.hss.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.system.entity.FilesUtil;
import com.hss.modules.system.mapper.FilesUtilMapper;
import com.hss.modules.system.service.IFilesUtilService;
import org.springframework.stereotype.Service;

/**
 * @Description: 附件及图片上传表
 * @Author: zpc
 * @Date:   2023-02-01
 * @Version: V1.0
 */
@Service
public class FilesUtilServiceImpl extends ServiceImpl<FilesUtilMapper, FilesUtil> implements IFilesUtilService {

}
