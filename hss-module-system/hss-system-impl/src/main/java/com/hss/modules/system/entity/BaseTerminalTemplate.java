package com.hss.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.hss.modules.system.model.TerminalInfoModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 终端模板
 * @Author: zpc
 * @Date:   2023-04-04
 * @Version: V1.0
 */
@Data
@TableName(value = "BASE_TERMINAL_TEMPLATE", autoResultMap = true)
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BASE_TERMINAL_TEMPLATE对象", description="终端模板")
public class BaseTerminalTemplate {
    
	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**名称*/
	@Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
	private java.lang.String name;

	@ApiModelProperty(value = "背景颜色")
	private java.lang.String color;

	@ApiModelProperty(value = "颜色")
	private java.lang.String describe;

	@ApiModelProperty(value = "背景图片")
	private java.lang.String img;

	/**大屏列表信息*/
    @ApiModelProperty(value = "大屏列表信息")
	@TableField(typeHandler = JacksonTypeHandler.class)
	private TerminalInfoModel[] tempList;

	@ApiModelProperty(value = "报警级别")
	private String alarmLevel;

	@ApiModelProperty(value = "安检门,0不显示，1显示")
	private Integer ajm;

	@ApiModelProperty(value = "报警状态,0不报警，1报警")
	private Integer alarmStatus;

	@ApiModelProperty(value = "应急处置：0不启动，1启动")
	private Integer yjcz;

	@ApiModelProperty(value = "门禁：0不选择，1选择")
	private Integer mj;

	@ApiModelProperty(value = "门Id")
	private String doorId;

	@ApiModelProperty(value = "安检门id")
	private String checkDoorId;

}
