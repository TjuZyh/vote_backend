package com.tju.bclab.vote_backend.vo.resp;

import com.tju.bclab.vote_backend.entity.VoteBlockChain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value="区块返回信息")
public class VoteBlockChainResp {
    @ApiModelProperty(value = "区块链信息")
    List<VoteBlockChain> voteBlockChainList;

}
