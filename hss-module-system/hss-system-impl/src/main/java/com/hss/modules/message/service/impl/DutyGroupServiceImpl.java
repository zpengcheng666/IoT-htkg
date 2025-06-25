package com.hss.modules.message.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.core.common.exception.HssBootException;
import com.hss.core.common.util.LogUtil;
import com.hss.modules.message.entity.DutyGroup;
import com.hss.modules.message.mapper.DutyGroupMapper;
import com.hss.modules.message.model.DutyPersonModel;
import com.hss.modules.message.service.IDutyGroupService;
import com.hss.modules.system.service.IBaseDictDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 值班人员
 * @Author: zpc
 * @Date:   2023-04-21
 * @Version: V1.0
 */
@Service
public class DutyGroupServiceImpl extends ServiceImpl<DutyGroupMapper, DutyGroup> implements IDutyGroupService {

    @Override
    public List<DutyGroup> listByDutyId(String dutyId) {
        return baseMapper.listByDutyId(dutyId);
    }

    @Override
    public void add(DutyGroup dutyGroup) {
        checkBaseInfo(dutyGroup);
        int count = baseMapper.countByDutyIdAndCode(dutyGroup.getDutyId(), dutyGroup.getCode());
        if (count > 0) {
            throw new HssBootException("编号重复");
        }
        save(dutyGroup);
        LogUtil.setOperate(dutyGroup.getName());
    }

    private void checkBaseInfo(DutyGroup dutyGroup) {
        DutyPersonModel[] personList = dutyGroup.getPersonList();
        if (personList == null || personList.length == 0) {
            throw new HssBootException("值班人员不能为空");
        }
        if (StringUtils.isBlank(dutyGroup.getDutyId())) {
            throw new HssBootException("值班安排不能为空");
        }
        if (dutyGroup.getCode() == null) {
            throw new HssBootException("小组编号不能为空");
        }
    }

    @Override
    public void editById(DutyGroup dutyGroup) {
        DutyGroup byId = getById(dutyGroup.getId());
        if (byId == null) {
            throw new HssBootException("小组不存在");
        }
        checkBaseInfo(dutyGroup);
        if (!byId.getCode().equals(dutyGroup.getCode())) {
            int count = baseMapper.countByDutyIdAndCode(dutyGroup.getDutyId(), dutyGroup.getCode());
            if (count > 0) {
                throw new HssBootException("编号重复");
            }
        }
        updateById(dutyGroup);
        LogUtil.setOperate(byId.getName());
    }

}
