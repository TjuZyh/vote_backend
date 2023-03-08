package com.tju.bclab.vote_backend.vo.resp;

import io.swagger.annotations.ApiModelProperty;

public class UserLoginResp {
    @ApiModelProperty(value="自定义登录sKey")
    public String sKey;
    public String openid;
    public UserLoginResp(String sKey,String openid){
        this.sKey = sKey;
        this.openid = openid;
    }
}
