package com.tju.bclab.vote_backend.vo.req;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;

@Data
@ApiModel(value = "发起投票箱投票的前端json")
public class AddVoteBoxReq {

    @ApiModelProperty(value = "是否多选")
    private Integer type;

    @ApiModelProperty(value = "投票标题")
    private String title;

    @ApiModelProperty(value = "投票描述")
    private String voteDesc;

    @ApiModelProperty(value = "投票类型 0一般群投票 1有图群投票 2投票箱投票")
    private Integer voteType;

    @ApiModelProperty(value = "选项列表,保存选项描述")
    private ArrayList<String> optionList;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "计划进行时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm", timezone = "GMT+8")
    private Date startDate;

}
