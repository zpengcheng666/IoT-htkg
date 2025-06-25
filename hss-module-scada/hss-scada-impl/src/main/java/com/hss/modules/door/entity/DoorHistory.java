package com.hss.modules.door.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
* @description: 门禁子系统-历史数据管理-
* @author zpc
* @date 2022/12/5 14:40
* @version 1.0
*/
@Data
@TableName(value = "DOOR_HISTORY", autoResultMap = true)
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="DOOR_HISTORY对象", description="门禁子系统-历史数据管理-进出记录")
public class DoorHistory {
    
	/**ID*/
	@TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "ID")
	private java.lang.String id;
	/**门ID*/
//	@Excel(name = "门ID", width = 15)
    @ApiModelProperty(value = "门ID")
	private java.lang.String doorId;
	/**门编号*/
//	@Excel(name = "门编号", width = 15)
    @ApiModelProperty(value = "门编号")
	private java.lang.String doorCode;
	/**门名称*/
	@Excel(name = "门名称", width = 15)
    @ApiModelProperty(value = "门名称")
	private java.lang.String doorName;
	/**门类型（'UnidirectionalDoor', '单向门', 'BidirectionalDoor', '双向门'）*/
	@Excel(name = "门类型", width = 15)
	@ApiModelProperty(value = "门类型（'one', '单向门', 'two', '双向门'）")
	private java.lang.String doorType;
	/**开门时间*/
	@Excel(name = "开门时间", width =  15, exportFormat="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "开门时间")
	private java.util.Date openTime;
	/**开门/进出 人数*/
	@Excel(name = "开门/进出 人数", width = 15)
	@ApiModelProperty(value = "开门/进出 人数")
	private java.lang.Integer perNumRecord;
	/**刷卡人姓名1*/
	@Excel(name = "刷卡人员1", width = 15)
	@ApiModelProperty(value = "刷卡人员1")
	private java.lang.String perName1;
	/**刷卡人单位1*/
	@Excel(name = "刷卡人单位1", width = 15)
	@ApiModelProperty(value = "刷卡人单位1")
	private java.lang.String perDept1;
	/**刷卡人职务1*/
	@Excel(name = "刷卡人职务1", width = 15)
	@ApiModelProperty(value = "刷卡人职务1")
	private java.lang.String perTitle1;
	/**卡号1*/
	@Excel(name = "卡号1", width = 15)
	@ApiModelProperty(value = "卡号1")
	private java.lang.String cardCode1;
	/**刷卡时间1*/
	@Excel(name = "刷卡时间1", width = 15, exportFormat="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "刷卡时间1")
	private java.util.Date swipeTime1;
	/**进出类型（ 'entryCard', '进门', 'exitCard', '出门'）*/
	@Excel(name = "进出类型", width = 15,replace = {"进门_entryCard","出门_exitCard"})
	@ApiModelProperty(value = "进出类型（ 'entryCard', '进门', 'exitCard', '出门'）")
	private java.lang.String accessType;


//	@TableField(exist = false)
//	private String typeDisp;

	/**开门类型（'SingleOpen', '单人开门', 'DoubleOpen', '双人开门', 'SingleCheck', '单人核准', 'DoubleCheck','双人核准', 'RemoteOpen', '远程开门'）*/

	@ApiModelProperty(value = "开门类型（'SingleOpen', '单人开门', 'DoubleOpen', '双人开门', 'SingleCheck', '单人核准', 'DoubleCheck','双人核准', 'RemoteOpen', '远程开门'）")
	//@Dict(dictTable ="BASE_DICT_DATA",dicText = "openType_disp",dicCode = "id")
	private java.lang.String openType;

	@Excel(name = "开门类型", width = 15)
	@TableField(exist = false)
	private String openType_disp;

	/**记录类型，刷卡/手动录入*/
	@Excel(name = "记录类型", width = 15,replace = {"刷卡_0","手动录入_1"})
	@ApiModelProperty(value = "记录类型，0-自动/1-手动录入")
	private java.lang.Integer recordType;



	/**门位置*/
//	@Excel(name = "门位置", width = 15)
    @ApiModelProperty(value = "门位置")
	private java.lang.String doorLocation;
	/**进出人员信息*/
//	@Excel(name = "进出人员信息", width = 15)
	@ApiModelProperty(value = "进出人员信息")
	@TableField(typeHandler = JacksonTypeHandler.class)
	private DoorPerson[] perList;



	/**卡号1*/
//	@Excel(name = "卡号1", width = 15)
    @ApiModelProperty(value = "卡号1")
	private java.lang.String cardCode2;
	/**刷卡人姓名1*/
//	@Excel(name = "刷卡人姓名1", width = 15)
    @ApiModelProperty(value = "刷卡人姓名1")
	private java.lang.String perName2;
	/**刷卡人单位1*/
//	@Excel(name = "刷卡人单位1", width = 15)
    @ApiModelProperty(value = "刷卡人单位1")
	private java.lang.String perDept2;
	/**刷卡人职务1*/
//	@Excel(name = "刷卡人职务1", width = 15)
    @ApiModelProperty(value = "刷卡人职务1")
	private java.lang.String perTitle2;
	/**刷卡时间1*/
//    @ApiModelProperty(value = "刷卡时间1")
	private java.util.Date swipeTime2;
	/**卡号1*/
//	@Excel(name = "卡号1", width = 15)
    @ApiModelProperty(value = "卡号1")
	private java.lang.String cardCode3;
	/**刷卡人姓名1*/
//	@Excel(name = "刷卡人姓名1", width = 15)
    @ApiModelProperty(value = "刷卡人姓名1")
	private java.lang.String perName3;
	/**刷卡人单位1*/
//	@Excel(name = "刷卡人单位1", width = 15)
    @ApiModelProperty(value = "刷卡人单位1")
	private java.lang.String perDept3;
	/**刷卡人职务1*/
//	@Excel(name = "刷卡人职务1", width = 15)
    @ApiModelProperty(value = "刷卡人职务1")
	private java.lang.String perTitle3;
	/**刷卡时间1*/
    @ApiModelProperty(value = "刷卡时间1")
	private java.util.Date swipeTime3;
	/**核准卡号*/
//	@Excel(name = "核准卡号", width = 15)
    @ApiModelProperty(value = "核准卡号")
	private java.lang.String checkCode;
	/**核准人姓名*/
//	@Excel(name = "核准人姓名", width = 15)
    @ApiModelProperty(value = "核准人姓名")
	private java.lang.String checkName;
	/**核准人单位*/
//	@Excel(name = "核准人单位", width = 15)
    @ApiModelProperty(value = "核准人单位")
	private java.lang.String checkDept;
	/**核准人职位*/
//	@Excel(name = "核准人职位", width = 15)
    @ApiModelProperty(value = "核准人职位")
	private java.lang.String checkTitle;
	/**核准时间*/
    @ApiModelProperty(value = "核准时间")
	private java.util.Date checkTime;


	/**是否安检（0：否，1：是）*/
//	@Excel(name = "是否安检（0：否，1：是）", width = 15)
    @ApiModelProperty(value = "是否安检（0：否，1：是）")
	private java.lang.String isCheck;
	/**门锁状态（'0', '关闭', '1', '打开'）*/
//	@Excel(name = "门锁状态（'0', '关闭', '1', '打开'）", width = 15)
    @ApiModelProperty(value = "门锁状态（'0', '关闭', '1', '打开'）")
	private java.lang.String lockState;
	/**门磁状态（'0', '关闭', '1', '打开'）*/
//	@Excel(name = "门磁状态（'0', '关闭', '1', '打开'）", width = 15)
    @ApiModelProperty(value = "门磁状态（'0', '关闭', '1', '打开'）")
	private java.lang.String magnetState;
	/**门磁状态变量ID*/
//	@Excel(name = "门磁状态变量ID", width = 15)
    @ApiModelProperty(value = "门磁状态变量ID")
	private java.lang.String magnetStateId;


	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@TableField(exist = false)
	private Date begin_openTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@TableField(exist = false)
	private Date end_openTime;

	/**刷卡人2ID*/
//	@Excel(name = "刷卡人2ID", width = 15)
    @ApiModelProperty(value = "刷卡人2ID")
	private java.lang.String perId2;
	/**刷卡人3ID*/
//	@Excel(name = "刷卡人3ID", width = 15)
    @ApiModelProperty(value = "刷卡人3ID")
	private java.lang.String perId3;
	/**刷卡人1ID*/
//	@Excel(name = "刷卡人1ID", width = 15)
    @ApiModelProperty(value = "刷卡人1ID")
	private java.lang.String perId1;
	/**核准人ID*/
//	@Excel(name = "核准人ID", width = 15)
    @ApiModelProperty(value = "核准人ID")
	private java.lang.String checkId;
	/**出门按钮点（0：按钮短路，1：按钮按下，2：按钮空闲，3：按钮短路）*/
//	@Excel(name = "出门按钮点（0：按钮短路，1：按钮按下，2：按钮空闲，3：按钮短路）", width = 15)
    @ApiModelProperty(value = "出门按钮点（0：按钮短路，1：按钮按下，2：按钮空闲，3：按钮短路）")
	private java.lang.String exitButton;
	/**门状态点（0：关闭状态，1：开状态，2：暴力入侵状态）*/
//	@Excel(name = "门状态点（0：关闭状态，1：开状态，2：暴力入侵状态）", width = 15)
    @ApiModelProperty(value = "门状态点（0：关闭状态，1：开状态，2：暴力入侵状态）")
	private java.lang.String doorState;
	/**手自动点（0：手动，1：自动）*/
//	@Excel(name = "手自动点（0：手动，1：自动）", width = 15)
    @ApiModelProperty(value = "手自动点（0：手动，1：自动）")
	private java.lang.String manualAutomatic;
	/**刷卡人1性别*/
//	@Excel(name = "刷卡人1性别", width = 15)
    @ApiModelProperty(value = "刷卡人1性别")
	private java.lang.Integer perSex1;
	/**刷卡人2性别*/
//	@Excel(name = "刷卡人2性别", width = 15)
    @ApiModelProperty(value = "刷卡人2性别")
	private java.lang.Integer perSex2;
	/**刷卡人3性别*/
//	@Excel(name = "刷卡人3性别", width = 15)
    @ApiModelProperty(value = "刷卡人3性别")
	private java.lang.Integer perSex3;
	/**进入原因*/
//	@Excel(name = "进入原因", width = 15)
    @ApiModelProperty(value = "进入原因")
	private java.lang.String enterReason;
	/**核准人性别*/
//	@Excel(name = "核准人性别", width = 15)
    @ApiModelProperty(value = "核准人性别")
	private java.lang.Integer checkSex;
	/**createdTime*/
    @ApiModelProperty(value = "createdTime",hidden = true)
	private java.util.Date createdTime;
	/**updatedTime*/
    @ApiModelProperty(value = "updatedTime",hidden = true)
	private java.util.Date updatedTime;
	/**deleted*/
//	@Excel(name = "deleted", width = 15)
    @ApiModelProperty(value = "deleted",hidden = true)
	@TableLogic
	private java.lang.Integer deleted;
}
