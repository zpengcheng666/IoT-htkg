package com.hss.modules.system.entity;

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
 * @Description: 终端显示信息
 * @Author: zpc
 * @Date:   2023-02-27
 * @Version: V1.0
 */
@Data
@TableName("BASE_TERMINAL_INFO")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BASE_TERMINAL_INFO对象", description="终端显示信息")
public class BaseTermianlInfo {
    
	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**信息类型*/
	@Excel(name = "信息类型", width = 15)
    @ApiModelProperty(value = "信息类型")
	private java.lang.String infoType;
	/**是否显示*/
	@Excel(name = "是否显示", width = 15)
    @ApiModelProperty(value = "是否显示")
	private java.lang.Integer isShow;
	/**终端id*/
    @ApiModelProperty(value = "终端id")
	private java.lang.String terminalId;

	@ApiModelProperty(value = "终端名称")
	private java.lang.String terminalName;

	@ApiModelProperty("x轴")
	private Integer x;

	@ApiModelProperty("y轴")
	private Integer y;

	@ApiModelProperty("宽")
	private Integer w;

	@ApiModelProperty("高")
	private Integer h;

	@ApiModelProperty("")
	private Integer i;

	@ApiModelProperty(value = "背景颜色")
	private String backgroundColor;

	@ApiModelProperty(value = "背景图片")
	private String backgroundImg;

	@ApiModelProperty(value = "图片链接地址")
	private String imgUrl;

	@ApiModelProperty(value = "视频路径")
	private String videoUrl;

}
