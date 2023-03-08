package com.tju.bclab.vote_backend.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
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
@ApiModel(value = "Voteboxuser对象", description = "将投票箱发起者与投票人对应")
public class Voteboxuser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自动分配的主键")
    private Long id;

    @ApiModelProperty(value = "投票ID")
    private String voteId;

    @ApiModelProperty(value = "投票人ID")
    private String userId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;


}
