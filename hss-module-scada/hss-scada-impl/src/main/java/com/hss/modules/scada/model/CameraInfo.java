package com.hss.modules.scada.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 摄像头信息
 * @author hd
 */
@ApiModel("摄像头信息")
@Data
public class CameraInfo {

    @ApiModelProperty("ip")
    private String ip;
    @ApiModelProperty("端口")
    private String port;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("流类型")
    private String steamtype;
    @ApiModelProperty("通道id")
    private String channelid;



}
