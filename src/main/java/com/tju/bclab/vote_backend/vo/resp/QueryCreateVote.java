package com.tju.bclab.vote_backend.vo.resp;

import com.tju.bclab.vote_backend.vo.common.VoteInfo;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value="查询我创建的投票得到的response")
public class QueryCreateVote {
    private List<VoteInfo> myCreateVote;
}
