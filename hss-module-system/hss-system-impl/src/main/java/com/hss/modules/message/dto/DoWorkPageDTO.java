package com.hss.modules.message.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 手动值班分页请求参数
 * @author hd
 *
 */
@Data
@ApiModel("手动值班分页请求参数")
public class DoWorkPageDTO {

    @ApiModelProperty("终端ids")
    List<String> terminalIds;

    @ApiModelProperty("值班名称")
    String workName;

    @ApiModelProperty("值班日期")
    @DateTimeFormat(pattern = "yyy-MM-dd")
    Date workDate;

    @ApiModelProperty("pageNo")
    Integer pageNo;

    @ApiModelProperty("pageSize")
    Integer pageSize;


}
