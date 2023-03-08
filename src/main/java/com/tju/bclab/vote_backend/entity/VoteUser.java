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
@ApiModel(value="VoteUser对象", description="投票操作")
public class VoteUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自动分配的主键")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    private String userId;

    private String voteId;

    private String optionId;

    @ApiModelProperty(value = "创建时间")
    //TableField为自动填充的注解，当满足条件时，会自动对属性进行填充，比如更新
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    private String realName;
    private String phone;

    private String transactionId ;
}
