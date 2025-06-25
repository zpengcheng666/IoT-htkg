package com.hss.modules.maintain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.core.common.exception.HssBootException;
import com.hss.core.common.system.vo.LoginUser;
import com.hss.core.common.util.LogUtil;
import com.hss.core.common.util.OConvertUtils;
import com.hss.modules.maintain.constant.MaintainConstant;
import com.hss.modules.maintain.entity.MaintainRecord;
import com.hss.modules.maintain.entity.MaintainRecordOP;
import com.hss.modules.maintain.entity.MaintainSchemas;
import com.hss.modules.maintain.entity.R_MTDeviceRecordItem;
import com.hss.modules.maintain.mapper.MaintainRecordMapper;
import com.hss.modules.maintain.model.MaintainSubmitDTO;
import com.hss.modules.maintain.service.*;
import com.hss.modules.system.entity.BaseUser;
import com.hss.modules.system.service.IBaseUserService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 保养任务表
 * @Author: zpc
 * @Date: 2022-12-15
 * @Version: V1.0
 */
@Service
public class MaintainRecordServiceImpl extends ServiceImpl<MaintainRecordMapper, MaintainRecord> implements IMaintainRecordService {

    @Autowired
    private IMaintainRecordOPService maintainRecordOPService;

    @Autowired
    private IR_MTDeviceRecordItemService r_MTDeviceRecordItemService;

    @Autowired
    private IMaintainSchemasService maintainSchemasService;

    @Autowired
    private IBaseUserService baseUserService;

    @Override
    public void addRecordDevice(MaintainRecord record) {
        // 1 . 保存保养任务列表
        record.setStatus(0);
        BaseUser creator = this.baseUserService.getById(record.getCreatorId());
        record.setCreator(creator == null ? "" : creator.getName());
        BaseUser prinpical = this.baseUserService.getById(record.getPrincipalId());
        record.setPrincipal(prinpical == null ? "" : prinpical.getName());
        MaintainSchemas schema = this.maintainSchemasService.getById(record.getSchemaId());
        record.setSchemaName(schema == null ? "": schema.getSchemasName());

        this.save(record);

        //2.保养任务Id
        String recordId = record.getId();

        //5. 设备id为多个
        List<String> deviceList = record.getDeviceList();
        if (deviceList == null || deviceList.isEmpty()){
            return ;
        }

        for (String devId : deviceList) {
            //6.保养任务与设备关系表
            R_MTDeviceRecordItem recordItem = new R_MTDeviceRecordItem();
            recordItem.setDeviceId(devId);
            recordItem.setRecordId(recordId);
            recordItem.setSchemasId(record.getSchemaId());
            this.r_MTDeviceRecordItemService.save(recordItem);
        }
    }

    @Override
    public void updateRecordDevice(MaintainRecord maintainRecord) {
        //更新保养任务主表内容
        BaseUser creator = this.baseUserService.getById(maintainRecord.getCreatorId());
        maintainRecord.setCreator(creator == null ? "" : creator.getName());
        BaseUser prinpical = this.baseUserService.getById(maintainRecord.getPrincipalId());
        maintainRecord.setPrincipal(prinpical == null ? "" : prinpical.getName());
        MaintainSchemas schema = this.maintainSchemasService.getById(maintainRecord.getSchemaId());
        maintainRecord.setSchemaName(schema == null ? "": schema.getSchemasName());
        this.updateById(maintainRecord);

        //保养任务id
        String recordId = maintainRecord.getId();
        //1.根据任务id删除保养任务和设备id关系
        LambdaQueryWrapper<R_MTDeviceRecordItem> queryWrapper = new LambdaQueryWrapper<>();
        if (OConvertUtils.isNotEmpty(recordId)){
            queryWrapper.eq(R_MTDeviceRecordItem::getRecordId,recordId);
        }
        this.r_MTDeviceRecordItemService.remove(queryWrapper);

        //2.根据任务id删除保养任务和保养类别、设备类别、设备id
        LambdaQueryWrapper<MaintainRecordOP> query = new LambdaQueryWrapper<>();
        if (OConvertUtils.isNotEmpty(recordId)){
            query.eq(MaintainRecordOP::getRecordId,recordId);
        }
        this.maintainRecordOPService.remove(query);

        //3. 设备id为多个
        List<String> deviceList = maintainRecord.getDeviceList();
        if (deviceList == null || deviceList.isEmpty()){
            return ;
        }

        for (String devId : deviceList) {
            //6.保养任务与设备关系表
            R_MTDeviceRecordItem recordItem = new R_MTDeviceRecordItem();
            recordItem.setDeviceId(devId);
            recordItem.setRecordId(recordId);
            recordItem.setSchemasId(maintainRecord.getSchemaId());
            this.r_MTDeviceRecordItemService.save(recordItem);
        }
    }

