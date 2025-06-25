package com.hss.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 组织机构
 * @Author: zpc
 * @Date:   2022-11-03
 * @Version: V1.0
 */
@Data
@TableName("BASE_ORGANIZATION")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BASE_ORGANIZATION对象", description="组织机构")
public class BaseOrgan {
    
	/**ID*/
	@TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "ID")
	private java.lang.String id;
	/**机构名称*/
	@Excel(name = "机构名称", width = 15)
    @ApiModelProperty(value = "机构名称")
	private java.lang.String name;
	/**创建时间*/
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createtime;
	/**更新时间*/
    @ApiModelProperty(value = "更新时间")
	private java.util.Date lasttime;
	/**排列顺序*/
	@Excel(name = "排列顺序", width = 15)
    @ApiModelProperty(value = "排列顺序")
	private java.lang.Integer orderNum;
	/**父节点ID*/
	@Excel(name = "父节点ID", width = 15)
    @ApiModelProperty(value = "父节点ID")
	private java.lang.String pid;
	/**是否有效*/
	@Excel(name = "是否有效", width = 15)
    @ApiModelProperty(value = "是否有效")
	private java.lang.Integer isEffective;
	/**描述*/
	@Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
	private java.lang.String description;
	/**createdTime*/
    @ApiModelProperty(value = "createdTime")
	private java.util.Date createdTime;
	/**updatedTime*/
    @ApiModelProperty(value = "updatedTime")
	private java.util.Date updatedTime;
	/**deleted*/
	@Excel(name = "deleted", width = 15)
    @ApiModelProperty(value = "deleted")
	@TableLogic//逻辑删除注解
	private java.lang.Integer deleted;
}
