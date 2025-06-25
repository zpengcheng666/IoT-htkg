package com.hss.modules.maintain.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author hd
 * 提交保养任务参数
 */
@Data
@ApiModel("提交保养任务参数")
public class MaintainSubmitDTO {

    @ApiModelProperty("方案id")
    private String id;

    @ApiModelProperty("完成的流程列表列表")
    private List<String> operateIds;
}
