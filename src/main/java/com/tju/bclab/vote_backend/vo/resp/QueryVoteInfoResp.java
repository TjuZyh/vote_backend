package com.tju.bclab.vote_backend.vo.resp;

import com.tju.bclab.vote_backend.entity.VoteOption;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "查询单个投票最新情况")
public class QueryVoteInfoResp {
    @ApiModelProperty(value = "投票标题")
    private String title;
    @ApiModelProperty(value = "投票描述")
    private String voteDesc;
    @ApiModelProperty(value = "描述图⽚的url")
    private String dsecImgUrl;
    @ApiModelProperty(value="选项列表,保存选项描述")
    private List<VoteOption> optionList;
    @ApiModelProperty(value = "投票人头像")
    private List<String> voteAvatarUrls;
    @ApiModelProperty(value = "投票分享图片url")
    private String shareImgUrl;
    @ApiModelProperty(value = "投票规则")
    private VoteRule rule;
    @ApiModelProperty(value = "发起时间")
    private Date voteCreate;
    @ApiModelProperty(value = "发起人头像")
    private String sponsorAvatarUrl;
    @ApiModelProperty(value = "发起人昵称")
    private String nickName;
    @ApiModelProperty(value = "投票人数")
    private Integer sumUser;
    @ApiModelProperty(value = "至少可选")
    private  Integer leastLimit;
    @ApiModelProperty(value = "至多可选")
    private Integer mostLimit;
    @ApiModelProperty(value = "投票者openID")
    private  String openId;
}
