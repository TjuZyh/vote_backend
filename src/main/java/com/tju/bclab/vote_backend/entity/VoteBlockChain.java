package com.tju.bclab.vote_backend.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BlockChain对象", description="投票区块链信息")
public class VoteBlockChain implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "投票id")
    @TableId(value = "transaction_id", type = IdType.INPUT)
    private String transactionId;

    @ApiModelProperty(value = "块hash")
    private String blockHash;

    @ApiModelProperty(value = "链状态")
    private String chainStatus;

    @ApiModelProperty(value = "块高")
    private Long blockHeight;
}
