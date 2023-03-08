package com.tju.bclab.vote_backend.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "投票选项信息")
public class OptionInfo {
    @ApiModelProperty(value = "选项ID")
    private String optionId;
    @ApiModelProperty(value = "选项描述")
    private String optionDesc;
}
