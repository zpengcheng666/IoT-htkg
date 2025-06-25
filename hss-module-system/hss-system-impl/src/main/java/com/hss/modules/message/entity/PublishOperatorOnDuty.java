package com.hss.modules.message.entity;

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
 * @Description: 值班人员表
 * @Author: zpc
 * @Date:   2022-12-08
 * @Version: V1.0
 */
@Data
@TableName("T_PUBLISH_OPERATOR_ON_DUTY")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="T_PUBLISH_OPERATOR_ON_DUTY对象", description="值班人员表")
public class PublishOperatorOnDuty {
    /**ID*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
    private java.lang.String id;
    /**值班人员*/
    @Excel(name = "值班人员", width = 15)
    @ApiModelProperty(value = "值班人员")
    private java.lang.String name;
    /**值班类型ID*/
    @Excel(name = "值班类型ID", width = 15)
    @ApiModelProperty(value = "值班类型ID")
    private java.lang.String typeId;
}
