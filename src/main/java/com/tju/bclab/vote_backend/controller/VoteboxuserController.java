package com.tju.bclab.vote_backend.controller;


import com.tju.bclab.vote_backend.common.R;
import com.tju.bclab.vote_backend.exception.ApiException;
import com.tju.bclab.vote_backend.service.VoteboxuserService;
import com.tju.bclab.vote_backend.vo.common.UserQrcodeInfo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/vote_backend/voteboxuser")
public class VoteboxuserController {
    @Resource
    VoteboxuserService voteboxuserService;

    @ApiOperation(value = "扫描用户身份码")
    @PostMapping("addUserToBox/{voteId}")
    public R addUserToBox(
            @ApiParam(name = "voteId", value = "当前投票Id", required = true)
            @PathVariable("voteId") String voteId,
            @RequestBody UserQrcodeInfo userQrcodeInfo) {
        Boolean flag = true;
        try {
            return R.ok(voteboxuserService.addUserToBox(voteId, userQrcodeInfo));
        } catch (ApiException e) {
            return R.error(e.getApiErrorCode());
        }
    }

/*
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
*/

}

