package com.tju.bclab.vote_backend.vo.resp;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "验证投票是否被修改的返回结果")
public class VerifyHaveModified {
    String  clientIp;
    String blockHash;
    Boolean haveModified;
    int countRecord;

}
