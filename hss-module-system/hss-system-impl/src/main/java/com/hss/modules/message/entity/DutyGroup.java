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

/**
 * @Description: personList人员表
 * @Author: zpc
 * @Date:   2023-04-21
 * @Version: V1.0
 */
@Data
@TableName(value = "BASE_DUTY_GROUP",autoResultMap = true)
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BASE_DUTY_GROUP对象", description="值班小组人员表")
public class DutyGroup {
    
	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**小组名*/
	@Excel(name = "小组名", width = 15)
    @ApiModelProperty(value = "小组名")
	private java.lang.String name;
	/**小组编号*/
	@Excel(name = "小组编号", width = 15)
    @ApiModelProperty(value = "小组编号")
	private java.lang.Integer code;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
	private java.lang.Integer status;
	/**值班区域*/
	@Excel(name = "值班区域", width = 15)
    @ApiModelProperty(value = "值班区域")
	private java.lang.String dutyLocation;
	/**值班岗位*/
	@Excel(name = "值班岗位", width = 15)
    @ApiModelProperty(value = "值班岗位")
	private java.lang.String dutyPost;

	@TableField(exist = false)
	private java.lang.String dutyPost_disp;

	/**职位*/
	@ApiModelProperty(value = "职位")
	private java.lang.String dutyPostion;

	@TableField(exist = false)
	private java.lang.String dutyPostion_disp;
	/**时间段*/
	@ApiModelProperty(value = "时间段")
	private java.lang.String dutySjd;

	@TableField(exist = false)
	private java.lang.String dutySjd_disp;

	@ApiModelProperty(value = "岗位人员列表")
	@TableField(typeHandler = JacksonTypeHandler.class)
	private DutyPersonModel[] personList;
	@ApiModelProperty(value = "值班id")
	private String dutyId;
}
