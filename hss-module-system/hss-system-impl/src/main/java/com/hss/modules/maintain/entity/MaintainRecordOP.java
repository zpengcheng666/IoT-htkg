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
 * @Description: 保养任务-新增修改-关联保养设备
 * @Author: zpc
 * @Date:   2022-12-28
 * @Version: V1.0
 */
@Data
@TableName("MT_BD_MAINTAIN_RECORD_OP")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MT_BD_MAINTAIN_RECORD_OP对象", description="保养任务-新增修改-关联保养设备")
public class MaintainRecordOP {
    
	/**id*/
	@TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**名称*/
	@Excel(name = "-名称", width = 15)
    @ApiModelProperty(value = "保养要求-名称")
	private java.lang.String name;
	/**技术要求*/
	@Excel(name = "保养要求-技术要求", width = 15)
    @ApiModelProperty(value = "保养要求-技术要求")
	private java.lang.String thchRequire;
	/**排序号*/
	@Excel(name = "排序号", width = 15)
    @ApiModelProperty(value = "排序号")
	private java.lang.Integer index1;
	/**保养要求ID*/
	@Excel(name = "保养要求ID", width = 15)
    @ApiModelProperty(value = "保养要求ID")
	private java.lang.String itemId;
	/**完成情况*/
	@Excel(name = "完成情况", width = 15)
    @ApiModelProperty(value = "完成情况")
	private java.lang.Integer isComplete;
	/**操作时间*/
    @ApiModelProperty(value = "操作时间")
	private java.util.Date time;
	/**保养类别ID*/
	@Excel(name = "保养类别ID", width = 15)
    @ApiModelProperty(value = "保养类别ID")
	private java.lang.String itemClass;
	/**设备ID*/
	@Excel(name = "设备ID", width = 15)
    @ApiModelProperty(value = "设备ID")
	private java.lang.String deviceId;
	/**保养任务ID*/
	@Excel(name = "保养任务ID", width = 15)
    @ApiModelProperty(value = "保养任务ID")
	private java.lang.String recordId;
	/**childDevClassId*/
	@Excel(name = "childDevClassId", width = 15)
    @ApiModelProperty(value = "childDevClassId")
	private java.lang.String childDevClassId;
	/**childDevClassName*/
	@Excel(name = "childDevClassName", width = 15)
    @ApiModelProperty(value = "childDevClassName")
	private java.lang.String childDevClassName;
	/**deviceName*/
	@Excel(name = "deviceName", width = 15)
    @ApiModelProperty(value = "deviceName")
	private java.lang.String deviceName;
}
