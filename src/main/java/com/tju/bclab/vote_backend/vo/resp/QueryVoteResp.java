package com.tju.bclab.vote_backend.vo.resp;

import com.tju.bclab.vote_backend.vo.common.VoteInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
@ApiModel(value="查询我的投票得到的response")
public class QueryVoteResp {
    @ApiModelProperty(value="我发起的投票")
    private List<VoteInfo> myVote;
    @ApiModelProperty(value="我参与的投票")
    private List<VoteInfo> myJoinedVote;
}
