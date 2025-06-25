package com.hss.modules.inOutPosition.entity;

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
 * @Description: 外部人员审批表
 * @Author: zpc
 * @Date:   2022-12-13
 * @Version: V1.0
 */
@Data
@TableName("MT_IN_OUT_EXTERNAL")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MT_IN_OUT_EXTERNAL对象", description="外部人员审批表")
public class InOutExternal {
    
	/**ID*/
	@TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "ID")
	private java.lang.String id;
	/**姓名*/
	@Excel(name = "姓名", width = 15)
    @ApiModelProperty(value = "姓名")
	private java.lang.String name;
	/**单位*/
	@Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位")
	private java.lang.String department;
	/**证件号码*/
	@Excel(name = "证件号码", width = 15)
    @ApiModelProperty(value = "证件号码")
	private java.lang.String idNum;
	/**职务*/
	@Excel(name = "职务", width = 15)
    @ApiModelProperty(value = "职务")
	private java.lang.String position2;
	/**审批单ID*/
	@Excel(name = "审批单ID", width = 15)
    @ApiModelProperty(value = "审批单ID")
	private java.lang.String listId;
	/**出入状态(0:未进入,1:已进入,-1:以出)*/
	@Excel(name = "出入状态(0:未进入,1:已进入,-1:以出)", width = 15)
    @ApiModelProperty(value = "出入状态(0:未进入,1:已进入,-1:以出)")
	private java.lang.Integer inOutStatus;
	/**入库时间*/
    @ApiModelProperty(value = "入库时间")
	private java.util.Date inTime;
	/**出库时间*/
    @ApiModelProperty(value = "出库时间")
	private java.util.Date outTime;
	/**人员信息ID*/
	@Excel(name = "人员信息ID", width = 15)
    @ApiModelProperty(value = "人员信息ID")
	private java.lang.String personId;
	/**人员类别(0:内部,1:外部)*/
	@Excel(name = "人员类别(0:内部,1:外部)", width = 15)
    @ApiModelProperty(value = "人员类别(0:内部,1:外部)")
	private java.lang.Integer personType;
	/**性别*/
	@Excel(name = "性别", width = 15)
    @ApiModelProperty(value = "性别")
	private java.lang.Integer sex;
	/**idType*/
	@Excel(name = "idType", width = 15)
    @ApiModelProperty(value = "idType")
	private java.lang.String idType;
}
