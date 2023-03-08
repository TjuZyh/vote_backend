package com.tju.bclab.vote_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Votedesc", description="投票图片描述")
public class Votedesc {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "投票id")
    @TableId(value = "vote_id", type = IdType.ID_WORKER_STR)
    private String voteId;

    @ApiModelProperty(value = "投票分享图片url")
    private String imgUrl;
}
