package com.tju.bclab.vote_backend.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Vote对象", description="投票表")
public class Vote implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "当前投票id")
    @TableId(value = "vote_id", type = IdType.ID_WORKER_STR)
    private String voteId;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "投票标题")
    private String title;

    @ApiModelProperty(value = "投票描述")
    private String voteDesc;

    @ApiModelProperty(value = "0单选题 1多选")
    private Integer type;

    @ApiModelProperty(value = "0不允许 1允许")
    private Integer isAnonymous;

    @ApiModelProperty(value = "创建时间")
    //TableField为自动填充的注解，当满足条件时，会自动对属性进行填充，比如更新
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    @ApiModelProperty(value = "统计人数")
    private Integer sumUser;

    @ApiModelProperty(value = "统计票数")
    private Integer sumVote;

    @ApiModelProperty(value = "截止时间")
    private Date endDate;

    @ApiModelProperty(value = "0一般群投票 1有图群投票 2投票箱投票")
    private Integer voteType;

    @ApiModelProperty(value = "0不随机重排选项 1随机重排选项")
    private Integer shuffleOptions;

    @ApiModelProperty(value = "0仅一次投票 1每日投票")
    private Integer dailyVote;

    @ApiModelProperty(value = "0不需要个人信息 1需要个人信息")
    private Integer needPersonalInformation;

    @ApiModelProperty(value = "'0显示票数 1显示票数和明细 2不显示票数")
    private Integer displayOption;

    @ApiModelProperty(value = "投票分享图片url")
    private String imgUrl;

    @ApiModelProperty(value = "交易id")
    private String transactionId ;

    @ApiModelProperty(value = "0 允许非群成员投票 1 不允许非群成员投票")
    private Integer onlyGroupMember ;

    @ApiModelProperty(value = "至少可选")
    private  Integer leastLimit;

    @ApiModelProperty(value = "至多可选")
    private Integer mostLimit;
}
