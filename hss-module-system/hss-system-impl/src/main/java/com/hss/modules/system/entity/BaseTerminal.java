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
 * @Description: 终端基本信息表
 * @Author: zpc
 * @Date: 2022-11-22
 * @Version: V1.0
 */
@Data
@TableName(value = "BASE_TERMINAL", autoResultMap = true)
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "BASE_TERMINAL对象", description = "终端基本信息表")
public class BaseTerminal {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
    private String id;
    /**
     * 终端名称
     */
    @Excel(name = "终端名称", width = 15)
    @ApiModelProperty(value = "终端名称")
    private String name;
    /**
     * 终端编码
     */
    @Excel(name = "终端编码", width = 15)
    @ApiModelProperty(value = "终端编码")
    private String code;
    /**
     * 位置ID
     */
    @Excel(name = "位置ID", width = 15)
    @ApiModelProperty(value = "位置ID")
    private String locationId;

    @TableField(exist = false)
    private String locationId_disp;
    /**
     * 模块切换时间(单位秒)
     */
    @Excel(name = "模块切换时间(单位秒)", width = 15)
    @ApiModelProperty(value = "模块切换时间(单位秒)")
    private Integer switchInterval;
    /**
     * 信息滚动速度(单位秒)
     */
    @Excel(name = "信息滚动速度(单位秒)", width = 15)
    @ApiModelProperty(value = "信息滚动速度(单位秒)")
    private Integer rotateSpeed;
    /**
     * 描述
     */
    @Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
    private String description;
    /**
     * 门Id
     */
    @Excel(name = "门Id", width = 15)
    @ApiModelProperty(value = "门Id")
    private String doorId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "背景颜色")
    private String backgroundColor;

    @ApiModelProperty(value = "背景图片")
    private String backgroundImg;

    @ApiModelProperty(value = "终端信息")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private TerminalInfoModel[] infoList;

	@ApiModelProperty(value = "应急处置：0不启动，1启动")
	private Integer yjcz;

	@ApiModelProperty(value = "门禁：0不选择，1选择")
	private Integer mj;

    @ApiModelProperty(value = "视频路径")
    private String videoUrl;

    @ApiModelProperty(value = "报警级别")
    private String alarmLevel;

    @ApiModelProperty(value = "报警状态,0不报警，1报警")
    private Integer alarmStatus;

    @ApiModelProperty(value = "安检门id")
    private String checkDoorId;

    @ApiModelProperty(value = "安检门,0不显示，1显示")
    private Integer ajm;
}
