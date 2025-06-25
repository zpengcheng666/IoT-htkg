package com.hss.modules.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.List;

/**
 * @Description: 角色表
 * @Author: zpc
 * @Date:   2022-11-03
 * @Version: V1.0
 */
@Data
@TableName("BASE_ROLE")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BASE_ROLE对象", description="角色表")
public class BaseRole {
    
	/**ID*/
	@TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "ID")
	private java.lang.String id;
	/**名称*/
	@Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
	private java.lang.String name;
	/**状态（0：无效，1：有效）*/
	@Excel(name = "状态（0：无效，1：有效）", width = 15)
    @ApiModelProperty(value = "状态（0：无效，1：有效）")
	private java.lang.Integer state;
	/**描述*/
	@Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
	private java.lang.String describe;
	/**是否可删除（0：不可删除，1：可删除）*/
	@Excel(name = "是否可删除（0：不可删除，1：可删除）", width = 15)
    @ApiModelProperty(value = "是否可删除（0：不可删除，1：可删除）")
	private java.lang.Integer isEditable;
	/**createdTime*/
    @ApiModelProperty(value = "createdTime",hidden = true)
	private java.util.Date createdTime;
	/**updatedTime*/
    @ApiModelProperty(value = "updatedTime",hidden = true)
	private java.util.Date updatedTime;
	/**deleted*/
	@Excel(name = "deleted", width = 15)
    @ApiModelProperty(value = "deleted",hidden = true)
	@TableLogic//逻辑删除标志0未删除1删除
	@TableField(select = true)//按照删除标志来查询
	private java.lang.Integer deleted;

	@TableField(exist = false)
	private List<BaseRoleMenu> permissions;
}
