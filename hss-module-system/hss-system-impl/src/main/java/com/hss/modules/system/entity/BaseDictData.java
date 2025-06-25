package com.hss.modules.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 字典数据
 * @Author: zpc
 * @Date:   2022-11-03
 * @Version: V1.0
 */
@Data
@TableName("BASE_DICT_DATA")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BASE_DICT_DATA对象", description="字典数据")
public class BaseDictData {
    
	/**ID*/
	@TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "ID")
	private java.lang.String id;
	/**编码*/
	@Excel(name = "编码", width = 15)
    @ApiModelProperty(value = "编码")
	private java.lang.String code;
	/**名称*/
	@Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
	private java.lang.String name;
	/**英文名称*/
	@Excel(name = "英文名称", width = 15)
    @ApiModelProperty(value = "英文名称")
	private java.lang.String enName;
	/**字典类型ID*/
	@Excel(name = "字典类型ID", width = 15)
    @ApiModelProperty(value = "字典类型ID")
	private java.lang.String dictTypeId;
	@TableField(exist = false)
	private java.lang.String dictTypeId_disp;
	/**字典项顺序*/
	@Excel(name = "字典项顺序", width = 15)
    @ApiModelProperty(value = "字典项顺序")
	private java.lang.Integer orderNum;
	/**是否可用*/
	@Excel(name = "是否可用", width = 15)
    @ApiModelProperty(value = "是否可用")
	private java.lang.Integer isEnable;
	/**创建时间*/
    @ApiModelProperty(value = "创建时间")
	@OrderBy
	private java.util.Date createtime;
	/**更新时间*/
    @ApiModelProperty(value = "更新时间")
	private java.util.Date lasttime;
	/**是否可编辑*/
	@Excel(name = "是否可编辑", width = 15)
    @ApiModelProperty(value = "是否可编辑")
	private java.lang.Integer editable;
//	/**createdTime*/
//    @ApiModelProperty(value = "createdTime")
//	private java.util.Date createdTime;
	/**updatedTime*/
    @ApiModelProperty(value = "updatedTime")
	private java.util.Date updatedTime;
	/**deleted*/
	@Excel(name = "deleted", width = 15)
    @ApiModelProperty(value = "deleted")
	@TableLogic
	@TableField(select = true)
	private java.lang.Integer deleted;
}
