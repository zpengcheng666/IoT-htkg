package com.hss.modules.inOutPosition.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.modules.inOutPosition.entity.InOutExternal;
import com.hss.modules.inOutPosition.entity.InOutInternal;
import com.hss.modules.inOutPosition.entity.InOutList;
import com.hss.modules.inOutPosition.mapper.InOutListMapper;
import com.hss.modules.inOutPosition.service.IInOutExternalService;
import com.hss.modules.inOutPosition.service.IInOutInternalService;
import com.hss.modules.inOutPosition.service.IInOutListService;
import com.hss.modules.system.entity.BaseUser;
import com.hss.modules.system.service.IBaseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Description: 进出阵地列表
 * @Author: zpc
 * @Date: 2022-12-13
 * @Version: V1.0
 */
@Service
public class InOutListServiceImpl extends ServiceImpl<InOutListMapper, InOutList> implements IInOutListService {

    @Autowired
    private IInOutExternalService inOutExternalService;
    @Autowired
    private IInOutInternalService inOutInternalService;
    @Autowired
    private IBaseUserService baseUserService;

    /**
     * @description: 添加审批单列表，同时保存内部、外部人员
     * @author zpc
     * @date 2022/12/14 9:41
     * @version 1.0
     */
    @Override
    public void addAll(InOutList inOutList) {
        List<InOutInternal> inList = inOutList.getInsiderList();

        List<InOutExternal> exitList = inOutList.getOutsiderList();

        // 1 . 创建一个申请单
        this.save(inOutList);

        // 2. 申请单的Id
        String listIds = inOutList.getId();

        // 3. 外部人员列表
        exitList.forEach(e -> {
            e.setListId(listIds);
            inOutExternalService.save(e);
        });

        // 4. 内部人员
        inList.forEach(e -> {
            e.setListId(listIds);
            inOutInternalService.save(e);
        });
    }

    /**
     * @description: 修改审批列表，同时也修改内部、外部人员信息
     * @author zpc
     * @date 2022/12/14 9:42
     * @version 1.0
     */
    @Override
    public void editAll(InOutList inOutList) {
        List<InOutInternal> inList = inOutList.getInsiderList();
        List<InOutExternal> exitList = inOutList.getOutsiderList();

        InOutList list = this.getById(inOutList.getId());
        list.setDepartment(inOutList.getDepartment());
        list.setLeaderId(inOutList.getLeaderId());
        list.setCount(inOutList.getCount());
        list.setZone(inOutList.getZone());
        list.setPosition1(inOutList.getPosition1());
        list.setReason(inOutList.getReason());
        list.setInTime(inOutList.getInTime());
        list.setOutTime(inOutList.getOutTime());
        list.setStatus(inOutList.getStatus());
        this.saveOrUpdate(list);

        String listIds = inOutList.getId();

        //外部人员列表
        LambdaQueryWrapper<InOutExternal> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(InOutExternal::getListId, inOutList.getId());
        this.inOutExternalService.remove(deleteWrapper);

        exitList.forEach(e -> {
            e.setListId(listIds);
            inOutExternalService.save(e);
        });

        //内部人员
        LambdaQueryWrapper<InOutInternal> deleteWrapper1 = new LambdaQueryWrapper<>();
        deleteWrapper1.eq(InOutInternal::getListId, inOutList.getId());
        this.inOutInternalService.remove(deleteWrapper1);

        inList.forEach(e -> {
            e.setListId(listIds);
            inOutInternalService.save(e);
        });
    }

    @Override
    public List<InOutList> listByPublish(Date date) {
        List<InOutList> list = baseMapper.listByPublish(date);
        list.forEach(e ->{
            //带队人
            BaseUser byId = baseUserService.getById(e.getLeaderId());
            e.setLeaderId_disp(byId == null ? "" :byId.getName());
        });
        return list;
    }
}
