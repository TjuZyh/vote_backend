package com.tju.bclab.vote_backend.vo.resp;

import com.tju.bclab.vote_backend.entity.Vote;
import com.tju.bclab.vote_backend.entity.VoteOption;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
@ApiModel(value = "查询投票具体信息")
public class QueryVoteMessageResp {
    @ApiModelProperty(value = "投票信息")
    Vote vote;
    @ApiModelProperty(value = "投票选项信息")
    List<VoteOption> voteOptions;
}
