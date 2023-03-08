package com.tju.bclab.vote_backend.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value="投票验证返回信息")
public class VerifyVoteResp {
    @ApiModelProperty(value = "区块链信息")
    List<VerifyVote> verifyVoteList;
}
