package com.tju.bclab.vote_backend.vo.resp;


import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.ArrayList;

@Data
@ApiModel(value = "查询投票详细页的返回结果")
public class QueryVoteDetail {
    ArrayList<VoteDetail> voteDetails;
}
