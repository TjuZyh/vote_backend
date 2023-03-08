package com.tju.bclab.vote_backend.vo.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;


@Data
@ApiModel(value = "发起投票的前端json")
public class AddVoteReq {

    @ApiModelProperty(value = "是否多选")
    private Integer type;

    @ApiModelProperty(value = "投票标题")
    private String title;

    @ApiModelProperty(value = "投票描述")
    private String voteDesc;

    @ApiModelProperty(value = "选项列表,保存选项描述")
    private ArrayList<String> optionList;

    @ApiModelProperty(value = "选项图片url")
    private ArrayList<String> urls;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "0不允许 1允许")
    private Integer isAnonymous;

    @ApiModelProperty(value = "截止时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm", timezone = "GMT+8")
    private Date endDate;

    //update time:2021/10/31 with zzZ
    @ApiModelProperty(value = "投票类型 0一般群投票 1有图群投票 2投票箱投票")
    private Integer voteType;

    @ApiModelProperty(value = "0不随机重排选项 1随机重排选项")
    private Integer shuffleOptions;

    @ApiModelProperty(value = "0仅一次投票 1每日投票")
    private Integer dailyVote;

    @ApiModelProperty(value = "0不需要个人信息 1需要个人信息")
    private Integer needPersonalInformation;

    @ApiModelProperty(value = "0显示票数 1显示票数和明细 2不显示票数")
    private Integer displayOption;

    @ApiModelProperty(value = "投票分享图片url")
    private String imgUrl;

    @ApiModelProperty(value = "补充描述图片url")
    private ArrayList<String> descImgUrls;

    @ApiModelProperty(value = "0 允许非群成员投票 1 不允许非群成员投票")
    private Integer onlyGroupMember;

    @ApiModelProperty(value = "至少可选")
    private Integer leastLimit;

    @ApiModelProperty(value = "至多可选")
    private Integer mostLimit;
}
