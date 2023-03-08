package com.tju.bclab.vote_backend.vo.req;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="用户登录时的前端json")
public class UserLoginReq {

    @ApiModelProperty(value = "授权类型：0--WEB端 1--微信端")
    private Integer authType;

    @ApiModelProperty(value = "临时登录凭证")
    private String code;

    @ApiModelProperty(value = "用户非敏感信息")
    private String rawData;

    @ApiModelProperty(value = "用户签名")
    private String signature;

    @ApiModelProperty(value = "用户敏感信息")
    private String encryptedData;

    @ApiModelProperty(value = "解密算法的向量")
    private String iv;

}
