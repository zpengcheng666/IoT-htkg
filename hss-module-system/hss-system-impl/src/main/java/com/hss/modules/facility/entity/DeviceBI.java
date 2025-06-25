package com.hss.modules.facility.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import javax.persistence.Transient;
import java.util.List;
import java.util.Map;

/**
 * @Description: 设施设备
 * @Author: zpc
 * @Date:   2022-12-08
 * @Version: V1.0
 */
@Data
@TableName("DF_BI_DEVICE")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="DF_BI_DEVICE对象", description="设施设备")
public class DeviceBI {
    
	/**id*/
	@TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**名称*/
	@Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
	private java.lang.String name;

	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
	private java.lang.String code;
	/**设备类别ID*/
    @ApiModelProperty(value = "设备类别ID")
	private java.lang.String classId;

	@Excel(name = "设备类别名称", width = 15)
	@TableField(exist = false)
	private String classId_disp;
	/**规格型号*/
	@Excel(name = "规格型号", width = 15)
    @ApiModelProperty(value = "规格型号")
	private java.lang.String devModel;
	/**责任人*/
	@Excel(name = "责任人", width = 15)
    @ApiModelProperty(value = "责任人")
	private java.lang.String owner;
	/**质量状况*/
	@Excel(name = "质量状况", width = 15,replace = {"新品_5711f7e900a440919167a379b19a6ed1","堪用_9b6273126d9146609518455c962eff1d",
			"待维修_3977c4903a284049867f0b9bf1bff2bb","待报废_7571ec34a3b14fd7920de904643e8f90"})
    @ApiModelProperty(value = "质量状况")
	private java.lang.String quality;
	@TableField(exist = false)
	private String quality_disp;
	/**所在位置*/
    @ApiModelProperty(value = "所在位置")
	private java.lang.String site;

	@Excel(name = "所在位置", width = 15)
	@TableField(exist = false)
	private String site_disp;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private java.lang.String note;
	/**安装时间*/
    @ApiModelProperty(value = "安装时间")
	@JsonFormat(locale="zh", timezone="GMT+8", pattern = "yyyy-MM-dd")
	private java.util.Date installationTime;
	/**出场时间*/
    @ApiModelProperty(value = "出场时间")
	@JsonFormat(locale="zh", timezone="GMT+8", pattern = "yyyy-MM-dd")
	private java.util.Date productionTime;
	/**设备关联*/
    @ApiModelProperty(value = "设备关联")
	private java.lang.String deviceContext;
	/**射频标签*/
    @ApiModelProperty(value = "射频标签")
	private java.lang.String rfLabel;
	/**带出授权（0：未授权，1：已授权）*/
    @ApiModelProperty(value = "带出授权（0：未授权，1：已授权）")
	private java.lang.Integer isAuthorized;
	/**隶属单位*/
	@Excel(name = "隶属单位", width = 15)
    @ApiModelProperty(value = "隶属单位")
	private java.lang.String unit;
	/**生产厂家*/
	@Excel(name = "生产厂家", width = 15)
    @ApiModelProperty(value = "生产厂家")
	private java.lang.String manufacturer;
	/**阵地代号*/
	@Excel(name = "阵地代号", width = 15)
    @ApiModelProperty(value = "阵地代号")
	private java.lang.String zdCode;
	/**父设备编号*/
    @ApiModelProperty(value = "父设备编号")
	private java.lang.String pCode;
	/**是否可见*/
    @ApiModelProperty(value = "是否可见")
	private java.lang.Integer isVisible;
	/**责任单位*/
	@Excel(name = "责任单位", width = 15)
    @ApiModelProperty(value = "责任单位")
	private java.lang.String chargeDepartmentId;
	/**子系统*/
    @ApiModelProperty(value = "子系统")
	private java.lang.String subSystem;
	/**totalDuration*/
    @ApiModelProperty(value = "totalDuration")
	private java.lang.Integer totalDuration;
	/**阵管编码*/
	@Excel(name = "阵管编码", width = 15)
	@ApiModelProperty(value = "阵管编码")
	private java.lang.String zgCode;

	@TableField()
	//@TableLogic
	@ApiModelProperty(value = "deleted",hidden = true)
	private Integer deleted;

	@ApiModelProperty(value = "设备类型")
	@TableField(exist = false)
	private DeviceType deviceType;

	/**文件路径*/
	@ApiModelProperty(value = "文件路径")
	private java.lang.String imgUrls;

	@TableField(exist = false)
	private List<DeviceAttr> deviceAttrs;

	// 高亮字段（非存储字段）
	@Transient
	private Map<String, List<String>> highlight;

}
