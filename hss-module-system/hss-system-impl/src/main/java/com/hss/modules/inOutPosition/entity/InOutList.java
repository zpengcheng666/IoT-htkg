package com.hss.modules.inOutPosition.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.List;

/**
 * @Description: 进出阵地列表
 * @Author: zpc
 * @Date:   2022-12-13
 * @Version: V1.0
 */
@Data
@TableName("MT_IN_OUT_LIST")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MT_IN_OUT_LIST对象", description="进出阵地列表")
public class InOutList {
    
	/**ID*/
	@TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "ID")
	private java.lang.String id;
	/**申请单位*/
	@Excel(name = "申请单位", width = 15)
    @ApiModelProperty(value = "申请单位")
	private java.lang.String department;
	/**带队人*/
	@Excel(name = "带队人", width = 15)
    @ApiModelProperty(value = "带队人")
	private java.lang.String leader;
	/**人数*/
	@Excel(name = "人数", width = 15)
    @ApiModelProperty(value = "人数")
	private java.lang.Integer count;
	/**活动区域*/
	@Excel(name = "活动区域", width = 15)
    @ApiModelProperty(value = "活动区域")
	private java.lang.String zone;
	/**职务*/
	@Excel(name = "职务", width = 15)
    @ApiModelProperty(value = "职务")
	private java.lang.String position1;
	/**入库事由*/
	@Excel(name = "入库事由", width = 15)
    @ApiModelProperty(value = "入库事由")
	private java.lang.String reason;
	/**入库时间*/
    @ApiModelProperty(value = "入库时间")
	private java.util.Date inTime;
	/**出库时间*/
    @ApiModelProperty(value = "出库时间")
	private java.util.Date outTime;
	/**带队人ID*/
	@Excel(name = "带队人ID", width = 15)
    @ApiModelProperty(value = "带队人ID")
	private java.lang.String leaderId;
	@ApiModelProperty(value = "带队人转码")
	@TableField(exist = false)
	private java.lang.String leaderId_disp;

	/**申请状态(0:待审核,1:已审核,-1:未通过)*/
	@Excel(name = "申请状态(0:待审核,1:已审核,2:未通过)", width = 15)
    @ApiModelProperty(value = "申请状态(0:待审核,1:已审核,2:未通过)")
	private java.lang.Integer status;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private java.lang.String remark;
	/**审批单类型*/
	@Excel(name = "审批单类型", width = 15)
    @ApiModelProperty(value = "审批单类型")
	private java.lang.Integer approveType;

	@ApiModelProperty(value = "内部人员")
	@TableField(exist = false)
	private List<InOutInternal> insiderList;

	@ApiModelProperty(value = "外部人员")
	@TableField(exist = false)
	private List<InOutExternal> outsiderList;

	@TableField(select = true)
	@TableLogic
	@ApiModelProperty(value = "删除标志0未删除，1删除",hidden = true)
	private Integer deleted;
}
