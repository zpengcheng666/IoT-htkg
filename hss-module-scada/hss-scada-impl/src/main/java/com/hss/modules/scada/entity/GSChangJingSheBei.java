package com.hss.modules.scada.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.Date;

/**
 * @Description: 场景、设备关联关系表
 * @Author: zpc
 * @Date:   2022-12-14
 * @Version: V1.0
 */
@Data
@TableName("GS_CHANGJING_SHEBEI")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="GS_CHANGJING_SHEBEI对象", description="场景、设备关联关系表")
public class GSChangJingSheBei {
    
	/**ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
	private String id;
	/**场景ID*/
	@Excel(name = "场景ID", width = 15)
    @ApiModelProperty(value = "场景ID")
	private String sceneId;
	/**分区ID*/
	@Excel(name = "分区ID", width = 15)
    @ApiModelProperty(value = "分区ID")
	private String zoneId;
	/**源场景ID*/
	@Excel(name = "源场景ID", width = 15)
    @ApiModelProperty(value = "源场景ID")
	private String sourceSceneId;
	/**网关ID*/
	@Excel(name = "网关ID", width = 15)
	@ApiModelProperty(value = "网关ID")
	private String wgId;
	/**设备ID*/
	@Excel(name = "设备ID", width = 15)
    @ApiModelProperty(value = "设备ID")
	private String deviceId;

	/**xAxis*/
	@Excel(name = "xAxis", width = 15)
    @ApiModelProperty(value = "xAxis")
	private Integer xAxis;
	/**yAxis*/
	@Excel(name = "yAxis", width = 15)
    @ApiModelProperty(value = "yAxis")
	private Integer yAxis;
	/**angle*/
	@Excel(name = "angle", width = 15)
    @ApiModelProperty(value = "angle")
	private Integer angle;
	/**ratio*/
	@Excel(name = "ratio", width = 15)
    @ApiModelProperty(value = "ratio")
	private java.math.BigDecimal ratio;
	/**pattern*/
	@Excel(name = "pattern", width = 15)
    @ApiModelProperty(value = "pattern")
	private String pattern;
	/**设备镜像翻转*/
	@Excel(name = "设备镜像翻转", width = 15)
    @ApiModelProperty(value = "设备镜像翻转")
	private String mirrorFlip;
	/**createdTime*/
    @ApiModelProperty(value = "createdTime")
	private Date createdTime;
	/**updatedTime*/
    @ApiModelProperty(value = "updatedTime")
	private Date updatedTime;
	/**deleted*/
	@Excel(name = "deleted", width = 15)
    @ApiModelProperty(value = "deleted")
	private Integer deleted;

	/**
	 * 序号
	 */
	private Integer sortNumber;
}
