package com.hss.modules.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 用户表
 * @Author: zpc
 * @Date:   2022-10-27
 * @Version: V1.0
 */
@Data
@TableName("BASE_USER")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BASE_USER对象", description="用户表")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BaseUser {

	/**用户ID*/
	@TableId(type = IdType.ASSIGN_UUID)
	@ApiModelProperty(value = "用户ID")
	private java.lang.String id;

	/**用户名*/
	@Excel(name = "用户名", width = 15)
	@ApiModelProperty(value = "用户名")
	private java.lang.String username;

	/**密码*/
	@Excel(name = "密码", width = 15)
	@ApiModelProperty(value = "密码",hidden = true)
	//@FieldEncrypt(algorithm = Algorithm.MD5_32)
	private java.lang.String password;

	/**姓名*/
	@Excel(name = "姓名", width = 15)
	@ApiModelProperty(value = "姓名")
	private java.lang.String name;

	/**组织机构ID*/
	@Excel(name = "组织机构ID", width = 15)
	@ApiModelProperty(value = "组织机构ID")
	private java.lang.String organizationId;
	@Excel(name = "组织机构ID", width = 15)
	@ApiModelProperty(value = "组织机构ID")
	@TableField(exist = false)
	private java.lang.String organizationIdDisp;

	/**部门*/
	@Excel(name = "部门", width = 15)
	@ApiModelProperty(value = "部门")
	private java.lang.String department;

	/**证件类别ID*/
	@Excel(name = "证件类别ID", width = 15)
	@ApiModelProperty(value = "证件类别ID")
	private java.lang.String papersId;
	@Excel(name = "证件类别ID", width = 15)
	@ApiModelProperty(value = "证件类别ID")
	@TableField(exist = false)//或者加transient关键字
	private java.lang.String papersIdDisp;

	/**证件号码*/
	@Excel(name = "证件号码", width = 15)
	@ApiModelProperty(value = "证件号码")
	private java.lang.String numberId;

	/**skillLevelId*/
	@Excel(name = "skillLevelId", width = 15)
	@ApiModelProperty(value = "skillLevelId")
	private java.lang.String skillLevelId;

	/**邮箱*/
	@Excel(name = "邮箱", width = 15)
	@ApiModelProperty(value = "邮箱")
	private java.lang.String email;

	/**座机*/
	@Excel(name = "座机", width = 15)
	@ApiModelProperty(value = "座机")
	private java.lang.String plane;

	/**地址*/
	@Excel(name = "地址", width = 15)
	@ApiModelProperty(value = "地址")
	private java.lang.String address;

	/**专业*/
	@Excel(name = "专业", width = 15)
	@ApiModelProperty(value = "专业")
	private java.lang.String major;

	/**手机*/
	@Excel(name = "手机", width = 15)
	@ApiModelProperty(value = "手机")
	private java.lang.String phone;

	/**军衔ID*/
	@Excel(name = "军衔ID", width = 15)
	@ApiModelProperty(value = "军衔ID")
	private java.lang.String militaryId;

	/**职称*/
	@Excel(name = "职称", width = 15)
    @ApiModelProperty(value = "职称")
	private java.lang.String positionId;

	/**finger 指纹*/
	@Excel(name = "finger 指纹", width = 15)
	@ApiModelProperty(value = "finger 指纹")
	private java.lang.String finger;

	/**editable 是否可编辑*/
	@Excel(name = "editable 是否可编辑", width = 15)
	@ApiModelProperty(value = "editable 是否可编辑")
	private java.lang.Integer editable;

	/**头像*/
	@Excel(name = "头像", width = 15)
	@ApiModelProperty(value = "头像")
	private java.lang.String headImg;

	/**年龄*/
	@Excel(name = "年龄", width = 15)
	@ApiModelProperty(value = "年龄")
	private java.lang.Integer age;

	/**性别  0 男 1 女*/
	@Excel(name = "性别  0 男 1 女", width = 15)
	@ApiModelProperty(value = "性别  0 男 1 女")
	private java.lang.Integer sex;
	@Excel(name = "性别  0 男 1 女", width = 15)
	@ApiModelProperty(value = "性别  0 男 1 女")
	@TableField(exist=false)
	private java.lang.Integer sex_disp;

	/**锁定状态 0 锁定 1 不锁定*/
	@Excel(name = "锁定状态 0 锁定 1 不锁定", width = 15)
	@ApiModelProperty(value = "锁定状态 0 锁定 1 不锁定")
	private java.lang.Integer lockStatus;
	@Excel(name = "锁定状态 0 锁定 1 不锁定", width = 15)
	@ApiModelProperty(value = "锁定状态 0 锁定 1 不锁定")
	@TableField(exist = false)
	private java.lang.Integer lockStatus_disp;

	/**remark*/
	@Excel(name = "remark", width = 15)
	@ApiModelProperty(value = "remark")
	private java.lang.String remark;
	/**最后登录时间*/
	@ApiModelProperty(value = "最后登录时间",hidden = true)
	private java.util.Date lasttime;
	/**创建时间*/
	@ApiModelProperty(value = "创建时间",hidden = true)
	private java.util.Date createtime;
	/**盐值*/
	@Excel(name = "盐值", width = 15)
	@ApiModelProperty(value = "盐值",hidden = true)
	private java.lang.String salt;
	/**deleted*/
	@Excel(name = "deleted", width = 15)
	@ApiModelProperty(value = "deleted",hidden = true)
	@TableField(select = true)
	@TableLogic//逻辑删除注解，同一个class中不支持多个此注解
	private java.lang.Integer deleted;
	/**updatedTime*/
	@ApiModelProperty(value = "updatedTime",hidden = true)
	private java.util.Date updatedTime;
}
