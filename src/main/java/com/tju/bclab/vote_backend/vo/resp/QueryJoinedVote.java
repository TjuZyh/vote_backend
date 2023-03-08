package com.tju.bclab.vote_backend.vo.resp;

import com.tju.bclab.vote_backend.vo.common.VoteInfo;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value="查询我参与的投票得到的response")
public class QueryJoinedVote {
    private List<VoteInfo> myJoinedVote;
}
