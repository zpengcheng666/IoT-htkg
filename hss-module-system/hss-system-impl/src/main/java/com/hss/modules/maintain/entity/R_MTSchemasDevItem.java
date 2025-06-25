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
 * @Description: 保养方案-设备类别-保养类别关系表
 * @Author: zpc
 * @Date:   2022-12-28
 * @Version: V1.0
 */
@Data
@TableName("R_MT_SCHEMAS_DEV_ITEM")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="R_MT_SCHEMAS_DEV_ITEM对象", description="保养方案-设备类别-保养类别关系表")
public class R_MTSchemasDevItem {
    
	/**ID*/
	@TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "ID")
	private java.lang.String id;
	/**保养方案ID*/
	@Excel(name = "保养方案ID", width = 15)
    @ApiModelProperty(value = "保养方案ID")
	private java.lang.String schemasId;
	/**保养设备ID*/
	@Excel(name = "保养设备类ID", width = 15)
    @ApiModelProperty(value = "保养设备类别ID")
	private java.lang.String deviceClassId;
	/**保养项目ID*/
	@Excel(name = "保养项目ID", width = 15)
    @ApiModelProperty(value = "保养项目ID")
	private java.lang.String itemId;
	/**标准时长*/
	@Excel(name = "标准时长", width = 15)
    @ApiModelProperty(value = "标准时长")
	private java.lang.Integer standardTime;
	/**itemClass*/
	@Excel(name = "itemClass", width = 15)
    @ApiModelProperty(value = "itemClass")
	private java.lang.String itemClass;
}
