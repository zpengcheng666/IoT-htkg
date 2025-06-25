package com.hss.modules.preplan.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.Date;

/**
 * @Description: 应急预案工作
 * @Author: zpc
 * @Date:   2023-02-27
 * @Version: V1.0
 */
@Data
@TableName("T_CONTINGENCY_PLAN_WORKITEM")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="T_CONTINGENCY_PLAN_WORKITEM对象", description="应急预案工作")
public class ContingencyPlanWorkitem {
    
	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**工作项名称*/
	@Excel(name = "工作项名称", width = 15)
    @ApiModelProperty(value = "工作项名称")
	private java.lang.String name;
	/**工作内容*/
	@Excel(name = "工作内容", width = 15)
    @ApiModelProperty(value = "工作内容")
	private java.lang.String content;
	/**序号*/
	@Excel(name = "序号", width = 15)
    @ApiModelProperty(value = "序号")
	private java.lang.Integer index1;
	/**阶段ID*/
	@Excel(name = "阶段ID", width = 15)
    @ApiModelProperty(value = "阶段ID")
	private java.lang.String stageId;
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

	@ApiModelProperty(value = "提交时间")
	@TableField(exist = false)
	private Date sumbitTime;

	@ApiModelProperty(value = "提交时间")
	@TableField(exist = false)
	private String sumbitPerson;

	@ApiModelProperty(value = "状态")
	@TableField(exist = false)
	private Integer status;
}
