package com.tju.bclab.vote_backend.vo.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;

@Data
@ApiModel(value="用户投票的前端json")
public class VoteReq {

    @ApiModelProperty(value="投票者Id")
    private String userId;

    @ApiModelProperty(value="投票Id")
    private String voteId;

    @ApiModelProperty(value="选项列表，保存选项id")
    private ArrayList<String> optionList;

    @ApiModelProperty(value="投票时间")
    @JsonFormat(shape= JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date currentTime;
}
