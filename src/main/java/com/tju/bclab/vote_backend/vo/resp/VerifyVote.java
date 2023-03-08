package com.tju.bclab.vote_backend.vo.resp;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="投票验证结构")
public class VerifyVote {
    @ApiModelProperty(value = "数据库hash值")
    private String dataBaseHash;
    @ApiModelProperty(value = "区块链hash值")
    private String chainHash;
    @ApiModelProperty(value = "hash值是否相等")
    private boolean isEqual;
}
