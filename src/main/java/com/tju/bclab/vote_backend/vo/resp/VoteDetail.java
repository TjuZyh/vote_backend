package com.tju.bclab.vote_backend.vo.resp;


import com.tju.bclab.vote_backend.entity.VoteBlockChain;
import com.tju.bclab.vote_backend.entity.VoteOption;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="投票细节")
public class VoteDetail {
    @ApiModelProperty(value = "区块链信息")
    VoteBlockChain voteBlockChain;
    @ApiModelProperty(value = "区块所对应的投票选项信息")
    VoteOption voteOption;
}
