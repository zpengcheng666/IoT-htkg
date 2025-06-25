package com.hss.modules.door.entity;

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

/**
 * @Description: 门禁人员
 * @Author: zpc
 * @Date:   2023-03-02
 * @Version: V1.0
 */
@Data
@TableName("DOOR_PERSON")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="DOOR_PERSON对象", description="门禁人员")
public class DoorPersonSys {
    
	/**ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
	private java.lang.String id;
	/**人员ID*/
	@Excel(name = "人员ID", width = 15)
    @ApiModelProperty(value = "人员ID")
	private java.lang.String personId;
	/**姓名*/
	@Excel(name = "姓名", width = 15)
    @ApiModelProperty(value = "姓名")
	private java.lang.String name;
	/**单位*/
	@Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位")
	private java.lang.String department;
	/**职务*/
	@Excel(name = "职务", width = 15)
    @ApiModelProperty(value = "职务")
	private java.lang.String title;
	/**性别*/
	@Excel(name = "性别", width = 15)
    @ApiModelProperty(value = "性别")
	private java.lang.Integer sex;
	/**安全*/
	@Excel(name = "安全", width = 15)
    @ApiModelProperty(value = "安全")
	private java.lang.String socialSecurity;
	/**安全类型*/
	@Excel(name = "安全类型", width = 15)
    @ApiModelProperty(value = "安全类型")
	private java.lang.Integer socialSecurityType;
	/**相片*/
	@Excel(name = "相片", width = 15)
    @ApiModelProperty(value = "相片")
	private byte[] photo;
	/**createdTime*/
    @ApiModelProperty(value = "createdTime")
	private java.util.Date createdTime;
	/**updatedTime*/
    @ApiModelProperty(value = "updatedTime")
	private java.util.Date updatedTime;
	/**deleted*/
	@Excel(name = "deleted", width = 15)
    @ApiModelProperty(value = "deleted")
	private java.lang.Integer deleted;

	@ExcelIgnore
	@TableField(exist = false)
	@ApiModelProperty(value = "主卡")
	private String mainCard;

	@ExcelIgnore
	@TableField(exist = false)
	@ApiModelProperty(value = "附卡")
	private String attacheCard;
}
