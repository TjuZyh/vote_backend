package com.tju.bclab.vote_backend.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@Data
public class VoteboxTime {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自动分配的主键")
    private Long id;

    @ApiModelProperty(value = "投票ID")
    private String voteId;

    @ApiModelProperty(value = "计划开始时间")
    private Date planStartTime;

}
