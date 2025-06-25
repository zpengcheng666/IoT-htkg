package com.hss.modules.preplan.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 应急预案终端关系
 * @Author: zpc
 * @Date:   2023-02-27
 * @Version: V1.0
 */
@Data
@TableName("T_CONTINGENCY_PLAN_R_TERMINAL")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="T_CONTINGENCY_PLAN_R_TERMINAL对象", description="应急预案终端关系")
public class ContingencyPlanTerminal {
    
	/**ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
	private java.lang.String id;
	/**终端ID*/
	@Excel(name = "终端ID", width = 15)
    @ApiModelProperty(value = "终端ID")
	private java.lang.String terminalId;
	/**预案ID*/
	@Excel(name = "预案ID", width = 15)
    @ApiModelProperty(value = "预案ID")
	private java.lang.String planId;
	/**createdTime*/
    @ApiModelProperty(value = "createdTime")
	private java.util.Date createdTime;
	/**updatedTime*/
    @ApiModelProperty(value = "updatedTime")
	private java.util.Date updatedTime;
	/**deleted*/
	@Excel(name = "deleted", width = 15)
    @ApiModelProperty(value = "deleted")
	private java.lang.Integer deleted;
}
