package com.hss.modules.door.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 最后一次通行记录
 * @author hd
 */
@Data
@ApiModel("最后一次通行记录")
public class GetLastVO {

    @ApiModelProperty("进门信息")
    private GetLastVOInOut in;


    @ApiModelProperty("出门信息")
    private GetLastVOInOut out;


}
