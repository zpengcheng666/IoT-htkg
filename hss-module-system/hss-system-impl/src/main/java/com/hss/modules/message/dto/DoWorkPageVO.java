package com.hss.modules.message.dto;

import com.hss.modules.message.entity.DoWork;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
* @description: 手动值班分页返回参数
* @author zpc
* @date 2024/3/21 10:01
* @version 1.0
*/
@Data
@ApiModel("手动值班分页返回参数")
public class DoWorkPageVO extends DoWork {

    @ApiModelProperty("终端ids")
    private List<String> terminalIds;
}
