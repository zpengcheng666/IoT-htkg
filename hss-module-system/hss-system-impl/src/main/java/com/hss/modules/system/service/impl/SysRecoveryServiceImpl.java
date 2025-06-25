package com.hss.modules.system.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.constant.CommonConstant;
import com.hss.core.common.system.vo.LoginUser;
import com.hss.core.common.util.LoginUserUtils;
import com.hss.modules.system.entity.SysBackup;
import com.hss.modules.system.entity.SysRecovery;
import com.hss.modules.system.mapper.SysRecoveryMapper;
import com.hss.modules.system.service.IBaseParamService;
import com.hss.modules.system.service.ISysBackupService;
import com.hss.modules.system.service.ISysRecoveryService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;

/**
 * @Description: 系统备份
 * @Author: zpc
 * @Date:   2023-05-15
 * @Version: V1.0
 */
@Service
@Log4j2
public class SysRecoveryServiceImpl extends ServiceImpl<SysRecoveryMapper, SysRecovery> implements ISysRecoveryService {

    public static final String BACKUP_URL_KEY = "system_devops_backup_url";

    public static final String RESTORE_METHOD = "/restore";

    @Autowired
    private IBaseParamService baseParamService;

    @Autowired
    private ISysBackupService sysBackupService;

    @Override
    public Result<?> recovery(SysRecovery sysRecovery) {

        Long startTime = System.currentTimeMillis();
        LoginUser loginUser = LoginUserUtils.getUser();

        // 1: 判断备份文件是否存在
        SysBackup backup = sysBackupService.getById(sysRecovery.getBackupId());
        if (backup == null){
            return Result.error("备份记录不存在，请选择有效的备份记录进行恢复！");
        }
        if (StringUtils.isEmpty(backup.getFilePath())){
            return Result.error("备份文件路径不存在，请选择有效的备份记录进行恢复！");
        }

        // 2: 获取备份文件，进行备份操作，调用备用服务
        String results = SysBackup.RESULTS_ERR;
        String backupPath = backup.getFilePath();

        HashMap<String, Object> paramMap = new HashMap<>(0);
        paramMap.put("fileName", backupPath);
        paramMap.put("backupType", sysRecovery.getRecoveryType());

        String backupUrl = baseParamService.getParamByCode(BACKUP_URL_KEY);
        if (StringUtils.isBlank(backupUrl)){
            log.error("备份/恢复 url为空");
            return Result.error(-1, "备份/恢复 url为空");
        }
        backupUrl += RESTORE_METHOD;

        String notes = sysRecovery.getNotes();
        try {
            String resultJson = HttpUtil.post(backupUrl, JSONObject.toJSONString(paramMap));
            log.info("请求备份/恢复： result={}", resultJson);

            if (StringUtils.isNotBlank(resultJson)){
                JSONObject jsonObject = JSONObject.parseObject(resultJson);
                if (CommonConstant.SC_OK_200.equals(jsonObject.getInteger("code"))){
                    results = SysBackup.RESULTS_OK;
                }
            }
        } catch (Exception e) {
            notes += e.getMessage();
            log.error("请求备份/恢复失败： param={},e={}", JSONObject.toJSONString(paramMap), e);
        }

        // 3: 记录恢复操作日志
        SysRecovery entity = new SysRecovery();
        entity.setBackupId(sysRecovery.getBackupId());
        entity.setRecoveryType(backup.getBackupType());
        entity.setStartTime(new Date());
        Long duration = (System.currentTimeMillis() - startTime) / 1000;
        log.info("duration: {};  results: {}", duration, results);
        entity.setDuration(duration);

        entity.setResults(results);
        entity.setOperator(loginUser == null ? "admin" : loginUser.getRealname());
        entity.setNotes(notes);
        this.save(entity);

        // 4. 更新恢复次数
        backup.setRecoveryCnt(backup.getRecoveryCnt() + 1);
        sysBackupService.saveOrUpdate(backup);

        return Result.OK("系统恢复成功");
    }
}
