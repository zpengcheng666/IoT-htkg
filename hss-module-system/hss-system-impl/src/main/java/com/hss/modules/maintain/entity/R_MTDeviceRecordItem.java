package com.hss.modules.maintain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 保养任务-设备关系表
 * @Author: zpc
 * @Date:   2022-12-28
 * @Version: V1.0
 */
@Data
@TableName("R_MT_DEVICE_RECORD_ITEM")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="R_MT_DEVICE_RECORD_ITEM对象", description="保养任务-设备关系表")
public class R_MTDeviceRecordItem {
    
	/**ID*/
	@TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "ID")
	private java.lang.String id;
	/**项目ID*/
	@Excel(name = "项目ID", width = 15)
    @ApiModelProperty(value = "项目ID")
	private java.lang.String itemId;
	/**保养记录ID*/
	@Excel(name = "保养记录ID", width = 15)
    @ApiModelProperty(value = "保养记录ID")
	private java.lang.String recordId;
	/**保养设备ID*/
	@Excel(name = "保养设备ID", width = 15)
    @ApiModelProperty(value = "保养设备ID")
	private java.lang.String deviceId;
	/**组名称*/
	@Excel(name = "组名称", width = 15)
    @ApiModelProperty(value = "组名称")
	private java.lang.String groupName;
	/**扫码时间*/
    @ApiModelProperty(value = "扫码时间")
	private java.util.Date time;
	/**负责人ID*/
	@Excel(name = "负责人ID", width = 15)
    @ApiModelProperty(value = "负责人ID")
	private java.lang.String principalId;

	/**保养方案ID*/
	@Excel(name = "保养方案ID", width = 15)
	@ApiModelProperty(value = "保养方案ID")
	private java.lang.String schemasId;
}
