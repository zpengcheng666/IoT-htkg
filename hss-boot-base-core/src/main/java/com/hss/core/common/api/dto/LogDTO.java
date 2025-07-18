package com.hss.core.common.api.dto;
import com.hss.core.common.system.vo.LoginUser;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 日志对象
 * cloud api 用到的接口传输对象
 * @author: jeecg-boot
 */
@Data
public class LogDTO implements Serializable {

    private static final long serialVersionUID = 8482720462943906924L;

    //记录名称
    private String recordName;
    //记录id
    private String recordId;

    /**内容*/
    private String logContent;
    private String operateContent;

    /**日志类型(0:操作日志;1:登录日志;2:定时任务)  */
    private Integer logType;

    /**操作类型(1:添加;2:修改;3:删除;) */
    private Integer operateType;

    /**登录用户 */
    private LoginUser loginUser;

    private String id;
    private String createBy;

    private Date createTime;
    private Date stateTime;

    private Long costTime;
    private String ip;

    /**请求参数 */
    private String requestParam;

    /**请求类型*/
    private String requestType;

    /**请求路径*/
    private String requestUrl;

    /**请求方法 */
    private String method;

    /**操作人用户名称*/
    private String username;

    /**操作人用户账户*/
    private String userid;
    private String userId;

    public LogDTO(){

    }

    public LogDTO(String logContent, Integer logType, Integer operatetype){
        this.logContent = logContent;
        this.logType = logType;
        this.operateType = operatetype;
    }

    public LogDTO(String logContent, Integer logType, Integer operatetype, LoginUser loginUser){
        this.logContent = logContent;
        this.logType = logType;
        this.operateType = operatetype;
        this.loginUser = loginUser;
    }
}
