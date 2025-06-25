package com.hss.modules.system.entity;

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

/**
 * @Description: 日志表
 * @Author: zpc
 * @Date:   2022-12-05
 * @Version: V1.0
 */
@Data
@TableName("BASE_LOG_OPERATION")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BASE_LOG_OPERATION对象", description="日志表")
public class BaseLog {
    
	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**用户id*/
//	@Excel(name = "用户id", width = 15)
    @ApiModelProperty(value = "用户id")
	private java.lang.String userId;
	/**用户名称*/
	@Excel(name = "用户名", width = 15)
    @ApiModelProperty(value = "用户名")
	private java.lang.String username;
	/**操作时间*/
	@Excel(name = "操作时间", width = 15,exportFormat="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "操作时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date stateTime;
	/**操作开始时间*/
	@ApiModelProperty(value = "操作开始时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@TableField(exist = false)
	private java.util.Date beginTime;
	/**操作结束时间*/
	@ApiModelProperty(value = "操作结束时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@TableField(exist = false)
	private java.util.Date endTime;
	/**操作内容*/
	@Excel(name = "操作", width = 15)
    @ApiModelProperty(value = "操作")
	private java.lang.String operateContent;
	/**记录名称*/
//	@Excel(name = "记录名称", width = 15)
    @ApiModelProperty(value = "记录名称")
	private java.lang.String recordName;
	/**记录id*/
//	@Excel(name = "记录id", width = 15)
    @ApiModelProperty(value = "记录id")
	private java.lang.String recordId;
}
