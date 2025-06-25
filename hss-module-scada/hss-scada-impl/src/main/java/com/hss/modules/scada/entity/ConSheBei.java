package com.hss.modules.scada.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelIgnore;

import java.util.Date;
import java.util.List;

/**
 * @Description: 场景设备
 * @Author: zpc
 * @Date:   2022-11-16
 * @Version: V1.0
 */
@Data
@TableName("CON_SHEBEI")
@Accessors(chain = true)
@ApiModel(value="CON_SHEBEI对象", description="场景设备")
@EqualsAndHashCode(of = {"id", "name", "type", "subsystem", "locationId", "description", "wgid"})
public class ConSheBei {
    
	/**ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
	private String id;
	/**设备名称*/
	@Excel(name = "设备名称", width = 15)
    @ApiModelProperty(value = "设备名称")
	private String name;
	/**设备别名*/
	@Excel(name = "设备别名", width = 15)
	@ApiModelProperty(value = "设备别名")
	private String otherName;
	/**设备编码*/
	@Excel(name = "设备编码", width = 15)
    @ApiModelProperty(value = "设备编码")
	private String code;
	/**设备类型*/
	@Excel(name = "设备类型", width = 15)
    @ApiModelProperty(value = "设备类型")
	private String type;
	/**所属子系统*/
	@Excel(name = "所属子系统", width = 15)
    @ApiModelProperty(value = "所属子系统")
	private String subsystem;
	/**位置ID*/
	@Excel(name = "位置ID", width = 15)
    @ApiModelProperty(value = "位置ID")
	private String locationId;
	/**描述*/
	@Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
	private String description;
	/**xAxis*/
	@Excel(name = "xAxis", width = 15)
    @ApiModelProperty(value = "xAxis")
	private Integer xAxis;
	/**yAxis*/
	@Excel(name = "yAxis", width = 15)
    @ApiModelProperty(value = "yAxis")
	private Integer yAxis;
	/**pathPoints*/
	@Excel(name = "pathPoints", width = 15)
    @ApiModelProperty(value = "pathPoints")
	private String pathPoints;
	/**ratio*/
	@Excel(name = "ratio", width = 15)
    @ApiModelProperty(value = "ratio")
	private java.math.BigDecimal ratio;
	/**旋转角度*/
	@Excel(name = "旋转角度", width = 15)
    @ApiModelProperty(value = "旋转角度")
	private Integer angle;
	/**设备镜像翻转*/
	@Excel(name = "设备镜像翻转", width = 15)
    @ApiModelProperty(value = "设备镜像翻转")
	private String mirrorFlip;
	/**index*/
	@Excel(name = "index", width = 15)
    @ApiModelProperty(value = "index")
	private Integer index_;
	/**totalDuration*/
	@Excel(name = "totalDuration", width = 15)
    @ApiModelProperty(value = "totalDuration")
	private Integer totalDuration;
	/**createdTime*/
    @ApiModelProperty(value = "createdTime")
	@TableField(fill = FieldFill.INSERT)
	private Date createdTime;
	/**updatedTime*/
    @ApiModelProperty(value = "updatedTime")
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updatedTime;
	/**deleted*/
	@Excel(name = "deleted", width = 15)
    @ApiModelProperty(value = "deleted")
	private Integer deleted;

	/**网关id**/
	@Excel(name = "网关id", width = 15)
	@ApiModelProperty(value = "网关id")
	private String wgid;

	@Excel(name = "设备id", width = 15)
	@ApiModelProperty(value = "设备id")
	private String deviceId;

	@Excel(name = "设备类型id", width = 15)
	@ApiModelProperty(value = "设备类型id")
	private String deviceTypeId;

	/**
	 * 不做eques判断
	 */
	@ApiModelProperty(value = "属性列表",hidden = true)
	@ExcelIgnore
	@TableField(exist = false)
	private List<ConDeviceAttribute> attrList;

	@ApiModelProperty(value = "属性列表",hidden = true)
	@ExcelIgnore
	@TableField(exist = false)
	private String countStr;

	@ApiModelProperty(value = "位置",hidden = true)
	@ExcelIgnore
	@TableField(exist = false)
	private String locationName;

	/**
	 * 排序序号
	 */
	@ExcelIgnore
	@TableField(exist = false)
	private Integer sortNumber;




}
