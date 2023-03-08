package com.tju.bclab.vote_backend.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="VoteOption对象", description="选项表")
public class VoteOption implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "选项id")
    @TableId(value = "option_id", type = IdType.ID_WORKER_STR)
    private String optionId;

    @ApiModelProperty(value = "题目id")
    private String voteId;

    @ApiModelProperty(value = "选项")
    private String optionStr;

    @ApiModelProperty(value = "是当前投票的第几个选项")
    private Integer optionIndex;

    @ApiModelProperty(value = "创建时间")
    //TableField为自动填充的注解，当满足条件时，会自动对属性进行填充，比如更新
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    @ApiModelProperty(value = "图片路径")
    private String imgUrl;

    @ApiModelProperty(value = "选择当前选项的人数")
    private Integer count;


}
