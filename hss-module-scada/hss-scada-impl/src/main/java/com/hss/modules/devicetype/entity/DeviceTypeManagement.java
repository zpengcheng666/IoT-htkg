package com.hss.modules.devicetype.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelIgnore;

import java.util.List;

/**
 * @Description: 设备类型管理
 * @Author: zpc
 * @Date:   2023-01-05
 * @Version: V1.0
 */
@Data
@TableName("T_DEVICE_TYPE_MANAGEMENT")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="T_DEVICE_TYPE_MANAGEMENT对象", description="设备类型管理")
public class DeviceTypeManagement {
    
	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**类型名称*/
	@Excel(name = "类型名称", width = 15)
    @ApiModelProperty(value = "类型名称")
	private java.lang.String name;
	/**类型*/
	@Excel(name = "类型", width = 15)
    @ApiModelProperty(value = "类型")
	private java.lang.String type;
	/**分组*/
	@Excel(name = "分组", width = 15)
    @ApiModelProperty(value = "分组")
	private java.lang.String tags;
	/**默认状态*/
	@Excel(name = "默认状态", width = 15)
    @ApiModelProperty(value = "默认状态")
	private java.lang.String defaultState;
	/**显示悬浮窗口*/
	@Excel(name = "显示悬浮窗口", width = 15)
    @ApiModelProperty(value = "显示悬浮窗口")
	private java.lang.Integer isShowDataTable;
	/**在场景中显示*/
	@Excel(name = "在场景中显示", width = 15)
    @ApiModelProperty(value = "在场景中显示")
	private java.lang.Integer isShowInScene;
	/**关联摄像机*/
	@Excel(name = "关联摄像机", width = 15)
    @ApiModelProperty(value = "关联摄像机")
	private java.lang.Integer isCameraLinked;

	@ExcelIgnore
	@TableField(exist = false)
	@ApiModelProperty(value = "状态名称列表")
	private List<String> stateNames;
}
