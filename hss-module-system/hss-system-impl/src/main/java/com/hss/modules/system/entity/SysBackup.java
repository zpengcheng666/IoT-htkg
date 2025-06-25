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
@TableName("BASE_SYS_BACKUP")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BASE_SYS_BACKUP对象", description="系统备份")
public class SysBackup {

	public static final String RESULTS_OK = "0";

	public static final String RESULTS_ERR = "1";

	public static final String BACKUP_TYPE_PLATFORM = "1";

	public static final String BACKUP_TYPE_DATA = "2";

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**备份名称*/
	@Excel(name = "备份名称", width = 15)
    @ApiModelProperty(value = "备份名称")
	private java.lang.String backupName;
	/**备份文件的路径*/
	@Excel(name = "备份文件的路径", width = 15)
    @ApiModelProperty(value = "备份文件的路径")
	private java.lang.String filePath;
	/**操作人员*/
	@Excel(name = "操作人员", width = 15)
    @ApiModelProperty(value = "操作人员")
	private java.lang.String operator;
	/**备份时间*/
	@Excel(name = "备份时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "备份时间")
	private java.util.Date startTime;
	/**备份持续的时长(秒）*/
	@Excel(name = "备份持续的时长(秒）", width = 15)
    @ApiModelProperty(value = "备份持续的时长(秒）")
	private java.lang.Long duration;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private java.lang.String notes;
	/**备份被恢复次数*/
	@Excel(name = "备份被恢复次数", width = 15)
    @ApiModelProperty(value = "备份被恢复次数")
	private java.lang.Integer recoveryCnt;
	/**备份类型（1：平台备份；2：数据备份）*/
	@Excel(name = "备份类型（1：平台备份；2：数据备份）", width = 15)
    @ApiModelProperty(value = "备份类型（1：平台备份；2：数据备份）")
	private java.lang.String backupType;
	/**操作结果（0：成功；1：失败）*/
	@Excel(name = "操作结果（0：成功；1：失败）", width = 15)
	@ApiModelProperty(value = "操作结果（0：成功；1：失败）")
	private java.lang.String results;
}
