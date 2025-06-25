package com.hss.modules.maintain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.hss.modules.maintain.model.MaintainOperateWrapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Description: 保养任务表
 * @Author: zpc
 * @Date:   2022-12-15
 * @Version: V1.0
 */
@Data
@TableName("MT_MAINTAIN_RECORD")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MT_MAINTAIN_RECORD对象", description="保养任务表")
public class MaintainRecord {
    
	/**ID*/
	@TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "ID")
	private java.lang.String id;
	/**工作开始时间*/
    @ApiModelProperty(value = "工作开始时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date startTime;



	/**工作截止时间*/
    @ApiModelProperty(value = "工作截止时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date endTime;

	/**签发人*/
	@Excel(name = "签发人", width = 15)
    @ApiModelProperty(value = "签发人")
	private java.lang.String creator;

	/**作业负责人*/
	@Excel(name = "作业负责人", width = 15)
    @ApiModelProperty(value = "作业负责人")
	private java.lang.String principal;

	/**作业参加人*/
	@Excel(name = "作业参加人", width = 15)
    @ApiModelProperty(value = "作业参加人")
	private java.lang.String actor;

	/**保养类别*/
	@Excel(name = "保养类别", width = 15)
    @ApiModelProperty(value = "保养类别")
	private java.lang.String itemClass;

	/**问题及处理情况*/
	@Excel(name = "问题及处理情况", width = 15)
    @ApiModelProperty(value = "问题及处理情况")
	private java.lang.String crics;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态(0待签发，1待执行，2执行中,3待审核,4完成)")
	private java.lang.Integer status;
	/**保养方案ID*/
	@Excel(name = "保养方案ID", width = 15)
    @ApiModelProperty(value = "保养方案ID")
	private java.lang.String schemaId;

	/**签发人ID*/
	@Excel(name = "签发人ID", width = 15)
    @ApiModelProperty(value = "签发人ID")
	private java.lang.String creatorId;
	@TableField(exist = false)
	private java.lang.String creatorId_disp;
	/**负责人ID*/
	@Excel(name = "负责人ID", width = 15)
    @ApiModelProperty(value = "负责人ID")
	private java.lang.String principalId;
	@TableField(exist = false)
	private java.lang.String principalId_disp;
	/**备注*/
	@Excel(name = "备注/责任变更", width = 15)
    @ApiModelProperty(value = "备注/责任变更")
	private java.lang.String remark;

	@ApiModelProperty(value = "签发时间")
	private Date confirmTime;

	@ApiModelProperty(value = "执行时间")
	private Date actTime;
    @ApiModelProperty(value = "提交时间")
	private java.util.Date submitTime;

	@ApiModelProperty(value = "提交时间")
	private java.util.Date completeTime;
	/**参加人ID*/
	@Excel(name = "参加人ID", width = 15)
    @ApiModelProperty(value = "参加人ID")
	private java.lang.String actorId;
	/**方案名称*/
	@Excel(name = "方案名称", width = 15)
    @ApiModelProperty(value = "方案名称")
	private java.lang.String schemaName;
	/**材料*/
	@Excel(name = "material", width = 15)
    @ApiModelProperty(value = "material")
	private java.lang.String material;
	/**其他材料*/
	@Excel(name = "otherMaterial", width = 15)
    @ApiModelProperty(value = "otherMaterial")
	private java.lang.String otherMaterial;
	/**负责人*/
	@Excel(name = "partLeader", width = 15)
    @ApiModelProperty(value = "partLeader")
	private java.lang.String partLeader;
	/**负责人id*/
	@Excel(name = "partLeaderId", width = 15)
    @ApiModelProperty(value = "partLeaderId")
	private java.lang.String partLeaderId;

	@ApiModelProperty(value = "删除标志")
	@TableField(select = true)
	@TableLogic
	private Integer deleted;

	@ApiModelProperty(value = "设备列表")
	@TableField(exist = false)
	private List<String> deviceList;

	@ApiModelProperty(value = "设备列表")
	@TableField(exist = false)
	private List<MaintainOperateWrapper> operates;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@TableField(exist = false)
	private Date startTime_begin;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@TableField(exist = false)
	private Date startTime_end;

}
