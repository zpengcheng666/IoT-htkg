package com.hss.modules.preplan.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.List;

/**
 * @Description: 预案主表
 * @Author: zpc
 * @Date:   2023-02-07
 * @Version: V1.0
 */
@Data
@TableName("T_CONTINGENCY_PLAN")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="T_CONTINGENCY_PLAN对象", description="预案主表")
public class ContingencyPlan {
    
	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**应急预案名称*/
	@Excel(name = "应急预案名称", width = 15)
    @ApiModelProperty(value = "应急预案名称")
	private java.lang.String name;
	/**描述*/
	@Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
	private java.lang.String description;
	/**突发事件类型内码*/
	@Excel(name = "突发事件类型内码", width = 15)
    @ApiModelProperty(value = "突发事件类型内码")
	private java.lang.String contingencyClass;

	@TableField(exist = false)
	private java.lang.String contingencyClass_disp;
	/**createdTime*/
    @ApiModelProperty(value = "createdTime")
	private java.util.Date createdTime;
	/**updatedTime*/
    @ApiModelProperty(value = "updatedTime")
	private java.util.Date updatedTime;
	/**deleted*/
	@Excel(name = "deleted", width = 15)
    @ApiModelProperty(value = "deleted")
	@TableField(select = true)
	@TableLogic
	private java.lang.Integer deleted;

	/**文件路径*/
	@Excel(name = "文件路径", width = 15)
	@ApiModelProperty(value = "文件路径")
	private java.lang.String imgUrls;

	@TableField(exist = false)
	@ApiModelProperty(value = "阶段列表")
	private List<ContingencyPlanStage> planStageList;

}
