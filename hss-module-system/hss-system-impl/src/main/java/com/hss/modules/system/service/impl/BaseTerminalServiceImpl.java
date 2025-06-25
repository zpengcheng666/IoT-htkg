package com.hss.modules.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.system.entity.BaseTermianlInfo;
import com.hss.modules.system.entity.BaseTerminal;
import com.hss.modules.system.mapper.BaseTerminalMapper;
import com.hss.modules.system.model.TerminalInfoModel;
import com.hss.modules.system.service.IBaseTermianlInfoService;
import com.hss.modules.system.service.IBaseTerminalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @Description: 终端基本信息表
 * @Author: zpc
 * @Date: 2022-11-22
 * @Version: V1.0
 */
@Service
public class BaseTerminalServiceImpl extends ServiceImpl<BaseTerminalMapper, BaseTerminal> implements IBaseTerminalService {

    @Autowired
    private IBaseTermianlInfoService baseTermianlInfoService;
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void saveTerminalInfo(BaseTerminal baseTerminal) {
        this.save(baseTerminal);

        infoList(baseTerminal);
    }

    @Override
    public void updateTerminalInfo(BaseTerminal baseTerminal) {
        this.updateById(baseTerminal);

        LambdaQueryWrapper<BaseTermianlInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BaseTermianlInfo::getTerminalId,baseTerminal.getId());
        baseTermianlInfoService.remove(queryWrapper);
        infoList(baseTerminal);

    }

    private void infoList(BaseTerminal baseTerminal) {
        TerminalInfoModel[] infoList = baseTerminal.getInfoList();
        if (CollectionUtil.isEmpty(Arrays.asList(infoList))) {
            return;
        }
        Arrays.stream(infoList).forEach(e -> {
            BaseTermianlInfo info = new BaseTermianlInfo();
            info.setId(IdWorker.getIdStr());
            info.setTerminalId(baseTerminal.getId());
            info.setTerminalName(baseTerminal.getName());
            info.setVideoUrl(baseTerminal.getVideoUrl());
            info.setInfoType(e.getInfoType());
            info.setBackgroundImg(baseTerminal.getBackgroundImg());
            info.setBackgroundColor(baseTerminal.getBackgroundColor());
            info.setIsShow(0);
            info.setX(e.getX());
            info.setY(e.getY());
            info.setH(e.getH());
            info.setW(e.getW());
            info.setI(e.getI());
            baseTermianlInfoService.save(info);
        });
    }
}
