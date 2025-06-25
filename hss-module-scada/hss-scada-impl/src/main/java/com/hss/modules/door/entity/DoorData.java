package com.hss.modules.door.entity;

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
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 门禁数据表
 * @Author: zpc
 * @Date:   2023-02-17
 * @Version: V1.0
 */
@Data
@TableName("DOOR_DATA")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="DOOR_DATA对象", description="门禁数据表")
public class DoorData {

	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
	private java.lang.String id;
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
    @ApiModelProperty(value = "门类型（'UnidirectionalDoor', '单向门', 'BidirectionalDoor', '双向门'）")
	private java.lang.String doorType;
	/**门位置*/
//	@Excel(name = "门位置", width = 15)
    @ApiModelProperty(value = "门位置")
	private java.lang.String doorLocation;
	/**卡变量ID*/
//	@Excel(name = "卡变量ID", width = 15)
    @ApiModelProperty(value = "卡变量ID")
	private java.lang.String cardId;
	/**卡号*/
//	@Excel(name = "卡号", width = 15)
    @ApiModelProperty(value = "卡号")
	private java.lang.String cardCode;
	/**刷卡人姓名*/
	@Excel(name = "操作人员", width = 15)
    @ApiModelProperty(value = "操作人员")
	private java.lang.String perName;
	/**刷卡人单位*/
//	@Excel(name = "刷卡人单位", width = 15)
    @ApiModelProperty(value = "刷卡人单位")
	private java.lang.String perDept;
	/**刷卡人职务*/
//	@Excel(name = "刷卡人职务", width = 15)
    @ApiModelProperty(value = "刷卡人职务")
	private java.lang.String perTitle;
	/**时间变量ID*/
//	@Excel(name = "时间变量ID", width = 15)
    @ApiModelProperty(value = "时间变量ID")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.lang.String timeId;
	/**刷卡时间*/
	@Excel(name = "操作时间",width = 15,exportFormat="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "操作时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date swipeTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@TableField(exist = false)
	private java.util.Date start_swipeTime;

	@TableField(exist = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date end_swipeTime;


	/**出入类型（'entryCard', '进门', 'exitCard', '出门'）*/
//	@Excel(name = "出入类型（'entryCard', '进门', 'exitCard', '出门'）", width = 15)
    @ApiModelProperty(value = "出入类型（'entryCard', '进门', 'exitCard', '出门'）")
	private java.lang.String accessType;

	/**开门类型（'SingleOpen', '单人开门', 'DoubleOpen', '双人开门', 'SingleCheck', '单人核准', 'DoubleCheck',
'双人核准', 'RemoteOpen', '远程开门')*/
    @ApiModelProperty(value = "开门类型'SingleOpen','单人开门','DoubleOpen','双人开门','SingleCheck','单人核准','DoubleCheck', '双人核准', 'RemoteOpen', '远程开门'")
	private java.lang.String openType;
	@Excel(name = "开门类型", width = 15,replace = {"单人开门_SingleOpen","双人开门_DoubleOpen","单人核准_SingleCheck","双人核准_DoubleCheck","远程开门_RemoteOpen"})
	@TableField(exist = false)
	private java.lang.String openType_disp;

	/**刷卡结果（'0', '有效，单卡成功开门', '5', '有效，卡没到齐', '20', '有效刷卡，跟前面多卡组合成功开门','8', '有效，但跟前面多卡组合失败', '2', '无效，没有授权'）*/
//	@Excel(name = "刷卡结果（'0', '有效，单卡成功开门', '5', '有效，卡没到齐', '20', '有效刷卡，跟前面多卡组合成功开门','8', '有效，但跟前面多卡组合失败', '2', '无效，没有授权'）", width = 15)
    @ApiModelProperty(value = "刷卡结果（'0', '有效，单卡成功开门', '5', '有效，卡没到齐', '20', '有效刷卡，跟前面多卡组合成功开门','8', '有效，但跟前面多卡组合失败', '2', '无效，没有授权'）")
	private java.lang.String cardResult;

	@TableField(exist = false)
	private java.lang.String cardResult_disp;

	/**刷卡人ID*/
//	@Excel(name = "刷卡人ID", width = 15)
    @ApiModelProperty(value = "刷卡人ID")
	private java.lang.String perId;
	/**刷卡人性别*/
//	@Excel(name = "刷卡人性别", width = 15)
    @ApiModelProperty(value = "刷卡人性别")
	private java.lang.Integer perSex;
}
