package com.hss.modules.spare.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.OrderBy;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 物料分类
 * @Author: wuyihan
 * @date 2024/4/25 14:38
 * @Version: V1.0
 */
@Data
@TableName("BP_ITEM_TYPE")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BP_ITEM_TYPE对象", description="物料分类")
public class MaterialClassification {

    /**ID*/
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "ID")
    private java.lang.String id;

    /**父id*/
    @Excel(name = "父id", width = 15)
    @ApiModelProperty(value = "父id")
    private java.lang.String parentId;

    /**祖先列表*/
    @Excel(name = "祖先列表", width = 15)
    @ApiModelProperty(value = "祖先列表")
    private java.lang.String ancestors;

    /**名称*/
    @Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private java.lang.String typeName;

    /**排序*/
    @Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
    private java.lang.Integer orderNum;

    /**状态*/
    @Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private java.lang.Integer status;

    /**isDeleted*/
    @Excel(name = "isDeleted", width = 15)
    @ApiModelProperty(value = "isDeleted")
    private java.lang.Integer isDeleted;

    /**创建时间*/
    @ApiModelProperty(value = "创建时间")
    @OrderBy
    private java.util.Date createTime;
    /**更新时间*/
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;

}
