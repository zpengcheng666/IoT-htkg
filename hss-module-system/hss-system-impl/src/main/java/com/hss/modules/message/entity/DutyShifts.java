package com.hss.modules.message.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.hss.modules.message.model.DutyPersonModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 排班班次
 * @Author: zpc
 * @Date:   2023-04-26
 * @Version: V1.0
 */
@Data
@TableName(value = "BASE_DUTY_SHIFTS",autoResultMap = true)
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BASE_DUTY_SHIFTS对象", description="排班班次")
public class DutyShifts {
    
	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**班次列表*/
    @ApiModelProperty(value = "班次列表")
	@TableField(typeHandler = JacksonTypeHandler.class)
	private DutyPersonModel[] shifts;
	/**组id*/
	@Excel(name = "组id", width = 15)
    @ApiModelProperty(value = "组id")

	private java.lang.String dutyGroupId;
	/**日期*/
	@Excel(name = "日期", width = 15)
	@ApiModelProperty(value = "日期")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date date;

	@TableField(exist = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date beginTime;
	@TableField(exist = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endTime;
	/**编号*/
	@Excel(name = "编号", width = 15)
	@ApiModelProperty(value = "编号")
	private java.lang.Integer code;
	/**小组名称*/
	@Excel(name = "小组名称", width = 15)
	@ApiModelProperty(value = "小组名称")
	private java.lang.String name;

	/**职位*/
	@ApiModelProperty(value = "职位")
	private java.lang.String dutyPostion;

	@ApiModelProperty(value = "值班id")
	private String dutyId;
}
