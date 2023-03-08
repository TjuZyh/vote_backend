package com.tju.bclab.vote_backend.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "投票的简要信息")
public class VoteInfo {
    @ApiModelProperty(value = "vote id")
    private String voteId;
    @ApiModelProperty(value = "title")
    private String title;
    @ApiModelProperty(value = "Vote Descript")
    private String voteDesc;
    @ApiModelProperty(value = "是否群投票")
    private Boolean isGroup;
    @ApiModelProperty(value = "是否有图片描述")
    private Boolean isImg;
    @ApiModelProperty(value = "图片url")
    private String shareImgUrl;
    @ApiModelProperty(value = "是否过期，1为过期，0为未过期")
    private Boolean isExpired;
    @ApiModelProperty(value = "发起人头像")
    private String creatorHead;
}
