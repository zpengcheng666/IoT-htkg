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
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
 * @Description: 特情处置记录
 * @Author: zpc
 * @Date:   2023-02-13
 * @Version: V1.0
 */
@Data
@TableName("T_CONTINGENCY_RECORD")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="T_CONTINGENCY_RECORD对象", description="特情处置记录")
public class ContingencyRecord {
    
	/**ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
	private java.lang.String id;
	/**发起人*/
	@Excel(name = "发起人", width = 15)
    @ApiModelProperty(value = "发起人")
	private java.lang.String initiator;
	/**启动时间*/
    @ApiModelProperty(value = "启动时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date startTime;
	/**结束时间*/
    @ApiModelProperty(value = "结束时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date endTime;
	/**完成状态*/
	@Excel(name = "完成状态", width = 15)
    @ApiModelProperty(value = "完成状态0未完成，1完成")
	private java.lang.Integer isCompleted;

	@TableField(exist = false)
	private java.lang.String isCompleted_disp;
	/**报警记录ID*/
	@Excel(name = "报警记录ID", width = 15)
    @ApiModelProperty(value = "报警记录ID")
	private java.lang.String alarmRecordId;
	/**突发事件类型内码*/
	@Excel(name = "突发事件类型内码", width = 15)
    @ApiModelProperty(value = "突发事件类型内码")
	private java.lang.String contingencyClass;
	/**应急预案名称*/
	@Excel(name = "应急预案名称", width = 15)
    @ApiModelProperty(value = "应急预案名称")
	private java.lang.String name;
	/**contingencyName*/
	@Excel(name = "contingencyName", width = 15)
    @ApiModelProperty(value = "contingencyName")
	private java.lang.String contingencyName;
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

	/**预案id*/
	@ApiModelProperty(value = "预案id")
	private java.lang.String planId;

	@TableField(exist = false)
	List<ContingencyRecordStage> stageList;

	@TableField(exist = false)
	Integer stepIndex;


}
