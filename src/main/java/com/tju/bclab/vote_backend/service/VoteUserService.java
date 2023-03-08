package com.tju.bclab.vote_backend.service;

import com.tju.bclab.vote_backend.entity.VoteBlockChain;
import com.tju.bclab.vote_backend.entity.VoteUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tju.bclab.vote_backend.vo.req.VoteReq;
import com.tju.bclab.vote_backend.vo.resp.*;

import java.util.*;

public interface VoteUserService extends IService<VoteUser> {
    Set<String> qryVoteByUserId(String userId);

    Set<String> qryVoteByMap(HashMap<String, Object> map);

    List<VoteBlockChain> Vote(VoteReq voteReq);

    QueryHaveJoinedResp qryHaverJoined(String userId, String voteId);

    QueryVoteResp qryVote(String userId);

    ArrayList<VoteDetail> qryVoteDetail(String voteId, String userId);

    Boolean haveVoted(String userId, String voteId);

    Date dataBaseTime(String userId, String voteId);

    QueryCreateVote qryCreateVote(String userId);

    QueryJoinedVote qryJoinedVote(String userId);
}
