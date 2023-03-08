package com.tju.bclab.vote_backend.service;

import com.tju.bclab.vote_backend.entity.VoteBlockChain;
import com.tju.bclab.vote_backend.io.aelf.schemas.KeyPairInfo;
import com.tju.bclab.vote_backend.vo.resp.VerifyHaveModified;
import com.tju.bclab.vote_backend.vo.resp.VerifyVoteResp;

import java.util.List;


public interface BlockChainService {

    /**
     * @param hash：未上链的哈希,由userId voteId optionId time组成
     * @param privateKey:用户的私钥
     * @return 成功上链后返回true,否则抛出合约调用失败异常
     */
    VoteBlockChain VoteContract(String hash, String privateKey)throws Exception;

    /**
     * 存储结果合约调用
     * @param hash：未上链的哈希
     * @param privateKey：用户的私钥
     * @return 成功上链后返回true，否则抛出合约调用失败异常
     * @throws Exception
     */
    VoteBlockChain ResultContract(String hash, String privateKey) throws Exception;

    VoteBlockChain InitialContract(String hash, String privateKey) throws Exception;

    VoteBlockChain InitializeContract(String privateKey) throws Exception;

    VoteBlockChain AddUserContract(String privateKey) throws Exception;

    KeyPairInfo createWallet()throws Exception;

    List<VerifyHaveModified>voteVerify( Long blockHeight)throws Exception;

    VerifyVoteResp queryMultiNodes(String transactionId) throws Exception;
}
