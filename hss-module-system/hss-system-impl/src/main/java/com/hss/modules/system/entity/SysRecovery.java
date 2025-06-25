package com.hss.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 系统备份
 * @Author: zpc
 * @Date:   2023-05-15
 * @Version: V1.0
 */
@Data
@TableName("BASE_SYS_RECOVERY")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BASE_SYS_RECOVERY对象", description="系统备份")
public class SysRecovery {
    
	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**恢复类型（1:平台备份; 2:数据备份)*/
	@Excel(name = "恢复类型（1:平台备份; 2:数据备份)", width = 15)
    @ApiModelProperty(value = "恢复类型（1:平台备份; 2:数据备份)")
	private java.lang.String recoveryType;
	/**备份的ID*/
	@Excel(name = "备份的ID", width = 15)
    @ApiModelProperty(value = "备份的ID")
	private java.lang.String backupId;
	/**操作员*/
	@Excel(name = "操作员", width = 15)
    @ApiModelProperty(value = "操作员")
	private java.lang.String operator;
	/**恢复开始时间*/
	@Excel(name = "恢复开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "恢复开始时间")
	private java.util.Date startTime;
	/**系统恢复时长（秒）*/
	@Excel(name = "系统恢复时长（秒）", width = 15)
    @ApiModelProperty(value = "系统恢复时长（秒）")
	private java.lang.Long duration;
	/**操作结果（0：成功；1：失败）*/
	@Excel(name = "操作结果（0：成功；1：失败）", width = 15)
    @ApiModelProperty(value = "操作结果（0：成功；1：失败）")
	private java.lang.String results;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private java.lang.String notes;
}
