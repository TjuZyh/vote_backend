package com.tju.bclab.vote_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tju.bclab.vote_backend.entity.User;
import com.tju.bclab.vote_backend.vo.req.UserLoginReq;
import com.tju.bclab.vote_backend.vo.resp.UserLoginResp;


public interface UserService extends IService<User> {
    UserLoginResp Login(UserLoginReq userLoginReq);

    String getQrCode(String userId);
}
