package com.hss.modules.system.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
* @description: 资料上传
* @author zpc
* @date 2024/3/21 13:32
* @version 1.0
*/
@Data
public class FilesList implements java.io.Serializable{
    @ApiModelProperty(value = "附件名称")
    private String name;

    @ApiModelProperty(value = "附件路径")
    private String url;
}
