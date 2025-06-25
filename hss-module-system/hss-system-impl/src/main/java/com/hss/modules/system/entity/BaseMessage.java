package com.hss.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 消息管理
 * @Author: zpc
 * @Date:   2022-11-03
 * @Version: V1.0
 */
@Data
@TableName("BASE_MESSAGE")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BASE_MESSAGE对象", description="消息管理")
public class BaseMessage {
    
	/**ID*/
	@TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "ID")
	private java.lang.String id;
	/**用户ID*/
	@Excel(name = "用户ID", width = 15)
    @ApiModelProperty(value = "用户ID")
	private java.lang.String userId;
	/**createTime*/
    @ApiModelProperty(value = "createTime",hidden = true)
	private java.util.Date createTime;
	/**消息内容*/
	@Excel(name = "消息内容", width = 15)
    @ApiModelProperty(value = "消息内容")
	private java.lang.String detail;
	/**消息标识符*/
	@Excel(name = "消息标识符", width = 15)
    @ApiModelProperty(value = "消息标识符")
	private java.lang.String identifier;
	/**消息类型*/
	@Excel(name = "消息类型", width = 15)
    @ApiModelProperty(value = "消息类型")
	private java.lang.String type;
	@TableField(exist = false)
	private java.lang.String type_disp;
	/**消息状态*/
	@Excel(name = "消息状态", width = 15)
    @ApiModelProperty(value = "消息状态")
	private java.lang.Integer status;
	/**开始时间*/
    @ApiModelProperty(value = "开始时间")
	@JsonFormat(locale="zh", timezone="GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date startTime;
	/**结束时间*/
    @ApiModelProperty(value = "结束时间")
	@JsonFormat(locale="zh", timezone="GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date endTime;
	/**1：审核提醒，2：任务提醒*/
	@Excel(name = "1：审核提醒，2：任务提醒", width = 15)
    @ApiModelProperty(value = "1：审核提醒，2：任务提醒")
	private java.lang.Integer reviewOrRecord;
}
