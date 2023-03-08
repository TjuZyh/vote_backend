package com.tju.bclab.vote_backend.controller;


import com.tju.bclab.vote_backend.common.R;
import com.tju.bclab.vote_backend.entity.Votedesc;
import com.tju.bclab.vote_backend.service.VoteService;
import com.tju.bclab.vote_backend.service.VoteUserService;
import com.tju.bclab.vote_backend.service.VotedescService;
import com.tju.bclab.vote_backend.vo.resp.QueryVoteDetail;
import com.tju.bclab.vote_backend.vo.resp.QueryVoteInfoResp;
import com.tju.bclab.vote_backend.vo.resp.VerifyHaveModified;
import com.tju.bclab.vote_backend.vo.resp.VoteDetail;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/vote_backend/vote")
public class VoteController {

    @Autowired
    private VotedescService votedescService;
    @Autowired
    private VoteService voteService;
    @Autowired
    private VoteUserService voteUserService;

    @ApiOperation(value = "添加投票图片描述")
    @PostMapping("/addVotedesc/{voteId}/{url}")
    public R addVotedesc(
            @ApiParam(name = "voteId", value = "投票id", required = true)
            @PathVariable("voteId") String voteId,
            @ApiParam(name = "url", value = "描述图片url", required = true)
            @PathVariable("url") String imgUrl
    ) {
        Votedesc votedesc = new Votedesc();
        votedesc.setVoteId(voteId).setImgUrl(imgUrl);
        return R.ok(votedescService.addVotedesc(votedesc));
    }

    @ApiOperation(value = "查询我发起的投票是否被篡改api")
    @GetMapping("/verifyVoteModified")
    public R<List<VerifyHaveModified>> verifyVote(String voteId) throws Exception {
        List<VerifyHaveModified> result = voteService.verifyVote(voteId);
        return R.ok(result);
    }

    @ApiOperation(value = "查询单个投票最新情况")
    @GetMapping("/queryVoteInfo/{voteId}")
    public R<QueryVoteInfoResp> queryVoteInfo(
            @ApiParam(name = "voteId", value = "投票ID", required = true)
            @PathVariable("voteId") String voteId
    ) {
        QueryVoteInfoResp queryVoteInfoResp = voteService.qryVote2(voteId);
        return R.ok(queryVoteInfoResp);
    }

    // 不建议使用
/*    @ApiOperation(value = "查询投票信息api")
    @GetMapping("/queryVoteMessage/{voteId}")
    public R<QueryVoteMessageResp> queryVoteMessage(
            @ApiParam(name = "voteId", value = "投票ID", required = true)
            @PathVariable("voteId") String voteId
    ) {
        QueryVoteMessageResp queryVoteMessageResp = new QueryVoteMessageResp();
        queryVoteMessageResp.setVote(voteService.qryVote(voteId));
        queryVoteMessageResp.setVoteOptions(voteOptionService.qryVoteOptionsByVoteId(voteId));
        return R.ok(queryVoteMessageResp);
    }*/

    @ApiOperation(value = "查询投票详细页，可以查询到区块链信息")
    @GetMapping("/queryVoteDetail/{voteId}")
    public R<QueryVoteDetail> queryVoteDetail(
            @ApiParam(name = "voteId", value = "投票ID", required = true)
            @PathVariable("voteId") String voteId, String userId
    ) {
        ArrayList<VoteDetail> voteDetails = voteUserService.qryVoteDetail(voteId, userId);
        QueryVoteDetail queryVoteDetail = new QueryVoteDetail();
        queryVoteDetail.setVoteDetails(voteDetails);
        return R.ok(queryVoteDetail);
    }


}



