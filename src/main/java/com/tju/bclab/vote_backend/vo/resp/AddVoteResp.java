package com.tju.bclab.vote_backend.vo.resp;

import io.swagger.annotations.ApiModel;
import lombok.Data;


@Data
@ApiModel(value = "发起投票的响应类")
public class AddVoteResp {
    private String voteId;
}
