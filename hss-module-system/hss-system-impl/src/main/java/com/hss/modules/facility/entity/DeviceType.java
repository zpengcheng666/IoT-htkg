package com.hss.modules.facility.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hss.modules.maintain.entity.MaintainOperate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.List;

/**
 * @Description: 类别管理
 * @Author: zpc
 * @Date:   2022-12-08
 * @Version: V1.0
 */
@Data
@TableName("DF_BD_DEVICE")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="DF_BD_DEVICE对象", description="类别管理")
public class DeviceType {
    
	/**id*/
	@TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**名称*/
	@Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
	private java.lang.String name;
	/**编码*/
	@Excel(name = "编码", width = 15)
    @ApiModelProperty(value = "编码")
	private java.lang.String code;
	/**层次码，限制10层*/
	@Excel(name = "层次码，限制10层", width = 15)
    @ApiModelProperty(value = "层次码，限制10层")
	private java.lang.String stratumCode;
	/**父节点*/
	@Excel(name = "父节点", width = 15)
    @ApiModelProperty(value = "父节点")
	private java.lang.String pId;
	/**是否设备(0:否,1:是)*/
	@Excel(name = "是否设备(0:否,1:是)", width = 15)
    @ApiModelProperty(value = "是否设备(0:否,1:是)")
	private java.lang.Integer isDevice;
	/**是否备件(0:否,1:是)*/
	@Excel(name = "是否备件(0:否,1:是)", width = 15)
    @ApiModelProperty(value = "是否备件(0:否,1:是)")
	private java.lang.Integer isGoods;
	/**是否计量(0:否,1:是)*/
	@Excel(name = "是否计量(0:否,1:是)", width = 15)
    @ApiModelProperty(value = "是否计量(0:否,1:是)")
	private java.lang.Integer isMeasure;

	@ApiModelProperty(value = "保养要求列表")
	@TableField(exist = false)
	List<MaintainOperate> operates;
}
