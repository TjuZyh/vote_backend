package com.tju.bclab.vote_backend.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value = "用户二维码信息")
public class UserQrcodeInfo {

    @ApiModelProperty(value = "用户唯一id，对应微信的openid，固定为28位")
    private String userId;

    @ApiModelProperty(value = "当前二维码生成时间")
    private Long createTime;
}
