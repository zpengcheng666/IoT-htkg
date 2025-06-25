package com.hss.modules.spare.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * @ClassDescription: 入库单分页查询参数
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/4/26 10:32
 */
@Data
@ApiModel("出库单分页查询参数")
public class MovementPageDTO {


    @ApiModelProperty(value = "单号")
    private String orderNo;

    @ApiModelProperty(value = "负责人")
    private String useName;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "日期开始")
    private LocalDate dateStart;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "日期结束")
    private LocalDate dateEnd;

    private Integer pageNo;
    private Integer pageSize;


}
