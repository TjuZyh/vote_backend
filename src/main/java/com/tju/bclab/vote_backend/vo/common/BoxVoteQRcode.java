package com.tju.bclab.vote_backend.vo.common;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "投票箱投票二维码")
public class BoxVoteQRcode {
    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "选项id")
    private String optionId;

    @ApiModelProperty(value = "题目id")
    private String voteId;

    @ApiModelProperty(value = "选项")
    private String optionStr;

    @ApiModelProperty(value = "是当前投票的第几个选项")
    private Integer optionIndex;

    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;
}
