package com.tju.bclab.vote_backend.controller;


import com.tju.bclab.vote_backend.common.R;
import com.tju.bclab.vote_backend.exception.ApiException;
import com.tju.bclab.vote_backend.service.*;
import com.tju.bclab.vote_backend.vo.req.AddVoteBoxReq;
import com.tju.bclab.vote_backend.vo.req.AddVoteReq;
import com.tju.bclab.vote_backend.vo.req.UserLoginReq;
import com.tju.bclab.vote_backend.vo.resp.AddVoteResp;
import com.tju.bclab.vote_backend.vo.resp.QueryCreateVote;
import com.tju.bclab.vote_backend.vo.resp.QueryJoinedVote;
import com.tju.bclab.vote_backend.vo.resp.UserLoginResp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/vote_backend/user")
@ApiModel
public class UserController {
    @Autowired
    private VoteService voteService;
    @Autowired
    private VoteUserService voteUserService;
    @Autowired
    private VoteOptionService voteOptionService;
    @Autowired
    private UserService userService;
    @Resource
    VoteboxuserService voteboxuserService;

    @ApiOperation(value = "用户登录,前端传递code")
    @PostMapping("/login")
    public R<UserLoginResp> UserLogin(@RequestBody UserLoginReq userLoginReq) {
        // 调用service中的方法进行投票添加
        return R.ok(userService.Login(userLoginReq));
    }


    @ApiOperation(value = "用户发起投票")
    @PostMapping("/addVote")
    public R addVote(@RequestBody AddVoteReq addVoteReq) {
        try {
            // 调用service中的方法进行投票添加
            String voteId = voteService.addVote(addVoteReq);
            // 封装响应类
            AddVoteResp addVoteResp = new AddVoteResp();
            addVoteResp.setVoteId(voteId);
            return R.ok(addVoteResp);
        } catch (ApiException e) {
            return R.error(e.getApiErrorCode());
        }

    }


    @ApiOperation(value = "用户发起投票箱投票")
    @PostMapping("/addBoxVote")
    public R addBoxVote(@RequestBody AddVoteBoxReq addVoteBoxReq) {
        try {
            return R.ok(voteboxuserService.addBoxVote(addVoteBoxReq));
        } catch (ApiException e) {
            return R.error(e.getApiErrorCode());
        }
    }

    @ApiOperation(value = "生成身份验证二维码")
    @GetMapping("/qrCode/{userId}")
    public R<String> getQrCode(@PathVariable("userId") String userId) {
        String qrCodeUrl = userService.getQrCode(userId);
        return R.ok(qrCodeUrl);
    }


    @ApiOperation(value = "查询当前用户发起的投票")
    @GetMapping("/queryMyCreateVote/{userId}")
    public R<QueryCreateVote> queryCreateVote(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @PathVariable("userId") String userId
    ) {
        return R.ok(voteUserService.qryCreateVote(userId));
    }

    @ApiOperation(value = "查询当前用户参与的投票")
    @GetMapping("/queryMyJoinedVote/{userId}")
    public R<QueryJoinedVote> queryJoinedVote(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @PathVariable("userId") String userId
    ) {
        return R.ok(voteUserService.qryJoinedVote(userId));
    }

    @ApiOperation(value = "结束当前投票箱投票")
    @PostMapping("/endBoxVote/{voteId}")
    public R endBoxVote(
            @ApiParam(name = "voteId", value = "投票ID", required = true)
            @PathVariable("voteId") String voteId
    ) {
        return R.ok(voteboxuserService.endBoxVote(voteId));
    }


}

