package com.tju.bclab.vote_backend.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value="投票规则")
public class VoteRule {
    @ApiModelProperty(value = "0单选题 1多选")
    private Integer type;
    @ApiModelProperty(value = "匿名 0不允许 1允许")
    private Integer isAnonymous;
    @ApiModelProperty(value = "0不需要个人信息 1需要个人信息")
    private Integer needPersonalInformation;
    @ApiModelProperty(value = "0一般群投票 1有图群投票 2投票箱投票")
    private Integer voteType;
    @ApiModelProperty(value = "'0显示票数 1显示票数和明细 2不显示票数")
    private Integer displayOption;
    @ApiModelProperty(value = "0仅一次投票 1每日投票")
    private Integer dailyVote;
    @ApiModelProperty(value = "0不随机重排选项 1随机重排选项")
    private Integer shuffleOptions;
    @ApiModelProperty(value = "0 允许非群成员投票 1 不允许非群成员投票")
    private Integer onlyGroupMember ;
    @ApiModelProperty(value = "截止时间")
    private Date endDate;
}
