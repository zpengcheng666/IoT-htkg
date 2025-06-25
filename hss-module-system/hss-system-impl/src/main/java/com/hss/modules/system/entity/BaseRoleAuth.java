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
 * @Description: 角色按钮关系
 * @Author: zpc
 * @Date:   2023-05-19
 * @Version: V1.0
 */
@Data
@TableName("BASE_ROLE_AUTH")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BASE_ROLE_AUTH对象", description="角色按钮关系")
public class BaseRoleAuth {
    
	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**roleId*/
	@Excel(name = "roleId", width = 15)
    @ApiModelProperty(value = "roleId")
	private java.lang.String roleId;
	/**childrenIds*/
	@Excel(name = "childrenIds", width = 15)
    @ApiModelProperty(value = "childrenIds")
	private java.lang.String childrenIds;
}