    @Override
    public void confirm(String id) {
        MaintainRecord byId = getById(id);
        if (!MaintainConstant.RECORD_STATE_DEFAULT.equals(byId.getStatus())){
            throw new HssBootException("签发失败,任务不是待签发状态!");
        }
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (!sysUser.getId().equals(byId.getCreatorId())){
            throw new HssBootException("签发失败,当前用户不是签发人!");
        }
        MaintainRecord update = new MaintainRecord();
        update.setId(id);
        update.setConfirmTime(new Date());
        update.setStatus(MaintainConstant.RECORD_STATE_CONFIRM);
        updateById(update);
        LogUtil.setOperate(byId.getSchemaName());
    }

    @Override
    public void act(String id) {
        MaintainRecord byId = getById(id);
        if (!MaintainConstant.RECORD_STATE_CONFIRM.equals(byId.getStatus())){
            throw new HssBootException("执行失败,任务不是待执行状态!");
        }
        MaintainRecord update = new MaintainRecord();
        update.setId(id);
        update.setActTime(new Date());
        update.setStatus(MaintainConstant.RECORD_STATE_ACT);
        updateById(update);
        LogUtil.setOperate(byId.getSchemaName());

    }

    @Override
    public void submit(MaintainSubmitDTO dto) {
        MaintainRecord byId = getById(dto.getId());
        if (!MaintainConstant.RECORD_STATE_ACT.equals(byId.getStatus())){
            throw new HssBootException("提交失败,任务不是执行中状态!");
        }
        List<MaintainRecordOP> list = maintainRecordOPService.listByRecordId(dto.getId());
        Map<String, MaintainRecordOP> map = list.stream()
                .filter(o -> !MaintainConstant.MAINTAIN_RECORD_OP_COMPLETE.equals(o.getIsComplete()))
                .collect(Collectors.toMap(MaintainRecordOP::getId, o -> o));
        ArrayList<MaintainRecordOP> updateItemList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(dto.getOperateIds())){
            Date date = new Date();
            for (String operateId : dto.getOperateIds()) {
                MaintainRecordOP maintainRecordOP = map.get(operateId);
                if (maintainRecordOP != null){
                    map.remove(operateId);
                    MaintainRecordOP update = new MaintainRecordOP();
                    update.setId(maintainRecordOP.getId());
                    update.setIsComplete(MaintainConstant.MAINTAIN_RECORD_OP_COMPLETE);
                    update.setTime(date);
                    updateItemList.add(update);

                }
            }
        }
        if (!updateItemList.isEmpty()){
            maintainRecordOPService.updateBatchById(updateItemList);
        }
        if (map.isEmpty()){
            MaintainRecord update = new MaintainRecord();
            update.setId(dto.getId());
            update.setStatus(MaintainConstant.RECORD_STATE_ACT_COMPLETE);
            update.setSubmitTime(new Date());
            updateById(update);
        }
        LogUtil.setOperate(byId.getSchemaName());
    }

    @Override
    public void complete(String id) {
        MaintainRecord byId = getById(id);
        if (!MaintainConstant.RECORD_STATE_ACT_COMPLETE.equals(byId.getStatus())){
            throw new HssBootException("审核失败,任务不是待审核状态!");
        }
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (!sysUser.getId().equals(byId.getCreatorId())){
            throw new HssBootException("审核失败,当前用户不是签发人!");
        }
        MaintainRecord update = new MaintainRecord();
        update.setId(id);
        update.setCompleteTime(new Date());
        update.setStatus(MaintainConstant.RECORD_STATE_COMPLETE);
        updateById(update);
        LogUtil.setOperate(byId.getSchemaName());
    }
}
