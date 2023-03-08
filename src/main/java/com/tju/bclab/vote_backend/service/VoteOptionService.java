package com.tju.bclab.vote_backend.service;

import com.tju.bclab.vote_backend.entity.VoteOption;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface VoteOptionService extends IService<VoteOption> {
    List<VoteOption> qryVoteOptionsByVoteId(String voteId);
    List<VoteOption> qryVoteOptionsByOptionId(String optionId);
}
