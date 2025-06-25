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

import java.util.List;

/**
 * @Description: 特情处置阶段
 * @Author: zpc
 * @Date:   2023-02-27
 * @Version: V1.0
 */
@Data
@TableName("T_CONTINGENCY_RECORD_STAGE")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="T_CONTINGENCY_RECORD_STAGE对象", description="特情处置阶段")
public class ContingencyRecordStage {
    
	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**阶段名称*/
	@Excel(name = "阶段名称", width = 15)
    @ApiModelProperty(value = "阶段名称")
	private java.lang.String name;
	/**序号*/
	@Excel(name = "序号", width = 15)
    @ApiModelProperty(value = "序号")
	private java.lang.Integer index1;
	/**预案记录ID*/
	@Excel(name = "预案记录ID", width = 15)
    @ApiModelProperty(value = "预案记录ID")
	private java.lang.String planId;
	/**完成状态*/
	@Excel(name = "完成状态", width = 15)
    @ApiModelProperty(value = "完成状态")
	private java.lang.Integer isCompleted;
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

	@TableField(exist = false)
	private List<ContingencyRecordWorkitem> workList;

	@TableField(exist = false)
	Integer stepIndex;
}
