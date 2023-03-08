package com.tju.bclab.vote_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tju.bclab.vote_backend.entity.Vote;
import com.tju.bclab.vote_backend.vo.req.AddVoteReq;
import com.tju.bclab.vote_backend.vo.resp.QueryVoteInfoResp;
import com.tju.bclab.vote_backend.vo.resp.VerifyHaveModified;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface VoteService extends IService<Vote> {

    String addVote(AddVoteReq addVoteReq);

    Set<String> qryVoteByUserId(String userId);

    Vote qryVote(String voteId);

    QueryVoteInfoResp qryVote2(String voteId);

    String qryTileByVoteId(String voteId);

    List<VerifyHaveModified> verifyVote(String voteId) throws Exception;

    Boolean dailyVote(String voteId);

    Date getEndVoteDate(String voteId);

    /**
     * 判断投票箱投票是否已经超过当前开始时间
     * @param voteId
     * @return {@link Boolean}
     * @author Zhang QiHang.
     * @date 2021/12/22 20:06
     */
    Boolean afterStartTime(String voteId);
}
