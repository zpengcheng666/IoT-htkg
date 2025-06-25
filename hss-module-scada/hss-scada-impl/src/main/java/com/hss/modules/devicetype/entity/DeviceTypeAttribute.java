package com.hss.modules.devicetype.entity;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hss.modules.devicetype.model.DeviceTypeAttrConfigOptionItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelIgnore;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 设备类型管理属性管理
 * @Author: zpc
 * @Date:   2023-01-05
 * @Version: V1.0
 */
@Data
@TableName("T_DEVICE_TYPE_ATTRIBUTE")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="T_DEVICE_TYPE_ATTRIBUTE对象", description="设备类型管理属性管理")
public class DeviceTypeAttribute {
    
	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**属性名称*/
	@Excel(name = "属性名称", width = 15)
    @ApiModelProperty(value = "属性名称")
	private java.lang.String name;
	/**属性英文名称*/
	@Excel(name = "属性英文名称", width = 15)
    @ApiModelProperty(value = "属性英文名称")
	private java.lang.String category;
	/**数据类型*/
	@Excel(name = "数据类型", width = 15)
    @ApiModelProperty(value = "数据类型")
	private java.lang.String dataType;
	/**初始值*/
	@Excel(name = "初始值", width = 15)
    @ApiModelProperty(value = "初始值")
	private java.lang.String initValue;
	/**最小值*/
	@Excel(name = "最小值", width = 15)
    @ApiModelProperty(value = "最小值")
	private java.lang.String minValue;
	/**最大值*/
	@Excel(name = "最大值", width = 15)
    @ApiModelProperty(value = "最大值")
	private java.lang.String maxValue;
	/**单位*/
	@Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位")
	private java.lang.String unit;
	/**排序*/
	@Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
	private java.lang.Integer sortNumber;
	/**展示区域*/
	@Excel(name = "展示区域", width = 15)
    @ApiModelProperty(value = "展示区域")
	private java.lang.String displayAreas;
	/**是否可控*/
	@Excel(name = "是否可控", width = 15)
    @ApiModelProperty(value = "是否可控")
	private java.lang.Integer isAct;
	/**是否联动*/
	@Excel(name = "是否联动", width = 15)
    @ApiModelProperty(value = "是否联动")
	private java.lang.Integer isAssociate;

	@Excel(name = "是否关联变量", width = 15)
	@ApiModelProperty(value = "是否关联变量")
	private Integer isAssociateVar;

	/**是否配置*/
	@Excel(name = "是否配置", width = 15)
    @ApiModelProperty(value = "是否配置")
	private java.lang.Integer isConfigurable;
	/**是否存储*/
	@Excel(name = "是否存储", width = 15)
    @ApiModelProperty(value = "是否存储")
	private java.lang.Integer isSave;
	/**类型id*/
	@Excel(name = "类型id", width = 15)
    @ApiModelProperty(value = "类型id")
	private java.lang.String typeId;
	/**控制信息json*/
	@Excel(name = "控制信息json", width = 15)
    @ApiModelProperty(value = "控制信息json")
	private java.lang.String actOrders;
	/**配置信息json*/
	@Excel(name = "配置信息json", width = 15)
    @ApiModelProperty(value = "配置信息json")
	private java.lang.String configOptions;
	/**值映射json*/
	@Excel(name = "值映射json", width = 15)
    @ApiModelProperty(value = "值映射json")
	private java.lang.String valueMap;

	@ExcelIgnore
	@TableField(exist = false)
	@ApiModelProperty(value = "配置信息")
	private List<DeviceTypeAttrConfigOptionItem> configOptionList;

	@ExcelIgnore
	@TableField(exist = false)
	@ApiModelProperty(value = "值映射项")
	private List<DeviceTypeAttrConfigOptionItem> valueMapList;

	@ExcelIgnore
	@TableField(exist = false)
	@ApiModelProperty(value = "控制项")
	private List<DeviceTypeAttrConfigOptionItem> actList;

	@ExcelIgnore
	@TableField(exist = false)
	@ApiModelProperty(value = "展示区域列表")
	private List<String> displayAreaList;


	@TableField(exist = false)
	@ExcelIgnore
	@ApiModelProperty(value = "变量ID")
	private String variableId;
	@TableField(exist = false)
	@ExcelIgnore
	@ApiModelProperty(value = "变量名称")
	private String variableName;

	@TableField(exist = false)
	@ExcelIgnore
	@ApiModelProperty(value = "网关id")
	private String wgId;

	@TableField(exist = false)
	@ExcelIgnore
	@ApiModelProperty(value = "设备ID")
	private String deviceId;



	public void listJson2List(){
		if (StringUtils.isNotEmpty(valueMap)){
			valueMapList = JSONObject.parseObject(valueMap, new TypeReference<HashMap<String, String>>() {
					})
					.entrySet()
					.stream()
					.map(e -> {
						DeviceTypeAttrConfigOptionItem item = new DeviceTypeAttrConfigOptionItem();
						item.setName(e.getKey());
						item.setValue(e.getValue());
						return item;
					}).collect(Collectors.toList());
		}else {
			valueMapList = Collections.emptyList();
		}
		if (StringUtils.isNotEmpty(configOptions)){
			configOptionList = JSONObject.parseArray(configOptions, DeviceTypeAttrConfigOptionItem.class);
		}else {
			configOptionList = Collections.emptyList();
		}
		if (StringUtils.isNotEmpty(actOrders)){
			actList = JSONObject.parseArray(actOrders).stream().map(o -> {
				JSONObject jo = (JSONObject) o;
				DeviceTypeAttrConfigOptionItem item = new DeviceTypeAttrConfigOptionItem();
				item.setName(jo.getString("name"));
				item.setValue(jo.getString("order"));
				return item;
			}).collect(Collectors.toList());
		}else {
			actList = Collections.emptyList();
		}
		if (StringUtils.isNotBlank(displayAreas)){
			displayAreaList = Arrays.stream(displayAreas.split(",")).collect(Collectors.toList());
		}else {
			displayAreaList = Collections.emptyList();
		}
	}
	public void list2JsonList(){
		if (valueMapList != null){
			Map<String, String> map = valueMapList.stream().collect(Collectors.toMap(DeviceTypeAttrConfigOptionItem::getName, DeviceTypeAttrConfigOptionItem::getValue, (x, y) -> {
				return y;
			}));
			valueMap = JSONObject.toJSONString(map);
		}
		if (configOptionList != null){
			configOptions = JSONObject.toJSONString(configOptionList);
		}
		if (actList != null){
			List<JSONObject> collect = actList.stream().map(item -> {
				JSONObject jo = new JSONObject();
				jo.put("name", item.getName());
				jo.put("order", item.getValue());
				return jo;
			}).collect(Collectors.toList());
			actOrders = JSONObject.toJSONString(collect);
		}
		if (CollectionUtils.isNotEmpty(displayAreaList)){
			displayAreas = String.join(",", displayAreaList);
		}else {
			displayAreas = "";
		}
	}
}
