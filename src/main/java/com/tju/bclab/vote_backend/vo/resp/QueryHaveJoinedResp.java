package com.tju.bclab.vote_backend.vo.resp;

import com.tju.bclab.vote_backend.vo.common.OptionInfo;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "查询我是否参与过某投票的返回结果")
public class QueryHaveJoinedResp {
    Boolean haveVoted;
    List<OptionInfo> userOptions;
}
