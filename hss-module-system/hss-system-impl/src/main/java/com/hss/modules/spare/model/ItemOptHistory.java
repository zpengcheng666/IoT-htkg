package com.hss.modules.spare.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassDescription: 库存操作记录
 * @JdkVersion: 1.8
 * @Author: hd
 * @Created: 2024/4/28 13:56
 */
@ApiModel("库存操作记录")
@Data
public class ItemOptHistory {

    @ApiModelProperty("操作类型")
    private String optType;


    @ApiModelProperty("物料名称")
    private String itemName;

    @ApiModelProperty("物料编号")
    private String itemNo;

    @ApiModelProperty("仓库")
    private String warehouse;

    @ApiModelProperty("变更数量")
    private BigDecimal quantity;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "操作时间")
    private LocalDateTime optTime;



}
