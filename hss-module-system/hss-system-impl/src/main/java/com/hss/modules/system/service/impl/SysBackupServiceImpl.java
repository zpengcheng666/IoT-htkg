package com.hss.modules.system.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.core.common.constant.CommonConstant;
import com.hss.core.common.system.vo.LoginUser;
import com.hss.core.common.util.LoginUserUtils;
import com.hss.modules.system.entity.SysBackup;
import com.hss.modules.system.mapper.SysBackupMapper;
import com.hss.modules.system.service.IBaseParamService;
import com.hss.modules.system.service.ISysBackupService;
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
public class SysBackupServiceImpl extends ServiceImpl<SysBackupMapper, SysBackup> implements ISysBackupService {

    public static final String BACKUP_URL_KEY = "system_devops_backup_url";

    public static final String BACKUP_METHOD = "/backup";

    @Autowired
    private IBaseParamService baseParamService;

    @Override
    public boolean backup(SysBackup sysBackup) {

        Long startTime = System.currentTimeMillis();
        LoginUser loginUser = LoginUserUtils.getUser();

        // 1: 调用备用服务
        String results = SysBackup.RESULTS_ERR;
        String backupPath = "";

        HashMap<String, Object> paramMap = new HashMap<>(0);
        paramMap.put("backupType", sysBackup.getBackupType());

        String backupUrl = baseParamService.getParamByCode(BACKUP_URL_KEY);
        if (StringUtils.isBlank(backupUrl)){
            log.error("备份/恢复 url为空： url {}", backupUrl);
            return false;
            //return Result.error(-1, "");
        }
        backupUrl += BACKUP_METHOD;

        String notes = sysBackup.getNotes();
        try {
            String resultJson = HttpUtil.post(backupUrl, JSONObject.toJSONString(paramMap));
            log.info("请求备份/恢复： result={}", resultJson);

            if (StringUtils.isNotBlank(resultJson)){
                JSONObject jsonObject = JSONObject.parseObject(resultJson);
                if (CommonConstant.SC_OK_200.equals(jsonObject.getInteger("code"))){
                    results = SysBackup.RESULTS_OK;
                    notes += "  SUCCESS";
                    backupPath = jsonObject.getJSONObject("result").getString("fileName");
                }
            }
        } catch (Exception e) {
            notes += e.getMessage();
            log.error("请求备份/恢复失败： param={},e={}", JSONObject.toJSONString(paramMap), e);
        }

        // 2: 记录备份记录，用于以后的恢复
        SysBackup entity = new SysBackup();
        entity.setBackupName(sysBackup.getBackupName());
        Long duration = (System.currentTimeMillis() - startTime) / 1000;
        log.info("duration: {};  results: {}", duration, results);
        entity.setDuration(duration) ;
        entity.setBackupType(sysBackup.getBackupType());
        entity.setOperator(loginUser == null ? "admin" : loginUser.getRealname());
        entity.setNotes(notes);
        entity.setFilePath(backupPath);
        entity.setStartTime(new Date());
        entity.setResults(results);
        entity.setRecoveryCnt(0);
        this.save(entity);
        return true;
    }
}
