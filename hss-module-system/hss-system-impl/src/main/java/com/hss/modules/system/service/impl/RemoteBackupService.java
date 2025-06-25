package com.hss.modules.system.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.hss.core.common.api.vo.Result;
import com.hss.core.common.constant.CommonConstant;
import com.hss.modules.system.entity.SysBackup;
import com.hss.modules.system.service.IBaseParamService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
* @description: 数据备份
* @author zpc
* @date 2024/3/20 16:29
* @version 1.0
*/
@Service
@Log4j2
public class RemoteBackupService {

    public static final String BACKUP_URL_KEY = "system_devops_backup_url";

    public static final String BACKUP_METHOD = "/backup";

    public static final String RESTORE_METHOD = "/restore";

    @Autowired
    private IBaseParamService baseParamService;

    /**
     * 远程调用备份服务
     * @param backupType
     * @return
     */
    public Result<?> backup(String backupType){

        HashMap<String, Object> paramMap = new HashMap<>(0);
        paramMap.put("backupType", backupType);

        String backupUrl = baseParamService.getParamByCode(BACKUP_URL_KEY);
        if (StringUtils.isBlank(backupUrl)){
            log.error("备份/恢复 url为空： url {}", backupUrl);
            return Result.error(-1, "备份/恢复 url为空");
        }
        backupUrl += BACKUP_METHOD;

        String backupPath = "";
        String results = SysBackup.RESULTS_ERR;

        try {
            String resultJson = HttpUtil.post(backupUrl, JSONObject.toJSONString(paramMap));
            log.info("请求备份/恢复： result={}", resultJson);

            if (StringUtils.isNotBlank(resultJson)){
                JSONObject jsonObject = JSONObject.parseObject(resultJson);
                if (CommonConstant.SC_OK_200.equals(jsonObject.getInteger("code"))){
                    results = SysBackup.RESULTS_OK;
                    backupPath = jsonObject.getJSONObject("result").getString("fileName");
                }
            }
        } catch (Exception e) {
            //notes += e.getMessage();
            log.error("请求备份/恢复失败： param={},e={}", JSONObject.toJSONString(paramMap), e);
        }
        return null;
    }
}
