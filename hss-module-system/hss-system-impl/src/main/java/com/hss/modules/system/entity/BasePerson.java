package com.hss.modules.system.entity;

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

/**
 * @Description: 人员管理表
 * @Author: zpc
 * @Date:   2022-11-03
 * @Version: V1.0
 */
@Data
@TableName("BASE_PERSON")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BASE_PERSON对象", description="人员管理表")
public class BasePerson {
    
	/**ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
	private java.lang.String id;
	/**编号*/
	@Excel(name = "编号", width = 15)
    @ApiModelProperty(value = "编号")
	private java.lang.String code;
	/**姓名*/
	@Excel(name = "姓名", width = 15)
    @ApiModelProperty(value = "姓名")
	private java.lang.String name;
	/**性别*/
	@Excel(name = "性别", width = 15)
    @ApiModelProperty(value = "性别")
	private java.lang.String sex;
	/**照片*/
	@Excel(name = "照片", width = 15)
    @ApiModelProperty(value = "照片")
	private java.lang.String photo;
	/**出生年月*/
	@Excel(name = "出生年月", width = 15)
    @ApiModelProperty(value = "出生年月")
	private java.lang.String birthday;
	/**籍贯*/
	@Excel(name = "籍贯", width = 15)
    @ApiModelProperty(value = "籍贯")
	private java.lang.String origin;
	/**文化程度*/
	@Excel(name = "文化程度", width = 15)
    @ApiModelProperty(value = "文化程度")
	private java.lang.String education;
	/**入伍时间*/
	@Excel(name = "入伍时间", width = 15)
    @ApiModelProperty(value = "入伍时间")
	private java.lang.String inDate;
	/**军衔*/
	@Excel(name = "军衔", width = 15)
    @ApiModelProperty(value = "军衔")
	private java.lang.String military;
	@TableField(exist = false)
	private java.lang.String military_disp;

	/**现军衔年月*/
	@Excel(name = "现军衔年月", width = 15)
    @ApiModelProperty(value = "现军衔年月")
	private java.lang.String militaryDate;
	/**职位等级*/
	@Excel(name = "职位等级", width = 15)
    @ApiModelProperty(value = "职位等级")
	private java.lang.String level;
	/**现职级年月*/
	@Excel(name = "现职级年月", width = 15)
    @ApiModelProperty(value = "现职级年月")
	private java.lang.String levelDate;
	/**军人证号码*/
	@Excel(name = "军人证号码", width = 15)
    @ApiModelProperty(value = "军人证号码")
	private java.lang.String militaryNumber;
	/**身份证号码*/
	@Excel(name = "身份证号码", width = 15)
    @ApiModelProperty(value = "身份证号码")
	private java.lang.String idNumber;
	/**工作单位*/
	@Excel(name = "工作单位", width = 15)
    @ApiModelProperty(value = "工作单位")
	private java.lang.String workUnit;
	/**现岗位*/
	@Excel(name = "现岗位", width = 15)
    @ApiModelProperty(value = "现岗位")
	private java.lang.String position;
	@TableField(exist = false)
	private java.lang.String position_disp;

	/**现岗位年月*/
	@Excel(name = "现岗位年月", width = 15)
    @ApiModelProperty(value = "现岗位年月")
	private java.lang.String positionDate;
	/**从事专业*/
	@Excel(name = "从事专业", width = 15)
    @ApiModelProperty(value = "从事专业")
	private java.lang.String major;
	/**服役状态*/
	@Excel(name = "服役状态", width = 15)
    @ApiModelProperty(value = "服役状态")
	private java.lang.String status;
	/**退伍时间*/
	@Excel(name = "退伍时间", width = 15)
    @ApiModelProperty(value = "退伍时间")
	private java.lang.String retireDate;
	/**综合评定*/
	@Excel(name = "综合评定", width = 15)
    @ApiModelProperty(value = "综合评定")
	private java.lang.String comprehensiveAssess;
	/**其它特长*/
	@Excel(name = "其它特长", width = 15)
    @ApiModelProperty(value = "其它特长")
	private java.lang.String otherForte;
	/**婚姻状态*/
	@Excel(name = "婚姻状态", width = 15)
    @ApiModelProperty(value = "婚姻状态")
	private java.lang.String marryStatus;
	/**家庭住址*/
	@Excel(name = "家庭住址", width = 15)
    @ApiModelProperty(value = "家庭住址")
	private java.lang.String address;
	/**人员阵地代号*/
	@Excel(name = "人员阵地代号", width = 15)
    @ApiModelProperty(value = "人员阵地代号")
	private java.lang.String zdCode;
}
