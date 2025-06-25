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
 * @Description: 提醒设置
 * @Author: zpc
 * @Date:   2022-11-21
 * @Version: V1.0
 */
@Data
@TableName("BASE_REMIND")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BASE_REMIND对象", description="提醒设置")
public class BaseRemind {
    
	/**ID*/
	@TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "ID")
	private String id;
	/**设置类型（0：系统默认，1：用户）*/
	@Excel(name = "设置类型（0：系统默认，1：用户）", width = 15)
    @ApiModelProperty(value = "设置类型（0：系统默认，1：用户）")
	private String type;
	/**类型名称*/
	@ApiModelProperty(value = "设置类型名称")
	private String typename;
	/**设置类型标识符（用户ID）*/
	@Excel(name = "设置类型标识符（用户ID）", width = 15)
    @ApiModelProperty(value = "设置类型标识符（用户ID）")
	private String typeTag;
	/**设置项*/
	@Excel(name = "设置项", width = 15)
    @ApiModelProperty(value = "设置项")
	private String item;
	/**设置项名称*/
	@ApiModelProperty(value = "设置项名称")
	private String itemname;
	/**设置值*/
	@Excel(name = "设置值", width = 15)
    @ApiModelProperty(value = "设置值")
	private String value;
	@ApiModelProperty(value = "设置值类型")
	private String valuetype;
}
