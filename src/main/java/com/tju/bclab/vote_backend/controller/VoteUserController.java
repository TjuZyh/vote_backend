package com.tju.bclab.vote_backend.controller;


import com.tju.bclab.vote_backend.common.R;
import com.tju.bclab.vote_backend.common.ResultCode;
import com.tju.bclab.vote_backend.common.myUtils;
import com.tju.bclab.vote_backend.entity.Vote;
import com.tju.bclab.vote_backend.entity.VoteBlockChain;
import com.tju.bclab.vote_backend.exception.ApiException;
import com.tju.bclab.vote_backend.service.VoteService;
import com.tju.bclab.vote_backend.service.VoteUserService;
import com.tju.bclab.vote_backend.vo.req.VoteReq;
import com.tju.bclab.vote_backend.vo.resp.QueryHaveJoinedResp;
import com.tju.bclab.vote_backend.vo.resp.VoteBlockChainResp;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/vote_backend/vote_user")
public class VoteUserController {
    @Autowired
    private VoteUserService voteUserService;
    @Autowired
    private VoteService voteService;

    @ApiOperation(value = "用户进行投票")
    @PostMapping("/vote")
    public R<VoteBlockChainResp> vote(@RequestBody VoteReq voterReq) {

        String voteId = voterReq.getVoteId();
        String userId = voterReq.getUserId();

        // 查看当前投票类型，如果是投票箱投票，判断是否超过当前时间
        Vote curVote = voteService.getById(voteId);
        Boolean afterStartTime = true;
        if (curVote.getVoteType() == 2) {
            afterStartTime = voteService.afterStartTime(voteId);
        }

        //当前时间
        Date currentTime = voterReq.getCurrentTime();
        //投票截止时间
        Date endVoteDate = voteService.getEndVoteDate(voteId);
        //截止日期
        int stopped = currentTime.compareTo(endVoteDate);
        if (stopped > 0) return null;
        //判断用户是否投过票。true为投过，调用之前的API
        R<QueryHaveJoinedResp> resp = queryHaveVoted(userId, voteId);
        Boolean haveVoted = resp.getData().getHaveVoted();
        Boolean canVote = true;
        if (haveVoted) {
            //用户之前的投票时间
            Date databaseTime = voteUserService.dataBaseTime(userId, voteId);
            //当天是否投过。true为投过。
            Boolean sameDay = myUtils.sameDay(currentTime, databaseTime);
            Boolean dailyVote = dailyVote(voteId);
            canVote = dailyVote && !sameDay;
        }
        //(没投过票的)或者是（投过票的且是每日一投的，且当天没有投过的）;可以投票
        if (canVote && afterStartTime) {
            // 调用service中的方法进行投票添加
            try {
                List<VoteBlockChain> voteBlockChainList = voteUserService.Vote(voterReq);
                VoteBlockChainResp voteBlockChainResp = new VoteBlockChainResp();
                voteBlockChainResp.setVoteBlockChainList(voteBlockChainList);
                return R.ok(voteBlockChainResp);
            } catch (ApiException e) {
                return R.error(e.getApiErrorCode());
            }
        } else {
            return R.error(ResultCode.HAVE_VOTED);
        }
    }

    //判断该投票是每天一票还是全程只能一票。true为每天一票。
    public Boolean dailyVote(String voteId) {
        Boolean dailyVote = voteService.dailyVote(voteId);
        return dailyVote;
    }

    @ApiOperation(value = "查询用户是否参与该投票api，返回bool和用户选项")
    @GetMapping("/queryHaveVoted/{userId}/{voteId}")
    public R<QueryHaveJoinedResp> queryHaveVoted(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @PathVariable("userId") String userId,
            @ApiParam(name = "voteId", value = "投票ID", required = true)
            @PathVariable("voteId") String voteId
    ) {
        QueryHaveJoinedResp queryHaveJoinedResp = voteUserService.qryHaverJoined(userId, voteId);
        return R.ok(queryHaveJoinedResp);
    }
}

