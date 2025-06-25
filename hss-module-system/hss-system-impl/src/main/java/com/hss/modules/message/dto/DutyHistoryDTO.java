package com.hss.modules.message.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author hd
 * 值班记录查询参数
 */
@ApiModel("值班记录查询参数")
@Data
public class DutyHistoryDTO {

    @ApiModelProperty("值班安排id")
    private String dutyId;

    @ApiModelProperty("值班日期查询开始")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @ApiModelProperty("值班日期查询结束")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @ApiModelProperty("页码")
    private Integer pageNo = 1;

    @ApiModelProperty("每页数量")
    private Integer pageSize = 10;


}
