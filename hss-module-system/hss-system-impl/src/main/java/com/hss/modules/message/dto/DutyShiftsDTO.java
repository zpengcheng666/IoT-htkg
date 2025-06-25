package com.hss.modules.message.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
* @description: 排班
* @author zpc
* @date 2024/3/21 10:02
* @version 1.0
*/
@Data
public class DutyShiftsDTO {
    /**日期*/
    @ApiModelProperty(value = "日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date date;

    /**编号*/
    @ApiModelProperty(value = "编号")
    private java.lang.Integer code;
}
