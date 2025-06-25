package com.hss.modules.message.entity;

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

import java.util.List;

/**
 * @Description: 值班类型表
 * @Author: zpc
 * @Date:   2022-12-08
 * @Version: V1.0
 */
@Data
@TableName("T_PUBLISH_DUTY_TYPE")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="T_PUBLISH_DUTY_TYPE对象", description="值班类型表")
public class PublishDutyType {
    /**ID*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
    private java.lang.String id;
    /**值班岗位名称*/
    @Excel(name = "值班岗位名称", width = 15)
    @ApiModelProperty(value = "值班岗位名称")
    private java.lang.String name;
    /**值班信息ID*/
    @Excel(name = "值班信息ID", width = 15)
    @ApiModelProperty(value = "值班信息ID")
    private java.lang.String dutyId;

    @ApiModelProperty(value = "岗位人员")
    @TableField(exist = false)
    private List<PublishOperatorOnDuty> watchkeepers;
}
