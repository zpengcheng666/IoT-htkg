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
 * @Description: 操作分组、角色关系
 * @Author: zpc
 * @Date:   2022-11-03
 * @Version: V1.0
 */
@Data
@TableName("BASE_GROUP_ROLE")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BASE_GROUP_ROLE对象", description="操作分组、角色关系")
public class BaseGroupRole {
    
	/**ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
	private java.lang.String id;
	/**分组ID*/
	@Excel(name = "分组ID", width = 15)
    @ApiModelProperty(value = "分组ID")
	private java.lang.String groupId;
	/**角色ID*/
	@Excel(name = "角色ID", width = 15)
    @ApiModelProperty(value = "角色ID")
	private java.lang.String menuId;
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

	private String childrenIds;
}
