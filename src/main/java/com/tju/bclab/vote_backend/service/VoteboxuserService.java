package com.tju.bclab.vote_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tju.bclab.vote_backend.entity.Voteboxuser;
import com.tju.bclab.vote_backend.vo.common.UserQrcodeInfo;
import com.tju.bclab.vote_backend.vo.req.AddVoteBoxReq;


public interface VoteboxuserService extends IService<Voteboxuser> {
    Boolean addUserToBox(String voteID, UserQrcodeInfo addUserToBoxReq);
    String addBoxVote(AddVoteBoxReq addVoteBoxReq);

    Boolean endBoxVote(String voteId);
}
